package com.techstar.modules.flexpaper.util;

import java.io.File;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import com.techstar.modules.flexpaper.domain.QueryParam;

public final class ConverUtils {

	public static File getConverFile(final QueryParam param, final String swfPath, final String extension) {

		File converFile = param.getTargerFile();
		if (converFile == null) {

			File pdfFile = param.getSrcFile();
			String pathsrc = FilenameUtils.normalize(pdfFile.getParent()).replace('\\', '/');// 文件目录
			String srcname = pdfFile.getName();// 文件名称

			String swfDir = StringUtils.trim(swfPath);
			if (StringUtils.isEmpty(swfDir)) {
				swfDir = System.getProperty("catalina.home") + "/temp";
			}
			if (StringUtils.isEmpty(swfDir)) {
				swfDir = System.getProperty("java.io.tmpdir");
			}
			if (StringUtils.isEmpty(swfDir)) {
				swfDir = System.getProperty("user.dir");
			}
			swfDir = swfDir.replace('\\', '/');

			StringBuilder sb = new StringBuilder();
			sb.append(swfDir);
			if (StringUtils.isNotEmpty(param.getVolumeId())) {
				sb.append('/');
				sb.append(param.getVolumeId());
			}

			if (pathsrc.startsWith(FilenameUtils.normalize(sb.toString()).replace('\\', '/'))) {
				sb = new StringBuilder(pathsrc);
			} else {
				sb.append('/');
				sb.append(StringUtils.substringAfter(pathsrc, "/"));
			}

			File dir = new File(FilenameUtils.normalize(sb.toString()));
			if (!dir.exists()) {
				dir.mkdirs();
			}

			converFile = (param.isSplitmode() && StringUtils.equals("swf", extension)) ? new File(dir, srcname + '_'
					+ param.getPage() + "." + extension) : new File(dir, srcname + "." + extension);

		}
		return converFile;
	}
}
