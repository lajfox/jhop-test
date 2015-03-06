package com.techstar.modules.flexpaper.commands;

public class Gif2swfCommond extends Image2swfCommond {

	@Override
	protected String getCommand() {
		return propertySource.getGif2swf();
	}

}
