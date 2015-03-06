package com.techstar.modules.elfinder.compress;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.techstar.modules.elfinder.controller.executor.FsItemEx;
import com.techstar.modules.elfinder.domain.Param;
import com.techstar.modules.elfinder.service.FsService;

/**
 * zip压缩、解压
 * 
 * @author sundoctor
 * 
 */

public class ZipCompressCommand extends AbstractCompressCommand {

	@Override
	public FsItemEx compress(final FsService fsService, final Param param) throws IOException {

		String[] targets = param.getTargets_();

		FsItemEx fsi = super.findItem(fsService, targets[0]);
		FsItemEx parent = fsi.getParent();

		FsItemEx newFile = getNewArchiveFile(parent, "zip");
		ZipArchiveOutputStream zos = new ZipArchiveOutputStream(new BufferedOutputStream(new FileOutputStream(
				newFile.getFile())));
		zos.setEncoding("UTF-8");

		for (String target : targets) {
			fsi = super.findItem(fsService, target);
			archive(fsi.getFile(), zos, BASE_DIR);
		}

		IOUtils.closeQuietly(zos);

		return newFile;
	}

	public FsItemEx extract(final FsService fsService, final Param param) throws IOException {

		String target = param.getTarget();
		FsItemEx fsi = super.findItem(fsService, target);
		InputStream is = fsi.openInputStream();

		FsItemEx dir = this.getNewExtractDir(fsi.getParent());
		ZipArchiveInputStream zis = new ZipArchiveInputStream(is, "UTF-8");

		dearchive(dir.getFile(), zis);

		IOUtils.closeQuietly(zis);
		IOUtils.closeQuietly(is);

		return dir;
	}
	
	
	/***
	 * Description: 解压到当前文件夹，返回解压列表
	 * Create by: lrm 2014-3-5 上午10:32:55
	 */
	public List<FsItemEx> extractCwd(final FsService fsService, final Param param) throws IOException {
		String target = param.getTarget();
		FsItemEx fsi = super.findItem(fsService, target);
		InputStream is = fsi.openInputStream();

		FsItemEx dir = fsi.getParent();
		ZipArchiveInputStream zis = new ZipArchiveInputStream(is,"GBK");

		List<FsItemEx> added = dearchiveCwd(dir, zis);

		IOUtils.closeQuietly(zis);
		IOUtils.closeQuietly(is);

		return added;
	}
	
	/***
	 * Description: 解压到当前文件夹，返回解压列表
	 * @param destFile
	 * @param zais
	 * @return
	 * @throws IOException
	 * Create by: lrm 2014-3-5 上午10:32:55
	 */
	private List<FsItemEx> dearchiveCwd(FsItemEx parentFile, ZipArchiveInputStream zais) throws IOException {
		List<FsItemEx> added = new ArrayList<FsItemEx>();
		ZipArchiveEntry entry = null;
		String parentPath = parentFile.getFile().getPath();
		while ((entry = zais.getNextZipEntry()) != null) {
			// 文件
			String dir = parentPath + File.separator + entry.getName();
			File dirFile = new File(dir);
			if(parentPath.equalsIgnoreCase(dirFile.getParent())){
				added.add(new FsItemEx(parentFile,entry.getName()));
			}
			// 文件检查
			fileProber(dirFile);
			if (entry.isDirectory()) {
				dirFile.mkdirs();
			} else {
				dearchiveFile(dirFile, zais);
			}
		}
		return added;
	}

	/**
	 * 归档
	 * 
	 * @param srcFile
	 *            源路径
	 * @param zaos
	 *            ZipArchiveOutputStream
	 * @param basePath
	 *            归档包内相对路径
	 * @throws IOException
	 */
	private static void archive(File srcFile, ZipArchiveOutputStream zaos, String basePath) throws IOException {
		if (srcFile.isDirectory()) {
			archiveDir(srcFile, zaos, basePath);
		} else {
			archiveFile(srcFile, zaos, basePath);
		}
	}

	/**
	 * 目录归档
	 * 
	 * @param dir
	 * @param zaos
	 *            ZipArchiveOutputStream
	 * @param basePath
	 * @throws IOException
	 */
	private static void archiveDir(File dir, ZipArchiveOutputStream zaos, String basePath) throws IOException {
		File[] files = dir.listFiles();
		if (files.length < 1) {
			ZipArchiveEntry entry = new ZipArchiveEntry(basePath + dir.getName() + PATH);
			zaos.putArchiveEntry(entry);
			zaos.closeArchiveEntry();
		}
		for (File file : files) {
			// 递归归档
			archive(file, zaos, basePath + dir.getName() + PATH);
		}
	}

	/**
	 * 数据归档
	 * 
	 * @param file
	 *            待归档文件
	 * @param dir
	 *            归档包内相对路径
	 * @param zaos
	 *            ZipArchiveOutputStream
	 * @throws IOException
	 */
	private static void archiveFile(File file, ZipArchiveOutputStream zaos, String dir) throws IOException {
		ZipArchiveEntry entry = new ZipArchiveEntry(dir + file.getName());
		entry.setSize(file.length());
		zaos.putArchiveEntry(entry);
		FileUtils.copyFile(file, zaos);
		zaos.closeArchiveEntry();
	}

	/**
	 * 文件 解归档
	 * 
	 * @param destFile
	 *            目标文件
	 * @param tais
	 *            ZipInputStream
	 * @throws Exception
	 */
	private void dearchive(File destFile, ZipArchiveInputStream zais) throws IOException {
		ZipArchiveEntry entry = null;
		while ((entry = zais.getNextZipEntry()) != null) {
			// 文件
			String dir = destFile.getPath() + File.separator + entry.getName();
			File dirFile = new File(dir);
			// 文件检查
			fileProber(dirFile);
			if (entry.isDirectory()) {
				dirFile.mkdirs();
			} else {
				dearchiveFile(dirFile, zais);
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
	 * @throws Exception
	 */
	private static void dearchiveFile(File destFile, ZipArchiveInputStream zais) throws IOException {		
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destFile));
		IOUtils.copy(zais, bos);
		IOUtils.closeQuietly(bos);
	}

}
