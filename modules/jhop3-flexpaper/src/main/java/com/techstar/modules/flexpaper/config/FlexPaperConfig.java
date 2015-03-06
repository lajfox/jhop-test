package com.techstar.modules.flexpaper.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.techstar.modules.flexpaper.FlexpaperPropertySource;
import com.techstar.modules.flexpaper.command.DefaultSwftoolsCommandFactory;
import com.techstar.modules.flexpaper.command.SwftoolsCommandFactory;
import com.techstar.modules.flexpaper.commands.Gif2swfCommond;
import com.techstar.modules.flexpaper.commands.Jpeg2swfCommond;
import com.techstar.modules.flexpaper.commands.OthersImage2swfCommond;
import com.techstar.modules.flexpaper.commands.Pdf2jsonCommond;
import com.techstar.modules.flexpaper.commands.Pdf2swfCommond;
import com.techstar.modules.flexpaper.commands.Png2swfCommond;
import com.techstar.modules.flexpaper.commands.SwfdumpCommond;
import com.techstar.modules.flexpaper.commands.SwfrenderCommond;
import com.techstar.modules.flexpaper.commands.SwfstringsCommond;
import com.techstar.modules.flexpaper.commands.Tiff2swfCommond;

@Configuration
// @ComponentScan(basePackages = "com.techstar.modules.flexpaper")
// @ImportResource("classpath:com/techstar/modules/flexpaper/applicationContext.xml")
@PropertySource(value = { "classpath:com/techstar/modules/flexpaper/config/flexpaper.properties",
		"classpath:flexpaper.properties", "classpath:application.properties" }, ignoreResourceNotFound = true)
public class FlexPaperConfig {

	@Bean
	public FlexpaperPropertySource flexpaperPropertySource() {
		return new FlexpaperPropertySource();
	}

	@Bean
	public SwftoolsCommandFactory swftoolsCommandFactory() {

		return new DefaultSwftoolsCommandFactory();
	}

	@Bean(initMethod = "init", destroyMethod = "destroy")
	public Pdf2swfCommond swfCommond() {
		return new Pdf2swfCommond();
	}

	@Bean
	public Png2swfCommond pngCommond() {
		return new Png2swfCommond();
	}

	@Bean
	public Gif2swfCommond gifCommond() {
		return new Gif2swfCommond();
	}

	@Bean
	public Jpeg2swfCommond jpegCommond() {
		return new Jpeg2swfCommond();
	}

	@Bean
	public OthersImage2swfCommond otherCommond() {
		return new OthersImage2swfCommond();
	}

	@Bean
	public Tiff2swfCommond tiffCommond() {
		return new Tiff2swfCommond();
	}

	@Bean
	public SwfdumpCommond swfdumpCommond() {
		return new SwfdumpCommond();
	}

	@Bean
	public SwfstringsCommond swfstringsCommond() {
		return new SwfstringsCommond();
	}

	@Bean
	public SwfrenderCommond swfrenderCommond() {
		return new SwfrenderCommond();
	}

	@Bean
	public Pdf2jsonCommond jsonCommond() {
		return new Pdf2jsonCommond();
	}

	@Bean
	public Pdf2jsonCommond jsonpCommond() {
		return new Pdf2jsonCommond();
	}
}
