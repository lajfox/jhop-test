package com.techstar.sys.utils;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.ContextLoader;

import com.google.common.collect.Lists;
import com.techstar.sys.entity.SystemDicItem;
import com.techstar.sys.repository.jpa.SystemDicItemDao;

/***
 * 字典工具类
 * 
 * @author lrm
 * @version 2013-04-25
 */

public final class DictUtils {

	private final static SystemDicItemDao systemDicItemDao = ContextLoader.getCurrentWebApplicationContext().getBean("systemDicItemDao",
			SystemDicItemDao.class);
	

	/***
	 * 根据字典类型以及相关字典项获取字典项列表
	 * @Date 2013-4-28 上午10:42:36
	 * @author lrm
	 */
	public static List<SystemDicItem> getSysDicItems(String type,String itemname){
		if(StringUtils.isEmpty(itemname)){
			return getDictList(type);//标准字典项获取
		}else{
			return getSubDictList(type,itemname);//级联字典项列表获取
		}
	}
	/***
	 * 根据字典类型获取字典项对象列表
	 * 
	 * @Date 2013-4-25 下午3:49:27
	 * @author lrm
	 */
	public static List<SystemDicItem> getDictList(String type) {

		List<SystemDicItem> systemDicItems = systemDicItemDao.findObjByStdSign(type);
		return systemDicItems;
	}

	/***
	 * TODO 根据字典类型以及字典项，获取子字典项列表
	 * 
	 * @Date 2013-4-25 下午3:49:27
	 * @author lrm
	 */
	public static List<SystemDicItem> getSubDictList(String type, String itemname) {
		SystemDicItem systemDicItem = systemDicItemDao.findOne("systemDicType.signAndItemnameAndValidTrue", type, itemname);
		List<SystemDicItem> subSystemDicItems;
		if (systemDicItem != null) {
			subSystemDicItems = systemDicItemDao.findSubDictList(systemDicItem.getId());
		} else {
			subSystemDicItems = Lists.newArrayList();
		}
		return subSystemDicItems;
	}

}
