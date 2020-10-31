package com.briup.client;

import java.util.Collection;

import com.briup.bean.Environment;
import com.briup.util.Configuration;
import com.briup.util.impl.ConfigurationImpl;
import com.briup.web.client.impl.ClientImpl;
import com.briup.web.client.impl.GatherImpl;

public class ClientTest {
	public static void main(String[] args) {
		try {
			//1.采集模块 coll =  new GatherImpl().gather();
			//Collection<Environment> coll = new GatherImpl().gather();
			//2.发送coll对象  new ClientImpl().send(coll);
			//new ClientImpl().send(coll);
			 Class<?> c = Class.forName("com.briup.util.impl.ConfigurationImpl");
			 Configuration con = (Configuration) c.newInstance();
			 Collection<Environment> coll = con.getGather().gather();
			 con.getClient().send(coll);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
