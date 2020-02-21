package com.mec.service_discover.test;

import com.mec.service_discover.appClient.AppClient;

public class TestAppClient {
	
	public static void main(String[] args) {
		AppClient client = new AppClient();
		client.connectToServiceDiscoverPort(54199, "127.0.0.1");
		client.requestService("001");
	}
	
}
