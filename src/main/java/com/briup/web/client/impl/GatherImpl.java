package com.briup.web.client.impl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import javax.imageio.stream.FileImageInputStream;

import com.briup.bean.Environment;
import com.briup.log.Log;
import com.briup.util.Backup;
import com.briup.util.Configuration;
import com.briup.web.client.Gather;

/**
 * 采集模块
 *  需要注意一点：数据内容是随时会增加的
 *  下次采集前需要跳过上次已经采集的字节数
 *  
 * @author niurui
 *
 */
public class GatherImpl implements Gather{
	private Log log;
	private String srcFile;
	private String recordFile; //记录文件
	private Configuration con;
	private String backFile;
	
	@Override
	public void init(Properties properties) throws Exception {
		srcFile = properties.getProperty("src-file");
		recordFile = properties.getProperty("record-file");
		backFile = properties.getProperty("back-file");
	}

	@Override
	public void setConfiguration(Configuration con) {
		this.con = con;
	}

	@SuppressWarnings("resource")
	@Override
	public Collection<Environment> gather() throws Exception {
		/**
		 * 采集模块：radwtmp文档数据的解析以及封装形成清单
		 * 1.选取什么IO流 需要一行一行读取数据  readLine()
		 * 		BufferedReader readLine()
		 *      RandomAccessFile readLine() skip()
		 * 2.数据如何封装成环境对象
		 *    1>创建环境对象  new 
		 *    2>解析后的内容设置给对象相应的属性 
		 *    str = 100|101|2|256|1|3|007c03|1|1516945923610
		 *    strs[] =str.split("[|]");
		 *    e.set(strs[0]);
		 *    
		 *    if("16".equals(strs[3])){   
		 *    		//5d606f7802
		 *    		//温度   前两个字节  2个字节=16个二进制---->个十六进制位
		 *    		//湿度  后两个字节
		 *    }
		 *    
		 * 3.如何将对象放到集合中
		 *  list.add(e)
		 */
		Collection<Environment> list = null;
		//获取备份对象
		Backup backup = con.getBackup();
		//是否存在备份文件
		Object obj = backup.load(backFile);
		//判断备份文件中对象是否存在
		if(obj!=null) {
			//之前出现问题，备份文件存储了对象
			//本次采集需要加载备份文件中的对象
			list = (Collection<Environment>) obj;
			log.info("备份的条数:"+list.size());
			//删除备份文件
			backup.deleteBackup(backFile);
		}else {
			list = new ArrayList<>();
		}
		log = con.getLogger();
		//1.创建流，解析数据，封装数据
		RandomAccessFile raf = new RandomAccessFile(srcFile, "r");
		//判断此次采集是否是第一次采集,即判断记录文件是否存在
		File record = new File(recordFile);
		long num1 = 0; //统计记录文件中的字节数
		if(record.exists()) {
			//记录文件存在，此时不是第一次采集，本次需跳过记录的字节数
			DataInputStream dis = new DataInputStream(new FileInputStream(record));
			num1 = dis.readLong()+2;
			log.info("跳过的字节数："+num1);
		}
		//记录本次采集的最大字节数
		long num2 = raf.length();
		//把当前所采集的字节数写入到记录文件中
		DataOutputStream dos = new DataOutputStream(new FileOutputStream(record));
		//跳过记录文件中的字节数
		raf.seek(num1);
		//1>声明变量str 接收每次读取一行的数据
		String str = null;
		//4>创建集合对象
	
		//定义变量 统计次数
		int count1 = 0;
		int count2 = 0;
		int count3 = 0;
		int count4 = 0;
		try {
			while((str=raf.readLine())!=null) {
				//3>构建environment对象
				Environment e = new Environment();
				//str="100|101|2|16|1|3|5d606f7802|1|1516323596029"
				//2>解析数据
				String[] strs = str.split("[|]");
				//发送端id
				e.setSrcId(strs[0]);
				//树莓派id
				e.setDstId(strs[1]);
				//实验区域id
				e.setDevId(strs[2]);
				//传感器地址
				e.setSersorAddress(strs[3]);
				//传感器个数
				e.setCount(Integer.parseInt(strs[4]));
				//指令标号
				e.setCmd(strs[5]);
				//状态
				e.setStatus(Integer.parseInt(strs[7]));
				//采集时间
				e.setGather_date(new Timestamp(Long.parseLong(strs[8])));
				//数据 温度 湿度 光照强度 二氧化碳
				if("16".equals(strs[3])) {
					//温度  前两个字节  ((float)value*0.00268127)-46.85
					e.setName("温度");
					//前两个字节转化为十进制
					int value = Integer.parseInt(strs[6].substring(0, 4),16);
					e.setData((float)(value*0.00268127-46.85));
					count1++;
					list.add(e);
					//湿度 ((float)value*0.00190735)-6
					int value1 = Integer.parseInt(strs[6].substring(4, 8),16);
					Environment en = new Environment("湿度", strs[0], strs[1], strs[2], strs[3], Integer.parseInt(strs[4]), strs[5], Integer.parseInt(strs[7]),(float)(value1*0.00190735-6),new Timestamp(Long.parseLong(strs[8])));
					count2++;
					list.add(en);
				}else if("256".equals(strs[3])) {
					//光照强度
					e.setName("光照强度");
					e.setData(Integer.parseInt(strs[6].substring(0, 4),16));
					count3++;
					list.add(e);
				}else if("1280".equals(strs[3])) {
					//二氧化碳
					e.setName("二氧化碳");
					e.setData(Integer.parseInt(strs[6].substring(0, 4),16));
					count4++;
					list.add(e);
					if(count4==1000) {
						throw new Exception("自定义异常");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			//此时出现了问题，需要备份数据
			backup.backup(backFile, list);
			//流读取的字节数
			num2 = raf.getFilePointer();
		}
		dos.writeLong(num2);
		log.info("温度条数"+count1);
		log.info("湿度条数"+count2);
		log.info("光照强度条数"+count3);
		log.info("二氧化碳条数"+count4);
		return list;
	}

}
