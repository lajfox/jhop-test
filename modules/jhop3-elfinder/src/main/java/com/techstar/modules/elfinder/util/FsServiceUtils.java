package com.techstar.modules.elfinder.util;

import java.io.File;

import org.apache.commons.lang3.StringUtils;

import com.techstar.modules.elfinder.controller.executor.FsItemEx;
import com.techstar.modules.elfinder.service.FsItem;
import com.techstar.modules.elfinder.service.FsService;

public final class FsServiceUtils {
	private static final String OS_NAME = System.getProperty("os.name").toLowerCase();

	public static FsItemEx findItem(FsService fsService, String hash) {
		FsItem fsi = fsService.fromHash(hash);
		if (fsi == null) {
			return null;
		}

		return new FsItemEx(fsi, fsService);
	}

	/**
	 * windows磁盘
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isWinVolume(final File file) {
		boolean br = false;
		if (isWindows()) {
			String volume = StringUtils.substringAfter(file.getAbsolutePath().replace('\\', '/'), ":/");
			br = StringUtils.isEmpty(volume);
		}
		return br;
	}


	public static boolean isWindows() {
		return OS_NAME.startsWith("windows");
	}

	public static boolean isLinux() {
		return OS_NAME.startsWith("linux");
	}

	public static boolean isMac() {
		return OS_NAME.startsWith("mac");
	}
}
