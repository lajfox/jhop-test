package com.techstar.modules.elfinder.domain;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class Param {

	// open
	private String cmd;
	private String target;
	private int init;
	private int tree;

	// download
	private int download;
	// mkfile/mkdir/new file
	private String name;
	// edit file
	private String content;

	// cut/copy/paste
	private int cut;
	private String dst;
	private String src;
	private String targets_[];
	
	//upload
	private CommonsMultipartFile uploadFiles[];

	// resize crop rotate
	private int width;
	private int height;
	private String mode;
	private int x;
	private int y;
	private int degree;

	// 压缩类型
	private String type;
	
	private String q;

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public int getInit() {
		return init;
	}

	public void setInit(int init) {
		this.init = init;
	}

	public int getTree() {
		return tree;
	}

	public void setTree(int tree) {
		this.tree = tree;
	}

	public int getDownload() {
		return download;
	}

	public void setDownload(int download) {
		this.download = download;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getCut() {
		return cut;
	}

	public void setCut(int cut) {
		this.cut = cut;
	}

	public String getDst() {
		return dst;
	}

	public void setDst(String dst) {
		this.dst = dst;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String[] getTargets_() {
		return targets_;
	}

	public void setTargets_(String[] targets) {
		this.targets_ = targets;
	}

	public CommonsMultipartFile[] getUploadFiles() {
		return uploadFiles;
	}

	public void setUploadFiles(CommonsMultipartFile[] uploadFiles) {
		this.uploadFiles = uploadFiles;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getDegree() {
		return degree;
	}

	public void setDegree(int degree) {
		this.degree = degree;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}
	
	

}
