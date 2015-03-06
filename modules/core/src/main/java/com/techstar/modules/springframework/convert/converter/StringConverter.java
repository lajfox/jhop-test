package com.techstar.modules.springframework.convert.converter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;

public class StringConverter implements Converter<String, String> {

	@Override
	public String convert(String source) {
		return StringUtils.isEmpty(source) ? null : source;
	}
}
