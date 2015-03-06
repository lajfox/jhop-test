package com.techstar.sys.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.validation.Valid;

import net.sf.ehcache.Cache;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefaults;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;

import com.google.common.collect.Maps;
import com.techstar.modules.springframework.data.jpa.domain.PageResponse;
import com.techstar.modules.springframework.data.jpa.domain.Response;
import com.techstar.modules.springframework.data.jpa.domain.Results;
import com.techstar.modules.springframework.web.bind.annotation.PreModelAttribute;
import com.techstar.modules.utils.FreeMarkerUtils;
import com.techstar.sys.entity.SystemDicItem;
import com.techstar.sys.entity.SystemDicType;
import com.techstar.sys.service.SystemDicItemService;
import com.techstar.sys.utils.DicItemCacheUtils;

/**
 * 系统字典
 * 
 * @Date 2012-11-2 下午2:06:05
 * @author lrm
 */
@Controller
@RequestMapping(value = "/zd/sysdicitem")
public class SystemDicItemController implements ServletContextAware {
	private static Logger logger = LoggerFactory.getLogger(SystemDicItemController.class);

	@Autowired
	private SystemDicItemService systemDicItemService;
	@Autowired
	private Cache cache;

	private ServletContext servletContext;

	public void setServletContext(ServletContext sc) {
		this.servletContext = sc;
	}

	private static Map<String, String> sysValids = Maps.newHashMap();

	static {
		sysValids.put("1", "有效");
		sysValids.put("0", "无效");
	}

	// 特别设定多个ReuireRoles之间为Or关系，而不是默认的And.
	@RequestMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	Results search(@PageableDefaults(pageNumber = 0, value = 10) Pageable pageable, Specification<SystemDicItem> spc) {
		Page<SystemDicItem> systemDicItem = systemDicItemService.findAll(spc, pageable);
		return new PageResponse<SystemDicItem>(systemDicItem);
	}

	/***
	 * TODO 查询树列表
	 * 
	 * @Date 2013-4-24 上午9:58:08
	 * @author lrm
	 */
	@RequestMapping("/searchTree")
	public @ResponseBody
	Results searchTree(Specification<SystemDicItem> spec) {
		List<SystemDicItem> systemDicItem = systemDicItemService.findAll(spec, new Sort(Sort.Direction.ASC, "orderno"),
				true);
		return new Response<SystemDicItem>(systemDicItem);
	}

	/***
	 * 数据编辑维护
	 * 
	 * @Date 2013-1-21 上午9:38:02
	 * @author lrm
	 */
	@RequestMapping(value = "edit", method = RequestMethod.POST)
	public @ResponseBody
	Results edit(@Valid @PreModelAttribute(disallowedFields = { "parent" }) SystemDicItem systemDicItem,
			@RequestParam("oper") String oper, @RequestParam("sign") String sign,
			@RequestParam("parentid") String typeid, @RequestParam(value = "parentx", required = false) String parentid) {
		long start = System.currentTimeMillis();
		Map<String, Object> data = new HashMap<String, Object>();
		String message = "修改成功";
		if (StringUtils.equals("del", oper)) {
			this.systemDicItemService.deletex(systemDicItem);
			long deleteT = System.currentTimeMillis();
			logger.info("删除字典{}项，用时{}微秒", systemDicItem.getItemname(), (deleteT - start));
			// this.systemDicItemService.delete(systemDicItem);
			// 从缓存中删除
			List<SystemDicItem> items = DicItemCacheUtils.remove(cache, sign, systemDicItem);
			data.put("data", items);
			FreeMarkerUtils.crateHTML(servletContext, data, "/WEB-INF/freemarker/", "dicitem.ftl", "dic", sign);
			logger.info("删除字典项后,生成字典{}html静态内容完成,用时{}微秒", sign, (System.currentTimeMillis() - deleteT));
		} else {
			if (StringUtils.equals("add", oper)) {
				systemDicItem.setId(null);
				message = "保存成功";
			}
			if (StringUtils.isNotEmpty(parentid)) {
				systemDicItem.setParent(systemDicItemService.findOne(parentid));
			}
			SystemDicType systemDicType = this.systemDicItemService.findOne(SystemDicType.class, typeid);
			systemDicItem.setSystemDicType(systemDicType);
			// systemDicItemService.save(systemDicItem);
			systemDicItemService.savex(systemDicItem);

			long saveT = System.currentTimeMillis();
			logger.info("保存字典{}项，用时{}微秒", systemDicItem.getItemname(), (saveT - start));

			// 修改缓存内容
			List<SystemDicItem> items;
			if (systemDicItem.getValid()) {
				items = DicItemCacheUtils.put(cache, sign, systemDicItem);
			} else {
				items = DicItemCacheUtils.remove(cache, sign, systemDicItem);
			}

			long cacheT = System.currentTimeMillis();
			logger.info("修改缓存用时{}微秒", (cacheT - saveT));
			data.put("data", items);
			FreeMarkerUtils.crateHTML(servletContext, data, "/WEB-INF/freemarker/", "dicitem.ftl", "dic", sign);
			logger.info("生成字典{}html静态内容完成，用时{}微秒", sign, (System.currentTimeMillis() - cacheT));
		}
		return new Results(true, message, systemDicItem);
	}

	/***
	 * 字典项唯一性验证
	 * 
	 * @Date 2013-5-3 上午11:29:51
	 * @author lrm
	 */
	@RequestMapping("checkExists/{propName}")
	public @ResponseBody
	Results checkExists(@RequestParam(value = "id", required = false) String id, @RequestParam("name") String name,
			@RequestParam(value = "typeid", required = false) String typeid,
			@RequestParam(value = "mainid", required = false) String mainid, @PathVariable("propName") String propName)
			throws ClassNotFoundException {
		boolean exists = StringUtils.isEmpty(mainid) ? (StringUtils.isNotEmpty(typeid) ? systemDicItemService.exists(
				"itemnameAndSystemDicType.id", name, typeid) : systemDicItemService.exists(propName, name))
				: (StringUtils.isNotEmpty(typeid) ? systemDicItemService.exists(propName
						+ "AndSystemDicType.idAndIdNot", name, typeid, mainid) : systemDicItemService.exists(propName
						+ "AndIdNot", name, mainid));
		return new Results(!exists);
	}

	/***
	 * 选择父节点（在自关联字典项中使用）
	 * 
	 * @Date 2013-5-3 上午11:30:25
	 * @author lrm
	 */
	@RequestMapping("/selectItemParent")
	public String selectItemParent(@RequestParam(value = "edit_id", required = false) String id, Model model,
			@RequestParam(value = "typeid", required = false) String typeid) {
		Sort sort = new Sort(Sort.Direction.ASC, "itemname");
		List<SystemDicItem> systemDicItems = StringUtils.isEmpty(typeid) ? null
				: (StringUtils.isEmpty(id) ? systemDicItemService.findBy("systemDicType.id", sort, typeid)
						: systemDicItemService.findBy("systemDicType.idAndIdNot", sort, typeid, id));
		model.addAttribute("systemDicItems", systemDicItems);
		return "sys/selectItemParent";
	}

	/***
	 * 根据字典类型获取字典项
	 * 
	 * @Date 2013-5-2 下午4:50:05
	 * @author lrm
	 */
	@RequestMapping("type/{type}")
	public String getSelect(@PathVariable("type") String stdSign, Model model) {
		// 先从缓存中读取
		List<SystemDicItem> items = DicItemCacheUtils.get(cache, stdSign);
		if (CollectionUtils.isEmpty(items)) {
			items = this.systemDicItemService.findBy("systemDicTypeSignAndValidTrue", stdSign);
			// 存入缓存
			DicItemCacheUtils.put(cache, stdSign, items);
		}
		model.addAttribute("list", items);
		return "sys/getItemsByType";
	}

	/***
	 * 根据字典类型获取字典项
	 * 
	 * @Date 2013-5-2 下午4:50:05
	 * @author lrm
	 */
	@RequestMapping("type2/{type}")
	public String getSelect2(@PathVariable("type") String stdSign, Model model) {
		// 先从缓存中读取
		List<SystemDicItem> items = DicItemCacheUtils.get(cache, stdSign);
		if (CollectionUtils.isEmpty(items)) {
			items = this.systemDicItemService.findBy("systemDicTypeSignAndValidTrue", stdSign);
			// 存入缓存
			DicItemCacheUtils.put(cache, stdSign, items);
		}
		model.addAttribute("list", items);
		return "sys/getItemsByType2";
	}
}
