package com.techstar.modules.elfinder.compress;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.io.IOUtils;

import com.techstar.modules.elfinder.controller.executor.FsItemEx;
import com.techstar.modules.elfinder.domain.Param;
import com.techstar.modules.elfinder.service.FsService;

public class GZipCompressCommand extends TarCompressCommand {

	@Override
	public FsItemEx compress(final FsService fsService, final Param param) throws IOException {

		String[] targets = param.getTargets_();
		FsItemEx fsi = super.findItem(fsService, targets[0]);
		FsItemEx parent = fsi.getParent();
		FsItemEx newFile = getNewArchiveFile(parent, "tar.gz");

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		TarArchiveOutputStream tos = new TarArchiveOutputStream(new BufferedOutputStream(bos));
		tos.setLongFileMode(TarArchiveOutputStream.LONGFILE_POSIX);

		for (String target : targets) {
			fsi = super.findItem(fsService, target);
			archive(fsi.getFile(), tos, BASE_DIR);
		}

		IOUtils.closeQuietly(tos);
		IOUtils.closeQuietly(bos);

		InputStream bis = new BufferedInputStream(new ByteArrayInputStream(bos.toByteArray()));
		GZIPOutputStream gos = new GZIPOutputStream(new FileOutputStream(newFile.getAbsolutePath()), 65536);

		IOUtils.copy(bis, gos);

		gos.finish();
		gos.flush();

		IOUtils.closeQuietly(gos);
		IOUtils.closeQuietly(bis);

		return newFile;
	}

	@Override
	public FsItemEx extract(FsService fsService, Param param) throws IOException {

		String target = param.getTarget();
		FsItemEx fsi = super.findItem(fsService, target);
		InputStream is = fsi.openInputStream();
		FsItemEx dir = this.getNewExtractDir(fsi.getParent());

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		GZIPInputStream gzIn = new GZIPInputStream(is, 65536);
		IOUtils.copy(gzIn, bos);

		IOUtils.closeQuietly(gzIn);
		IOUtils.closeQuietly(bos);
		IOUtils.closeQuietly(is);

		TarArchiveInputStream tais = new TarArchiveInputStream(new BufferedInputStream(new ByteArrayInputStream(
				bos.toByteArray())));
		dearchive(dir.getFile(), tais);

		IOUtils.closeQuietly(tais);

		return dir;
	}

}
