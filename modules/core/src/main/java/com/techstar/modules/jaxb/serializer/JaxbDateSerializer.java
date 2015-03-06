package com.techstar.modules.jaxb.serializer;

import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

public class JaxbDateSerializer extends XmlAdapter<String, Date> {

	private static final String parsePatterns[] = new String[] { "yyyy", "yyyy-MM", "yyyy-MM-dd", "yyyy-MM-dd HH",
		"yyyy-MM-dd HH:mm", "yyyy-MM-dd HH:mm:ss", "yyyy/MM", "yyyy/MM/dd", "yyyy/MM/dd HH", "yyyy/MM/dd HH:mm",
		"yyyy/MM/dd HH:mm:ss" };
	
	@Override
	public String marshal(Date date) {
		return DateFormatUtils.format(date, "yyyy-MM-dd");
	}

	@Override
	public Date unmarshal(String date) throws Exception {
		return DateUtils.parseDate(date, parsePatterns);
	}
}