package com.github.shenwii;

import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

public class ThreadGetHandle implements ThreadMethodHandle {
	
	private final static int BUFFER_LEN = 4096 * 4096;
	
	private final HttpExchange exchange;

	private final static Map<String, FileDto> fileCache = new HashMap<>();
	Object fileCacheLocker = new Object();

	public ThreadGetHandle(HttpExchange exchange) {
		this.exchange = exchange;
	}

	@Override
	public void run() {
		try {
			System.out.println(new Date().toString() + "\t" + exchange.getRemoteAddress().getHostString() + "\t" + "GET " + URLDecoder.decode(exchange.getRequestURI().getPath(), Utils.ENCODE.name()));
			File accessedFile = new File(new File(".").getPath() + URLDecoder.decode(exchange.getRequestURI().getPath(), Utils.ENCODE.name()));
			//当访问的资源不存在时，返回404
			if(!accessedFile.exists()) {
				doNotFound(exchange);
				return;
			}
			if(accessedFile.isDirectory()) {
				//当访问的资源为文件夹时，判断该文件夹下是否存在index.html
				//如果存在index.html文件时，则返回index.html作为前端页面
				//如果不存在index.html文件时，则将该目录下的所有资源返回给前端
				File indexHtml = new File(accessedFile, "index.html");
				if(indexHtml.isFile())
					doParseFile(exchange, indexHtml);
				else
					doListDirectory(exchange, accessedFile);
			} else {
				//如果该资源为文件时，则直接返回该文件内容
				doParseFile(exchange, accessedFile);
			}
		} catch(Throwable e) {
			try {
				String responString = e.getMessage();
				byte[] responBytes = Utils.toBytes(responString);
				Utils.setHtmlContext(exchange.getResponseHeaders());
				exchange.sendResponseHeaders(500, responBytes.length);
				exchange.getResponseBody().write(responBytes);
				exchange.getRequestBody().close();
			} catch(Throwable e1) {
				e1.printStackTrace();
			}
		} finally {
			exchange.close();
		}
	}

	/**
	 * 返回404响应
	 * @param exchange
	 * @throws IOException
	 */
	private void doNotFound(HttpExchange exchange) throws IOException {
		String responString = "Page Not Found";
		byte[] responBytes = Utils.toBytes(responString);
		Utils.setHtmlContext(exchange.getResponseHeaders());
		exchange.sendResponseHeaders(404, responBytes.length);
		exchange.getResponseBody().write(responBytes);
		exchange.getRequestBody().close();
	}

	/**
	 * 遍历文件夹下的所有文件
	 * @param exchange
	 * @param dir
	 * @throws IOException
	 */
	private void doListDirectory(HttpExchange exchange, File dir) throws IOException {
		byte[] responBytes = parseDirectoryHtml(dir, exchange.getRequestURI().getPath());
		Utils.setHtmlContext(exchange.getResponseHeaders());
		exchange.sendResponseHeaders(200, responBytes.length);
		exchange.getResponseBody().write(responBytes);
		exchange.getRequestBody().close();
	}

	/**
	 * 解析文件，返回文件内容
	 * 支持断点续传功能
	 * @param exchange
	 * @param file
	 * @throws IOException
	 */
	private void doParseFile(HttpExchange exchange, File file) throws IOException, InterruptedException {
		setFileContext(exchange.getResponseHeaders(), file);
		FileDto fileDto = null;
		synchronized (fileCacheLocker) {
			fileDto = fileCache.get(file.getAbsolutePath());
			if((fileDto == null) || (file.lastModified() != fileDto.getLastModified() || file.length() != fileDto.getFileSize())) {
				fileDto = new FileDto();
				fileDto.setFile(file);
				fileDto.setLastModified(file.lastModified());
				fileDto.setFileSize(file.length());
				fileDto.setHandling(true);
				fileCache.put(file.getAbsolutePath(), fileDto);
				FileMd5sumThread fileMd5sumThread = new FileMd5sumThread(fileDto);
				new Thread(fileMd5sumThread).start();
			}
		}
		synchronized (fileDto) {
			if(fileDto.isHandling) {
				fileDto.wait();
			}
		}
		String md5 = fileDto.getMd5();
		if(md5 == null)
			throw new IOException("计算MD5失败");
		String modifiedDate = Utils.dateToString(new Date(fileDto.getLastModified()));
		exchange.getResponseHeaders().set("accept-ranges", "bytes");
		exchange.getResponseHeaders().set("ETag", md5);
		exchange.getResponseHeaders().set("Last-Modified", modifiedDate);
		{
			List<String> etags = exchange.getRequestHeaders().get("if-none-match");
			String etag = null;
			if(etags != null && etags.size() != 0)
				etag = etags.get(0);
			List<String> modifieds  = exchange.getRequestHeaders().get("if-modified-since");
			String modified = null;
			if(modifieds != null && modifieds.size() != 0)
				modified = modifieds.get(0);
			if(etag != null) {
				if(md5.equals(etag)) {
					exchange.sendResponseHeaders(304, -1);
					return;
				}
			} else {
				if(modified != null) {
					if(modifiedDate.equals(modified)) {
						exchange.sendResponseHeaders(304, -1);
						return;
					}
				}
			}
		}
		long readStart;
		long readLength;
		{
			String ifRange = null;
			String range = null;
			List<String> ifRanges = exchange.getRequestHeaders().get("if-range");
			List<String> ranges = exchange.getRequestHeaders().get("range");
			if(ifRanges != null && ifRanges.size() != 0)
				ifRange = ifRanges.get(0);
			if(ranges != null && ranges.size() != 0)
				range = ranges.get(0);
			t: while(true) {
				if((ifRange == null) || (ifRange != null && (ifRange.equals(md5) || ifRange.equals(modifiedDate)))) {
					while(true) {
						long readEnd;
						if(range == null)
							break;
						String[] parms = range.split("=");
						if(parms.length != 2)
							break;
						if(!parms[0].trim().toLowerCase().equals("bytes"))
							break;
						if(!parms[1].contains("-"))
							break;
						try {
							String[] bs = parms[1].split("-");
							if(bs[0].equals("")) {
								readStart = file.length() - Long.valueOf(bs[1]);
								readEnd = file.length() - 1;
							} else if(bs.length == 1 || bs[1].equals("")) {
								readStart = Long.valueOf(bs[0]);
								readEnd = file.length() - 1;
							} else {
								readStart = Long.valueOf(bs[0]);
								readEnd = Long.valueOf(bs[1]);
								if(readEnd <= readStart)
									break;
							}
							readLength = readEnd - readStart + 1;
							exchange.getResponseHeaders().set("Content-Range", "bytes " + readStart + "-" + readEnd + "/" + file.length());
							exchange.sendResponseHeaders(206, readLength);
							break t;
						} catch(NumberFormatException e) {
							break;
						}
					}
				}
				readStart = 0L;
				readLength = file.length();
				exchange.sendResponseHeaders(200, file.length());
				break;
			}
		}
		try(FileInputStream fis = new FileInputStream(file); OutputStream os = exchange.getResponseBody()){
			int c;
			long rt = 0;
			byte[] buffer = new byte[BUFFER_LEN];
			fis.skip(readStart);
			while((c = fis.read(buffer)) > 0) {
				if(rt + c > readLength) {
					os.write(buffer, 0, (int) (readLength - rt));
					break;
				} else {
					os.write(buffer, 0, c);
				}
				rt += c;
			}
		}
	}

	/**
	 * 遍历文件夹下所有文件，生成html内容
	 * @param dir
	 * @param url
	 * @return
	 */
	private byte[] parseDirectoryHtml(File dir, String url) {
		StringBuilder html = new StringBuilder();
		html.append("<html>\n");
		html.append("<title>Directory listing for " + url + "</title>\n");
		html.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=" + Utils.ENCODE.name() + "\" />\n");
		html.append("<body>\n");
		html.append("<h2>Directory listing for " + url + "</h2>\n");
		html.append("<hr>\n");
		html.append("<ul>\n");
		List<File> files = Arrays.asList(dir.listFiles());
		//将文件夹下的文件进行排序，规则为：文件夹优先于文件，然后根据名字排序
		files.sort((f1, f2)->{
			if(f1.isDirectory() && !f2.isDirectory())
				return -1;
			else if(!f1.isDirectory() && f2.isDirectory())
				return 1;
			else
				return f1.getName().compareTo(f2.getName());
		});
		for(File f: files) {
			String fileName = f.getName();
			try {
				html.append("<li><a href=\"" + URLEncoder.encode(fileName, Utils.ENCODE.name()) + (f.isDirectory()? "/": "") + "\">" + fileName + (f.isDirectory()? "/": "") + "</a></li>\n");
			} catch (UnsupportedEncodingException e) {}
		}
		html.append("</ul>\n");
		html.append("<hr>\n");
		html.append("</body>\n");
		html.append("</html>\n");
		return Utils.toBytes(html.toString());
	}

	/**
	 * 设置响应的Content-type
	 * @param headers
	 * @param file
	 */
	private void setFileContext(Headers headers, File file) {
		String ct;
		String[] es = file.getName().split("\\.");
		if(es.length == 1) {
			ct = ContentType.getContentType(null);
		} else {
			String s = es[es.length - 1];
			ct = ContentType.getContentType(s);
		}
		headers.set("Content-type", ct);
	}

	class FileDto {
		private File file;
		private long lastModified;
		private long fileSize;
		private String md5;
		private boolean isHandling;

		public File getFile() {
			return file;
		}

		public void setFile(File file) {
			this.file = file;
		}

		public long getLastModified() {
			return lastModified;
		}

		public void setLastModified(long lastModified) {
			this.lastModified = lastModified;
		}

		public long getFileSize() {
			return fileSize;
		}

		public void setFileSize(long fileSize) {
			this.fileSize = fileSize;
		}

		public String getMd5() {
			return md5;
		}

		public void setMd5(String md5) {
			this.md5 = md5;
		}

		public boolean isHandling() {
			return isHandling;
		}

		public void setHandling(boolean handling) {
			isHandling = handling;
		}
	}

	class FileMd5sumThread extends Thread {
		final private FileDto fileDto;

		public FileMd5sumThread(FileDto fileDto) {
			this.fileDto = fileDto;
		}

		@Override
		public void run() {
			String md5 = null;
			try{
				md5 = Utils.md5Sum(fileDto.getFile());
			} catch (IOException e) {
			}
			synchronized (fileDto) {
				fileDto.setMd5(md5);
				fileDto.notifyAll();
				fileDto.setHandling(false);
			}
		}
	}
}
