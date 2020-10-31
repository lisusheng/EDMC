package com.briup.server;

import java.util.Collection;

import com.briup.bean.Environment;
import com.briup.web.server.impl.ServerImpl;

public class ServerTest {
	public static void main(String[] args){
		//接收数据  new ServerImpl().reciver();
		try {
			Collection<Environment> coll;
			coll = new ServerImpl().reciver();
			//coll.forEach(System.out::println);
			System.out.println(coll.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
