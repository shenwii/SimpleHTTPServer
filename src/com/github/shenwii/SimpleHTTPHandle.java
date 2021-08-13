package com.github.shenwii;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class SimpleHTTPHandle implements HttpHandler {
	
	private final ThreadPoolExecutor executor;
	
	public SimpleHTTPHandle() {
		int staticThread = 5;
		int maxThread = 20;
		try {
			String p = System.getProperty("SimpleHTTPServer.staticThread");
			if(p != null)
				staticThread = Integer.parseInt(p);
		} catch (Exception e) {}
		try {
			String p = System.getProperty("SimpleHTTPServer.maxThread");
			if(p != null)
				maxThread = Integer.parseInt(p);
		} catch (Exception e) {}
		System.out.println("debug: staticThread = " + staticThread);
		System.out.println("debug: maxThread = " + maxThread);
		executor = new ThreadPoolExecutor(staticThread, maxThread, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(50));
	}
	
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		exchange.getResponseHeaders().add("Server", "Simple HTTP Server By Shenw/" + SimpleHTTPServer.VERSION);
		//判断http method类型，现在只支持get方法，其他方法会返回405
		if("GET".equals(exchange.getRequestMethod()))
			executor.execute(new ThreadGetHandle(exchange));
		else
			doMethodNotAllowed(exchange);
	}

	/**
	 * 返回405响应
	 * @param exchange
	 * @throws IOException
	 */
	private void doMethodNotAllowed(HttpExchange exchange) throws IOException {
		String responString = "Method '" + exchange.getRequestMethod() + "' Not Supported";
		byte[] responBytes = Utils.toBytes(responString);
		Utils.setHtmlContext(exchange.getResponseHeaders());
		exchange.sendResponseHeaders(405, responBytes.length);
		exchange.getResponseBody().write(responBytes);
		exchange.getRequestBody().close();
		exchange.close();
	}
}
