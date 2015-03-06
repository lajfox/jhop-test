package com.techstar.modules.mail.domain;

import java.io.InputStream;

/**
 * 邮件附件
 * 
 * @author sundoctor
 * 
 */
public class StreamAttachment implements Attachment {

	private String fileName;// 附件名称
	private InputStream inputStream;// 附件内容

	public StreamAttachment(String fileName, InputStream inputStream) {
		this.fileName = fileName;
		this.inputStream = inputStream;
	}

	@Override
	public String getFileName() {
		return fileName;
	}

	@Override
	public InputStream getInputStream() {
		return inputStream;
	}

}
