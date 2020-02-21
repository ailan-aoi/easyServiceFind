package com.mec.service_discover.appClient;

import java.util.List;

import com.mec.mec_rmi.core.INode;
import com.mec.mec_rmi.core.INodeSelector;
import com.mec.mec_rmi.core.RmiClient;
import com.mec.service_discover.registryCenter.RmiServer.IServiceDiscover;
import com.mec.service_discover.registryCenter.core.ServiceDefinition;
import com.my.util.core.TickTick;

class NodeSelector implements INodeSelector {
	private static final int DEFAULT_FLASHTIME = 300000; //5分钟
	private TickTick tickTick;
	private RmiClient client;
	private String serviceId;
	private IServiceDiscover isd;
	private List<INode> nodes;
	private int version;
	private int flashTime;
	private ServersHeartCheck heartCheck;
	private int nodeNumber;
	
	NodeSelector() {
		client = new RmiClient();
		heartCheck = new ServersHeartCheck();
		flashTime = DEFAULT_FLASHTIME;
		nodeNumber = 0;
	}

	void setPort(int port) {
		client.setPort(port);
	}
	
	void connectToServiceDiscover() {
		isd = client.getProxy(IServiceDiscover.class);
		tickTick = new TickTick() {
			@Override
			public void doSomething() {
				synchronized (NodeSelector.class) {
					System.out.println("开始定时检测node");
					flashList(serviceId, version);
					heartCheck.healthCheck();
					nodeNumber = 0;
				}
			}
		}.setTiming(flashTime);
	}

	private void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	
	void requestAppServerLIst(String serviceId, ServiceDefinition sd) {
		if (tickTick.isActive()) {
			tickTick.stop();
		}
		synchronized (NodeSelector.class) {
			nodes = sd.getNodeList();
			version = sd.getVersion();
			nodeNumber = 0;
			setServiceId(serviceId);
			heartCheck.setNodeList(nodes);
		}
		tickTick.startup();
	}
	
	private void flashList(String serviceId, int version) {
		ServiceDefinition sd = isd.getNodeLIst(serviceId, version);
		if (sd == null) {
			return;
		}
		
		nodes = sd.getNodeList();
		this.version = sd.getVersion();
	}

	@Override
	public INode getNode() {
		if (nodeNumber >= nodes.size()) {
			flashList(serviceId, version);
			nodeNumber = 0;
		}
		return nodes.get(nodeNumber++);
	}

	public List<INode> getNodeList() {
		return nodes;
	}

	@Override
	public int getDelayTime() {
		return 100;
	}

	@Override
	public void connectFaliure(INode node) {
		nodes.remove(node);
	}

	void setIp(String ip) {
		client.setServerIp(ip);
	}

	void rewind() {
		nodeNumber = 0;
	}
	
	void stop() {
		tickTick.stop();
	}
	
}
