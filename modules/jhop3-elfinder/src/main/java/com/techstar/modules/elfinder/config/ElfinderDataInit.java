package com.techstar.modules.elfinder.config;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.techstar.modules.elfinder.domain.Elfinder;
import com.techstar.modules.elfinder.repository.jpa.ElfinderDao;

/**
 * 数据初始化
 * 
 * @author sundoctor
 * 
 */
public class ElfinderDataInit {

	@Autowired
	private ElfinderDao elfinderDao;

	public void init() {

		if (this.elfinderDao.count() <= 0) {

			List<Elfinder> elfs = new ArrayList<Elfinder>();

			Elfinder elf = new Elfinder();
			elf.setName("我的文档");
			elf.setMtime(new Date());
			elf.setMime("directory");
			elf.setRead(true);
			elf.setWrite(true);
			elf.setLocked(false);
			elf.setHidden(false);
			elf.setCategory("1");
			elfs.add(elf);

			elf = new Elfinder();
			elf.setName("共享文档");
			elf.setMtime(new Date());
			elf.setMime("directory");
			elf.setRead(true);
			elf.setWrite(false);
			elf.setLocked(true);
			elf.setHidden(false);
			elf.setCategory("1");
			elfs.add(elf);

			elf = new Elfinder();
			elf.setName("只读文档");
			elf.setMtime(new Date());
			elf.setMime("directory");
			elf.setRead(false);
			elf.setWrite(false);
			elf.setLocked(true);
			elf.setHidden(false);
			elf.setCategory("1");
			elfs.add(elf);

			this.elfinderDao.save(elfs);
		}
	}

}
