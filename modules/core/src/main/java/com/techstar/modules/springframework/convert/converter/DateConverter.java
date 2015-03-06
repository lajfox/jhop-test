package com.techstar.modules.springframework.convert.converter;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;

import com.techstar.modules.commons.lang3.time.DateUtils;

public class DateConverter implements Converter<String, Date> {


	@Override
	public Date convert(String source) {
		String src = StringUtils.trim(source);
		if (StringUtils.isEmpty(src)) {
			return null;
		}

		return DateUtils.parseDate(src);

	}

}
