package com.techstar.modules.flexpaper.thread;

import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.techstar.modules.flexpaper.commands.Pdf2swfCommond;
import com.techstar.modules.flexpaper.domain.QueryParam;

public class Pdf2swfThread implements Runnable {

	private Pdf2swfCommond pdf2swfCommond;
	private QueryParam param;
	private boolean upload;

	public Pdf2swfThread(Pdf2swfCommond pdf2swfCommond, QueryParam param, boolean upload) {
		this.param = param;
		this.pdf2swfCommond = pdf2swfCommond;
		this.upload = upload;
	}

	@Override
	public void run() {
		try {
			pdf2swfCommond.execute(param, upload);
		} catch (IOException e) {
		}
		
		// 删除中openoffice生成的pdf文件
		if (!StringUtils.equals(param.getOfficeFile().getName(), param.getSrcFile().getName())) {
			FileUtils.deleteQuietly(param.getSrcFile());
		}
	}
}
