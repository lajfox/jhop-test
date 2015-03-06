package com.techstar.modules.mail.domain;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.InputStreamSource;

/**
 * 邮件附件
 * 
 * @author sundoctor
 * 
 */
public class ResourceAttachment implements Attachment {

	private String fileName;// 附件名称
	private InputStreamSource inputStreamSource;// 附件内容

	public ResourceAttachment(String fileName, InputStreamSource inputStreamSource) {
		this.fileName = fileName;
		this.inputStreamSource = inputStreamSource;
	}

	@Override
	public String getFileName() {
		return fileName;
	}

	@Override
	public InputStream getInputStream() {
		try {
			return inputStreamSource == null ? null : inputStreamSource.getInputStream();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
