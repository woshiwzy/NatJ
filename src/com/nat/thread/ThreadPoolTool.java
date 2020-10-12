package com.nat.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolTool {

	private static ExecutorService ftp;

	public static ExecutorService getThreadPool() {
		if (null == ftp) {
			ftp = Executors.newCachedThreadPool();
		}
		return ftp;
	}

}
