package com.mec.service_discover.registryCenter.core;

import com.mec.CS_Frame_Work.core.Server;
import com.mec.action.ActionFactory;
import com.mec.mec_rmi.core.RmiResourceFactory;
import com.mec.mec_rmi.core.RmiServer;
import com.mec.service_discover.registryCenter.RmiServer.IServiceDiscover;
import com.mec.service_discover.registryCenter.RmiServer.ServiceDiscover;
import com.mec.service_discover.registryCenter.csServer.ServerAction;

public class RegistryCenterServer {
    private Server csServer;
    private RmiServer rmiServer;

    public RegistryCenterServer() {
    	csServer = new Server();
        rmiServer = new RmiServer();
    }
    
    public void startup() {
        try {
        	ActionFactory.scanAction("com.mec.service_discover.registryCenter.csServer");
        	RmiResourceFactory.registery(ServiceDiscover.class, IServiceDiscover.class);
        	csServer.setServerAction(new ServerAction());
            csServer.startup();
            rmiServer.startup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void shutdown() {
    	csServer.shutdown();
    	rmiServer.shutdown();
    }
    
	public void initActionFactory(String packagePath) {
		ActionFactory.scanAction(packagePath);
	}
	
	public void setCSServerPort(int port) {
		csServer.setport(port);
	}
	
	public void setRmiServerPort(int port) {
		rmiServer.setPort(port);
	}
	
	public void setPort(int csServerPort, int rmiServerPort) {
		setCSServerPort(csServerPort);
		setRmiServerPort(rmiServerPort);
	}
	
	public void initServer(String cfgPath) throws Exception {
		csServer.initServer(cfgPath);
		rmiServer.initServer(cfgPath);
	}
	
}
