package com.github.shenwii;

import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.util.*;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

public class ThreadGetHandle implements ThreadMethodHandle {
	
	private final static int BUFFER_LEN = 4096 * 4096;
	
	private final HttpExchange exchange;

	public ThreadGetHandle(HttpExchange exchange) {
		this.exchange = exchange;
	}

	private boolean headerSend = false;

	@Override
	public void run() {
		try {
			System.out.println(new Date() + "\t" + exchange.getRemoteAddress().getHostString() + "\t" + "GET " + URLDecoder.decode(exchange.getRequestURI().getRawPath(), Utils.ENCODE.name()));
			File accessedFile = new File(new File(".").getPath() + URLDecoder.decode(exchange.getRequestURI().getRawPath(), Utils.ENCODE.name()));
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
			e.printStackTrace();
			try {
				if(!headerSend) {
					String responseString = e.getMessage();
					byte[] responseBytes = Utils.toBytes(responseString);
					Utils.setHtmlContext(exchange.getResponseHeaders());
					exchange.sendResponseHeaders(500, responseBytes.length);
					exchange.getResponseBody().write(responseBytes);
				}
			} catch(Throwable e1) {
				e1.printStackTrace();
			}
		} finally {
			try {
				exchange.getRequestBody().close();
			} catch (IOException ignored) { }
			exchange.close();
		}
	}

	/**
	 * 返回404响应
	 * @param exchange 交换
	 * @throws IOException IO异常
	 */
	private void doNotFound(HttpExchange exchange) throws IOException {
		String responseString = "Page Not Found";
		byte[] responseBytes = Utils.toBytes(responseString);
		Utils.setHtmlContext(exchange.getResponseHeaders());
		exchange.sendResponseHeaders(404, responseBytes.length);
		headerSend = true;
		exchange.getResponseBody().write(responseBytes);
	}

	/**
	 * 遍历文件夹下的所有文件
	 * @param exchange 交换
	 * @param dir 文件夹
	 * @throws IOException IO异常
	 */
	private void doListDirectory(HttpExchange exchange, File dir) throws IOException {
		byte[] responseBytes = parseDirectoryHtml(dir, URLDecoder.decode(exchange.getRequestURI().getRawPath(), Utils.ENCODE.name()));
		Utils.setHtmlContext(exchange.getResponseHeaders());
		exchange.sendResponseHeaders(200, responseBytes.length);
		headerSend = true;
		exchange.getResponseBody().write(responseBytes);
	}

	/**
	 * 解析文件，返回文件内容
	 * 支持断点续传功能
	 * @param exchange 交换
	 * @param file 文件
	 * @throws IOException IO异常
	 */
	private void doParseFile(HttpExchange exchange, File file) throws IOException {
		setFileContext(exchange.getResponseHeaders(), file);
		long lastModified = file.lastModified();
		ByteBuffer byteBuffer = ByteBuffer.allocate(8);
		byteBuffer.putLong(lastModified);
		String md5 = Utils.md5String(byteBuffer.array());
		String modifiedDate = Utils.dateToString(new Date(lastModified));
		exchange.getResponseHeaders().set("accept-ranges", "bytes");
		exchange.getResponseHeaders().set("ETag", md5);
		exchange.getResponseHeaders().set("Last-Modified", modifiedDate);
		{
			List<String> etagList = exchange.getRequestHeaders().get("if-none-match");
			String etag = null;
			if(etagList != null && etagList.size() != 0)
				etag = etagList.get(0);
			List<String> modifiedList  = exchange.getRequestHeaders().get("if-modified-since");
			String modified = null;
			if(modifiedList != null && modifiedList.size() != 0)
				modified = modifiedList.get(0);
			if(etag != null) {
				if(md5.equals(etag)) {
					exchange.sendResponseHeaders(304, -1);
					headerSend = true;
					return;
				}
			} else {
				if(modified != null) {
					if(modifiedDate.equals(modified)) {
						exchange.sendResponseHeaders(304, -1);
						headerSend = true;
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
				if((ifRange == null) || (ifRange.equals(md5) || ifRange.equals(modifiedDate))) {
					while(true) {
						long readEnd;
						if(range == null)
							break;
						String[] parms = range.split("=");
						if(parms.length != 2)
							break;
						if(!parms[0].trim().equalsIgnoreCase("bytes"))
							break;
						if(!parms[1].contains("-"))
							break;
						try {
							String[] bs = parms[1].split("-");
							if(bs[0].equals("")) {
								readStart = file.length() - Long.parseLong(bs[1]);
								readEnd = file.length() - 1;
							} else if(bs.length == 1 || bs[1].equals("")) {
								readStart = Long.parseLong(bs[0]);
								readEnd = file.length() - 1;
							} else {
								readStart = Long.parseLong(bs[0]);
								readEnd = Long.parseLong(bs[1]);
								if(readEnd <= readStart)
									break;
							}
							readLength = readEnd - readStart + 1;
							exchange.getResponseHeaders().set("Content-Range", "bytes " + readStart + "-" + readEnd + "/" + file.length());
							exchange.sendResponseHeaders(206, readLength);
							headerSend = true;
							break t;
						} catch(NumberFormatException e) {
							break;
						}
					}
				}
				readStart = 0L;
				readLength = file.length();
				exchange.sendResponseHeaders(200, file.length());
				headerSend = true;
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
	 * @param dir 文件夹
	 * @param url 当前url
	 * @return 字节数组
	 */
	private byte[] parseDirectoryHtml(File dir, String url) {
		StringBuilder html = new StringBuilder();
		html.append("<html>\n");
		html.append("<title>Directory listing for ").append(url).append("</title>\n");
		html.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=").append(Utils.ENCODE.name()).append("\" />\n");
		html.append("<body>\n");
		html.append("<h2>Directory listing for ").append(url).append("</h2>\n");
		html.append("<hr>\n");
		html.append("<ul>\n");
		List<File> files = Arrays.asList(Objects.requireNonNull(dir.listFiles()));
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
				html.append("<li><a href=\"").append(URLEncoder.encode(fileName, Utils.ENCODE.name())).append(f.isDirectory() ? "/" : "").append("\">").append(fileName).append(f.isDirectory() ? "/" : "").append("</a></li>\n");
			} catch (UnsupportedEncodingException ignored) {}
		}
		html.append("</ul>\n");
		html.append("<hr>\n");
		html.append("</body>\n");
		html.append("</html>\n");
		return Utils.toBytes(html.toString());
	}

	/**
	 * 设置响应的Content-type
	 * @param headers http头
	 * @param file 文件
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

}
