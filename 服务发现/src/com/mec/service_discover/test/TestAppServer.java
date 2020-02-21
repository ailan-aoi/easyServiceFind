package com.mec.service_discover.test;

import com.mec.service_discover.appServer.AppServer;

public class TestAppServer {
	
	public static void main(String[] args) {
		AppServer server = new AppServer();
		server.setServerDiscoverIp("127.0.0.1");
		server.setServerDiscoverPort(54188);
		server.startup();
		server.signIn("001", 54190, "192.168.34.233");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		server.signIn("001", 54190, "192.168.34.23");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
//		server.signOut();
	}
	
}
