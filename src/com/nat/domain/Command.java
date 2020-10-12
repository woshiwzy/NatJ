package com.nat.domain;

import com.alibaba.fastjson.JSON;

public class Command {

	private String taskHost;
	private int taskPort;
	private int forWardPort;
	
	private String taskProtocol;
	
	public static String PROTOCOL_HTTP="http";
	public static String PROTOCOL_HTTPS="https";
	
	
	public Command(String taskHost, int taskPort,int forWardPort, String taskProtocol) {
		super();
		this.taskHost = taskHost;
		this.taskPort = taskPort;
		this.forWardPort=forWardPort;
		this.taskProtocol = taskProtocol;
	}
	
	
	public String getTaskHost() {
		return taskHost;
	}
	public void setTaskHost(String taskHost) {
		this.taskHost = taskHost;
	}
	public int getTaskPort() {
		return taskPort;
	}
	public void setTaskPort(int taskPort) {
		this.taskPort = taskPort;
	}
	public String getTaskProtocol() {
		return taskProtocol;
	}
	public void setTaskProtocol(String taskProtocol) {
		this.taskProtocol = taskProtocol;
	}
	
	

	public int getForWardPort() {
		return forWardPort;
	}


	public void setForWardPort(int forWardPort) {
		this.forWardPort = forWardPort;
	}


	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
