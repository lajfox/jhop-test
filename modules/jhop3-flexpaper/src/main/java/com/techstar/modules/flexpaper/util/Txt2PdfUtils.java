package com.techstar.modules.flexpaper.util;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;

public class Txt2PdfUtils {

	private static final Logger logger = LoggerFactory.getLogger(Txt2PdfUtils.class);

	public static void txt2pdf(File txtFile, File pdfFile) {
		txt2pdf(txtFile.getAbsolutePath(), pdfFile.getAbsolutePath());
	}

	public static void txt2pdf(String txtPath, String pdfPath) {
		Document document = new Document(PageSize.A4, 40, 40, 30, 30);
		BufferedReader reader = null;
		InputStream is = null;
		try {
			// 读取文本内容
			is = new FileInputStream(txtPath);
			reader = new BufferedReader(new InputStreamReader(is,CpDetectorUtils.getFileCodeByPlugin(new File(txtPath))));
			PdfWriter.getInstance(document, new FileOutputStream(pdfPath));

			/**
			 * 新建一个字体,iText的方法 STSongStd-Light 是字体，在iTextAsian.jar 中以property为后缀
			 * UniGB-UCS2-H 是编码，在iTextAsian.jar 中以cmap为后缀 H 代表文字版式是 横版， 相应的 V 代表
			 * 竖版
			 */
			BaseFont bfChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", false);
			Font fontChinese = new Font(bfChinese, 12, Font.NORMAL, Color.BLACK);
			document.open();
			String line = reader.readLine();
			while (line != null) {
				Paragraph pg = new Paragraph(line, fontChinese);
				pg.setAlignment(Element.ALIGN_LEFT);
				pg.setLeading(20.0f);
				document.add(pg);
				line = reader.readLine();
			}
			document.close();
		} catch (Exception e) {
			logger.error("{}目标文件不存，或者不可读！" + pdfPath);
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(reader);
			IOUtils.closeQuietly(is);
		}
	}

	public static void main(String[] args) throws DocumentException, IOException {
		String txtPath = "E:/环境配置.txt"; // txt文件
		String pdfPath = "E:/环境配置.txt.pdf"; // 生成的pdf文件
		txt2pdf(txtPath, pdfPath);
	}
}
