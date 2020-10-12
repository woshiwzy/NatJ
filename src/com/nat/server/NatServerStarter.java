package com.nat.server;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;

import com.nat.domain.WorkPoint;
import com.nat.thread.ForwardRunnable;
import com.nat.util.Log;

/**
 * 运行在公网服务端的启动程序
 */
public class NatServerStarter {

	public static void main(String[] args) throws IOException {

		// 启动等待服务
		int mainPort = 6000;
		MainConnectServer mainConnectServer = MainConnectServer.getInstance(mainPort);

		InetAddress addr = InetAddress.getLocalHost();
		String localIp = addr.getHostAddress();
		Log.println("启动转发根服务===>>> 本机IP:" + localIp + " 根服务端口:" + mainPort);
		
		while(true) {
			
			mainConnectServer.startWaiter();

			ArrayList<WorkPoint> workPoints = new ArrayList<>();

			workPoints.add(new WorkPoint(localIp, 3000));

			for (int i = 0, isize = workPoints.size(); i < isize; i++) {
				WorkPoint workPoint = workPoints.get(i);
				Thread threadForward = new Thread(new ForwardRunnable(workPoint, mainConnectServer));// .start();
				threadForward.start();
			}
		}

	

	}

}
