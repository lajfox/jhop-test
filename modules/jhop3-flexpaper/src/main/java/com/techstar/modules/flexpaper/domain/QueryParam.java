package com.techstar.modules.flexpaper.domain;

import java.io.File;

public class QueryParam {

	private String doc;
	private Integer page;
	private String format;
	private String resolution;
	private String callback;
	private String searchterm;

	private File officeFile;
	private File srcFile;
	private File targerFile;
	private String target;
	private String volumeId;

	public String getDoc() {
		return doc;
	}

	public void setDoc(String doc) {
		this.doc = doc;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public String getCallback() {
		return callback;
	}

	public void setCallback(String callback) {
		this.callback = callback;
	}

	public File getSrcFile() {
		return srcFile;
	}

	public void setSrcFile(File srcFile) {
		this.srcFile = srcFile;
	}

	public File getTargerFile() {
		return targerFile;
	}

	public void setTargerFile(File targerFile) {
		this.targerFile = targerFile;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getVolumeId() {
		return volumeId;
	}

	public void setVolumeId(String volumeId) {
		this.volumeId = volumeId;
	}

	public String getSearchterm() {
		return searchterm;
	}

	public void setSearchterm(String searchterm) {
		this.searchterm = searchterm;
	}

	public boolean isSplitmode() {
		return getPage() != null && getPage() > 0;
	}

	public File getOfficeFile() {
		return officeFile;
	}

	public void setOfficeFile(File officeFile) {
		this.officeFile = officeFile;
	}
	
	

}
