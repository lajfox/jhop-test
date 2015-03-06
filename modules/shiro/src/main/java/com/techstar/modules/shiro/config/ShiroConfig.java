package com.techstar.modules.shiro.config;

import static org.springframework.util.StringUtils.tokenizeToStringArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.core.env.Environment;

import com.octo.captcha.service.captchastore.FastHashMapCaptchaStore;
import com.octo.captcha.service.image.DefaultManageableImageCaptchaService;
import com.octo.captcha.service.image.ImageCaptchaService;
import com.techstar.modules.jcaptcha.engine.GMailEngine;

@Configuration
@PropertySource(value = { "classpath:com/techstar/modules/shiro/config/shiro.properties", "classpath:shiro.properties",
		"classpath:application.properties" }, ignoreResourceNotFound = true)
@ImportResource("classpath:com/techstar/modules/shiro/config/applicationContext.xml")
public class ShiroConfig extends ApplicationObjectSupport {

	private static final Logger logger = LoggerFactory.getLogger(ShiroConfig.class);

	@Autowired
	private Environment env;

	@Bean
	public FastHashMapCaptchaStore fastHashMapCaptchaStore() {
		return new FastHashMapCaptchaStore();
	}

	@Bean
	public GMailEngine captchaEngine() {
		return new GMailEngine();
	}

	@Bean
	public ImageCaptchaService imageCaptchaService() {

		ImageCaptchaService imageCaptchaService = new DefaultManageableImageCaptchaService(fastHashMapCaptchaStore(),
				captchaEngine(), 180, 100000, 75000);

		return imageCaptchaService;
	}

	/**
	 * 用户授权信息Cache, 采用EhCache
	 * 
	 * @return
	 */
	@Bean
	public EhCacheManager shiroEhcacheManager() {
		EhCacheManager manager = new EhCacheManager();

		String cacheManagerConfigFile = this.getProperty("shiro.cacheManagerConfigFile");
		if (StringUtils.isNotEmpty(cacheManagerConfigFile)) {
			logger.info("shiro.cacheManagerConfigFile:{}", cacheManagerConfigFile);
			manager.setCacheManagerConfigFile(cacheManagerConfigFile);
		}
		return manager;
	}

	/**
	 * Shiro's main business-tier object for web-enabled applications
	 * 
	 * @return
	 */
	@Bean
	public DefaultSecurityManager securityManager() {
		
		print();

		DefaultSecurityManager manager = new DefaultWebSecurityManager();
		manager.setCacheManager(shiroEhcacheManager());

		String realm = this.getProperty("shiro.realm");
		if (StringUtils.isEmpty(realm)) {
			Map<String, Realm> beans = this.getApplicationContext().getBeansOfType(Realm.class);
			if (MapUtils.isEmpty(beans)) {
				manager.setRealm(new AuthorizingRealm() {
					@Override
					protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
						return new SimpleAuthorizationInfo();
					}

					@Override
					protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
							throws AuthenticationException {
						return new SimpleAuthenticationInfo();
					}
				});
				logger.error("严重警告：Realm argument cannot be null，在生产环境中请至少实现一个{}", Realm.class.getName());
			} else {
				logger.info("Realm bean is:{}",beans.keySet());
				manager.setRealms(beans.values());
			}
		} else {
			logger.info("shiro.realm:{}", realm);
			String realmArray[] = tokenizeToStringArray(realm,
					ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
			List<Realm> realms = new ArrayList<Realm>(realmArray.length);
			for (String re : realmArray) {
				realms.add(this.getApplicationContext().getBean(StringUtils.trim(re), Realm.class));
			}
			manager.setRealms(realms);
		}

		return manager;
	}
	
	private void print(){
		String property = this.getProperty("shiro.realm");
		if(StringUtils.isNotEmpty(property)){
			logger.info("shiro.realm:{}", property);
		}
		
		property = this.getProperty("shiro.verify");
		if(StringUtils.isNotEmpty(property)){
			logger.info("shiro.verify:{}", property);
		}
		
		property = this.getProperty("shiro.verifyCode");
		if(StringUtils.isNotEmpty(property)){
			logger.info("shiro.verifyCode:{}", property);
		}
		
		property = this.getProperty("shiro.authc.calss");
		if(StringUtils.isNotEmpty(property)){
			logger.info("shiro.authc.calss:{}", property);
		}
		
		property = this.getProperty("shiro.loginUrl");
		if(StringUtils.isNotEmpty(property)){
			logger.info("shiro.loginUrl:{}", property);
		}
		
		property = this.getProperty("shiro.successUrl");
		if(StringUtils.isNotEmpty(property)){
			logger.info("shiro.successUrl:{}", property);
		}
	}

	private String getProperty(final String key) {
		return StringUtils.trim(env.getProperty(key));
	}

}
