package com.github.shenwii;

import java.net.InetSocketAddress;
import java.nio.file.Paths;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsServer;
import nl.altindag.ssl.SSLFactory;
import nl.altindag.ssl.util.PemUtils;

import javax.net.ssl.*;

public class SimpleHTTPServer {
	final public static String VERSION = "1.0.2";
	private static String host = "0.0.0.0";
	private static int port = 8000;

	public static void main(String[] args) throws Exception {
		String publicKeyFilePath = null;
		String privateKeyFilePath = null;
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
				case 2:
					publicKeyFilePath = args[i];
					break;
				case 3:
					privateKeyFilePath = args[i];
					break;
				default:
					break;
			}
		}
		HttpServer server = null;
		if(publicKeyFilePath == null && privateKeyFilePath == null) {
			server = HttpServer.create(new InetSocketAddress(host, port), 0);
		} else if(publicKeyFilePath != null && privateKeyFilePath != null) {
			server = HttpsServer.create(new InetSocketAddress(host, port), 0);
			((HttpsServer) server).setHttpsConfigurator(new HttpsConfigurator(createSSLContext(publicKeyFilePath, privateKeyFilePath)));
		} else {
			System.err.println("公钥文件和私钥文件必须同时存在");
			System.exit(1);
		}
		System.out.println("Serving HTTP on " + host + " port " + port + " ...");
		server.createContext("/", new SimpleHTTPHandle());
		server.start();
	}

	private static SSLContext createSSLContext(String publicKeyFilePath, String privateKeyFilePath) {
		X509ExtendedKeyManager keyManager = PemUtils.loadIdentityMaterial(Paths.get(publicKeyFilePath), Paths.get(privateKeyFilePath));
		X509ExtendedTrustManager trustManager = PemUtils.loadTrustMaterial(Paths.get(publicKeyFilePath));

		SSLFactory sslFactory = SSLFactory.builder()
				.withIdentityMaterial(keyManager)
				.withTrustMaterial(trustManager)
				.build();

		return sslFactory.getSslContext();
	}


	/**
	 * 打印出使用方法
	 */
	private static void usage() {
		System.err.println("Usage: SimpleHTTPServer [Host] [Port] [PublicKeyFile] [PrivateKeyFile]");
		System.exit(1);
	}
}
