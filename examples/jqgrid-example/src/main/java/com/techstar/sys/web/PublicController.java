package com.techstar.sys.web;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.techstar.modules.springframework.data.jpa.domain.PageResponse;
import com.techstar.modules.springframework.data.jpa.domain.Results;
import com.techstar.sys.entity.CalendarEvent;
import com.techstar.sys.service.SystemDicTypeService;

/***
 * 公共实现类
 * 
 * @author lrm
 */
@Controller
@RequestMapping(value = "/public")
public class PublicController {
    private static Logger logger = LoggerFactory.getLogger(PublicController.class);

    @Autowired
    private SystemDicTypeService systemDicTypeService;;

    /***
     * 功能：单表属性值唯一性校验
     * 
     * @param {className} 要验证属性的实体类全路径
     * @param {propName} 要验证的属性名称
     * @param id
     *            当前编辑数据的ID
     * @param name
     *            当前编辑数据的值
     * @Date 2013-5-6 上午10:50:29
     * @author lrm
     * @throws ClassNotFoundException
     */
    @RequestMapping("checkExists/{className}/{propName}")
    public @ResponseBody
    Results checkExists(@RequestParam(value = "id", required = false) String id, @RequestParam("name") String name,
	    @PathVariable("className") String className, @PathVariable("propName") String propName)
	    throws ClassNotFoundException {
	boolean exists = StringUtils.isEmpty(id) ? systemDicTypeService
		.exists(Class.forName(className), propName, name) : systemDicTypeService.exists(
		Class.forName(className), propName + "AndIdNot", name, id);
	return new Results(!exists);
    }

    /***
     * 功能描述：主对象下子对象数据唯一性验证
     * 
     * @param {className} 要验证属性的实体类全路径：com.techstar.sys.entity.SystemDicItem
     * @param {parentId} 主对象主键引用：如SystemDicItem中的SystemDicType.id
     * @param {propName} 要验证的属性：itemname
     * @param id
     *            当前编辑数据的ID
     * @param name
     *            当前编辑数据的值
     * @param parentid
     *            父ID
     * @throws ClassNotFoundException
     * @since 2013-05-21
     * @author lrm
     */
    @RequestMapping("checkExists/{className}/{parentId}/{propName}")
    public @ResponseBody
    Results checkExists(@RequestParam(value = "id", required = false) String id, @RequestParam("name") String name,
	    @RequestParam(value = "parentid", required = false) String parentid,
	    @PathVariable("className") String className, @PathVariable("propName") String propName,
	    @PathVariable("parentId") String parent) throws ClassNotFoundException {
	boolean exists = StringUtils.isEmpty(id) ? (StringUtils.isNotEmpty(parentid) ? systemDicTypeService.exists(
		Class.forName(className), propName + "And" + parent, name, parentid) : systemDicTypeService.exists(
		Class.forName(className), propName, name)) : (StringUtils.isNotEmpty(parentid) ? systemDicTypeService
		.exists(Class.forName(className), propName + "And" + parent + "AndIdNot", name, parentid, id)
		: systemDicTypeService.exists(Class.forName(className), propName + "AndIdNot", name, id));
	return new Results(!exists);
    }

    @RequestMapping(value = "/searchEvents", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Results searchEvents() {
	List<CalendarEvent> calendarEventList = Lists.newArrayList();
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	Calendar calendar = Calendar.getInstance();
	calendar.add(Calendar.DAY_OF_MONTH, -2);
	calendarEventList.add(new CalendarEvent("重要事件(2)", formatter.format(new Date()),"http://mail.163.com","label label-important"));
	calendarEventList.add(new CalendarEvent("警告事件(5)", formatter.format(calendar.getTime()),null,"label label-warning"));
	return new PageResponse<CalendarEvent>(calendarEventList);
    }

    @RequestMapping(value = "/searchEvents1", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    List<CalendarEvent> searchEvents1() {
	List<CalendarEvent> calendarEventList = Lists.newArrayList();
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat formatter24 = new SimpleDateFormat("yyyy-MM-dd HH:ss");
	Calendar calendar = Calendar.getInstance();
	calendar.add(Calendar.DAY_OF_MONTH, -2);
	
	calendarEventList.add(new CalendarEvent("重要事件(2)", formatter.format(new Date()),"http://mail.163.com","label label-important"));
	calendarEventList.add(new CalendarEvent("警告事件(5)", formatter.format(calendar.getTime()),null,"label label-warning"));
	calendar.add(Calendar.DAY_OF_MONTH, 1);
	calendar.set(Calendar.HOUR_OF_DAY, 9);
	calendar.set(Calendar.MINUTE, 30);
	calendarEventList.add(new CalendarEvent("默认事件(7)", formatter24.format(calendar.getTime()),null,"label label-default"));
	return calendarEventList;
    }
    
    public static void main(String []args){
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat formatter24 = new SimpleDateFormat("yyyy-MM-dd HH:ss");
	Calendar calendar = Calendar.getInstance();
	calendar.add(Calendar.DAY_OF_MONTH, -2);
	
	System.out.println(formatter.format(new Date())+"  "+formatter.format(calendar.getTime()));
	
	calendar.add(Calendar.DAY_OF_MONTH, 1);
	calendar.set(Calendar.HOUR_OF_DAY, 9);
	calendar.set(Calendar.MINUTE, 30);
	System.out.println(formatter.format(new Date())+"  "+formatter24.format(calendar.getTime()));
    }
}
