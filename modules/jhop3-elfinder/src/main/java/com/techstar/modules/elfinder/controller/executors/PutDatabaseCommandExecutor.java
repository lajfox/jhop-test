package com.techstar.modules.elfinder.controller.executors;

import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.transaction.annotation.Transactional;

import com.techstar.modules.elfinder.controller.executor.AbstractDatabaseJsonCommandExecutor;
import com.techstar.modules.elfinder.controller.executor.CommandExecutor;
import com.techstar.modules.elfinder.domain.Content;
import com.techstar.modules.elfinder.domain.Elfinder;
import com.techstar.modules.elfinder.domain.Param;

@Transactional(readOnly = true)
public class PutDatabaseCommandExecutor extends AbstractDatabaseJsonCommandExecutor implements CommandExecutor {

	@Transactional(readOnly = false)
	@Override
	public void execute(Param param, HttpServletRequest request, ServletContext servletContext, JSONObject json)
			throws Exception {
		String target = param.getTarget();
		Elfinder elfinder = super.findItem(target);

		String data = request.getParameter("content");
		if (StringUtils.isNotEmpty(data)) {
			byte[] bytes = data.getBytes("UTF-8");
			
			Content content = elfinder.getContent();
			if(content == null){
				content = new Content();
			}
			content.setContent(bytes);
			content.setElfinder(elfinder);
			
			elfinder.setContent(content);
			elfinder.setMtime(new Date());
			elfinder.setSize((long)bytes.length);

			this.elfinderDao.save(elfinder);

		}


		json.put("changed", new Object[] { super.getElfinderfo(request, elfinder,getCheckers()) });
	}
}
