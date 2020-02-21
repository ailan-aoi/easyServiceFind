package com.mec.service_discover.appServer;

import java.io.IOException;

import com.mec.CS_Frame_Work.core.Client;
import com.mec.CS_Frame_Work.core.IClientAction;
import com.mec.action.ActionFactory;
import com.mec.mec_rmi.core.RmiResourceFactory;
import com.mec.mec_rmi.core.RmiServer;
import com.my.util.core.ArguementMarker;

public class AppServer {
	private Client client;
	private RmiServer server;
	
	public AppServer() {
		try {
			this.client = new Client();
			server = new RmiServer();
			ActionFactory.scanAction("com.mec.service_discover.appServer");
			RmiResourceFactory.registery(ReportStatus.class, IReportStatus.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setServiceDiscoverClientAction(IClientAction clientAction) {
		client.setClientAction(clientAction);
	}
	
	public void setServerDiscoverPort(int port) {
		client.setServerPort(port);
	}
	
	public void setServerDiscoverIp(String ip) {
		client.setServerIp(ip);
	}
	
	private void setHeartCheckPort(int port) {
		server.setPort(port);
	}
	
	public void startup() {
		try {
			setServiceDiscoverClientAction(new ClientActionAdapter());
			client.connectTOServer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void signIn(String serverId, int servicePort, String serviceIp) {
		try {
			System.out.println("开始注册!!!");
			String clientId = client.getConversation().getId();
			synchronized (AppServer.class) {
				if (clientId == null) {
					try {
						System.out.println("等待conversation返回结果！！进入阻塞队列!!");
						AppServer.class.wait();
						clientId = client.getConversation().getId();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			ArguementMarker am = new ArguementMarker();
			am.addParam("clientId", clientId);
			am.addParam("serviceId", serverId);
			am.addParam("port", servicePort);
			am.addParam("ip", serviceIp);
			client.sentRequest("signIn", "signIn", am.toString());
			setHeartCheckPort(servicePort);
			server.startup();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void signOut() {
		ArguementMarker am = new ArguementMarker();
		String clientId = client.getConversation().getId();
		am.addParam("clientId", clientId);
		client.sentRequest("signOut", "signOut", am.toString());
	}
	
	public void offline() {
		server.shutdown();
		client.shutdown();
	}
	
}
