package com.techstar.modules.flexpaper.commands;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;

import com.techstar.modules.flexpaper.domain.QueryParam;
import com.techstar.modules.flexpaper.util.ConverUtils;

public class OthersImage2swfCommond extends Png2swfCommond {

	@Override
	protected void before(final QueryParam param) throws IOException {
		File file = param.getSrcFile();

		BufferedImage image = ImageIO.read(file);

		File outFile = ConverUtils.getConverFile(param,propertySource.getPathswf(),"png");
		Thumbnails.of(file).size(image.getWidth(), image.getHeight()).outputFormat("png").toFile(outFile);
		param.setSrcFile(outFile);
	}

}
