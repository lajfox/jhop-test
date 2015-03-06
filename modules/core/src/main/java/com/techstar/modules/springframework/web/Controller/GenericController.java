package com.techstar.modules.springframework.web.Controller;

import java.io.Serializable;
import java.util.Collection;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techstar.modules.springframework.data.jpa.domain.Results;
import com.techstar.modules.springframework.data.jpa.service.MyJpaService;
import com.techstar.modules.springframework.web.bind.annotation.PreModelAttribute;
import com.techstar.modules.springframework.web.bind.annotation.Preparable;
import com.techstar.modules.utils.Identities;
import com.techstar.modules.utils.Reflections;

/**
 * 增册改查公共handler类
 * 
 * <p>
 * 1:generic或generic/list 数据列表
 * </p>
 * <p>
 * 子类可以复写 {@link GenericController#listBefore(Model)} 对数据列表进行前处理
 * </p>
 * <p>
 * 2:generic/edit 编辑保存数据
 * </p>
 * <p>
 * 子类可以复写 {@link GenericController#editBefore(Object, String)} 对编辑保存数据进行前处理
 * </p>
 * <p>
 * 子类可以复写 {@link GenericController#editAfter(Object, String)} 对编辑保存数据进行后处理
 * </p>
 * <p>
 * 3:generic/get/{id} 读取业务数据
 * </p>
 * <p>
 * 4:generic/delete?id= 删除数据
 * </p>
 * <p>
 * 子类可以复写 {@link GenericController#deleteBefore(Object)} 对数据据删除进行前处理
 * </p>
 * <p>
 * 子类可以复写 {@link GenericController#deleteAfter(Object)} 对数据据删除进行后处理
 * </p>
 * <p>
 * 5:generic/batch/delete?id=&id=&id... 批量删除数据
 * </p>
 * 
 * 
 * @author sundoctor
 * 
 * @param <T>
 * @param <ID>
 */
public abstract class GenericController<T, ID extends Serializable> {

	protected static final Logger logger = LoggerFactory.getLogger(GenericController.class);

	protected Class<T> entityClass;// Controller所管理的Entity类型.

	/**
	 * 在构造函数中将泛型T.class赋给entityClass.
	 */
	public GenericController() {
		entityClass = Reflections.getClassGenricType(getClass());
	}

	protected abstract MyJpaService<T, ID> getService();

	/**
	 * 数据列表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "generic", "generic/list" })
	public String generic(Model model, @RequestParam(value = "forward", required = false) String forward) {

		if (StringUtils.isEmpty(forward)) {
			listBefore(model);

			model.addAttribute("uuid", Identities.uuid2());
			String simpleName = StringUtils.lowerCase(entityClass.getSimpleName());
			return new StringBuilder(simpleName).append("/").append(simpleName).append("List").toString();
		} else {
			model.addAttribute("uuid", Identities.uuid2());
			return forward;
		}
	}

	/**
	 * 编辑保存数据
	 * 
	 * @param entity
	 *            业务实体
	 * @param oper
	 *            操作标识，如add,edit,del
	 * @return
	 */
	@RequestMapping(value = "generic/edit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	Results edit(@Valid @PreModelAttribute T entity, @RequestParam(value = "oper", required = false) String oper) {

		String message = "修改成功";
		if (StringUtils.equals("add", oper)) {
			message = "保存成功";
		}

		editBefore(entity, oper);

		getService().save(entity);

		editAfter(entity, oper);

		return new Results(true, message, entity);
	}

	/**
	 * 读取详细数据
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "generic/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Results get(@PathVariable("id") String id) {
		T entity = getService().findOne(getEntityClass(), id);
		return new Results(entity);
	}

	/**
	 * 删除数据
	 * 
	 * @param entity
	 * @return
	 */
	@RequestMapping(value = "generic/delete", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	Results delete(@Preparable("id") T entity) {

		// T entity = getService().findOne(getEntityClass() ,id);

		deleteBefore(entity);

		this.getService().delete(entity);

		deleteAfter(entity);

		return new Results(true, "删除成功");
	}

	/**
	 * 批量删除数据
	 * 
	 * @param ids
	 *            业务主键集全
	 * @return
	 */
	@RequestMapping(value = "generic/batch/delete", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	Results deleteInBatch(@RequestParam("id") Collection<ID> ids) {

		this.getService().deleteInBatch(ids);
		return new Results(true, "删除成功");
	}

	/**
	 * 取得entityClass.JDK1.4不支持泛型的子类可以抛开Class<T> entityClass,重载此函数达到相同效果。
	 */
	protected Class<T> getEntityClass() {
		return entityClass;
	}

	/**
	 * 列表前处理
	 * 
	 * @param model
	 */
	protected void listBefore(final Model model) {

	}

	/**
	 * 删除前处理
	 * 
	 * @param entity
	 *            业务实体
	 */
	protected void deleteBefore(final T entity) {

	}

	/**
	 * 删除后处理
	 * 
	 * @param entity
	 *            业务实体
	 */
	protected void deleteAfter(final T entity) {

	}

	/**
	 * 编辑前处理
	 * 
	 * @param entity
	 *            业务实体
	 * @param oper
	 *            操作标识，如add,edit,del
	 * @see GenericController#edit(T,String)
	 */
	protected void editBefore(final T entity, final String oper) {

	}

	/**
	 * 编辑后处理
	 * 
	 * @param entity
	 *            业务实体
	 * @param oper
	 *            操作，如add,edit,del
	 */
	protected void editAfter(final T entity, final String oper) {

	}

}
