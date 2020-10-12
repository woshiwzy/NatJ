package com.nat.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.IOUtils;
import com.nat.domain.Channel;
import com.nat.domain.Command;
import com.nat.util.Constant;
import com.nat.util.Log;

/**
 * 家庭服务器和外网服务器的主通道 通过这个主通道告诉家庭服务器把数据转发到指定端口
 */
public class MainConnectThreadClient extends Thread {

	private String remoteHost;
	private int remotePort;
	private Socket socket;

	private InputStream inputStream;
	private OutputStream outputStream;

	public MainConnectThreadClient(String remoteHost, int remoteport) throws UnknownHostException, IOException {
		this.remoteHost = remoteHost;
		this.remotePort = remoteport;
		this.socket = new Socket(this.remoteHost, this.remotePort);
		this.inputStream = this.socket.getInputStream();
		this.outputStream = this.socket.getOutputStream();
	}

	/**
	 * 
	 * @param localPort             本地端口
	 * @param remoteHostFromCommand 远程主机发过来的消息，这个IP不准，所以弃用
	 * @param remotePort            //本地主端口
	 */
	private void conenctLocal2Remote(int localPort, String remoteHostFromCommand, int remotePort) {

		try {

			Socket socketLocal = new Socket("localhost", localPort);
			String reDirectHost = this.remoteHost;
			Log.println("转发到公网的:" + reDirectHost + " port:" + remotePort);

			Socket socketRemote = new Socket(reDirectHost, remotePort);
			Channel channel = new Channel(socketLocal, socketRemote);
			Log.print("本地转发连接完成！！！");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void run() {
		int ret = -1;
		byte[] buffer = new byte[Constant.BUFFER_SIZE];
		try {

			while ((ret = inputStream.read(buffer)) != -1) {

				try {
					String firstData = new String(buffer, 0, ret);
					Command command = JSON.parseObject(firstData, Command.class);
					Log.println("家里服务器收到命令:" + command);
					Log.println("本地端口:" + command.getTaskPort() + " ===> " + command.getTaskHost() + ":"
							+ command.getForWardPort());
					conenctLocal2Remote(command.getTaskPort(), command.getTaskHost(), command.getForWardPort());

				} catch (Exception e) {
					Log.redPrint("本地转发错误，阁下继续工作:" + e.getLocalizedMessage());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.close(inputStream);
			IOUtils.close(outputStream);
			IOUtils.close(socket);
		}

	}

}
