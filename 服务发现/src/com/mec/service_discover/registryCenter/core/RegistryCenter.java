package com.mec.service_discover.registryCenter.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.mec.mec_rmi.core.INode;
import com.mec.mec_rmi.core.Node;

public class RegistryCenter {
    private static Map<String, ServiceDefinition> serviceMap;
    private static Map<String, INode> appServerMap;
    
    static {
        serviceMap = new ConcurrentHashMap<>();
        appServerMap = new ConcurrentHashMap<>();
    }

    public RegistryCenter() { }

    public static ServiceDefinition getNodeList(String serviceId) {
        ServiceDefinition definition = serviceMap.get(serviceId);
        return definition;
    }

    public static ServiceDefinition getNodeList(String serviceId, int version) {
        ServiceDefinition definition = serviceMap.get(serviceId);
        if (definition.getVersion() == version) {
        	return null;
        }
        return definition;
    }

    public static boolean serverSignIn(String clientId, String serviceId, int port, String ip) {
    	INode node = new Node(port, ip, serviceId);
    	boolean result = addServer(clientId, node);
    	if (!result) {
    		return false;
    	}
    	System.out.println("开始正式注册客户端");
        if (serviceMap.containsKey(serviceId)) {
            synchronized (RegistryCenter.class) {
                if (serviceMap.containsKey(serviceId)) {
                    ServiceDefinition definition = serviceMap.get(serviceId);
                    return definition.addServerNode(node);
                }
            }
        }
        System.out.println("客户端注册成功:"+ node + "nodeServiceId:" + node.getServiceId());
        ServiceDefinition definiton = new ServiceDefinition();
        definiton.addServerNode(node);
        serviceMap.put(serviceId, definiton);
        return true;
    }
    
    private static boolean addServer(String clientId, INode node) {
    	if (appServerMap.containsKey(clientId)) {
    		return false;
    	}
    	System.out.println("注册的客户端:" + clientId);
    	appServerMap.put(clientId, node);
    	return true;
    }

    public static boolean serverSignOut(String clientId) {
    	System.out.println("clientId:" + clientId);
    	INode node = appServerMap.remove(clientId);
    	if (node != null) {
    		System.out.println("客户端下线:"+ node + "nodeServiceId" + node.getServiceId());
    		offLine(node);
    		return true;
    	}
    	return false;
    }
    
    private static void offLine(INode node) {
    	String serviceId = node.getServiceId();
    	System.out.println(serviceId);
        if (serviceMap.containsKey(serviceId)) {
            synchronized (RegistryCenter.class) {
                if (serviceMap.containsKey(serviceId)) {
                    ServiceDefinition definition = serviceMap.get(serviceId);
                    definition.removeServerNode(node);
                }
            }
        }
    }
}
