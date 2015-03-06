package com.techstar.modules.jaxb.serializer;

import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;

public class JaxbDateTimeSerializer extends JaxbDateSerializer {

	@Override
	public String marshal(Date date) {
		return DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss");
	}

}