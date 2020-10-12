package com.nat.client;

import java.io.IOException;
import java.net.UnknownHostException;

import com.nat.util.Log;

public class NatClientStarter {

	public static void main(String[] args) throws UnknownHostException, IOException {

		String remoteHost = "161.117.195.45";
		int reportPort = 6000;
		MainConnectThreadClient mainConnectThreadClient = new MainConnectThreadClient(remoteHost, reportPort);
		mainConnectThreadClient.start();
		Log.println("Nat客户端已经启动");
		
	}

}
