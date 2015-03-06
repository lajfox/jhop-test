package com.techstar.modules.elfinder.compress;

import java.io.IOException;
import java.util.List;

import com.techstar.modules.elfinder.controller.executor.FsItemEx;
import com.techstar.modules.elfinder.domain.Param;
import com.techstar.modules.elfinder.service.FsService;

public interface CompressCommand {

	/**
	 * 压缩文件
	 * 
	 * @param fsService
	 * @param param
	 * @return
	 * @throws IOException
	 */
	FsItemEx compress(final FsService fsService, final Param param) throws IOException;

	/**
	 * 解压文件
	 * 
	 * @param fsService
	 * @param param
	 * @return
	 * @throws IOException
	 */
	FsItemEx extract(final FsService fsService, final Param param) throws IOException;
	
	/**
	 * 解压文件,解压到当前文件夹,返回在当前文件夹生成的文件
	 * 
	 * @param fsService
	 * @param param
	 * @return
	 * @throws IOException
	 */
	List<FsItemEx> extractCwd(final FsService fsService, final Param param) throws IOException;
}
