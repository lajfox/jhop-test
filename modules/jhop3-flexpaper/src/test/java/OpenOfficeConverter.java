import java.io.File;
import java.io.FileNotFoundException;

import org.apache.commons.io.FileUtils;
import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeManager;

public class OpenOfficeConverter {

	private static OfficeManager officeManager;

	public static void main(String[] args) {
		OpenOfficeConverter cov = new OpenOfficeConverter();
		String inputFile = "/home/sundoctor/文档/240多个jQuery_UI插件.doc";
		cov.convert(inputFile);
	}

	/**
	 * 转换格式
	 * 
	 * @param inputFile
	 *            需要转换的原文件路径
	 * @param fileType
	 *            要转换的目标文件类型 html,pdf
	 */
	public void convert(String inputFile) {

		startService();

		OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);
		converter.convert(new File(inputFile), new File("/home/sundoctor/docs/aaa.pdf"));
		stopService();
	}

	public static void startService() {
		DefaultOfficeManagerConfiguration configuration = new DefaultOfficeManagerConfiguration();
		try {
			System.out.println("准备启动服务....");

			//configuration.setPortNumbers(8100); // 设置转换端口，默认为2002
			configuration.setTaskExecutionTimeout(1000 * 60 * 5L);// 设置任务执行超时为5分钟
			configuration.setTaskQueueTimeout(1000 * 60 * 60 * 24L);// 设置任务队列超时为24小时

			officeManager = configuration.buildOfficeManager();
			officeManager.start(); // 启动服务
			System.out.println("office转换服务启动成功!");
		} catch (Exception ce) {
			System.out.println("office转换服务启动失败!详细信息:" + ce);
		}
	}

	public static void stopService() {
		System.out.println("关闭office转换服务....");
		if (officeManager != null) {
			officeManager.stop();
		}
		System.out.println("关闭office转换成功!");
	}
}
