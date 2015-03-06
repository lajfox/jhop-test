package com.techstar.modules.activiti.web.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.Model;
import org.activiti.explorer.Messages;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.techstar.modules.activiti.engine.impl.bpmn.diagram.CustomProcessDiagramGenerator;
import com.techstar.modules.activiti.service.ModelService;
import com.techstar.modules.mybatis.domain.MyRowBounds;
import com.techstar.modules.mybatis.domain.Page;
import com.techstar.modules.springframework.data.jpa.domain.PageResponse;
import com.techstar.modules.springframework.data.jpa.domain.Results;
import com.techstar.modules.springframework.web.bind.annotation.IeResponseBody;
import com.techstar.modules.utils.Identities;

/**
 * 流程模型控制器
 * 
 * @author ZengWenfeng
 */
@Controller
@RequestMapping(value = "/workflow/model")
public class ModelController {

	private static final Logger logger = LoggerFactory.getLogger(ModelController.class);

	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private ModelService modelService;

	@Autowired
	private ProcessEngineConfigurationImpl processEngineConfiguration;

	/**
	 * 流程模型列表
	 * 
	 * @return 调转到流程模型列表页面
	 */
	@RequestMapping("")
	public ModelAndView modelList() {
		ModelAndView mav = new ModelAndView("workflow/modelList");
		mav.addObject("uuid", Identities.uuid2());
		return mav;
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
		Page<Model> page = modelService.findPageBy(rowBounds);
		return new PageResponse<Model>(page);
	}

	/**
	 * 创建流程模型
	 * 
	 * @param name
	 *            模型流程名称
	 * @param key
	 *            模型流程关键字
	 * @param description
	 *            模型流程描述
	 * @return
	 */
	@RequestMapping("create")
	public @ResponseBody
	Results create(@RequestParam("name") String name, @RequestParam("key") String key,
			@RequestParam("description") String description) {

		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode editorNode = objectMapper.createObjectNode();
		editorNode.put("id", "canvas");
		editorNode.put("resourceId", "canvas");
		ObjectNode stencilSetNode = objectMapper.createObjectNode();
		stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
		editorNode.put("stencilset", stencilSetNode);

		ObjectNode modelObjectNode = objectMapper.createObjectNode();
		modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, name);
		modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
		description = StringUtils.defaultString(description);
		modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);

		Model modelData = repositoryService.newModel();
		modelData.setMetaInfo(modelObjectNode.toString());
		modelData.setName(name);
		modelData.setKey(StringUtils.defaultString(key));

		modelService.save(modelData, editorNode);

		return new Results(modelData);
	}

	/**
	 * 复制模型
	 * 
	 * @param modelId
	 *            源流程模型标识ID
	 * @param name
	 *            目标流程模型名称
	 * @param key
	 *            目标流程模型关键字
	 * @param description
	 *            目标流程模型描述
	 * @return
	 */
	@RequestMapping("copy")
	public @ResponseBody
	Results copy(@RequestParam("id") String modelId, @RequestParam("name") String name,
			@RequestParam("key") String key, @RequestParam("description") String description) {

		ObjectNode modelObjectNode = new ObjectMapper().createObjectNode();
		modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, name);
		modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
		description = StringUtils.defaultString(description);
		modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);

		Model newModelData = repositoryService.newModel();
		newModelData.setMetaInfo(modelObjectNode.toString());
		newModelData.setName(name);
		newModelData.setKey(StringUtils.defaultString(key));

		Model modelData = repositoryService.getModel(modelId);

		modelService.copy(newModelData, modelData);

		return new Results(newModelData);
	}

	/**
	 * 删除流程模型
	 * 
	 * @param modelId
	 *            流程模型标识ID
	 * @return
	 */
	@RequestMapping("delete")
	public @ResponseBody
	Results delete(@RequestParam("id") String modelId) {
		modelService.delete(modelId);
		return new Results(true);
	}

	@RequestMapping("formkey")
	public ModelAndView formkey() {
		ModelAndView mav = new ModelAndView("workflow/formkey");
		mav.addObject("uuid", Identities.uuid2());
		return mav;
	}

	/**
	 * 部署流程模型
	 * 
	 * @param modelId
	 *            流程模型标识ID
	 * @return
	 */
	@RequestMapping(value = "deploy/{modelId}")
	public @IeResponseBody
	Results deploy(@PathVariable("modelId") String modelId,
			@RequestParam(value = "model_deploy_file", required = false) CommonsMultipartFile uploadfiles[]) {

		/*
		 * ByteArrayOutputStream dest = null; ZipArchiveOutputStream zos = null;
		 * ZipInputStream zip = null;
		 */

		Model modelData = repositoryService.getModel(modelId);
		try {
			ObjectNode modelNode = (ObjectNode) new ObjectMapper().readTree(repositoryService
					.getModelEditorSource(modelData.getId()));

			BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
			// byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(model);

			Deployment deployment = null;
			String processName = StringUtils.isEmpty(modelData.getKey()) ? modelData.getName() : modelData.getKey()
					+ ".bpmn20.xml";
			if (ArrayUtils.isEmpty(uploadfiles)) {
				// deployment =
				// repositoryService.createDeployment().name(modelData.getName())
				// .addString(processName, new String(bpmnBytes)).deploy();
				deployment = repositoryService.createDeployment().addBpmnModel(processName, model).deploy();
			} else {

				/*
				 * // zip压缩 dest = new ByteArrayOutputStream(); zos = new
				 * ZipArchiveOutputStream(dest); zos.setEncoding("UTF-8");
				 * 
				 * // 压缩定义文件 ZipArchiveEntry ze = new
				 * ZipArchiveEntry(processName); zos.putArchiveEntry(ze); byte[]
				 * bpmnBytes = new BpmnXMLConverter().convertToXML(model);
				 * IOUtils.copy(new ByteArrayInputStream(bpmnBytes), zos);
				 * zos.closeArchiveEntry();
				 * 
				 * // 压缩补充资源文件，如外置表单文件等 for (CommonsMultipartFile file :
				 * uploadfiles) {
				 * 
				 * if (file != null && !file.isEmpty()) { ze = new
				 * ZipArchiveEntry(file.getOriginalFilename());
				 * zos.putArchiveEntry(ze); IOUtils.copy(file.getInputStream(),
				 * zos); zos.closeArchiveEntry(); } } // zos.close();
				 * 
				 * // zip部署 zip = new ZipInputStream(new
				 * ByteArrayInputStream(dest.toByteArray())); deployment =
				 * repositoryService
				 * .createDeployment().addZipInputStream(zip).deploy();
				 */

				DeploymentBuilder builder = repositoryService.createDeployment().name(processName + ".bar");
				builder.addBpmnModel(processName, model);
				for (CommonsMultipartFile file : uploadfiles) {
					if (file != null && !file.isEmpty()) {
						builder.addInputStream(file.getOriginalFilename(), file.getInputStream());
					}
				}
				deployment = builder.deploy();
			}

			return new Results(true, "部署成功，部署ID:" + deployment.getId());
		} catch (JsonProcessingException e) {
			logger.error("JsonProcessingException:{}", e.getMessage());
			return new Results(false, "部署失败，模型ID:" + modelData.getId() + "<br>" + e.getMessage());
		} catch (IOException e) {
			logger.error("IOException:{}", e.getMessage());
			return new Results(false, "部署失败，模型ID:" + modelData.getId() + "<br>" + e.getMessage());
		} catch (Exception e) {
			logger.error("Exception:{}", e.getMessage());
			return new Results(false, "部署失败，模型ID:" + modelData.getId() + "<br>" + e.getMessage());
		} /*
		 * finally { IOUtils.closeQuietly(zip); IOUtils.closeQuietly(zos);
		 * IOUtils.closeQuietly(dest); }
		 */
	}

	/**
	 * 导出流程模型文件
	 * 
	 * @param modelId
	 *            流程模型标识ID
	 * @param response
	 *            HttpServletResponse
	 */
	@RequestMapping(value = "export/{modelId}")
	public void export(@PathVariable("modelId") String modelId, HttpServletResponse response) {
		ByteArrayInputStream in = null;
		OutputStream out = null;

		Model modelData = repositoryService.getModel(modelId);
		BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
		try {

			JsonNode editorNode = new ObjectMapper()
					.readTree(repositoryService.getModelEditorSource(modelData.getId()));
			BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);
			BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
			byte[] bpmnBytes = xmlConverter.convertToXML(bpmnModel);

			String filename = bpmnModel.getMainProcess().getId() + ".bpmn20.xml";
			filename = new String(filename.getBytes("GBK"), "ISO8859-1");
			response.setContentType("application/xml;charset=GBK");
			response.setHeader("Content-Disposition", "attachment; filename=" + filename);

			out = response.getOutputStream();
			in = new ByteArrayInputStream(bpmnBytes);
			IOUtils.copy(in, out);

			response.flushBuffer();
		} catch (Exception e) {
			logger.error("导出model的xml文件失败：modelId={}", modelId, e);
		} finally {
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(out);
		}
	}

	/**
	 * 导入流程模型表单
	 * 
	 * @return　调转到导入流程模型表单页面
	 */
	@RequestMapping("importform")
	public String importform() {
		return "workflow/importform";
	}

	/**
	 * 导入流程模型文件
	 * 
	 * @param uploadfile
	 *            　流程模型文件
	 * @param locale
	 *            语言和区域
	 */
	@RequestMapping("import")
	public @IeResponseBody
	Results importbpmn(@RequestParam("activiti_model_file") CommonsMultipartFile uploadfile, Locale locale) {

		InputStreamReader in = null;
		InputStream imageStream = null;
		ByteArrayOutputStream output = null;
		ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);
		BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
		XMLInputFactory xif = XMLInputFactory.newInstance();

		Context.setProcessEngineConfiguration(processEngineConfiguration);
		try {
			in = new InputStreamReader(new ByteArrayInputStream(uploadfile.getBytes()), "UTF-8");
			XMLStreamReader xtr = xif.createXMLStreamReader(in);
			// BpmnModel bpmnModel = new
			// BpmnXMLConverter().convertToBpmnModel(xtr);
			BpmnModel bpmnModel = xmlConverter.convertToBpmnModel(xtr);

			if (bpmnModel.getMainProcess() == null || bpmnModel.getMainProcess().getId() == null) {
				return new Results(false, bundle.getString(Messages.MODEL_IMPORT_FAILED) + "<br>"
						+ bundle.getString(Messages.MODEL_IMPORT_INVALID_BPMN_EXPLANATION));
			} else {

				if (bpmnModel.getLocationMap().size() == 0) {
					return new Results(false, bundle.getString(Messages.MODEL_IMPORT_INVALID_BPMNDI) + "<br>"
							+ bundle.getString(Messages.MODEL_IMPORT_INVALID_BPMNDI_EXPLANATION));
				} else {

					String processName = null;
					if (StringUtils.isNotEmpty(bpmnModel.getMainProcess().getName())) {
						processName = bpmnModel.getMainProcess().getName();
					} else {
						processName = bpmnModel.getMainProcess().getId();
					}

					Model modelData = repositoryService.newModel();
					ObjectNode modelObjectNode = new ObjectMapper().createObjectNode();
					modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, processName);
					modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
					modelData.setMetaInfo(modelObjectNode.toString());
					modelData.setName(processName);
					modelData.setKey(bpmnModel.getMainProcess().getId());

					BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
					ObjectNode editorNode = jsonConverter.convertToJson(bpmnModel);

					imageStream = CustomProcessDiagramGenerator.generatePngDiagram(bpmnModel);
					byte[] image = null;
					if (imageStream != null) {
						output = new ByteArrayOutputStream();
						IOUtils.copy(imageStream, output);
						image = output.toByteArray();
					}

					modelService.save(modelData, editorNode, image);

					return new Results(true, "导入成功,模型ID:" + modelData.getId(), modelData);
				}
			}

		} catch (Exception e) {
			String errorMsg = e.getMessage().replace(System.getProperty("line.separator"), "<br/>");
			return new Results(false, bundle.getString(Messages.MODEL_IMPORT_FAILED) + "<br>" + errorMsg);
		} finally {
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(output);
			IOUtils.closeQuietly(imageStream);
			Context.removeProcessEngineConfiguration();
		}
	}
}
