package com.techstar.modules.elfinder.controller.executors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.json.JSONObject;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.techstar.modules.elfinder.controller.executor.AbstractDatabaseJsonCommandExecutor;
import com.techstar.modules.elfinder.controller.executor.CommandExecutor;
import com.techstar.modules.elfinder.domain.Content;
import com.techstar.modules.elfinder.domain.Elfinder;
import com.techstar.modules.elfinder.domain.Param;

import eu.medsea.mimeutil.MimeException;
import eu.medsea.mimeutil.MimeType;
import eu.medsea.mimeutil.MimeUtil;

@Transactional(readOnly = true)
public class UploadDatabaseCommandExecutor extends AbstractDatabaseJsonCommandExecutor implements CommandExecutor {

	static {
		// MIME types initialization
		MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
		MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.ExtensionMimeDetector");
	}

	@Transactional(readOnly = false)
	@Override
	public void execute(Param param, HttpServletRequest request, ServletContext servletContext, JSONObject json)
			throws Exception {

		List<Elfinder> added = new ArrayList<Elfinder>();

		String target = param.getTarget();
		Elfinder parent = super.findItem(target);

		Elfinder newFile = null;
		Content content = null;
		for (CommonsMultipartFile fis : param.getUploadFiles()) {

			newFile = new Elfinder();
			newFile.setName(fis.getOriginalFilename());
			newFile.setSize(fis.getSize());
			newFile.setMtime(new Date());
			newFile.setMime(getMimeType(fis));
			newFile.setRead(parent.getRead());
			newFile.setWrite(parent.getWrite());
			newFile.setLocked(parent.getLocked());
			newFile.setParent(parent);
			newFile.setCategory(parent.getCategory());

			// 设置根目录
			newFile.setRootDir( parent.getRootDir());

			content = new Content();
			content.setContent(fis.getBytes());
			content.setElfinder(newFile);
			newFile.setContent(content);

			added.add(newFile);
		}
		if (CollectionUtils.isNotEmpty(added)) {
			this.elfinderDao.save(added);
		}

		
		json.put("added", elfinder2JsonArray(request, added,getCheckers()));
	}

	@SuppressWarnings("unchecked")
	private String getMimeType(final CommonsMultipartFile fis) throws MimeException, IOException {

		Collection<MimeType> mimes = MimeUtil.getMimeTypes(fis.getOriginalFilename());
		if (CollectionUtils.isEmpty(mimes)) {
			mimes = MimeUtil.getMimeTypes(fis.getInputStream());
		}
		if (CollectionUtils.isNotEmpty(mimes)) {
			return mimes.iterator().next().toString();
		}

		return new MimetypesFileTypeMap().getContentType(fis.getOriginalFilename());

	}
}
