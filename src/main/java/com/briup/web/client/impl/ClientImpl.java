package com.briup.web.client.impl;

import java.io.BufferedOutputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Collection;
import java.util.Properties;

import com.briup.bean.Environment;
import com.briup.log.Log;
import com.briup.log.impl.LogImpl;
import com.briup.util.Configuration;
import com.briup.web.client.Client;
/**
 * 网络模块：将采集的数据发送给服务端
 * 
 * @author niurui
 *
 */
public class ClientImpl implements Client{
	private Log log;
	private String ip;
	private int port;
	private Configuration con;
	
	@Override
	public void init(Properties properties) throws Exception {
		 ip = properties.getProperty("ip");
		 port = Integer.parseInt(properties.getProperty("port"));
	}

	@Override
	public void setConfiguration(Configuration con) {
		this.con = con;
	}

	@SuppressWarnings("resource")
	@Override
	public void send(Collection<Environment> coll) throws Exception {
		/**
		 * 客户端：把采集的数据(coll对象)发送到服务端
		 * 1.与服务端建立连接 (网络编程)
		 * 2.数据发送 (IO流)
		 * 	  序列化：把对象写入到流中  前提：实现序列化接口
		 */
		log = con.getLogger();
		log.info("客户端已开启.....");
		//1>创建套接字socket(ip,port)
		Socket socket = new Socket(ip,port);
		//2>发送数据(coll对象)  
		//获取对象输出流 (缓冲流输出流(网络输出流))
		ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
		log.info("开始发送数据...");
		oos.writeObject(coll);
		log.info("发送数据完毕！");
		//3>关闭资源
		if(oos != null) {
			oos.close();
		}
		if(socket != null) {
			socket.close();
		}
	}

}
