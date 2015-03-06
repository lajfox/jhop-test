package com.techstar.modules.activiti.web.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableDefaults;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.techstar.modules.activiti.service.ProcdefService;
import com.techstar.modules.mybatis.domain.MyRowBounds;
import com.techstar.modules.mybatis.domain.Page;
import com.techstar.modules.shiro.domain.ShiroUser;
import com.techstar.modules.shiro.web.util.SubjectUtils;
import com.techstar.modules.springframework.data.jpa.domain.PageResponse;
import com.techstar.modules.springframework.data.jpa.domain.Results;
import com.techstar.modules.springframework.web.bind.annotation.IeResponseBody;
import com.techstar.modules.utils.Identities;
import com.techstar.modules.web.Servlets;

/**
 * 己部署流程定义Controller类
 * 
 * @author sundoctor
 * 
 */
@Controller
@RequestMapping(value = "/workflow/procdef")
public class ProcessDefinitionController {

	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private ProcdefService procdefService;

	/**
	 * 流程定义列表
	 * 
	 * @return 调转到流程定义列表页面
	 */
	@RequestMapping("")
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView("workflow/procdefList");
		mav.addObject("uuid", Identities.uuid2());
		return mav;
	}

	/**
	 * 部署流程表单
	 * 
	 * @return　
	 */
	@RequestMapping("deployform")
	public String deployform() {
		return "workflow/deployform";
	}

	/**
	 * 查询流程模型
	 * 
	 * @param spec
	 * @return
	 */
	@RequestMapping("search")
	public @ResponseBody
	Results search(@PageableDefaults(pageNumber = 0, value = 10) MyRowBounds rowBounds) {
		Page<ProcessDefinition> page = procdefService.findPageBy(rowBounds);
		return new PageResponse<ProcessDefinition>(page);
	}

	/**
	 * 删除部署的流程，级联删除流程实例
	 * 
	 * @param deploymentId
	 *            流程部署ID
	 */
	@RequestMapping(value = "/delete/{deploymentId}")
	public @ResponseBody
	Results delete(@PathVariable("deploymentId") String deploymentId) {
		repositoryService.deleteDeployment(deploymentId, true);
		return new Results(true);
	}

	/**
	 * 挂起、激活流程定义
	 * 
	 * @param state
	 *            流程定义状态
	 * @param processDefinitionId
	 *            流程定义ID
	 */
	@RequestMapping(value = "/update/{state}")
	public @ResponseBody
	Results updateState(@PathVariable("state") int state,
			@RequestParam("processDefinitionId") String processDefinitionId) {
		if (state == 2) {
			repositoryService.activateProcessDefinitionById(processDefinitionId, true, null);
		} else {
			repositoryService.suspendProcessDefinitionById(processDefinitionId, true, null);
		}
		return new Results(true, state == 2 ? 1 : 2);
	}

	/**
	 * 读取资源，通过部署ID
	 * 
	 * @param deploymentId
	 *            流程部署的ID
	 * @param resourceName
	 *            资源名称(foo.xml|foo.png)
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/resource/deployment/{deploymentId}")
	public void loadByDeployment(@PathVariable("deploymentId") String deploymentId,
			@RequestParam("resourceName") String resourceName, HttpServletResponse response) throws Exception {

		OutputStream out = null;
		InputStream resourceAsStream = repositoryService.getResourceAsStream(deploymentId, resourceName);
		try {

			String filename = new String(resourceName.getBytes("GBK"), "ISO8859-1");
			response.setContentType(Servlets.getContentType2(resourceName, "GBk", "application/octet-stream"));
			response.setHeader("Content-Disposition", "attachment; filename=" + filename);

			out = response.getOutputStream();
			IOUtils.copy(resourceAsStream, out);

			response.flushBuffer();
		} finally {
			IOUtils.closeQuietly(out);
			IOUtils.closeQuietly(resourceAsStream);
		}
	}

	/**
	 * 部署流程
	 * 
	 * @param file
	 *            流程文件
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/deploy")
	public @IeResponseBody
	Results deploy(@RequestParam("procdef_deploy_file") MultipartFile file) throws IOException {

		String fileName = file.getOriginalFilename();
		InputStream fileInputStream = file.getInputStream();
		Deployment deployment = null;

		String extension = FilenameUtils.getExtension(fileName);
		if (extension.equals("zip") || extension.equals("bar")) {
			ZipInputStream zip = new ZipInputStream(fileInputStream);
			deployment = repositoryService.createDeployment().addZipInputStream(zip).deploy();
			IOUtils.closeQuietly(zip);
		} else {
			deployment = repositoryService.createDeployment().addInputStream(fileName, fileInputStream).deploy();
		}

		return new Results(deployment.getId());

	}

	/**
	 * <p>
	 * 确定用户是否有权启动流程
	 * </p>
	 * <p>
	 * success=false无权启动流程
	 * </p>
	 * <p>
	 * success=true有权限启动流程
	 * </p>
	 * 
	 * @param processDefinitionKey
	 *            　流程定义KEY,不能为空
	 * @return Results
	 */
	@RequestMapping(value = "decision/{processDefinitionKey}", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	Results realtimeDecision(@PathVariable("processDefinitionKey") String processDefinitionKey) {

		String userId = SubjectUtils.getPrincipal(ShiroUser.class).getLoginName();
		long count = repositoryService.createProcessDefinitionQuery().startableByUser(userId)
				.processDefinitionKey(processDefinitionKey).latestVersion().count();
		return new Results(count > 0);

		/*
		 * List<IdentityLink> links =
		 * repositoryService.getIdentityLinksForProcessDefinition
		 * (processDefinitionId); if (CollectionUtils.isNotEmpty(links)) {
		 * return new Results(true, "流程启动没有设置权限，任何人均可启动流程"); } else {
		 * 
		 * for (IdentityLink link : links) { if
		 * (StringUtils.equals(link.getUserId(), userId) ||
		 * SubjectUtils.hasRole(link.getGroupId())) { return new Results(true,
		 * "有权限启动流程"); } } }
		 * 
		 * return new Results(false, "没有权限启动流程");
		 */
	}
}
