package com.briup.server;

import java.util.Collection;

import com.briup.bean.Environment;
import com.briup.util.Configuration;
import com.briup.web.server.impl.DBStoreImpl;
import com.briup.web.server.impl.ServerImpl;

public class DBStoreTest {
	public static void main(String[] args) throws Exception {
		//1.接收数据
		//Collection<Environment> coll = new ServerImpl().reciver();
		//2.数据入库
		//new DBStoreImpl().saveDb(coll);
		 Class<?> c = Class.forName("com.briup.util.impl.ConfigurationImpl");
		 Configuration con = (Configuration) c.newInstance();
		 Collection<Environment> coll = con.getServer().reciver();
		 con.getDbStore().saveDb(coll);
	}
}
