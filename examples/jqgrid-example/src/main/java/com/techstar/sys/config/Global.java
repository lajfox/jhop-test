package com.techstar.sys.config;

import com.techstar.modules.utils.PropertiesLoader;

/**
 * 全局配置类
 * @author lrm
 * @version 2013-05-02
 */
public class Global {
	
	/**
	 * 属性文件加载对象
	 */
	private static PropertiesLoader propertiesLoader;
	
	/**
	 * 获取配置
	 */
	public static String getConfig(String key) {
		if (propertiesLoader == null){
			propertiesLoader = new PropertiesLoader("application.properties");
		}
		return propertiesLoader.getProperty(key);
	}
	
}
