package com.techstar.modules.springframework.convert.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class CommonsMultipartFileConverter implements Converter<String, CommonsMultipartFile>  {

	@Override
	public CommonsMultipartFile convert(String source) {		
		return null;
	}

}
