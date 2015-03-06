package com.techstar.modules.activiti.config;

import static org.springframework.util.StringUtils.tokenizeToStringArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.form.AbstractFormType;
import org.activiti.engine.impl.interceptor.SessionFactory;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.transaction.PlatformTransactionManager;

import com.techstar.modules.activiti.engine.impl.persistence.GroupEntityManagerFactory;
import com.techstar.modules.activiti.engine.impl.persistence.UserEntityManagerFactory;
import com.techstar.modules.activiti.engine.impl.persistence.entity.CustomGroupEntityManager;
import com.techstar.modules.activiti.engine.impl.persistence.entity.CustomUserEntityManger;
import com.techstar.modules.activiti.form.CustomFormType;
import com.techstar.modules.activiti.form.DateTimeFormType;
import com.techstar.modules.activiti.form.UserFormType;
import com.techstar.modules.activiti.form.UsersFormType;
import com.techstar.modules.activiti.spring.SpringProcessEngineConfiguration;

@Configuration
@ImportResource("classpath:com/techstar/modules/activiti/config/applicationContext.xml")
// @ComponentScan(basePackages = "com.techstar.modules.activiti")
@PropertySource(value = { "classpath:com/techstar/modules/activiti/config/activiti.properties",
		"classpath:activiti.properties", "classpath:application.properties" }, ignoreResourceNotFound = true)
public class ActivitiConfig implements ApplicationContextAware {

	private static final Logger logger = LoggerFactory.getLogger(ActivitiConfig.class);

	@Autowired
	private Environment env;
	private ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.context = applicationContext;
	}

	@Bean
	public UserEntityManagerFactory userEntityMangerFactory() {
		return new UserEntityManagerFactory(new CustomUserEntityManger());
	}

	@Bean
	public GroupEntityManagerFactory groupEntityMangerFactory() {
		return new GroupEntityManagerFactory(new CustomGroupEntityManager());
	}

	@Bean
	public ProcessEngineConfiguration processEngineConfiguration() {

		logger.info("ProcessEngineConfiguration 开始");

		SpringProcessEngineConfiguration configuration = new SpringProcessEngineConfiguration();

		// customSessionFactories
		List<SessionFactory> customSessionFactories = new ArrayList<SessionFactory>();
		customSessionFactories.add(userEntityMangerFactory());
		customSessionFactories.add(groupEntityMangerFactory());
		configuration.setCustomSessionFactories(customSessionFactories);

		// customFormTypes
		List<AbstractFormType> customFormTypes = new ArrayList<AbstractFormType>();
		customFormTypes.add(new CustomFormType());
		customFormTypes.add(new UserFormType());
		customFormTypes.add(new UsersFormType());
		customFormTypes.add(new DateTimeFormType("yyyy-MM-dd HH:mm"));
		configuration.setCustomFormTypes(customFormTypes);

		// 数据源
		DataSource dataSource = getBean("activiti.dataSource", DataSource.class);
		if (dataSource != null) {
			configuration.setDataSource(dataSource);
		}

		// 事务
		PlatformTransactionManager transactionManager = getBean("activiti.transactionManager",
				PlatformTransactionManager.class);
		if (transactionManager != null) {
			configuration.setTransactionManager(transactionManager);
		}

		// jpaEntityManagerFactory
		EntityManagerFactory entityManagerFactory = getBean("activiti.jpaEntityManagerFactory",
				EntityManagerFactory.class);
		if (entityManagerFactory != null) {
			configuration.setJpaEntityManagerFactory(entityManagerFactory);
		}

		// databaseSchemaUpdate
		String name = StringUtils.trim(env.getProperty("activiti.databaseSchemaUpdate"));
		logger.info("activiti.databaseSchemaUpdate:{}", name);
		if (StringUtils.isNotEmpty(name)) {
			configuration.setDatabaseSchemaUpdate(name);
		}

		// jpaHandleTransaction
		Boolean jpaHandleTransaction = env.getProperty("activiti.jpaHandleTransaction", Boolean.class);
		logger.info("activiti.jpaHandleTransaction:{}", jpaHandleTransaction);
		configuration.setJpaHandleTransaction(jpaHandleTransaction);

		// jpaCloseEntityManager
		Boolean jpaCloseEntityManager = env.getProperty("activiti.jpaCloseEntityManager", Boolean.class);
		logger.info("activiti.jpaCloseEntityManager:{}", jpaCloseEntityManager);
		configuration.setJpaCloseEntityManager(jpaCloseEntityManager);

		// jobExecutorActivate
		Boolean jobExecutorActivate = env.getProperty("activiti.jobExecutorActivate", Boolean.class);
		logger.info("activiti.jobExecutorActivate:{}", jobExecutorActivate);
		configuration.setJobExecutorActivate(jobExecutorActivate);

		// history level
		name = StringUtils.trim(env.getProperty("activiti.history"));
		logger.info("activiti.history:{}", name);
		if (StringUtils.isNotEmpty(name)) {
			configuration.setHistory(name);
		}

		// activityFontName
		name = StringUtils.trim(env.getProperty("activiti.activityFontName"));
		logger.info("activiti.activityFontName:{}", name);
		if (StringUtils.isNotEmpty(name)) {
			configuration.setActivityFontName(name);
		}

		// labelFontName
		name = StringUtils.trim(env.getProperty("activiti.labelFontName"));
		logger.info("activiti.labelFontName:{}", name);
		if (StringUtils.isNotEmpty(name)) {
			configuration.setLabelFontName(name);
		}

		// deploymentResources
		name = StringUtils.trim(env.getProperty("activiti.deploymentResources"));
		logger.info("activiti.deploymentResources:{}", name);
		if (StringUtils.isNotEmpty(name)) {

			String[] resourcesArray = tokenizeToStringArray(name,
					ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);

			Resource[] resources = new Resource[0];
			ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

			for (String resource : resourcesArray) {
				resource = StringUtils.trim(resource);
				if (StringUtils.isNotEmpty(resource)) {

					try {
						resources = ArrayUtils.addAll(resources, resolver.getResources(resource));
					} catch (IOException e) {
						logger.error(e.getMessage());
					}

				}

			}

			if (ArrayUtils.isNotEmpty(resources)) {
				configuration.setDeploymentResources(resources);
			}
		}

		// dataSourceJndiName
		name = StringUtils.trim(env.getProperty("activiti.dataSourceJndiName"));
		logger.info("activiti.dataSourceJndiName:{}", name);
		if (StringUtils.isNotEmpty(name)) {
			configuration.setDataSourceJndiName(name);
		}

		// databaseSchema
		name = StringUtils.trim(env.getProperty("activiti.databaseSchema"));
		name = StringUtils.isEmpty(name) ? StringUtils.trim(env.getProperty("databaseSchema")) : name;
		logger.info("activiti.databaseSchema:{}", name);
		if (StringUtils.isNotEmpty(name)) {
			configuration.setDatabaseSchema(name);
		}

		logger.info("ProcessEngineConfiguration 结束");

		return configuration;
	}

	private <T> T getBean(String config, Class<T> requiredType) throws BeansException {

		String name = StringUtils.trim(env.getProperty(config));

		if (StringUtils.isEmpty(name)) {
			Map<String, T> beansMap = context.getBeansOfType(requiredType);
			if (MapUtils.isNotEmpty(beansMap)) {
				if (beansMap.size() == 1) {
					return beansMap.values().iterator().next();
				} else {
					logger.error(
							"{}定义多于一个:{}，请在classpath:activiti.properties或classpath:application.properties中配置{}指定一个{}",
							new Object[] { requiredType.getName(), beansMap.keySet(), config, requiredType.getName() });

					throw new FatalBeanException(requiredType.getName() + "定义多于一个:" + beansMap.keySet()
							+ "，请在classpath:activiti.properties或classpath:application.properties中配置" + config + "指定一个"
							+ requiredType.getName());
				}
			}
		} else {
			logger.info("{}:{}", config, name);
			// if (context.containsBean(name)) {
			return context.getBean(name, requiredType);
			// }
		}

		return null;
	}

}
