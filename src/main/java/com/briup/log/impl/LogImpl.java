package com.briup.log.impl;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.briup.log.Log;

/*
 * 日志模块：log4j
 */
public class LogImpl implements Log{
	//1.获取根Logger 日志级别-->方法
	private Logger logger = Logger.getRootLogger();
	
	@Override
	public void init(Properties properties) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void debug(String message) {
		//调用log4j相关方法处理信息
		logger.debug(message);
	}

	@Override
	public void info(String message) {
		logger.info(message);
	}

	@Override
	public void warn(String message) {
		logger.warn(message);
	}

	@Override
	public void error(String message) {
		logger.error(message);
	}

	@Override
	public void fatal(String message) {
		logger.fatal(message);
	}

}
