package com.mec.service_discover.appClient;

import com.mec.mec_rmi.core.INode;
import com.mec.mec_rmi.core.RmiClient;
import com.mec.service_discover.registryCenter.RmiServer.IServiceDiscover;
import com.mec.service_discover.registryCenter.core.ServiceDefinition;

public class AppClient {
    private RmiClient sdClient;
    private NodeSelector nodeSelector;
    private IServiceDiscover isd;
    private String serviceId;

    public AppClient() {
        sdClient = new RmiClient();
        nodeSelector = new NodeSelector();
    }

    public void connectToServiceDiscoverPort(int port, String ip) {
        sdClient.setPort(port);
        sdClient.setServerIp(ip);
        nodeSelector.setPort(port);
        nodeSelector.setIp(ip);
        isd = sdClient.getProxy(IServiceDiscover.class);
        nodeSelector.connectToServiceDiscover();
    }

    public INode requestService(String serviceId) {
        if (serviceId.equals("")) {
            return null;
        }

        if (this.serviceId != null && this.serviceId.equals(serviceId)) {
        	nodeSelector.rewind();
            return nodeSelector.getNode();
        }
        this.serviceId = serviceId;
        ServiceDefinition sd = isd.getNodeList(serviceId);
        System.out.println(sd);
        nodeSelector.requestAppServerLIst(serviceId, sd);
        return nodeSelector.getNode();
    }
    
    public INode getNode() {
        return nodeSelector.getNode();
    }

    public void shutdown() {
        nodeSelector.stop();
    }

    public void reWind() {
        nodeSelector.rewind();
    }

}
