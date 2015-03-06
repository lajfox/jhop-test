package com.techstar.modules.elfinder.compress;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.techstar.modules.elfinder.controller.executor.FsItemEx;
import com.techstar.modules.elfinder.domain.Param;
import com.techstar.modules.elfinder.service.FsService;
import com.techstar.modules.elfinder.util.FsServiceUtils;

public abstract class AbstractCompressCommand implements CompressCommand {

	protected static final String BASE_DIR = "";
	// 符号"/"用来作为目录标识判断符
	protected static final String PATH = "/";
	protected static final Logger logger = LoggerFactory.getLogger(AbstractCompressCommand.class);

	protected String getEntryName(final FsItemEx fsi, final String rootPath) {
		String entryName = fsi.getName();
		String canonicalPath = fsi.getAbsolutePath();
		if (canonicalPath.indexOf(rootPath) != -1) {
			entryName = canonicalPath.substring(rootPath.length() + 1);
		}

		if (fsi.isFolder()) {
			entryName += "/";// "/"后缀表示entry是文件夹
		}
		return entryName;
	}

	/**
	 * 文件探针
	 * 
	 * <pre>
	 * 当父目录不存在时，创建目录！
	 * </pre>
	 * 
	 * @param dirFile
	 */
	protected void fileProber(File dirFile) {
		File parentFile = dirFile.getParentFile();
		if (!parentFile.exists()) {
			// 递归寻找上级目录
			fileProber(parentFile);
			parentFile.mkdir();
		}
	}

	protected FsItemEx getNewArchiveFile(final FsItemEx parent, final String extension) throws IOException {
		FsItemEx newFile = new FsItemEx(parent, "archive." + extension);
		if (newFile.exists()) {
			int i = 1;
			while (true) {
				String newName = String.format("%s%d%s", "archive", i, "." + extension);
				newFile = new FsItemEx(parent, newName);
				if (!newFile.exists()) {
					break;
				}
				i++;
			}
		}
		return newFile;
	}

	protected FsItemEx getNewExtractDir(final FsItemEx parent) {
		FsItemEx newDir = new FsItemEx(parent, "archive");
		if (newDir.exists()) {
			int i = 1;
			while (true) {
				String newName = String.format("%s%d", "archive", i);
				newDir = new FsItemEx(parent, newName);
				if (!newDir.exists()) {
					break;
				}
				i++;
			}
		}

		if (!newDir.exists()) {
			newDir.createFolder();
		}

		return newDir;
	}

	protected File getNewExtractFile(FsItemEx fsi, String entryName) {
		entryName = entryName.replace('\\', '/');
		if (StringUtils.contains(entryName, "/")) {
			String fileName = StringUtils.substringAfterLast(entryName, "/");
			File dir = new File(fsi.getFile(), StringUtils.substringBeforeLast(entryName, "/"));
			if (!dir.exists()) {
				dir.mkdirs();
			}
			if (StringUtils.isEmpty(fileName)) {
				return dir;
			} else {
				return new File(dir, fileName);
			}
		} else {
			return new File(fsi.getFile(), entryName);
		}
	}

	protected FsItemEx findItem(FsService fsService, String hash) {
		return FsServiceUtils.findItem(fsService, hash);
	}

	/***
	 * Description: 解压到当前文件夹，返回解压列表 Create by: lrm 2014-3-5 上午10:32:55
	 */
	public List<FsItemEx> extractCwd(final FsService fsService, final Param param) throws IOException {
		return new ArrayList<FsItemEx>();
	}
}
