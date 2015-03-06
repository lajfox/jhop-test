package com.techstar.modules.elfinder.controller.executor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.techstar.modules.elfinder.service.FsItem;
import com.techstar.modules.elfinder.service.FsService;
import com.techstar.modules.elfinder.service.FsVolume;

public class FsItemEx {
	private FsItem fsItem;

	private FsService fsService;

	private FsVolume fsVolume;

	public FsItemEx(FsItem fsi, FsService fsService) {
		this.fsItem = fsi;
		this.fsVolume = fsi.getVolume();
		this.fsService = fsService;
	}

	public FsItemEx(FsItemEx parent, String name) {
		this.fsVolume = parent.fsVolume;
		this.fsService = parent.fsService;
		this.fsItem = fsVolume.fromPath(fsVolume.getPath(parent.fsItem) + "/" + name);
	}

	public FsItemEx createChild(String name) {
		return new FsItemEx(this, name);
	}

	public void createFile() throws IOException {
		fsVolume.createFile(fsItem);
	}

	public void createFolder() {
		fsVolume.createFolder(fsItem);
	}

	public void delete() throws IOException {
		if (fsVolume.isFolder(fsItem)) {
			fsVolume.deleteFolder(fsItem);
		} else {
			fsVolume.deleteFile(fsItem);
		}
	}

	public void deleteFile() {
		fsVolume.deleteFile(fsItem);
	}

	public void deleteFolder() throws IOException {
		fsVolume.deleteFolder(fsItem);
	}

	public boolean exists() {
		return fsVolume.exists(fsItem);
	}

	public String getHash() {
		return fsService.getHash(fsItem);
	}

	public long getLastModified() {
		return fsVolume.getLastModified(fsItem);
	}

	public String getMimeType() {
		return fsVolume.getMimeType(fsItem);
	}

	public String getName() {
		return fsVolume.getName(fsItem);
	}

	public FsItemEx getParent() {
		return new FsItemEx(fsVolume.getParent(fsItem), fsService);
	}

	public String getPath() throws IOException {
		return fsVolume.getPath(fsItem);
	}

	public String getAbsolutePath() {
		return fsVolume.getAbsolutePath(fsItem);
	}

	public long getSize() {
		return fsVolume.getSize(fsItem);
	}

	public String getVolumeId() {
		return fsService.getVolumeId(fsVolume);
	}

	public String getVolumeName() {
		return fsVolume.getVolumeName();
	}

	public boolean hasChildFolder() {
		return fsVolume.hasChildFolder(fsItem);
	}

	public boolean isFolder() {
		return fsVolume.isFolder(fsItem);
	}

	public boolean isSpecialDir() {
		return fsVolume.isSpecialDir(fsItem);
	}

	public boolean isLocked() {
		return fsVolume.isLocked() || !fsVolume.isWritable();
	}

	public boolean isReadable() {
		return fsVolume.isReadable();
	}

	public boolean isRoot()  {
		return fsVolume.isRoot(fsItem);
	}

	public boolean isWritable() {
		return fsVolume.isWritable();
	}

	public String[] getDisabled() {
		return fsVolume.getDisabled();
	}

	public List<FsItemEx> listChildren() {
		List<FsItemEx> list = new ArrayList<FsItemEx>();
		for (FsItem child : fsVolume.listChildren(fsItem)) {
			list.add(new FsItemEx(child, fsService));
		}
		return list;
	}

	public InputStream openInputStream() throws IOException {
		return fsVolume.openInputStream(fsItem);
	}

	public File getFile() {
		return fsVolume.getFile(fsItem);
	}

	public OutputStream openOutputStream() throws IOException {
		return fsVolume.openOutputStream(fsItem);
	}

	public void renameTo(FsItemEx dst) {
		fsVolume.rename(fsItem, dst.fsItem);
	}

}
