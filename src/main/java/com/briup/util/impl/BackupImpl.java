package com.briup.util.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Properties;

import com.briup.util.Backup;

public class BackupImpl implements Backup{

	@Override
	public void init(Properties properties) throws Exception {
		
	}
	
	/**
	 * 备份方法：将对象写入到备份文件中  序列化
	 */
	@Override
	public void backup(String fileName, Object data) throws Exception {
		//1.创建对象流
		ObjectOutputStream oos = new ObjectOutputStream(
				      new BufferedOutputStream(
				      new FileOutputStream(fileName)));
		//2.将对象写入到指定文件中
		oos.writeObject(data);
		//3.关闭资源
		oos.close();
	}

	/**
	 * 加载备份文件：从备份中读取对象  反序列化
	 */
	@Override
	public Object load(String fileName) throws Exception {
		//1.判断备份文件是否存在
		File file = new File(fileName);
		Object obj = null;
		if(file.exists()) {
			//2.创建对象输入流
			ObjectInputStream ois = new ObjectInputStream(
					              new BufferedInputStream(
					              new FileInputStream(file)));
			//3.获取对象
			obj = ois.readObject();
			//4.关闭资源
			ois.close();
		}
		return obj;
	}

	/**
	 * 删除备份文件
	 */
	@Override
	public void deleteBackup(String fileName) {
		File file = new File(fileName);
		file.deleteOnExit();
	}

}
