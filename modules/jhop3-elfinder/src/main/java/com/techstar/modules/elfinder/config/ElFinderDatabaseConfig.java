package com.techstar.modules.elfinder.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

import com.techstar.modules.elfinder.controller.executor.CommandExecutor;
import com.techstar.modules.elfinder.controller.executor.CommandExecutorFactory;
import com.techstar.modules.elfinder.controller.executor.DefaultCommandExecutorFactory;
import com.techstar.modules.elfinder.controller.executors.DimDatabaseCommandExecutor;
import com.techstar.modules.elfinder.controller.executors.DuplicateDatabaseCommandExecutor;
import com.techstar.modules.elfinder.controller.executors.FileDatabaseCommandExecutor;
import com.techstar.modules.elfinder.controller.executors.GetDatabaseCommandExecutor;
import com.techstar.modules.elfinder.controller.executors.LsDatabaseCommandExecutor;
import com.techstar.modules.elfinder.controller.executors.MkdirDatabaseCommandExecutor;
import com.techstar.modules.elfinder.controller.executors.MkfileDatabaseCommandExecutor;
import com.techstar.modules.elfinder.controller.executors.OpenDatabaseCommandExecutor;
import com.techstar.modules.elfinder.controller.executors.ParentsDatabaseCommandExecutor;
import com.techstar.modules.elfinder.controller.executors.PasteDatabaseCommandExecutor;
import com.techstar.modules.elfinder.controller.executors.PutDatabaseCommandExecutor;
import com.techstar.modules.elfinder.controller.executors.RenameDatabaseCommandExecutor;
import com.techstar.modules.elfinder.controller.executors.ResizeDatabaseCommandExecutor;
import com.techstar.modules.elfinder.controller.executors.RmDatabaseCommandExecutor;
import com.techstar.modules.elfinder.controller.executors.SearchDatabaseCommandExecutor;
import com.techstar.modules.elfinder.controller.executors.SizeDatabaseCommandExecutor;
import com.techstar.modules.elfinder.controller.executors.TmbDatabaseCommandExecutor;
import com.techstar.modules.elfinder.controller.executors.TreeDatabaseCommandExecutor;
import com.techstar.modules.elfinder.controller.executors.UploadDatabaseCommandExecutor;
import com.techstar.modules.elfinder.impl.DefaultDbSecurityChecker;
import com.techstar.modules.elfinder.impl.DefaultFsService;
import com.techstar.modules.elfinder.impl.StaticFsServiceFactory;
import com.techstar.modules.elfinder.service.DbSecurityChecker;
import com.techstar.modules.elfinder.service.FsService;
import com.techstar.modules.elfinder.service.FsServiceFactory;

@Configuration
@Profile("elfinder-database")
// @ComponentScan(basePackages = "com.techstar.modules.elfinder")
@PropertySource(value = { "classpath:com/techstar/modules/elfinder/config/elfinder.properties",
		"classpath:elfinder.properties", "classpath:application.properties" }, ignoreResourceNotFound = true)
public class ElFinderDatabaseConfig {

	@Bean(name = "commandExecutorFactory")
	public CommandExecutorFactory commandExecutorFactory() {
		DefaultCommandExecutorFactory commandExecutorFactory = new DefaultCommandExecutorFactory();
		commandExecutorFactory.setClassNamePattern("%sCommandExecutor");
		return commandExecutorFactory;
	}

	@Bean
	public CommandExecutor dimCommandExecutor() {
		return new DimDatabaseCommandExecutor();
	}

	@Bean
	public CommandExecutor duplicateCommandExecutor() {
		return new DuplicateDatabaseCommandExecutor();
	}

	@Bean
	public CommandExecutor fileCommandExecutor() {
		return new FileDatabaseCommandExecutor();
	}

	@Bean
	public CommandExecutor getCommandExecutor() {
		return new GetDatabaseCommandExecutor();
	}

	@Bean
	public CommandExecutor lsCommandExecutor() {
		return new LsDatabaseCommandExecutor();
	}

	@Bean
	public CommandExecutor mkdirCommandExecutor() {
		return new MkdirDatabaseCommandExecutor();
	}

	@Bean
	public CommandExecutor mkfileCommandExecutor() {
		return new MkfileDatabaseCommandExecutor();
	}

	@Bean
	public CommandExecutor openCommandExecutor() {
		return new OpenDatabaseCommandExecutor();
	}

	@Bean
	public CommandExecutor parentsCommandExecutor() {
		return new ParentsDatabaseCommandExecutor();
	}

	@Bean
	public CommandExecutor pasteCommandExecutor() {
		return new PasteDatabaseCommandExecutor();
	}

	@Bean
	public CommandExecutor putCommandExecutor() {
		return new PutDatabaseCommandExecutor();
	}

	@Bean
	public CommandExecutor renameCommandExecutor() {
		return new RenameDatabaseCommandExecutor();
	}

	@Bean
	public CommandExecutor rmCommandExecutor() {
		return new RmDatabaseCommandExecutor();
	}

	@Bean
	public CommandExecutor tmbCommandExecutor() {
		return new TmbDatabaseCommandExecutor();
	}

	@Bean
	public CommandExecutor treeCommandExecutor() {
		return new TreeDatabaseCommandExecutor();
	}

	@Bean
	public CommandExecutor uploadCommandExecutor() {
		return new UploadDatabaseCommandExecutor();
	}

	@Bean
	public CommandExecutor sizeCommandExecutor() {
		return new SizeDatabaseCommandExecutor();
	}

	@Bean
	public CommandExecutor resizeCommandExecutor() {
		return new ResizeDatabaseCommandExecutor();
	}

	@Bean
	public CommandExecutor searchCommandExecutor() {
		return new SearchDatabaseCommandExecutor();
	}

	@Bean
	public FsService fsService() {
		DefaultFsService fsService = new DefaultFsService();
		return fsService;
	}

	@Bean
	public FsServiceFactory fsServiceFactory() {
		StaticFsServiceFactory fsServiceFactory = new StaticFsServiceFactory();
		fsServiceFactory.setFsService(fsService());
		return fsServiceFactory;
	}

	@Bean
	public DbSecurityChecker dbSecurityChecker() {
		DbSecurityChecker securityChecker = new DefaultDbSecurityChecker();
		return securityChecker;
	}

	@Bean(initMethod = "init")
	public ElfinderDataInit elfinderDataInit() {
		return new ElfinderDataInit();
	}

}
