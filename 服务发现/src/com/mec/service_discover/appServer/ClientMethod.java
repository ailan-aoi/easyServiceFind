package com.mec.service_discover.appServer;

import com.mec.cs_annocation.ActionMethod;
import com.mec.cs_annocation.ActionPara;
import com.mec.cs_annocation.HasActionMethod;

@HasActionMethod
public class ClientMethod {
	
	@ActionMethod(action="signIn")
	public void signInSuccess(@ActionPara(para="result")boolean result) {
		if (result) {
			System.out.println("注册成功!!!!!");
			return;
		}
		System.out.println("注册失败！！！");
	}
	
	@ActionMethod(action="signOut")
	public void signOutSuccess(@ActionPara(para="result")boolean result) {
		if (result) {
			System.out.println("取消注册成功!!");
			return;
		}
		return;
	}
}
