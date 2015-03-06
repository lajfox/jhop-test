package com.techstar.modules.flexpaper.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.poi.xwpf.usermodel.BreakClear;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import com.lowagie.text.DocumentException;

public final class TxtUtils {

	public static void txt2docx(String txtFile, String docxFile) throws IOException {
		txt2docx(new File(txtFile), new File(docxFile));
	}

	public static void txt2docx(File txtFile, File docxFile) throws IOException {
		// BufferedReader txtReader = new BufferedReader(new
		// FileReader(txtFile));
		BufferedReader txtReader = new BufferedReader(new InputStreamReader(new FileInputStream(txtFile),
				CpDetectorUtils.getFileCodeByPlugin(txtFile)));
		OutputStream docxOutput = new BufferedOutputStream(new FileOutputStream(docxFile));
		txt2docx(txtReader, docxOutput);
	}

	public static void txt2docx(BufferedReader txtReader, OutputStream docxOutput) throws IOException {
		XWPFDocument doc = new XWPFDocument();

		XWPFParagraph paragraph = doc.createParagraph();
		// paragraph.setWordWrap(true);
		// paragraph.setPageBreak(true);

		// paragraph.setAlignment(ParagraphAlignment.DISTRIBUTE);
		// paragraph.setAlignment(ParagraphAlignment.BOTH);
		// paragraph.setSpacingLineRule(LineSpacingRule.EXACT);
		// paragraph.setIndentationFirstLine(600);

		XWPFRun xwpfrun = paragraph.createRun();
		// xwpfrun.setTextPosition(-10);
		// xwpfrun.addCarriageReturn();
		// xwpfrun.addBreak();

		String line = txtReader.readLine();
		while (line != null) {
			//System.out.println(line);
			xwpfrun.setText(line);
			xwpfrun.addBreak(BreakClear.ALL);
			line = txtReader.readLine();
		}

		doc.write(docxOutput);

		IOUtils.closeQuietly(docxOutput);
		IOUtils.closeQuietly(txtReader);
	}

	/***
	 *  以 UE 十六进制编辑模式的显示方式显示数据
	 * @param file
	 * @return
	 * @throws IOException
	 * Create by: lrm 2014-2-17 下午1:34:42
	 */
	public static String getHexDataAsUE(File file) throws IOException {
		InputStream is = new FileInputStream(file);
		int len = is.available();
		byte[] buf = new byte[is.available()];
		is.read(buf);
		int i = 0;
		StringBuilder sb = new StringBuilder();
		while (i < len){
			if (i % 16 == 0)
				sb.append(String.format("%08xh: ", i));
			for (int j = 0; j < 0x10; j++) {
				sb.append(String.format("%02x ", buf[i]).toUpperCase());
				i++;
				if (i >= buf.length) {
					break;
				}
			}
			i -= 0x10;
			sb.append("; ");
			for (int j = 0; j < 0x10; j++) {
				if (buf[i] <= 0x1f) {
					sb.append('.');
				} else {
					sb.append((char)buf[i]);
				}
				i++;
				if (i >= buf.length) {
					break;
				}
			}
			sb.append("\n");
		}
		
		IOUtils.closeQuietly(is);
		//System.out.println(sb.toString());
		return sb.toString();
	}

	/**
	 * 把以 UE 十六进制编辑模式文件转化成doc文件
	 * @param file
	 * @param docxFile
	 * @throws IOException
	 * @throws FileNotFoundException
	 * Create by: lrm 2014-2-17 下午2:54:11
	 */
	public static void hex2Doc(File hexFile, File docxFile) throws IOException, FileNotFoundException {
		BufferedReader txtReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(getHexDataAsUE(hexFile)
				.getBytes())));
		OutputStream docxOutput = new BufferedOutputStream(new FileOutputStream(docxFile));
		txt2docx(txtReader, docxOutput);
	}

	public static void main(String[] args) throws DocumentException, IOException {
		String txtPath = "C:/Documents and Settings/lrm/桌面/UltraEidt-File.001";// "E:/资料交换区/超级用户_admin/测试/CC-CALCU/HARM2000.005";// txt文件
		String pdfPath = "C:/Documents and Settings/lrm/桌面/UltraEidt-File.001.docx";// "E:/资料交换区/超级用户_admin/测试/CC-CALCU/HARM2000.005.docx";																	// 生成的pdf文件
		// txt2docx(txtPath, pdfPath);
//		int i = 0x1f;
//		System.out.println(String.format("%d", i));
		hex2Doc(new File(txtPath), new File(pdfPath));
	}
}
