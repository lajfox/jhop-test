package com.techstar.modules.activiti.spring;

import java.io.InputStream;

import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.form.BooleanFormType;
import org.activiti.engine.impl.form.DateFormType;
import org.activiti.engine.impl.form.LongFormType;
import org.activiti.engine.impl.form.StringFormType;
import org.activiti.engine.impl.util.ReflectUtil;

import com.techstar.modules.activiti.engine.CustomIdentityService;
import com.techstar.modules.activiti.engine.impl.CustomIdentityServiceImpl;
import com.techstar.modules.activiti.engine.impl.db.CustomDbSqlSessionFactory;
import com.techstar.modules.activiti.engine.impl.form.CustomFormTypes;
import com.techstar.modules.activiti.form.DateTimeFormType;
import com.techstar.modules.activiti.form.HiddenFormType;
import com.techstar.modules.activiti.form.TextAreaFormType;
import com.techstar.modules.activiti.util.MappingsUtils;

/**
 * 继承org.activiti.spring.SpringProcessEngineConfiguration，增加自定义服务
 * 
 * @author sundoctor
 * @see org.activiti.spring.SpringProcessEngineConfiguration
 * @see org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl
 * 
 */
public class SpringProcessEngineConfiguration extends org.activiti.spring.SpringProcessEngineConfiguration {

	public static final String CUSTOM_DEFAULT_MYBATIS_MAPPING_FILE = "com/techstar/modules/activiti/db/mapping/mappings.xml";

	protected CustomIdentityService customIdentityService = new CustomIdentityServiceImpl();

	/**
	 * 合并mybatis mappings文件
	 */
	@Override
	protected InputStream getMyBatisXmlConfigurationSteam() {
		InputStream sourceInputStream = super.getMyBatisXmlConfigurationSteam();
		InputStream targetInputStream = ReflectUtil.getResourceAsStream(CUSTOM_DEFAULT_MYBATIS_MAPPING_FILE);
		return targetInputStream == null ? sourceInputStream : MappingsUtils
				.merge(sourceInputStream, targetInputStream);
	}

	/*
	 * @Override public ProcessEngine buildProcessEngine() { init(); return new
	 * ProcessEngineImpl(this); }
	 */

	/**
	 * 复写超类方法，增加自定义表单类型
	 */
	@Override
	protected void initFormTypes() {
		if (formTypes == null) {
			formTypes = new CustomFormTypes();
			formTypes.addFormType(new StringFormType());
			formTypes.addFormType(new LongFormType());
			formTypes.addFormType(new DateFormType("yyyy-MM-dd"));
			formTypes.addFormType(new BooleanFormType());

			// 增加自定义表单类型
			formTypes.addFormType(new DateTimeFormType("yyyy-MM-dd HH:mm"));
			formTypes.addFormType(new HiddenFormType());
			formTypes.addFormType(new TextAreaFormType());
		}

		super.initFormTypes();
	}

	@Override
	protected void initServices() {
		initService(customIdentityService);
		super.initServices();
	}

	public CustomIdentityService getCustomIdentityService() {
		return customIdentityService;
	}

	public ProcessEngineConfigurationImpl setCustomIdentityService(CustomIdentityService customIdentityService) {
		this.customIdentityService = customIdentityService;
		return this;
	}

	@Override
	public void initDatabaseType() {
		// 添加达梦数据库支持
		databaseTypeMappings.put("DM DBMS", "dm");		
		CustomDbSqlSessionFactory customDbSqlSessionFactory = new CustomDbSqlSessionFactory();
		
		super.initDatabaseType();
	}


}
