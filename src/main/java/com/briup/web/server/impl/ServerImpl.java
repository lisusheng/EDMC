package com.briup.web.server.impl;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.Properties;

import com.briup.bean.Environment;
import com.briup.log.Log;
import com.briup.log.impl.LogImpl;
import com.briup.util.Configuration;
import com.briup.web.server.Server;
/**
 * 网络模块：
 * 		接收客户端采集后传递的数据
 * 
 * @author niurui
 *
 */
public class ServerImpl implements Server{
	private ServerSocket server = null;
	private Socket client = null;
	private int port;
	private Log log;
	private Configuration con;
	
	@Override
	public void init(Properties properties) throws Exception {
		port = Integer.parseInt(properties.getProperty("port"));
	}

	@Override
	public void setConfiguration(Configuration con) {
		this.con = con;
	}

	@SuppressWarnings("resource")
	@Override
	public Collection<Environment> reciver() throws Exception {
		/**
		 * 服务端：接收数据(coll对象)
		 * 1.与客户端建立连接
		 * 2.接收数据
		 * 	  反序列化：在流中读取对象  前提：实现序列化接口
		 */
		log = con.getLogger();
		log.info("服务端已开启,等待连接...");
		//1>创建套接字ServerSocket(port)
		server = new ServerSocket(port);
		//2>监听是否有客户端建立连接 accept();
		client = server.accept();
		log.info("已连接....");
		//3>接收数据
		//获取对象输入流(缓冲输入流(网络输入流))
		ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(client.getInputStream()));
		log.info("开始接收数据.....");
		Collection<Environment> coll = (Collection<Environment>) ois.readObject();
		log.info("接收完毕！");
		//4>关闭资源 流的资源
		if(ois != null) {
			ois.close();
		}
		return coll;
	}

	@Override
	public void shutdown() {
		if(server != null) {
			try {
				server.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(client != null) {
			try {
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
