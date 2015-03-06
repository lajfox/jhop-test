package com.techstar.modules.flexpaper.commands;


public class Jpeg2swfCommond extends Image2swfCommond {

	@Override
	protected String getCommand() {
		return propertySource.getJpeg2swf();
	}

}
