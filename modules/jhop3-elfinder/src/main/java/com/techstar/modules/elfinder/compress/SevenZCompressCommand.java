package com.techstar.modules.elfinder.compress;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile;
import org.apache.commons.io.IOUtils;

import com.techstar.modules.elfinder.controller.executor.FsItemEx;
import com.techstar.modules.elfinder.domain.Param;
import com.techstar.modules.elfinder.service.FsService;

public class SevenZCompressCommand extends AbstractCompressCommand {

	@Override
	public FsItemEx compress(final FsService fsService, final Param param) throws IOException {

		String[] targets = param.getTargets_();
		FsItemEx fsi = super.findItem(fsService, targets[0]);
		FsItemEx parent = fsi.getParent();

		FsItemEx newFile = getNewArchiveFile(parent, "7z");
		SevenZOutputFile sevenZOutput = new SevenZOutputFile(newFile.getFile());

		for (String target : targets) {
			fsi = super.findItem(fsService, target);
			archive(fsi.getFile(), sevenZOutput, BASE_DIR);
		}

		sevenZOutput.close();

		return newFile;
	}

	@Override
	public FsItemEx extract(FsService fsService, Param param) throws IOException {

		String target = param.getTarget();
		FsItemEx fsi = super.findItem(fsService, target);
		InputStream is = fsi.openInputStream();

		FsItemEx dir = this.getNewExtractDir(fsi.getParent());

		SevenZFile sevenZFile = new SevenZFile(fsi.getFile());

		dearchive(dir.getFile(), sevenZFile);

		sevenZFile.close();
		IOUtils.closeQuietly(is);

		return dir;
	}

	/**
	 * 归档
	 * 
	 * @param srcFile
	 *            源路径
	 * @param sevenZOutput
	 *            SevenZOutputFile
	 * @param basePath
	 *            归档包内相对路径
	 * @throws IOException
	 */
	protected void archive(File srcFile, SevenZOutputFile sevenZOutput, String basePath) throws IOException {
		if (srcFile.isDirectory()) {
			archiveDir(srcFile, sevenZOutput, basePath);
		} else {
			archiveFile(srcFile, sevenZOutput, basePath);
		}
	}

	/**
	 * 目录归档
	 * 
	 * @param dir
	 * @param sevenZOutput
	 *            SevenZOutputFile
	 * @param basePath
	 * @throws IOException
	 */
	private void archiveDir(File dir, SevenZOutputFile sevenZOutput, String basePath) throws IOException {
		File[] files = dir.listFiles();
		if (files.length < 1) {
			SevenZArchiveEntry entry = sevenZOutput.createArchiveEntry(dir, basePath + dir.getName() + PATH);
			sevenZOutput.putArchiveEntry(entry);
			sevenZOutput.closeArchiveEntry();
		}
		for (File file : files) {
			// 递归归档
			archive(file, sevenZOutput, basePath + dir.getName() + PATH);
		}
	}

	/**
	 * 数据归档
	 * 
	 * @param file
	 *            待归档文件
	 * @param dir
	 *            归档包内相对路径
	 * @param sevenZOutput
	 *            SevenZOutputFile
	 * @throws IOException
	 */
	private void archiveFile(File file, SevenZOutputFile sevenZOutput, String dir) throws IOException {

		SevenZArchiveEntry entry = sevenZOutput.createArchiveEntry(file, dir + file.getName());
		entry.setSize(file.length());
		sevenZOutput.putArchiveEntry(entry);

		byte[] buffer = new byte[4096];
		BufferedInputStream input = new BufferedInputStream(new FileInputStream(file));
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			sevenZOutput.write(buffer, 0, n);
		}

		IOUtils.closeQuietly(input);

		sevenZOutput.closeArchiveEntry();
	}

	/**
	 * 文件 解归档
	 * 
	 * @param destFile
	 *            目标文件
	 * @param sevenZFile
	 *            SevenZFile
	 * @throws IOException
	 */
	protected void dearchive(File destFile, SevenZFile sevenZFile) throws IOException {

		SevenZArchiveEntry entry = null;
		while ((entry = sevenZFile.getNextEntry()) != null) {
			// 文件
			String dir = destFile.getPath() + File.separator + entry.getName();
			File dirFile = new File(dir);
			// 文件检查
			fileProber(dirFile);
			if (entry.isDirectory()) {
				dirFile.mkdirs();
			} else {
				dearchiveFile(dirFile, sevenZFile);
			}

		}
	}

	/**
	 * 文件解归档
	 * 
	 * @param destFile
	 *            目标文件
	 * @param sevenZFile
	 *            SevenZFile
	 * @throws IOException
	 */
	private void dearchiveFile(File destFile, SevenZFile sevenZFile) throws IOException {
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destFile));

		byte[] buffer = new byte[4096];
		int n = 0;
		while (-1 != (n = sevenZFile.read(buffer))) {
			bos.write(buffer, 0, n);
		}

		IOUtils.closeQuietly(bos);
	}

}
