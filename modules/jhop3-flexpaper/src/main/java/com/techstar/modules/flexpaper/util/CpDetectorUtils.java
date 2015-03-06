package com.techstar.modules.flexpaper.util;

import info.monitorenter.cpdetector.io.ASCIIDetector;
import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;
import info.monitorenter.cpdetector.io.ParsingDetector;
import info.monitorenter.cpdetector.io.UnicodeDetector;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CpDetectorUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(CpDetectorUtils.class);
	
	public static String getFileCodeByPlugin(File file) {
		String result = null;
		/*------------------------------------------------ 
		detector是探测器，它把探测任务交给具体的探测实现类的实例完成。 
		cpDetector内置了一些常用的探测实现类，这些探测实现类的实例可以通过add方法 
		加进来，如ParsingDetector、 JChardetFacade、ASCIIDetector、UnicodeDetector。   
		detector按照“谁最先返回非空的探测结果，就以该结果为准”的原则返回探测到的 
		字符集编码。 
		---------------------------------------------------*/

		CodepageDetectorProxy proxy = CodepageDetectorProxy.getInstance();

		/*------------------------------------------------------
		  ParsingDetector可用于检查HTML、XML等文件或字符流的编码,构造方法中的参数用于 
		  指示是否显示探测过程的详细信息，为false不显示。 
		-------------------------------------------------------*/

		proxy.add(new ParsingDetector(false));
		proxy.add(JChardetFacade.getInstance());
		proxy.add(ASCIIDetector.getInstance());
		proxy.add(UnicodeDetector.getInstance());

		/*----------------------------------------------
		 JChardetFacade封装了由Mozilla组织提供的JChardet，它可以完成大多数文件的编码 
		 测定。所以，一般有了这个探测器就可满足大多数项目的要求，如果你还不放心，可以 
		 再多加几个探测器，比如下面的ASCIIDetector、UnicodeDetector等。 
		 ---------------------------------------------------------*/
		Charset cset = null;
		try {
			cset = proxy.detectCodepage(file.toURL());
			if (cset != null) {
				result = cset.name();
			} else {
				new RuntimeException("查不到对应的编码格式");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info(file.getAbsolutePath()+" 编码方式为："+cset);
		return result;
	}

}
