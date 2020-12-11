package com.github.shenwii;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.sun.net.httpserver.Headers;

public class Utils {
	
	public final static Charset ENCODE = Charset.forName("UTF-8");

	/**
	 * 将String类型转成UTF-8的byte数组
	 * @param inStr
	 * @return
	 */
	public static byte[] toBytes(String inStr) {
		return inStr.getBytes(ENCODE);
	}

	/**
	 * 设置HTML头的Content-type
	 * @param headers
	 */
	public static void setHtmlContext(Headers headers) {
		headers.set("Content-type", "text/html; charset=UTF-8");
	}

	/**
	 * 将日期以GMT格式返回
	 * @param date
	 * @return
	 */
	public static String dateToString(Date date) {
		final String GMT_FORMAT = "EEE, d MMM yyyy HH:mm:ss 'GMT'";
		SimpleDateFormat sdf = new SimpleDateFormat(GMT_FORMAT, Locale.US);
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		return sdf.format(date);
	}

	/**
	 * 将二进制的md5转成字符串形式
	 * @param md
	 * @return
	 */
	public static String md5String(byte[] md) {
		char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
		int j = md.length;
		char[] buf = new char[j * 2];
		int k = 0;
		for(byte b: md) {
			buf[k++] = hexDigits[b >>> 4 & 0xf];
			buf[k++] = hexDigits[b & 0xf];
		}
		return new String(buf);
	}

	/**
	 * 计算文件的md5值
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String md5Sum(File file) throws IOException {
		final int BUFFER_LEN = 4096 * 4096;
		FileInputStream fis = null;
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) { }
		try {
			int c;
			byte[] buffer = new byte[BUFFER_LEN];
			fis = new FileInputStream(file);
			while((c = fis.read(buffer)) > 0) {
				md.update(buffer, 0, c);
			}
			return md5String(md.digest());
		} catch (IOException e) {
			throw e;
		} finally {
			if(fis != null)
				try {
					fis.close();
				} catch(IOException e) {
					e.printStackTrace();
				}
		}
	}

}
