package com.github.shenwii;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.sun.net.httpserver.Headers;

public class Utils {
	
	public final static Charset ENCODE = StandardCharsets.UTF_8;

	/**
	 * 将String类型转成UTF-8的byte数组
	 * @param inStr 字符串
	 * @return byte数组
	 */
	public static byte[] toBytes(String inStr) {
		return inStr.getBytes(ENCODE);
	}

	/**
	 * 设置HTML头的Content-type
	 * @param headers http头
	 */
	public static void setHtmlContext(Headers headers) {
		headers.set("Content-type", "text/html; charset=UTF-8");
	}

	/**
	 * 将日期以GMT格式返回
	 * @param date 日期
	 * @return 字符串
	 */
	public static String dateToString(Date date) {
		final String GMT_FORMAT = "EEE, d MMM yyyy HH:mm:ss 'GMT'";
		SimpleDateFormat sdf = new SimpleDateFormat(GMT_FORMAT, Locale.US);
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		return sdf.format(date);
	}

	/**
	 * 将二进制的md5转成字符串形式
	 * @param md byte数组
	 * @return 字符串
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

}
