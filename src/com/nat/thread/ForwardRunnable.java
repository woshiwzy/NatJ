package com.nat.thread;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import com.nat.domain.Channel;
import com.nat.domain.Command;
import com.nat.domain.WorkPoint;
import com.nat.server.MainConnectServer;
import com.nat.util.Log;

/**
 * 服务器端口转发监听线程
 */
public class ForwardRunnable implements Runnable {

	private WorkPoint workPoint;
	private ServerSocket serverSocket;
	private boolean isAlive = true;
	private MainConnectServer mainConnectServer;

	public ForwardRunnable(WorkPoint workPoint, MainConnectServer mainConnectServer) throws IOException {
		try {
			this.workPoint = workPoint;
			this.serverSocket = new ServerSocket(workPoint.getPort());
			this.mainConnectServer = mainConnectServer;
			Log.println("启动根服务端口:" + workPoint.getPort());
		} catch (IOException e) {
			isAlive = false;
			Log.redPrint("" + e.getLocalizedMessage());
		}
	}

	@Override
	public void run() {
		while (isAlive) {
			try {
				// 接受到外部请求
				Socket receiverSocket = this.serverSocket.accept();
				
				ThreadPoolTool.getThreadPool().execute(new WaiterRunable(receiverSocket));

//				Log.println("本地端口：" + workPoint.getPort() + " 收到外部请求:" + receiverSocket.getInetAddress().getHostName()
//						+ ":" + receiverSocket.getPort());
//
//				// 开一个端口等待家庭网络连接
//				ServerSocket waiterServerSocket = new ServerSocket(0, 1,
//						InetAddress.getByAddress(new byte[] { 0, 0, 0, 0 }));
//
//				Log.println("开启本地Waitter端口:" + waiterServerSocket.getLocalPort());
//
//				// 通过主链接告诉家庭网络去连接的目的地
//				mainConnectServer.sendConnectCommand(new Command(workPoint.getHost(), workPoint.getPort(),
//						waiterServerSocket.getLocalPort(), Command.PROTOCOL_HTTP));
//				Log.println("已经告知家庭网络连接目的地端口:" + waiterServerSocket.getLocalPort());
//
//				// 等待家庭网络到来
//				Socket homeSocket = waiterServerSocket.accept();
//
//				Log.println("家庭网络到达端口:" + waiterServerSocket.getLocalPort());
//
//				// 链接这两个socket
//				Channel channel = new Channel(homeSocket, receiverSocket);
//				Log.println("数据链路完成，等待下一次");

			} catch (IOException e) {
				e.printStackTrace();
				isAlive = false;
			}
		}
	}

	class WaiterRunable implements Runnable {

		private Socket receiverSocket;

		public WaiterRunable(Socket receiverSocket) {
			this.receiverSocket = receiverSocket;
		}

		@Override
		public void run() {

			try {
				// 开一个端口等待家庭网络连接
				ServerSocket waiterServerSocket = new ServerSocket(0, 1,
						InetAddress.getByAddress(new byte[] { 0, 0, 0, 0 }));

				Log.println("开启本地Waitter端口:" + waiterServerSocket.getLocalPort());

				// 通过主链接告诉家庭网络去连接的目的地
				mainConnectServer.sendConnectCommand(new Command(workPoint.getHost(), workPoint.getPort(),
						waiterServerSocket.getLocalPort(), Command.PROTOCOL_HTTP));
				Log.println("已经告知家庭网络连接目的地端口:" + waiterServerSocket.getLocalPort());

				// 等待家庭网络到来
				Socket homeSocket = waiterServerSocket.accept();

				Log.println("家庭网络到达端口:" + waiterServerSocket.getLocalPort());

				// 链接这两个socket
				Channel channel = new Channel(homeSocket, receiverSocket);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

}
