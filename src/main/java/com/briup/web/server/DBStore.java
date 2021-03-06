package com.briup.web.server;

import java.util.Collection;

import com.briup.bean.Environment;
import com.briup.util.ConfigurationAware;
import com.briup.util.WossModuleInit;

/**
 * Simple to Introduction
 * @ProjectName:  物联网环境数据监测中心
 * @Description:  DBStore提供了入库模块的规范。<br/>
 * 				 该接口的实现类将Environment集合持久化。
 * @author lw
 * @Version: 1.0
 */
public interface DBStore extends WossModuleInit,ConfigurationAware{
	/**
	 * 将Environment集合进行持久化 。
	 * @param coll 需要储存的Environment集合
	 * @throws Exception
	 */
	public void saveDb(Collection<Environment> coll)throws Exception;
}
