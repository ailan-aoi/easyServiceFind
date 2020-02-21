package com.mec.service_discover.appClient;

import java.util.*;

import com.mec.mec_rmi.core.INode;
import com.mec.mec_rmi.core.RmiClient;
import com.mec.service_discover.appServer.IReportStatus;

class ServersHeartCheck {
	private List<INode> nodes;
	private Map<Long, INode> nodeMap;
	private RmiClient client;
	
	ServersHeartCheck() {
		client = new RmiClient();
		nodeMap = new HashMap<>();
	}

	void setNodeList(List<INode> nodes) {
		this.nodes = nodes;
	}
	
	void healthCheck() {
		for (INode node : nodes) {
			client.setPort(node.getPort());
			client.setServerIp(node.getIp());
			try {
				IReportStatus irs = client.getProxy(IReportStatus.class);
				long connectTime = getNetQuality(irs);
				nodeMap.put(connectTime, node);
			} catch(Exception e) {
				continue;
			}
		}
		Set<Long> times = nodeMap.keySet();
		sort(times);
		nodes.clear();
		for (Long time : times) {
			nodes.add(nodeMap.get(time));
		}
		nodeMap.clear();
	}

	private long getNetQuality(IReportStatus irs) {
		long startTime = System.currentTimeMillis();
		irs.getConnectQuality();
		long endTime = System.currentTimeMillis();
		
		return endTime - startTime;
	}
	
	private void sort(Set<Long> set) {
		Set<Long> sortSet = new TreeSet<>(new Comparator<Long>() {
			@Override
			public int compare(Long o1, Long o2) {
				return o1.compareTo(o2);
			}
		});
		sortSet.addAll(set);
	}
}
