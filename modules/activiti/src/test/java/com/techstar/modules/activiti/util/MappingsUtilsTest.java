package com.techstar.modules.activiti.util;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class MappingsUtilsTest {

	@Test
	public void merge() throws Exception {

		Resource re = new ClassPathResource("b.xml", this.getClass());
		InputStream sourceInputStream = re.getInputStream();

		re = new ClassPathResource("a.xml", this.getClass());
		InputStream targetInputStream = re.getInputStream();

		InputStream in = MappingsUtils.merge(sourceInputStream, targetInputStream);

		String xml = IOUtils.toString(in);

		int i = xml.indexOf("org/activiti/db/mapping/entity/Attachment3.xml");
		int j = xml.indexOf("org.activiti.engine.impl.persistence.entity.ByteArrayRef");

		Assert.assertTrue(i != -1 && j != -1);
	}
}
