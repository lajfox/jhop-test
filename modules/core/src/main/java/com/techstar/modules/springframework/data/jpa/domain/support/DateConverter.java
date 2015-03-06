package com.techstar.modules.springframework.data.jpa.domain.support;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

import com.techstar.modules.commons.lang3.time.DateUtils;

public class DateConverter implements Converter {

	private String[] patterns;

	public DateConverter() {
		this.patterns = DateUtils.PARSEPATTERNS;
	}

	public DateConverter(String[] patterns) {
		this.patterns = patterns;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public Object convert(Class clazz, Object obj) {

		if (!Date.class.isAssignableFrom(clazz)) {
			throw new ConversionException("转换类型错误:" + clazz);
		}

		if (obj == null) {
			return null;
		}

		if (Date.class.isAssignableFrom(obj.getClass())) {
			return obj;
		} else if (Number.class.isAssignableFrom(obj.getClass())) {
			Date date = new Date();
			date.setTime(((Number) obj).longValue());
			return date;
		} else {
			try {
				return DateUtils.parseDate(obj.toString(), this.patterns);
			} catch (ParseException e) {
				return null;
			}
		}

	}
}
