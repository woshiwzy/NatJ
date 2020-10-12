package com.nat.test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class TestRandPortServer {

	public static void main(String[] args) throws UnknownHostException, IOException {

		ServerSocket ss = new ServerSocket(0, 1, InetAddress.getByAddress(new byte[] { 127, 0, 0, 1 }));
		int bindPort = ss.getLocalPort();
		System.out.println("bind port:" + bindPort);

		while (true) {

			Socket socket = ss.accept();

			byte[] b = new byte[1024 * 5];
			socket.getInputStream().read(b);
			System.out.println(new String(b));
			System.out.println("===============");
			socket.getOutputStream().write("Hello".getBytes("utf8"));
			socket.getOutputStream().close();

		}

	}
}
