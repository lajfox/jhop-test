package com.techstar.sys.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import net.sf.ehcache.Cache;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.ServletContextAware;

import com.techstar.modules.springframework.data.jpa.repository.MyJpaRepository;
import com.techstar.modules.springframework.data.jpa.service.JsonTreeJpaServiceImpl;
import com.techstar.modules.utils.FreeMarkerUtils;
import com.techstar.sys.entity.SystemDicItem;
import com.techstar.sys.entity.SystemDicType;
import com.techstar.sys.repository.jpa.SystemDicItemDao;
import com.techstar.sys.utils.DicItemCacheUtils;

@Component
@Transactional(readOnly = true)
public class SystemDicItemService extends JsonTreeJpaServiceImpl<SystemDicItem, String> implements ServletContextAware {

	@Autowired
	private SystemDicItemDao systemDicItemDao;
	@Autowired
	private Cache cache;

	private ServletContext servletContext;

	@Override
	protected MyJpaRepository<SystemDicItem, String> getMyJpaRepository() {
		return systemDicItemDao;
	}

	public void setServletContext(ServletContext sc) {
		this.servletContext = sc;
	}

	/**
	 * 将数据放到缓存中并生成html静态内容
	 */
	@PostConstruct
	public void init() {
		logger.info("将字典内容放到缓存中 begin...");
		List<SystemDicItem> items = null;
		Map<String, Object> data = new HashMap<String, Object>();
		List<SystemDicType> types = this.findAll(SystemDicType.class);
		for (SystemDicType type : types) {
			items = this.findBy("systemDicTypeAndValidTrue", new Sort(Direction.ASC, "orderno"), type);
			if (CollectionUtils.isNotEmpty(items)) {
				DicItemCacheUtils.put(cache, type.getSign(), items);
				logger.info("将字典{}内容放到缓存完成", type.getTypename());

				data.put("data", items);
				FreeMarkerUtils.crateHTML( servletContext, data,"/WEB-INF/freemarker/", "dicitem.ftl",
						"dic", type.getSign());

				logger.info("生成字典{}html静态内容完成", type.getTypename());
			}
		}
		logger.info("将字典内容放到缓存中 end...");
	}

	public List<Map<String, Object>> getSelect(final String stdSign) {
		List<Map<String, Object>> systemDicItems = this.systemDicItemDao.findByStdSign(stdSign);
		return systemDicItems;
	}

	/***
	 * TODO 根据字典标识获取字典项
	 * 
	 * @Date 2013-1-23 上午10:54:02
	 * @author lrm
	 */
	public List<SystemDicItem> getSystemDicItem(final String stdSign) {
		List<SystemDicItem> systemDicItems = this.systemDicItemDao.findObjByStdSign(stdSign);
		return systemDicItems;
	}

}
