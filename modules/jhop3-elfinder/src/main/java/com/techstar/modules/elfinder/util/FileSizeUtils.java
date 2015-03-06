package com.techstar.modules.elfinder.util;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;

/***
 * 获取文件大小
 * 
 * @author lrm
 */
public class FileSizeUtils {

	/***
	 * 获取文件大小 Description: TODO
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 *             Create by: lrm 2013-12-18 下午4:17:27
	 */
	public static long getFileSize(File file) throws Exception {// 取得文件大小
		long size = 0;
		if (file.exists()) {
			FileInputStream fis = null;
			fis = new FileInputStream(file);
			size = fis.available();
		} else {
			file.createNewFile();
			System.out.println("文件不存在");
		}
		return size;
	}

	/***
	 * 获取文件目录大小 Description: TODO
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 *             Create by: lrm 2013-12-18 下午4:17:51
	 */
	public static long getDirSize(File file)// 取得文件夹大小
	{
		long size = 0;
		File flist[] = file.listFiles();
		for (File f : flist) {
			if (f.isDirectory()) {
				size = size + getDirSize(f);
			} else {
				size = size + f.length();
			}
		}
		return size;
	}

	/***
	 * 根据文件大小划分 Description: TODO
	 * 
	 * @param fileSize
	 * @return Create by: lrm 2013-12-18 下午4:13:09
	 */
	public static String getFormatFileSize(long fileSize) {// 转换文件大小
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileSize < 1024) {
			fileSizeString = df.format((double) fileSize) + "B";
		} else if (fileSize < 1048576) {
			fileSizeString = df.format((double) fileSize / 1024) + "K";
		} else if (fileSize < 1073741824) {
			fileSizeString = df.format((double) fileSize / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileSize / 1073741824) + "G";
		}
		return fileSizeString;
	}

	/***
	 * 根据文件大小划分 Description: TODO
	 * 
	 * @param fileSize
	 * @return Create by: lrm 2013-12-18 下午4:13:09
	 * @throws Exception
	 */
	public static String getFormatFileSize(File file) throws Exception {// 转换文件大小
		long fileSize;
		if (file.isDirectory()) {
			fileSize = getDirSize(file);
		} else {
			fileSize = getFileSize(file);
		}
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileSize < 1024) {
			fileSizeString = df.format((double) fileSize) + "B";
		} else if (fileSize < 1048576) {
			fileSizeString = df.format((double) fileSize / 1024) + "K";
		} else if (fileSize < 1073741824) {
			fileSizeString = df.format((double) fileSize / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileSize / 1073741824) + "G";
		}
		return fileSizeString;
	}

	/***
	 * 获取文件目录下包含的文件个数 Description: TODO
	 * 
	 * @param f
	 * @return Create by: lrm 2013-12-18 下午4:15:35
	 */
	public static long getDirFileSize(File file) {// 递归求取目录文件个数
		long size = 0;
		File flist[] = file.listFiles();
		size = flist.length;
		for (File f : flist) {
			if (f.isDirectory()) {
				size = size + getDirFileSize(f);
				size--;
			}
		}
		return size;
	}

	public static void main(String args[]) {
		FileSizeUtils g = new FileSizeUtils();
		long startTime = System.currentTimeMillis();
		try {
			long l = 0;
			String path = "E://待办工作//部门相关工作//2013年工作日志//Q4绩效考核表";
			File ff = new File(path);
			if (ff.isDirectory()) { // 如果路径是文件夹的时候
				System.out.println("文件个数           " + g.getDirFileSize(ff));
				System.out.println("目录");
				l = g.getDirSize(ff);
				System.out.println(path + "目录的大小为：" + g.getFormatFileSize(l));
			} else {
				System.out.println("     文件个数           1");
				System.out.println("文件");
				l = g.getFileSize(ff);
				System.out.println(path + "文件的大小为：" + g.getFormatFileSize(l));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		long endTime = System.currentTimeMillis();
		System.out.println("总共花费时间为：" + (endTime - startTime) + "毫秒...");
	}
}
