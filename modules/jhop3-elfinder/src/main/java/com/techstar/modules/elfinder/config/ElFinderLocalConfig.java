package com.techstar.modules.elfinder.config;

import java.io.File;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

import com.techstar.modules.elfinder.compress.CompressCommand;
import com.techstar.modules.elfinder.compress.CompressCommandFactory;
import com.techstar.modules.elfinder.compress.DefaultCompressCommandFactory;
import com.techstar.modules.elfinder.compress.GZipCompressCommand;
import com.techstar.modules.elfinder.compress.RarCompressCommand;
import com.techstar.modules.elfinder.compress.SevenZCompressCommand;
import com.techstar.modules.elfinder.compress.TarCompressCommand;
import com.techstar.modules.elfinder.compress.ZipCompressCommand;
import com.techstar.modules.elfinder.controller.executor.CommandExecutor;
import com.techstar.modules.elfinder.controller.executor.CommandExecutorFactory;
import com.techstar.modules.elfinder.controller.executor.DefaultCommandExecutorFactory;
import com.techstar.modules.elfinder.controller.executors.ArchiveCommandExecutor;
import com.techstar.modules.elfinder.controller.executors.DimLocalCommandExecutor;
import com.techstar.modules.elfinder.controller.executors.DuplicateLocalCommandExecutor;
import com.techstar.modules.elfinder.controller.executors.ExtractCommandExecutor;
import com.techstar.modules.elfinder.controller.executors.FileLocalCommandExecutor;
import com.techstar.modules.elfinder.controller.executors.GetLocalCommandExecutor;
import com.techstar.modules.elfinder.controller.executors.LsLocalCommandExecutor;
import com.techstar.modules.elfinder.controller.executors.MkdirLocalCommandExecutor;
import com.techstar.modules.elfinder.controller.executors.MkfileLocalCommandExecutor;
import com.techstar.modules.elfinder.controller.executors.OpenLocalCommandExecutor;
import com.techstar.modules.elfinder.controller.executors.ParentsLocalCommandExecutor;
import com.techstar.modules.elfinder.controller.executors.PasteLocalCommandExecutor;
import com.techstar.modules.elfinder.controller.executors.PutLocalCommandExecutor;
import com.techstar.modules.elfinder.controller.executors.RenameLocalCommandExecutor;
import com.techstar.modules.elfinder.controller.executors.ResizeLocalCommandExecutor;
import com.techstar.modules.elfinder.controller.executors.RmLocalCommandExecutor;
import com.techstar.modules.elfinder.controller.executors.SearchLocalCommandExecutor;
import com.techstar.modules.elfinder.controller.executors.SizeLocalCommandExecutor;
import com.techstar.modules.elfinder.controller.executors.TmbLocalCommandExecutor;
import com.techstar.modules.elfinder.controller.executors.TreeLocalCommandExecutor;
import com.techstar.modules.elfinder.controller.executors.UploadLocalCommandExecutor;
import com.techstar.modules.elfinder.impl.DefaultFsService;
import com.techstar.modules.elfinder.impl.DefaultFsServiceConfig;
import com.techstar.modules.elfinder.impl.StaticFsServiceFactory;
import com.techstar.modules.elfinder.localfs.LocalFsVolume;
import com.techstar.modules.elfinder.service.FsService;
import com.techstar.modules.elfinder.service.FsServiceFactory;

@Configuration
@Profile("elfinder-local")
// @ComponentScan(basePackages = "com.techstar.modules.elfinder")
@PropertySource(value = { "classpath:com/techstar/modules/elfinder/config/elfinder.properties",
		"classpath:elfinder.properties", "classpath:application.properties" }, ignoreResourceNotFound = true)
public class ElFinderLocalConfig {

	@Bean(name = "commandExecutorFactory")
	public CommandExecutorFactory commandExecutorFactory() {
		DefaultCommandExecutorFactory commandExecutorFactory = new DefaultCommandExecutorFactory();
		commandExecutorFactory.setClassNamePattern("%sCommandExecutor");
		return commandExecutorFactory;
	}

	@Bean
	public CommandExecutor dimCommandExecutor() {
		return new DimLocalCommandExecutor();
	}

	@Bean
	public CommandExecutor duplicateCommandExecutor() {
		return new DuplicateLocalCommandExecutor();
	}

	@Bean
	public CommandExecutor fileCommandExecutor() {
		return new FileLocalCommandExecutor();
	}

	@Bean
	public CommandExecutor getCommandExecutor() {
		return new GetLocalCommandExecutor();
	}

	@Bean
	public CommandExecutor lsCommandExecutor() {
		return new LsLocalCommandExecutor();
	}

	@Bean
	public CommandExecutor mkdirCommandExecutor() {
		return new MkdirLocalCommandExecutor();
	}

	@Bean
	public CommandExecutor mkfileCommandExecutor() {
		return new MkfileLocalCommandExecutor();
	}

	@Bean
	public CommandExecutor openCommandExecutor() {
		return new OpenLocalCommandExecutor();
	}

	@Bean
	public CommandExecutor parentsCommandExecutor() {
		return new ParentsLocalCommandExecutor();
	}

	@Bean
	public CommandExecutor pasteCommandExecutor() {
		return new PasteLocalCommandExecutor();
	}

	@Bean
	public CommandExecutor putCommandExecutor() {
		return new PutLocalCommandExecutor();
	}

	@Bean
	public CommandExecutor renameCommandExecutor() {
		return new RenameLocalCommandExecutor();
	}

	@Bean
	public CommandExecutor rmCommandExecutor() {
		return new RmLocalCommandExecutor();
	}

	@Bean
	public CommandExecutor tmbCommandExecutor() {
		return new TmbLocalCommandExecutor();
	}

	@Bean
	public CommandExecutor treeCommandExecutor() {
		return new TreeLocalCommandExecutor();
	}

	@Bean
	public CommandExecutor uploadCommandExecutor() {
		return new UploadLocalCommandExecutor();
	}

	@Bean
	public CommandExecutor sizeCommandExecutor() {
		return new SizeLocalCommandExecutor();
	}

	@Bean
	public CommandExecutor resizeCommandExecutor() {
		return new ResizeLocalCommandExecutor();
	}

	@Bean
	public CommandExecutor archiveCommandExecutor() {
		return new ArchiveCommandExecutor();
	}

	@Bean
	public CommandExecutor extractCommandExecutor() {
		return new ExtractCommandExecutor();
	}

	@Bean
	public CommandExecutor searchCommandExecutor() {
		return new SearchLocalCommandExecutor();
	}

	@Bean
	public FsService fsService() {
		DefaultFsService fsService = new DefaultFsService();

		// serviceConfig bein
		DefaultFsServiceConfig serviceConfig = new DefaultFsServiceConfig();

		serviceConfig.setTmbWidth(80);
		fsService.setServiceConfig(serviceConfig);
		// serviceConfig end

		// volumes begin
		LocalFsVolume volumes[] = new LocalFsVolume[2];

		File dir = new File("/tmp/a");
		if (!dir.exists()) {
			dir.mkdirs();
		}

		volumes[0] = new LocalFsVolume("我的文档", dir, "A");

		dir = new File("/tmp/b");
		if (!dir.exists()) {
			dir.mkdirs();
		}

		volumes[1] = new LocalFsVolume("共享文档", dir, "B", false);
		fsService.setVolumes(volumes);
		// volumes end

		return fsService;
	}

	@Bean
	public FsServiceFactory fsServiceFactory() {
		StaticFsServiceFactory fsServiceFactory = new StaticFsServiceFactory();
		fsServiceFactory.setFsService(fsService());
		return fsServiceFactory;
	}

	@Bean
	public CompressCommandFactory compressCommandFactory() {
		return new DefaultCompressCommandFactory();
	}

	@Bean
	public CompressCommand gzipCompressCommand() {
		return new GZipCompressCommand();
	}

	@Bean
	public CompressCommand sevenZCompressCommand() {
		return new SevenZCompressCommand();
	}

	@Bean
	public CompressCommand tarCompressCommand() {
		return new TarCompressCommand();
	}

	@Bean
	public CompressCommand zipCompressCommand() {
		return new ZipCompressCommand();
	}
	
	@Bean
	public CompressCommand rarCompressCommand() {
		return new RarCompressCommand();
	}
}
