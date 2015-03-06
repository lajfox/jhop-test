package com.techstar.modules.elfinder.compress;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.techstar.modules.elfinder.controller.executor.FsItemEx;
import com.techstar.modules.elfinder.domain.Param;
import com.techstar.modules.elfinder.service.FsService;

/**
 * tar压缩、解压
 * 
 * @author sundoctor
 * 
 */
public class TarCompressCommand extends AbstractCompressCommand {

	@Override
	public FsItemEx compress(final FsService fsService, final Param param) throws IOException {

		String[] targets = param.getTargets_();

		FsItemEx fsi = super.findItem(fsService, targets[0]);
		FsItemEx parent = fsi.getParent();

		FsItemEx newFile = getNewArchiveFile(parent, "tar");
		TarArchiveOutputStream tos = new TarArchiveOutputStream(new BufferedOutputStream(new FileOutputStream(
				newFile.getFile())));
		tos.setLongFileMode(TarArchiveOutputStream.LONGFILE_POSIX);

		for (String target : targets) {
			fsi = super.findItem(fsService, target);
			archive(fsi.getFile(), tos, BASE_DIR);
		}

		IOUtils.closeQuietly(tos);

		return newFile;
	}

	@Override
	public FsItemEx extract(FsService fsService, Param param) throws IOException {
		String target = param.getTarget();
		FsItemEx fsi = super.findItem(fsService, target);
		InputStream is = fsi.openInputStream();

		FsItemEx dir = this.getNewExtractDir(fsi.getParent());

		TarArchiveInputStream tis = new TarArchiveInputStream(is);

		dearchive(dir.getFile(), tis);

		IOUtils.closeQuietly(tis);
		IOUtils.closeQuietly(is);

		return dir;
	}

	/**
	 * 归档
	 * 
	 * @param srcFile
	 *            源路径
	 * @param taos
	 *            TarArchiveOutputStream
	 * @param basePath
	 *            归档包内相对路径
	 * @throws IOException
	 */
	protected void archive(File srcFile, TarArchiveOutputStream taos, String basePath) throws IOException {
		if (srcFile.isDirectory()) {
			archiveDir(srcFile, taos, basePath);
		} else {
			archiveFile(srcFile, taos, basePath);
		}
	}

	/**
	 * 目录归档
	 * 
	 * @param dir
	 * @param taos
	 *            TarArchiveOutputStream
	 * @param basePath
	 * @throws IOException
	 */
	private void archiveDir(File dir, TarArchiveOutputStream taos, String basePath) throws IOException {
		File[] files = dir.listFiles();
		if (files.length < 1) {
			TarArchiveEntry entry = new TarArchiveEntry(basePath + dir.getName() + PATH);
			taos.putArchiveEntry(entry);
			taos.closeArchiveEntry();
		}
		for (File file : files) {
			// 递归归档
			archive(file, taos, basePath + dir.getName() + PATH);
		}
	}

	/**
	 * 数据归档
	 * 
	 * @param file
	 *            待归档文件
	 * @param dir
	 *            归档包内相对路径
	 * @param taos
	 *            TarArchiveOutputStream
	 * @throws IOException
	 */
	private void archiveFile(File file, TarArchiveOutputStream taos, String dir) throws IOException {
		TarArchiveEntry entry = new TarArchiveEntry(dir + file.getName());
		entry.setSize(file.length());
		taos.putArchiveEntry(entry);
		FileUtils.copyFile(file, taos);
		taos.closeArchiveEntry();
	}

	/**
	 * 文件 解归档
	 * 
	 * @param destFile
	 *            目标文件
	 * @param tais
	 *            TarArchiveInputStream
	 * @throws IOException
	 */
	protected void dearchive(File destFile, TarArchiveInputStream tais) throws IOException {

		TarArchiveEntry entry = null;
		while ((entry = tais.getNextTarEntry()) != null) {
			// 文件
			String dir = destFile.getPath() + File.separator + entry.getName();
			File dirFile = new File(dir);
			// 文件检查
			fileProber(dirFile);
			if (entry.isDirectory()) {
				dirFile.mkdirs();
			} else {
				dearchiveFile(dirFile, tais);
			}

		}
	}

	/**
	 * 文件解归档
	 * 
	 * @param destFile
	 *            目标文件
	 * @param tais
	 *            TarArchiveInputStream
	 * @throws IOException
	 */
	private void dearchiveFile(File destFile, TarArchiveInputStream tais) throws IOException {
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destFile));
		IOUtils.copy(tais, bos);
		IOUtils.closeQuietly(bos);
	}

}
