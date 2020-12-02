package com.github.shenwii;

import java.nio.charset.Charset;

import com.sun.net.httpserver.Headers;

public class Utils {
	
	public final static Charset ENCODE = Charset.forName("UTF-8");

	public static byte[] toBytes(String inStr) {
		return inStr.getBytes(ENCODE);
	}
	
	public static void setHtmlContext(Headers headers) {
		headers.set("Content-type", "text/html; charset=UTF-8");
	}
}
