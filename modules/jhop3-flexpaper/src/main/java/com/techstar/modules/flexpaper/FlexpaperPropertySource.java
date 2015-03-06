package com.techstar.modules.flexpaper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

public class FlexpaperPropertySource {
	
    @Autowired  
    private Environment env; 

	public String getPathswf() {
		return env.getProperty("path.swf");
	}

	public boolean isPdf2swf() {
		return env.getProperty("pdf2swf", Boolean.class);
	}

	public String getLicensekey() {
		return env.getProperty("licensekey");
	}

	public String getRenderingorder() {
		return env.getProperty("renderingorder");
	}

	public boolean isAllowcache() {
		return env.getProperty("allowcache", Boolean.class);
	}

	public String getSingledoc() {
		return env.getProperty("cmd.conversion.singledoc");
	}

	public String getSplitpages() {
		return env.getProperty("cmd.conversion.splitpages");
	}

	public String getRenderpage() {
		return env.getProperty("cmd.conversion.renderpage");
	}

	public String getRendersplitpage() {
		return env.getProperty("cmd.conversion.rendersplitpage");
	}

	public String getJsonfile() {
		return env.getProperty("cmd.conversion.jsonfile");
	}

	public String getSplitjsonfile() {
		return env.getProperty("cmd.conversion.splitjsonfile");
	}

	public String getExtracttext() {
		return env.getProperty("cmd.searching.extracttext");
	}

	public String getSwfwidth() {
		return env.getProperty("cmd.query.swfwidth");
	}

	public String getSwfheight() {
		return env.getProperty("cmd.query.swfheight");
	}

	public String getPng2swf() {
		return env.getProperty("cmd.conversion.png2swf");
	}

	public String getGif2swf() {
		return env.getProperty("cmd.conversion.gif2swf");
	}

	public String getJpeg2swf() {
		return env.getProperty("cmd.conversion.jpeg2swf");
	}

}
