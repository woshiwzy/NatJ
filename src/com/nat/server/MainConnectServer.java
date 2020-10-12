package com.nat.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.nat.domain.Command;
import com.nat.util.Log;

/**
 * 等待家中服务器链接
 */
public class MainConnectServer  {

	private ServerSocket serverSocket;
	private Socket clientSocket;
	private InputStream inputStream;
	private OutputStream outputStream;
	private boolean isAvaliable = false;
	
	private static MainConnectServer mainConnectServer; 
	
	public static MainConnectServer getInstance(int port) throws IOException {
		if(null==mainConnectServer) {
			mainConnectServer=new MainConnectServer(port);
		}
		return mainConnectServer;
	}
	

	private MainConnectServer(int port) throws IOException {
		serverSocket = new ServerSocket(port);
		Log.println("主服务启动成功===");
	}

	/**
	 * 发送链接命令到家中的服务器
	 * @param command
	 */
	public void sendConnectCommand(Command command) {
		if(null!=outputStream) {
			try {
				outputStream.write(command.toString().getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 等待client链接
	 * @throws IOException
	 */
	public void startWaiter() throws IOException {
		clientSocket = serverSocket.accept();
		inputStream = clientSocket.getInputStream();
		outputStream = clientSocket.getOutputStream();
		isAvaliable = true;
		Log.println("MainConnectServer 主链接已经就绪");
	}

}
