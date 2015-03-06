package com.techstar.modules.elfinder.impl;

import java.util.HashMap;
import java.util.Map;

import com.techstar.modules.elfinder.service.FsServiceConfig;

public class DefaultFsServiceConfig implements FsServiceConfig {

	private int _tmbWidth = 80;

	private Map<String, String[]> archivers = new HashMap<String, String[]>();

	public  DefaultFsServiceConfig() {
		
		String[] create = new String[] { "application/x-zip-compressed", "application/x-tar", "application/x-gzip",
				"application/x-7z-compressed" };
		archivers.put("create", create);
		archivers.put("extract", create);
	}

	public void setTmbWidth(int tmbWidth) {
		_tmbWidth = tmbWidth;
	}

	@Override
	public int getTmbWidth() {
		return _tmbWidth;
	}

	@Override
	public Map<String, String[]> getArchivers() {
		return this.archivers;
	}

	public void setArchivers(Map<String, String[]> archivers) {
		this.archivers = archivers;
	}

}
