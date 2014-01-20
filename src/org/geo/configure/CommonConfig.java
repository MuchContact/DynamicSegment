package org.geo.configure;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Properties;

/**
 * 配置读取类
 * 
 * @author lemon
 * 
 */
public class CommonConfig {
	/**
	 * 配置文件名
	 */
	private String propFileName;
	/**
	 * 配置文件读取结点
	 */
	private Properties properties = null;

	/**
	 * 初始化配置文件名称
	 */
	public CommonConfig() {
		this.propFileName = "common.properties";
		getPropInputStream();
	}

	/**
	 * 初始化配置文件名称
	 * 
	 * @param propFileName
	 *            *.properties配置文件名称
	 */
	public CommonConfig(String propFileName) {
		this.propFileName = propFileName;
		getPropInputStream();
	}

	/**
	 * 获取配置文件中的参数
	 * 
	 * @param parameterName
	 *            需要获取的参数名称
	 * @return 返回配置参数
	 */
	public String getInitParameter(String parameterName) {
		String strRlt = "";
		byte[] temp = null;
		try {
			String strReturnValue = properties.getProperty(parameterName);
			// 判断属性值是否为null modify by cAn
			if (strReturnValue != null) {
				temp = strReturnValue.getBytes("ISO-8859-1");
				strRlt = new String(temp, "UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return strRlt;
	}

	/**
	 * 设置配置文件中的参数
	 * 
	 * @param parameterName
	 *            参数名称
	 * @param parameterValue
	 *            对应参数的值
	 */
	public synchronized void setInitParameter(String parameterName, String parameterValue) {
		properties.setProperty(parameterName, parameterValue);
	}

	/**
	 * 将此 Properties 表中的属性列表（键和元素对）写入输出流
	 * 
	 * @return
	 */
	public synchronized boolean storeInitParameter() {
		try {
			String absolutePath = URLDecoder.decode(getClass().getResource("/").getPath()+propFileName,"utf-8");
			OutputStream fos = new FileOutputStream(absolutePath);
			properties.store(fos, "## stored ###");
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	/**
	 * 移除配置文件中的参数
	 * 
	 * @param parameterName
	 *            参数名称
	 */
	public synchronized void removeInitParameter(String parameterName) {
		properties.remove(parameterName);
	}

	/**
	 * 
	 */
	private void getPropInputStream() {
		try {
			properties = new Properties();
			InputStream is = getClass().getClassLoader().getResourceAsStream(
					propFileName);
			properties.load(is);
			is.close();
		} catch (FileNotFoundException e) {
			System.err.println("配置文件"+this.propFileName+"找不到。");
			e.getMessage();
		} catch (IOException e) {
			System.err.println("读取配置文件"+this.propFileName+"错误。");
			e.getMessage();
		}
	}
}
