package com.techstar.modules.elfinder.compress;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import com.techstar.modules.elfinder.controller.executor.FsItemEx;
import com.techstar.modules.elfinder.domain.Param;
import com.techstar.modules.elfinder.service.FsService;

/***
 *rar解压
 * @author lrm
 */
public class RarCompressCommand extends AbstractCompressCommand {
	
	@Autowired
	private Environment env;
	
	/***
	 * Description: 解压到当前文件夹，返回解压列表
	 * Create by: lrm 2014-3-5 上午10:32:55
	 */
	public List<FsItemEx> extractCwd(final FsService fsService, final Param param) throws IOException {
		String target = param.getTarget();
		FsItemEx fsi = super.findItem(fsService, target);
		
		String unRarCmd = env.getProperty("cmd.WinRAR.unrar");
		unRarCmd = unRarCmd.replace("{rarFile}", fsi.getFile().getAbsolutePath().replace('\\', '/'));
		String fname = fsi.getFile().getName();
		if(fname.indexOf(".")!=-1){
			fname = fname.substring(0,fname.lastIndexOf("."));
		}
		FsItemEx targetFsi = new FsItemEx(fsi.getParent(),fname);
		if(!targetFsi.getFile().exists()){
			targetFsi.getFile().mkdirs();
		}
		unRarCmd = unRarCmd.replace("{targetPath}", targetFsi.getFile().getAbsolutePath().replace('\\', '/'));
		Runtime rt = Runtime.getRuntime();
		Process pre = rt.exec(unRarCmd);
		InputStreamReader isr = new InputStreamReader(pre.getInputStream());
		BufferedReader bf = new BufferedReader(isr);
		String line = null;
		while ((line = bf.readLine()) != null) {
			line = line.trim();
			if ("".equals(line)) {
				continue;
			}
		}
		bf.close();
		isr.close();
		List<FsItemEx> added = new ArrayList<FsItemEx>();
		added.add(targetFsi);
		return added;
	}


	@Override
	public FsItemEx compress(FsService fsService, Param param) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FsItemEx extract(FsService fsService, Param param) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
