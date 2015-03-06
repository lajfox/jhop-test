package com.techstar.modules.commons.beanutils;

import static org.apache.commons.beanutils.ConvertUtils.register;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.techstar.modules.springframework.data.jpa.domain.support.DateConverter;

@Component
public class BeanUtilsConfig {

	@PostConstruct
	public void init() {
		register(new DateConverter(), Date.class);
	}
}
