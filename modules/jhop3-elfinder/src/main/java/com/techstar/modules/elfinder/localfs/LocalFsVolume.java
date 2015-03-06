package com.techstar.modules.elfinder.localfs;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.techstar.modules.elfinder.service.FsItem;
import com.techstar.modules.elfinder.service.FsVolume;
import com.techstar.modules.elfinder.util.FsServiceUtils;

import eu.medsea.mimeutil.MimeType;
import eu.medsea.mimeutil.MimeUtil;

public class LocalFsVolume implements FsVolume {

	private static final Logger logger = LoggerFactory.getLogger(LocalFsVolume.class);

	static {
		// MIME types initialization
		MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
		MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.ExtensionMimeDetector");
		// MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.OpendesktopMimeDetector");
		// MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.WindowsRegistryMimeDetector");
	}

	private String volumeName;
	private File rootDir;
	private String volumeId;
	private boolean readable = true;
	private boolean writable = true;
	private boolean locked;
	/*
	 * 设置失效命令，commands : [ 'open', 'reload', 'home', 'up', 'back', 'forward',
	 * 'getfile', 'quicklook', 'download', 'rm', 'duplicate', 'rename', 'mkdir',
	 * 'mkfile', 'upload', 'copy', 'cut', 'paste', 'edit', 'extract', 'archive',
	 * 'search', 'info', 'view', 'help', 'resize', 'sort']
	 */
	private String[] disabled;

	public LocalFsVolume() {
	}

	public LocalFsVolume(String volumeName, File rootDir) {
		this.volumeName = volumeName;
		this.rootDir = rootDir;
	}

	public LocalFsVolume(String volumeName, File rootDir, boolean writable) {
		this(volumeName, rootDir);
		this.writable = writable;
	}

	public LocalFsVolume(String volumeName, File rootDir, boolean readable, boolean writable) {
		this(volumeName, rootDir);
		this.readable = readable;
		this.writable = writable;
	}

	public LocalFsVolume(String volumeName, File rootDir, boolean readable, boolean writable, boolean locked) {
		this(volumeName, rootDir, readable, writable);
		this.locked = locked;
	}

	public LocalFsVolume(String volumeName, File rootDir, String volumeId) {
		this(volumeName, rootDir);
		this.volumeId = volumeId;
	}

	public LocalFsVolume(String volumeName, File rootDir, String volumeId, boolean writable) {
		this(volumeName, rootDir, volumeId);
		this.writable = writable;
	}

	public LocalFsVolume(String volumeName, File rootDir, String volumeId, boolean readable, boolean writable) {
		this(volumeName, rootDir, volumeId);
		this.readable = readable;
		this.writable = writable;
	}

	public LocalFsVolume(String volumeName, File rootDir, String volumeId, boolean readable, boolean writable,
			boolean locked) {
		this(volumeName, rootDir, volumeId, readable, writable);
		this.locked = locked;
	}

	@Override
	public File getFile(FsItem fsi) {
		return ((LocalFsItem) fsi).getFile();
	}

	@Override
	public void createFile(FsItem fsi) throws IOException {
		getFile(fsi).createNewFile();
	}

	@Override
	public void createFolder(FsItem fsi)  {
		getFile(fsi).mkdirs();
	}

	@Override
	public void deleteFile(FsItem fsi)  {
		File file = getFile(fsi);
		if (!file.isDirectory()) {
			file.delete();
		}
	}

	@Override
	public void deleteFolder(FsItem fsi) throws IOException {
		File file = getFile(fsi);
		if (file.isDirectory()) {
			FileUtils.deleteDirectory(file);
		}
	}

	@Override
	public boolean exists(FsItem newFile) {
		return getFile(newFile).exists();
	}

	private LocalFsItem fromFile(File file) {
		return new LocalFsItem(this, file);
	}

	@Override
	public FsItem fromPath(String relativePath) {
		return fromFile(new File(rootDir, relativePath));
	}

	@Override
	public String getDimensions(FsItem fsi) {
		return null;
	}

	@Override
	public long getLastModified(FsItem fsi) {
		return getFile(fsi).lastModified();
	}

	@Override
	@SuppressWarnings("unchecked")
	public String getMimeType(FsItem fsi) {
		File file = getFile(fsi);

		if (file.isDirectory())
			return "directory";

		Collection<MimeType> mimes = MimeUtil.getMimeTypes(file);
		if (!mimes.isEmpty()) {
			return mimes.iterator().next().toString();
		}

		return new MimetypesFileTypeMap().getContentType(file);

	}

	public String getVolumeName() {
		return volumeName;
	}

	@Override
	public String getName(FsItem fsi) {
		return getFile(fsi).getName();
	}

	@Override
	public FsItem getParent(FsItem fsi) {
		return fromFile(getFile(fsi).getParentFile());
	}

	@Override
	public String getPath(FsItem fsi)  {
		String fullPath = getFile(fsi).getAbsolutePath();
		String rootPath = rootDir.getAbsolutePath();
		String relativePath = fullPath.substring(rootPath.length());
		return relativePath.replace('\\', '/');
	}

	@Override
	public String getAbsolutePath(FsItem fsi)  {
		String fullPath = getFile(fsi).getAbsolutePath();
		return fullPath.replace('\\', '/');
	}

	@Override
	public FsItem getRoot() {
		return fromFile(rootDir);
	}

	public File getRootDir() {
		return rootDir;
	}

	@Override
	public long getSize(FsItem fsi) {
		return getFile(fsi).length();
	}

	@Override
	public String getThumbnailFileName(FsItem fsi) {
		return null;
	}

	@Override
	public boolean hasChildFolder(FsItem fsi) {
		File file = getFile(fsi);

		return isFolder(fsi) && (!isSpecialDir(fsi)||FsServiceUtils.isWinVolume(file)) && file.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file.isDirectory();
			}
		}).length > 0;
	}

	@Override
	public boolean isFolder(FsItem fsi) {
		File file = getFile(fsi);
		return file.isDirectory() || FsServiceUtils.isWinVolume(file);
	}

	@Override
	public boolean isRoot(FsItem fsi) {

		String canonicalPath = getFile(fsi).getAbsolutePath().replace('\\', '/');
		String rootPath = rootDir.getAbsolutePath().replace('\\', '/');

		return StringUtils.equals(canonicalPath, rootPath);
	}

	@Override
	public FsItem[] listChildren(FsItem fsi) {
		List<FsItem> list = new ArrayList<FsItem>();
		File[] cs = getFile(fsi).listFiles();
		if (cs == null) {
			return new FsItem[0];
		}

		for (File c : cs) {
			list.add(fromFile(c));
		}

		return list.toArray(new FsItem[0]);
	}

	@Override
	public InputStream openInputStream(FsItem fsi) throws IOException {
		return new FileInputStream(getFile(fsi));
	}

	@Override
	public OutputStream openOutputStream(FsItem fsi) throws IOException {
		return new FileOutputStream(getFile(fsi));
	}



	@Override
	public void rename(FsItem src, FsItem dst) {
		getFile(src).renameTo(getFile(dst));
	}

	public void setVolumeName(String volumeName) {
		this.volumeName = volumeName;
	}

	public void setRootDir(File rootDir) {
		if (!rootDir.exists()) {
			rootDir.mkdirs();
		}

		this.rootDir = rootDir;
	}

	public String getVolumeId() {
		return volumeId;
	}

	public void setVolumeId(String volumeId) {
		this.volumeId = volumeId;
	}

	public boolean isReadable() {
		return readable;
	}

	public void setReadable(boolean readable) {
		this.readable = readable;
	}

	public boolean isWritable() {
		return writable;
	}

	public void setWritable(boolean writable) {
		this.writable = writable;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public String[] getDisabled() {
		return disabled;
	}

	public void setDisabled(String[] disabled) {
		this.disabled = disabled;
	}

	@Override
	public boolean isSpecialDir(FsItem fsi) {
		File file = getFile(fsi);
		String name = file.getName().toLowerCase();
		return (".".equals(name) || "..".equals(name) || name.equals(".fseventsd") || name.equals("$recycle.bin")
				|| name.equals(".trashes") || name.equals(".trash-1000") || name.equals("lost+found")
				|| "system volume information".equals(name) || name.equals(".svn") || name.endsWith(".svn-base") || file
					.isHidden());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}

		if (obj.getClass() != this.getClass()) {
			return false;
		}

		LocalFsVolume mb = (LocalFsVolume) obj;
		return new EqualsBuilder()
				.append(this.volumeName, mb.getVolumeName())
				.append(this.rootDir == null ? null : this.rootDir.getAbsolutePath(),
						mb.getRootDir() == null ? null : mb.getRootDir().getAbsolutePath()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.volumeName)
				.append(this.rootDir == null ? null : this.rootDir.getAbsolutePath()).toHashCode();

	}

}
