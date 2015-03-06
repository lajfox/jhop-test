package com.techstar.modules.flexpaper.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.RandomAccessFileOrArray;
import com.lowagie.text.pdf.codec.TiffImage;
import com.sun.media.jai.codec.FileSeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.TIFFDecodeParam;
import com.techstar.modules.flexpaper.domain.QueryParam;

public class TiffToPdfUtils {

	private static final Logger logger = LoggerFactory.getLogger(TiffToPdfUtils.class);

	public static void main(String[] args) {
		File tifffile = new File("E:/资料交换区/测试样例/IMAGE000.TIF");
		// tifToPdf(new String[] { tifffile.getPath() }, tifffile.getPath() +
		// ".pdf");
		tif2Pdf(tifffile.getPath(), tifffile.getPath() + ".pdf");
	}

	/***
	 * 把多个tiff文件拼成一个pdf文件
	 * 
	 * @param tifFiles
	 *            多个tif文件全路径字符数组
	 * @param pdfFile
	 *            pdf输出文件全路径 Create by: lrm 2014-1-21 下午3:48:01
	 */
	public static void tifToPdf(String tifFiles[], String pdfFile) {

		File _toFile = new File(pdfFile).getParentFile();
		if (!_toFile.exists()) {// 如果pdf目录不存在新建目录
			_toFile.mkdirs();
		}

		// 创建一个文档对象
		Document doc = new Document(PageSize.LETTER, 0, 0, 0, 0);
		int comps = 0;
		String sFileTif = null;
		try {
			// 定义输出位置并把文档对象装入输出对象中
			PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(pdfFile));
			// 打开文档对象
			doc.open();
			for (int i = 0; i < tifFiles.length; i++) {
				sFileTif = tifFiles[i];
				PdfContentByte cb = writer.getDirectContent();
				RandomAccessFileOrArray ra = null;
				try {
					ra = new RandomAccessFileOrArray(sFileTif);
					comps = TiffImage.getNumberOfPages(ra);
				} catch (Throwable e) {
					logger.error("Exception in {} {}", sFileTif, e.getMessage());
				}

				for (int c = 0; c < comps; ++c) {
					try {
						Image img = TiffImage.getTiffImage(ra, c + 1);
						if (img != null) {

							img.scalePercent(7200f / img.getDpiX(), 7200f / img.getDpiY());
							doc.setPageSize(new Rectangle(img.getScaledWidth(), img.getScaledHeight()));
							img.setAbsolutePosition(0, 0);
							cb.addImage(img);
							doc.newPage();
						}
					} catch (DocumentException e) {
						logger.error("Exception {} page {} {} ", new Object[] { sFileTif, (c + 1), e.getMessage() });
					}
				}
				try {
					ra.close();
				} catch (IOException e) {
					e.printStackTrace();
				}// 关闭
			}
			// 关闭文档对象，释放资源
			doc.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

	/***
	 * 把tiff文件转化成pdf
	 * 
	 * @param tifFile
	 *            tif文件全路径
	 * @param pdfFile
	 *            pdf文件全路径 Create by: lrm 2014-1-21 下午3:48:26
	 */
	public static void tif2Pdf(String tifFile, String pdfFile) {

		File _toFile = new File(pdfFile).getParentFile();
		if (!_toFile.exists()) {// 如果pdf目录不存在新建目录
			_toFile.mkdirs();
		}
		// 创建一个文档对象
		Document doc = new Document(PageSize.LETTER, 0, 0, 0, 0);
		int comps = 0;
		RandomAccessFileOrArray ra = null;
		try {
			// 定义输出位置并把文档对象装入输出对象中
			PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(pdfFile));
			// 打开文档对象
			doc.open();

			PdfContentByte cb = writer.getDirectContent();
			try {
				ra = new RandomAccessFileOrArray(tifFile);
				comps = TiffImage.getNumberOfPages(ra);
			} catch (Throwable e) {
				logger.error("Exception in {} {}", tifFile, e.getMessage());
			}

			for (int c = 0; c < comps; ++c) {
				try {
					Image img = TiffImage.getTiffImage(ra, c + 1);
					if (img != null) {
						img.scalePercent(7200f / img.getDpiX(), 7200f / img.getDpiY());
						doc.setPageSize(new Rectangle(img.getScaledWidth(), img.getScaledHeight()));
						img.setAbsolutePosition(0, 0);
						cb.addImage(img);
						doc.newPage();
					}
				} catch (DocumentException e) {
					logger.error("Exception {} page {} {}", new Object[] { tifFile, (c + 1), e.getMessage() });
				}
			}
			logger.info("{}转化成功！", pdfFile);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (ra != null)
					ra.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// 关闭文档对象，释放资源
			if (doc != null)
				doc.close();
		}
	}

	public static int getTiffNumberOfPages(File tiffFile) throws IOException {
		FileSeekableStream ss = new FileSeekableStream(tiffFile.getPath());
		TIFFDecodeParam param0 = null;
		ImageDecoder dec = ImageCodec.createImageDecoder("tiff", ss, param0);
		int count = dec.getNumPages();
		return count;
	}

	public static void tif2pdf(QueryParam param) {
		File tifFile = param.getOfficeFile();
		File pdfFile = param.getSrcFile();
		TiffToPdfUtils.tif2Pdf(tifFile.getPath(), pdfFile.getPath());
	}
}
