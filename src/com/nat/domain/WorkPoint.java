package com.nat.domain;

/**
 * 服务器可工作端口
 */
public class WorkPoint {
	
	private String host;
	private int port;
	
	

	public WorkPoint(String host, int port) {
		super();
		this.host = host;
		this.port = port;
	}



	public String getHost() {
		return host;
	}



	public void setHost(String host) {
		this.host = host;
	}



	public int getPort() {
		return port;
	}



	public void setPort(int port) {
		this.port = port;
	}

	
	
	
}
