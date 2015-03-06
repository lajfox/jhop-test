package com.techstar.modules.elfinder.controller.executor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.util.Assert;

import com.techstar.modules.elfinder.domain.Checker;
import com.techstar.modules.elfinder.domain.CheckerType;
import com.techstar.modules.elfinder.domain.Content;
import com.techstar.modules.elfinder.domain.Elfinder;
import com.techstar.modules.elfinder.domain.ElfinderChecker;
import com.techstar.modules.elfinder.domain.Param;
import com.techstar.modules.elfinder.repository.jpa.ElfinderDao;
import com.techstar.modules.elfinder.service.DbSecurityChecker;

public abstract class AbstractDatabaseCommandExecutor implements CommandExecutor {

	protected static final String DIRECTORY = "directory";
	protected static final Logger logger = LoggerFactory.getLogger(AbstractDatabaseCommandExecutor.class);

	@Autowired
	protected ElfinderDao elfinderDao;

	@Autowired
	private DbSecurityChecker dbSecurityChecker;

	private static final Map<Integer, Checker> CHECKERMAP = new HashMap<Integer, Checker>();
	static {
		CHECKERMAP.put(CheckerType.LOCKED.getValue(), new Checker(CheckerType.LOCKED));
		CHECKERMAP.put(CheckerType.READONLY.getValue(), new Checker(CheckerType.READONLY));
		CHECKERMAP.put(CheckerType.READANDWIRTE.getValue(), new Checker(CheckerType.READANDWIRTE));
	}

	protected void addChildren(final List<Elfinder> list, final Elfinder parent) {

		if (parent != null) {
			List<Elfinder> childrens = findChildrens(parent);
			if (CollectionUtils.isNotEmpty(childrens)) {
				for (Elfinder elf : childrens) {
					if (!list.contains(elf)) {
						list.add(elf);
					}
				}
			}
		}

	}

	protected void addSubfolders(final List<Elfinder> list, final List<Elfinder> roots) {
		if (CollectionUtils.isNotEmpty(roots)) {
			List<Elfinder> childrens = elfinderDao.findBy("parentInAndMime", roots, DIRECTORY);
			if (CollectionUtils.isNotEmpty(childrens)) {
				list.addAll(childrens);
			}
		}
	}

	protected void addSubfolders(final List<Elfinder> list, final Elfinder root) {

		List<Elfinder> childrens = elfinderDao.findBy("parentAndMime", root, DIRECTORY);
		if (CollectionUtils.isNotEmpty(childrens)) {
			list.addAll(childrens);
		}

	}

	/**
	 * 拷贝
	 * 
	 * @param src
	 * @return
	 */
	protected Elfinder copy(final Elfinder src, final Elfinder parent) {
		Date mtime = new Date();
		Elfinder dst = copy(src, parent, mtime);
		copy(src, dst, parent, mtime);
		return dst;
	}

	@Override
	public void execute(CommandExecutionContext ctx) throws Exception {
		execute(ctx.getParam(), ctx.getRequest(), ctx.getResponse(), ctx.getServletContext());

	}

	public abstract void execute(Param param, HttpServletRequest request, HttpServletResponse response,
			ServletContext servletContext) throws Exception;

	protected Object[] elfinder2JsonArray(final HttpServletRequest request, final Collection<Elfinder> list,
			final List<ElfinderChecker> checkers) {
		List<Map<String, Object>> los = new ArrayList<Map<String, Object>>();
		for (Elfinder fi : list) {
			los.add(getElfinderfo(request, fi, checkers));
		}

		return los.toArray();
	}

	protected Elfinder findCwd(String target, List<ElfinderChecker> checkers, String[] categories) {
		// current selected directory

		Elfinder elfinder = null;
		if (StringUtils.isNotEmpty(target)) {
			elfinder = findItem(target);
		}

		if (elfinder == null) {
			List<Elfinder> roots = findRoots(checkers, categories);
			elfinder = CollectionUtils.isEmpty(roots) ? null : roots.get(0);
		}

		return elfinder;
	}

	protected Map<String, Object> getElfinderfo(final HttpServletRequest request, final Elfinder elf,
			final List<ElfinderChecker> checkers) {
		Map<String, Object> info = new HashMap<String, Object>();

		if (elf != null) {
			info.put("hash", elf.getId());
			info.put("mime", elf.getMime());
			info.put("ts", elf.getMtime() == null ? null : elf.getMtime().getTime() / 1000);
			info.put("size", elf.getSize());

			if (this.dbSecurityChecker.isChecker()) {
				setChecker(info, checkers, elf);
			} else {
				info.put("read", elf.getRead() ? 1 : 0);
				info.put("write", elf.getWrite() ? 1 : 0);
				info.put("locked", elf.getLocked() ? 1 : 0);
			}

			if (elf.getMime().startsWith("image")) {
				StringBuffer qs = request.getRequestURL();
				info.put("tmb", qs.append(String.format("?cmd=tmb&target=%s", elf.getId())));
			}

			if (elf.isRoot()) {
				info.put("name", elf.getName());
				info.put("volumeid", elf.getId());
			} else {
				info.put("name", elf.getName());
				info.put("phash", elf.getParent().getId());
			}

			if (StringUtils.equals(DIRECTORY, elf.getMime())) {
				info.put("dirs", 1);
			}
		}

		return info;
	}

	protected void setChecker(final Map<String, Object> info, final List<ElfinderChecker> checkers, final Elfinder elf) {

		// 无权限，锁定
		info.put("read", 0);
		info.put("write", 0);
		info.put("locked", 1);

		// 查找根目录权限
		Checker checker = getChecker(checkers, elf);
		if (checker != null) {
			info.put("read", checker.isRead() ? 1 : 0);
			info.put("write", checker.isWrite() ? 1 : 0);
			info.put("locked", checker.isLocked() ? 1 : 0);
		}

	}

	protected Checker getChecker(final List<ElfinderChecker> checkers, final Elfinder elf) {

		int i = 100;
		Checker checker = null;
		if (CollectionUtils.isNotEmpty(checkers)) {
			// 根目录
			String root = elf.isRoot() ? elf.getId() : elf.getRootDir();
			Assert.hasText(root, "要目录不能为空");
			// 查找根目录权限
			for (ElfinderChecker elfChecker : checkers) {
				if (StringUtils.equals(root, elfChecker.getElfinder())) {
					if (elfChecker.getChecker() != null && elfChecker.getChecker() < i) {
						i = elfChecker.getChecker();
					}
				}
			}

			if (i != 100) {
				checker = CHECKERMAP.get(i);
			}

		}

		return checker == null ? CHECKERMAP.get(CheckerType.LOCKED.getValue()) : checker;
	}

	protected String getMimeDisposition(String mime) {
		String[] parts = mime.split("/");
		String disp = ("image".equals(parts[0]) || "text".equals(parts[0]) ? "inline" : "attachments");
		return disp;
	}

	protected Map<String, Object> getOptions(HttpServletRequest request, Elfinder cwd) {
		Map<String, Object> map = new HashMap<String, Object>();

		if (cwd != null) {
			map.put("path", cwd.getName());
			// map.put("disabled", ArrayUtils.isEmpty(cwd.getDisabled()) ? new
			// String[0] : cwd.getDisabled());
			map.put("separator", "/");
			map.put("copyOverwrite", 1);

			map.put("archivers", new Object());
		}

		return map;
	}

	/**
	 * 没有写权限
	 * 
	 * @param checkers
	 * @param parent
	 * @return
	 */
	protected boolean notPermisson(final List<ElfinderChecker> checkers, Elfinder parent) {
		if (this.dbSecurityChecker.isChecker()) {
			Checker checker = getChecker(checkers, parent);
			return (!checker.isWrite() || checker.isLocked());
		} else {
			return (!parent.getWrite() || parent.getLocked());
		}
	}

	protected Elfinder findItem(String id) {
		return elfinderDao.findOne(id);
	}

	protected List<Elfinder> findRoots(List<ElfinderChecker> checkers, String[] categories) {

		List<Elfinder> list = null;
		if (ArrayUtils.isEmpty(categories)) {
			list = elfinderDao.findBy("mimeAndParentNull", new Sort("name"), DIRECTORY);
		} else if (categories.length == 1 && StringUtils.isNotEmpty(categories[0])) {
			list = elfinderDao.findBy("mimeAndCategoryAndParentNull", new Sort("name"), DIRECTORY, categories[0]);
		} else {
			list = elfinderDao.findBy("mimeAndCategoryInAndParentNull", new Sort("name"), DIRECTORY,
					Arrays.asList(categories));
		}

		/*
		 * if (CollectionUtils.isEmpty(list)) { return list; } else {
		 * List<Elfinder> results = new ArrayList<Elfinder>(); for (Elfinder elf
		 * : list) { for (ElfinderChecker checker : checkers) { if
		 * (StringUtils.equals(elf.getId(), checker.getElfinder()) &&
		 * CheckerType.valueOf(checker.getChecker()) != CheckerType.LOCKED) {
		 * results.add(elf); break; } } }
		 * 
		 * return results; }
		 */
		return list;
	}

	protected List<Elfinder> findChildrens(final String parent) {
		return elfinderDao.findBy("parent.id", parent);
	}

	protected List<Elfinder> findChildrens(final Elfinder parent) {
		return elfinderDao.findBy("parent", parent);
	}

	protected List<Elfinder> findChildrens(final Collection<Elfinder> parents) {
		return elfinderDao.findBy("parentIn", parents);
	}

	protected List<ElfinderChecker> getCheckers() {
		return this.dbSecurityChecker.isChecker() ? this.dbSecurityChecker.getCheckers() : null;
	}

	/**
	 * 递归拷贝
	 * 
	 * @param src
	 * @param dst
	 * @return
	 */
	private void copy(final Elfinder src, final Elfinder dst, final Elfinder parent, final Date mtime) {

		if (src.isFolder()) {
			Set<Elfinder> childrens = src.getChildrens();
			if (CollectionUtils.isNotEmpty(childrens)) {

				Elfinder dstChildren = null;
				for (Elfinder children : childrens) {
					dstChildren = copy(children, dst, mtime);
					dst.getChildrens().add(dstChildren);
					// 递归拷贝
					if (children.isFolder()) {
						copy(children, dstChildren, dst, mtime);
					}
				}
			}
		}

	}

	/**
	 * 拷贝
	 * 
	 * @param src
	 * @param parent
	 * @param mtime
	 * @return
	 */
	private Elfinder copy(final Elfinder src, final Elfinder parent, Date mtime) {

		Elfinder dst = new Elfinder();
		dst.setName(src.getName());
		dst.setSize(src.getSize());
		dst.setMtime(mtime);
		dst.setMime(src.getMime());
		dst.setRead(src.getRead());
		dst.setWrite(src.getWrite());
		dst.setLocked(src.getLocked());
		dst.setHidden(src.getHidden());
		dst.setWidth(src.getWidth());
		dst.setRootDir(parent.getRootDir());
		dst.setParent(parent);
		dst.setCategory(parent.getCategory());

		if (src.isFile()) {
			Content srcContent = src.getContent();
			if (srcContent != null) {
				Content dstContent = new Content();
				dstContent.setContent(srcContent.getContent());
				dstContent.setElfinder(dst);
				dst.setContent(dstContent);
			}
		}

		return dst;
	}

}