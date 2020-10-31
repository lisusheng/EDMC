package com.briup.web.server.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Calendar;
import java.util.Collection;
import java.util.Properties;

import com.briup.bean.Environment;
import com.briup.log.Log;
import com.briup.log.impl.LogImpl;
import com.briup.util.Configuration;
import com.briup.web.server.DBStore;
/**
 * 入库模块：
 * 		将服务端接收的数据进行持久化
 * 
 * @author niurui
 *
 */
public class DBStoreImpl implements DBStore{
	private Configuration con;
	private Log log;
	private String driver;
	private String url;
	private String username;
	private String password;
	private int batchSize;
	
	@Override
	public void init(Properties properties) throws Exception {
		driver = properties.getProperty("driver");
		url = properties.getProperty("url");
		username = properties.getProperty("username");
		password = properties.getProperty("password");
		batchSize = Integer.parseInt(properties.getProperty("batch-size"));
		
		
	}

	@Override
	public void setConfiguration(Configuration con) {
		this.con = con;
	}

	@Override
	public void saveDb(Collection<Environment> coll) throws Exception {
		/**
		 * 入库模块：数据持久化 
		 * 1.与数据库建立连接(jdbc技术)
		 * 2.数据入库  表--实体类  列--属性值
		 * 
		 */
		log = con.getLogger();
		//1.与数据建立连接
		//1>注册驱动
		Class.forName(driver);
		//2>建立连接
		Connection conn = DriverManager.getConnection(url, username, password);
//		System.out.println(conn);
		//2.数据入库  1.对象？ 2.采集数据应该放到哪张表？1-31 3.执行速率？
		//3>创建Statement对象:异构sql   PrepareStatement对象:同构sql
		//当前日期是这个月的第几天   Calendar 日历类  静态常量 DAY_OF_MONTH
		int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		String sql = "insert into e_detail_"+day+" values(?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement ps = conn.prepareStatement(sql);
		//4>执行sql语句
		//指明？
		//批处理：批量操作sql语句
		//定义变量统计sql语句
		int i = 0;
		for(Environment e:coll) {
			ps.setString(1, e.getName());
			ps.setString(2, e.getSrcId());
			ps.setString(3, e.getDstId());
			ps.setString(4, e.getDevId());
			ps.setString(5, e.getSersorAddress());
			ps.setInt(6, e.getCount());
			ps.setString(7, e.getCmd());
			ps.setInt(8, e.getStatus());
			ps.setFloat(9, e.getData());
			ps.setTimestamp(10, e.getGather_date());
			//sql加入批处理中
			ps.addBatch();
			//变量i自增
			i++;
			if(i==batchSize) {
				log.info("开始入库...");
			}
			//判断多少条执行一次
			if(i%batchSize==0) {
				//每500条执行sql
				ps.executeBatch();
				//清除批处理集合
				ps.clearBatch();
			}
		}
		//处理不足500的sql
		ps.executeBatch();
		log.info("入库完毕...");
		//5>处理结果集(select)
		//6>关闭资源
		if(ps != null) {
			ps.close();
		}
		if(conn != null) {
			conn.close();
		}
	}

}
