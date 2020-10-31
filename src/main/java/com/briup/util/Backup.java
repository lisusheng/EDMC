package com.briup.util;
/**
 * Simple to Introduction
 * 
 * @ProjectName: 智能家居之环境监控系统
 * @Package: com.briup.environment.util
 * @InterfaceName: Backup
 * @Description: Configuration接口提供了备份模块的规范。
				Backup的实现类需要实现将程序中的数据写入本地文件进行存储的功能，
				以及从本地文件读取备份、删除备份的功能。
 * @CreateDate: 2018-1-25 14:28:30
 * @author briup
 * @Version: 1.0
 */
public interface Backup extends WossModuleInit {

	/**
	 * 备份数据
	 * 
	 * @param fileName
	 *            备份文件
	 * @param data
	 *            备份数据
	 * @throws Exception
	 */
	public void backup(String fileName, Object data) throws Exception;

	/**
	 * 加载备份
	 * 
	 * @param fileName
	 *            备份文件
	 * @return 备份数据
	 * @throws Exception
	 */
	public Object load(String fileName) throws Exception;

	/**
	 * 删除备份
	 * 
	 * @param fileName
	 */
	public void deleteBackup(String fileName);

}
