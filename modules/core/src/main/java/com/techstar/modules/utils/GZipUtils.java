package com.techstar.modules.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.io.IOUtils;

public final class GZipUtils {
	public static byte[] gzip(byte[] data) {
		ByteArrayOutputStream byteOutput = new ByteArrayOutputStream(10240);
		GZIPOutputStream output = null;
		try {
			output = new GZIPOutputStream(byteOutput);
			output.write(data);
		} catch (IOException e) {
			throw new RuntimeException("G-Zip failed.", e);
		} finally {
			IOUtils.closeQuietly(output);
		}
		return byteOutput.toByteArray();
	}
}
