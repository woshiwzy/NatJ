package com.nat.domain;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.alibaba.fastjson.util.IOUtils;
import com.nat.thread.ThreadPoolTool;
import com.nat.util.Constant;
import com.nat.util.Log;

/**
 * 家里主机和公网主机的一个通道 这个通道在家中的服务器链接到服务器的时候创建
 */
public class Channel {

	private Socket homeSocket;// 家中的socket
	private Socket publicRequestSocket;// 远程请求的Socket

	public Channel(Socket homeSocket, Socket publicRequestSocket) {
		this.homeSocket = homeSocket;
		this.publicRequestSocket = publicRequestSocket;
		this.connectSocket();
	}

	private void connectSocket() {
		try {
			// 来自外网的公共请求数据发送到家庭服务器中去
//			ConnectThread connectThreadPublic2Home = new ConnectThread(publicRequestSocket.getInputStream(),homeSocket.getOutputStream());
			// 来自家庭中的服务器数据，发送到外网中去
//			ConnectThread connectThreadHome2Public = new ConnectThread(homeSocket.getInputStream(),publicRequestSocket.getOutputStream());
			
			ThreadPoolTool.getThreadPool().execute(new ConnectThread(publicRequestSocket.getInputStream(),homeSocket.getOutputStream()));
			ThreadPoolTool.getThreadPool().execute(new ConnectThread(homeSocket.getInputStream(),publicRequestSocket.getOutputStream()));
			
			Log.println("链路链接成功=====");
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
//			IOUtils.close(homeSocket);
//			IOUtils.close(publicRequestSocket);
		}

	}

	/**
	 * 数据连接线程
	 */
	private class ConnectThread implements Runnable {
		private InputStream inputStream;
		private OutputStream outputStream;

		public ConnectThread(InputStream inputStream, OutputStream outPutStream) {
			this.inputStream = inputStream;
			this.outputStream = outPutStream;
//			this.start();
		}

		@Override
		public void run() {

			byte buffer[] = new byte[Constant.BUFFER_SIZE];
			try {
				int ret = -1;
				while ((ret = this.inputStream.read(buffer)) != -1) {
					this.outputStream.write(buffer, 0, ret);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
//				IOUtils.close(inputStream);
//				IOUtils.close(outputStream);
			}

		}
		
		@Override
		protected void finalize() throws Throwable {
			
			IOUtils.close(inputStream);
			IOUtils.close(outputStream);
			IOUtils.close(homeSocket);
			IOUtils.close(publicRequestSocket);
			Log.println("Channel关闭==");
			super.finalize();
		}

	}
	
	

}
