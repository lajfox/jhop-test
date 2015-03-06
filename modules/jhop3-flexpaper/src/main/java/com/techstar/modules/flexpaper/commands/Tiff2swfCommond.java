package com.techstar.modules.flexpaper.commands;

import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.sun.media.jai.codec.FileSeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.JPEGEncodeParam;
import com.sun.media.jai.codec.TIFFDecodeParam;
import com.sun.media.jai.codec.TIFFEncodeParam;
import com.techstar.modules.flexpaper.command.AbstractSwftoolsCommand;
import com.techstar.modules.flexpaper.domain.QueryParam;
import com.techstar.modules.flexpaper.util.ConverUtils;
import com.techstar.modules.flexpaper.util.ProcessUtils;

public class Tiff2swfCommond extends AbstractSwftoolsCommand {

	@Override
	protected void execute(final QueryParam param, final HttpServletRequest request,
			final HttpServletResponse response, final ServletContext servletContext) throws IOException {

		File swfFile = ConverUtils.getConverFile(param,propertySource.getPathswf(),"swf");
		
		File srcFile = param.getSrcFile();

		if (!param.getOfficeFile().exists()) {
			logger.error("文件:{}不存在！", param.getOfficeFile().getName());
			write("文件:" + param.getOfficeFile().getName() + "不存在！", response);
			return;
		}

		if (isNotConverted(param.getOfficeFile(), swfFile)) {
			 tiff2jpg2swf(srcFile,swfFile.getParent(),response);
		}

		if (swfFile.exists()) {
			if (propertySource.isAllowcache()) {
				setCacheHeaders(response);
			}

			if (!propertySource.isAllowcache() || (propertySource.isAllowcache() && endOrRespond(request, response))) {
				response.setContentType("application/x-shockwave-flash");
				response.setHeader("Accept-Ranges", "bytes");
				OutputStream out = null;
				try {
					out = response.getOutputStream();
					FileUtils.copyFile(swfFile, out);
				} finally {
					IOUtils.closeQuietly(out);
				}
			}
		} else {
			logger.error("SWF file :{}不存在.", swfFile.getName());
			write("SWF file:" + swfFile.getName() + "不存在.", response);
		}
	}
	
	public void tiff2jpg2swf(File tifFile,String swfpath,final HttpServletResponse response) throws IOException {
		FileSeekableStream ss = new FileSeekableStream(tifFile.getPath());
		TIFFDecodeParam param0 = null;
		TIFFEncodeParam param = new TIFFEncodeParam();
		JPEGEncodeParam param1 = new JPEGEncodeParam();
		ImageDecoder dec = ImageCodec.createImageDecoder("tiff", ss, param0);
		int count = dec.getNumPages();
		param.setCompression(TIFFEncodeParam.COMPRESSION_GROUP4);
		param.setLittleEndian(false); // Intel
		logger.info("This TIF has {}image(s)", count);
		
		String command = propertySource.getJpeg2swf();
		swfpath = swfpath.replace('\\', '/');
		for (int i = 0; i < count; i++) {
			RenderedImage page = dec.decodeAsRenderedImage(i);
			File srcFile = new File(swfpath+"/"+FilenameUtils.getBaseName(tifFile.getName())+"_" + (i+1) + ".jpg");
			logger.info("Saving {}",srcFile.getCanonicalPath());
			ParameterBlock pb = new ParameterBlock();
			pb.addSource(page);
			pb.add(srcFile.toString());
			pb.add("JPEG");
			pb.add(param1);
			RenderedOp r = JAI.create("filestore", pb);
			r.dispose();
			
			//转swf
			command = command.replace("{srcfile}", srcFile.getAbsolutePath());
			command = command.replace("{swffile}", new File(swfpath+"/"+tifFile.getName()+"_" + (i+1) + ".swf").getAbsolutePath());
		
			boolean return_var = ProcessUtils.exec(command);
			
			if (!return_var) {
				logger.error("文件转换失败, 请确认文件转换工具swftools己经安装和当前用户对swf输出目录拥有读写权限");
				write("文件转换失败, 请确认文件转换工具swftools己经安装和当前用户对swf输出目录拥有读写权限", response);
				break;
			}else{
				if(srcFile.exists()){
					srcFile.delete();
				}
			}
		}
	}

	private boolean isNotConverted(File srcFile, File swfFile) {
		return !swfFile.exists() || srcFile.lastModified() > swfFile.lastModified();
	}

}
