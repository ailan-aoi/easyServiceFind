package com.mec.service_discover.appServer;

import com.mec.CS_Frame_Work.core.IClientAction;

public class ClientActionAdapter implements IClientAction{
	
	@Override
	public void OutOfRoom() {
	}

	@Override
	public void connectTooFast() {
		
	}

	@Override
	public void connectSuccess() {
		synchronized (AppServer.class) {
			System.out.println("��ʼ�������߳�!!!");
			AppServer.class.notify();
		}
	}

	@Override
	public void dealAbnormalDrop() {
		
	}

	
}
