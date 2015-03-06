package com.techstar.modules.mail.domain;

import java.io.InputStream;

public interface Attachment {
	String getFileName();

	InputStream getInputStream();
}
