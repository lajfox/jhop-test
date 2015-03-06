package com.techstar.modules.flexpaper.commands;


public class Png2swfCommond extends Image2swfCommond {

	@Override
	protected String getCommand() {
		return propertySource.getPng2swf();
	}

}
