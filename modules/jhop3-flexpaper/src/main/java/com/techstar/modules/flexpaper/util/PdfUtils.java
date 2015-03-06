package com.techstar.modules.flexpaper.util;

import java.io.File;
import java.io.IOException;

import com.lowagie.text.pdf.PdfReader;

public final class PdfUtils {

	public static int getNumberOfPages(String filename) throws IOException {
		PdfReader reader = new PdfReader(filename);
		int i = reader.getNumberOfPages();
		reader.close();
		return i;
	}

	public static int getNumberOfPages(File file) throws IOException {
		return getNumberOfPages(file.getAbsolutePath());
	}

}
