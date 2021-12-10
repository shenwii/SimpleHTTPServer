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
		int maxThread = 100;
		try {
			String p = System.getProperty("SimpleHTTPServer.staticThread");
			if(p != null)
				staticThread = Integer.parseInt(p);
		} catch (Exception ignore) {}
		try {
			String p = System.getProperty("SimpleHTTPServer.maxThread");
			if(p != null)
				maxThread = Integer.parseInt(p);
		} catch (Exception ignore) {}
		executor = new ThreadPoolExecutor(staticThread, maxThread, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(50));
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
	 * @param exchange 交换
	 * @throws IOException IO异常
	 */
	private void doMethodNotAllowed(HttpExchange exchange) throws IOException {
		String responseString = "Method '" + exchange.getRequestMethod() + "' Not Supported";
		byte[] responseBytes = Utils.toBytes(responseString);
		Utils.setHtmlContext(exchange.getResponseHeaders());
		exchange.sendResponseHeaders(405, responseBytes.length);
		exchange.getResponseBody().write(responseBytes);
		exchange.getRequestBody().close();
		exchange.close();
	}
}
