package com.briup.util.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.briup.log.Log;
import com.briup.util.Backup;
import com.briup.util.Configuration;
import com.briup.util.ConfigurationAware;
import com.briup.util.WossModuleInit;
import com.briup.web.client.Client;
import com.briup.web.client.Gather;
import com.briup.web.server.DBStore;
import com.briup.web.server.Server;
/**
 * 配置模块：
 * 	   反射创建各个模块对象,完成初始化配置
 * 
 * @author niurui
 *
 */
public class ConfigurationImpl implements Configuration{
	//1.反射创建各个模块对象存储在Map<String,WossModuleInit>集合
	private Map<String,WossModuleInit> map = new HashMap<>();
	
    public ConfigurationImpl() {
    	try {
    		//2>完成各个模块的初始化配置(初始化信息config.xml) Properties
    		Properties properties = new Properties();
    		//2.解析xml文件 dom4j解析
    		//a>创建SaxReader对象
    		SAXReader reader = new SAXReader();
    		//b>解析文件，获取文档树doc
			Document doc = reader.read("src/main/java/com/briup/util/config.xml");
			//c>通过doc获取根节点
			Element root = doc.getRootElement();
			//d>遍历获取子节点
			Iterator<Element> it = root.elementIterator();
			while(it.hasNext()) {
				Element next = it.next();
				String name = next.getName();
				String className = next.attributeValue("class");
				//1>反射创建各个模块对象(获取每个模块的全限定名)
				Class<?> c = Class.forName(className);
				WossModuleInit wmi = (WossModuleInit) c.newInstance();
				map.put(name, wmi);
				//e>遍历获取子节点
				Iterator<Element> it2 = next.elementIterator();
				while(it2.hasNext()) {
				 Element next2 = it2.next();
				 String name2 = next2.getName();
				 String text2 = next2.getText();
				 properties.setProperty(name2, text2);
				}
				//给每个模块对象进行初始化配置 init(Properties)
				wmi.init(properties);
				//3.给各个模块对象传递配置对象  setConfiguration(配置对象)
				if(wmi instanceof ConfigurationAware) {
					((ConfigurationAware) wmi).setConfiguration(this);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	
	}
	@Override
	public Log getLogger() throws Exception {
		return (Log) map.get("logger");
	}

	@Override
	public Server getServer() throws Exception {
		return (Server) map.get("server");
	}

	@Override
	public Client getClient() throws Exception {
		return (Client) map.get("client");
	}

	@Override
	public DBStore getDbStore() throws Exception {
		return (DBStore) map.get("dbstore");
	}

	@Override
	public Gather getGather() throws Exception {
		return (Gather) map.get("gather");
	}
	@Override
	public Backup getBackup() throws Exception {
		return (Backup) map.get("backup");
	}

}
