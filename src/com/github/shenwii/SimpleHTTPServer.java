package com.github.shenwii;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

public class SimpleHTTPServer{
	final public static String VERSION = "1.0.1";
	private static String host = "0.0.0.0";
	private static int port = 8000;
	public static void main(String[] args) throws IOException {
		for(int i = 0; i < args.length; i++) {
			switch (i) {
			case 0:
				host = args[i];
				break;
			case 1:
				try {
					port = Integer.parseInt(args[i]);
				} catch(NumberFormatException e) {
					usage();
				}
				break;
			default:
				break;
			}
		}
		HttpServer server = HttpServer.create(new InetSocketAddress(host, port), 0);
		System.out.println("Serving HTTP on " + host + " port " + port + " ...");
		server.createContext("/", new SimpleHTTPHandle());
		server.start();
	}

	/**
	 * 打印出使用方法
	 */
	private static void usage() {
		System.err.println("Usage: SimpleHTTPServer [Host] [Port]");
		System.exit(1);
	}
}
