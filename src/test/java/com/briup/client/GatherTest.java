package com.briup.client;

import java.util.Collection;
import java.util.Iterator;

import com.briup.bean.Environment;
import com.briup.util.impl.ConfigurationImpl;
import com.briup.web.client.impl.GatherImpl;

public class GatherTest {
	public static void main(String[] args) throws Exception {
		ConfigurationImpl con = new ConfigurationImpl();
		Collection<Environment> coll = con.getGather().gather();
		System.out.println(coll.size());
	}
}
