package com.techstar.modules.activiti.service;

import java.io.UnsupportedEncodingException;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.techstar.modules.activiti.repository.mybatis.ActivitiModelMybatisDao;
import com.techstar.modules.activiti.repository.mybatis.ActivitiMybatisDao;

/**
 * 流程模型扩展业务类
 * 
 * @author sundoctor
 * 
 */
@Service
@Transactional(readOnly = true)
public class ModelService extends ActivitiService<Model> {

	@Autowired
	private ActivitiModelMybatisDao activitiModelMybatisDao;

	@Autowired
	RepositoryService repositoryService;

	@Override
	protected ActivitiMybatisDao<Model> getActivitiMybatisDao() {
		return activitiModelMybatisDao;
	}

	/**
	 * 保存流程模型
	 * 
	 * @param modelData
	 *            流程模型
	 * @param editorNode
	 *            流程模型JSON数据
	 * @see RepositoryService#saveModel(Model)
	 * @see RepositoryService#addModelEditorSource(String, byte[])
	 */
	@Transactional(readOnly = false)
	public void save(final Model modelData, final ObjectNode editorNode) {
		save(modelData, editorNode, null);
	}

	@Transactional(readOnly = false)
	public void save(final Model modelData, final ObjectNode editorNode, final byte image[]) {
		repositoryService.saveModel(modelData);
		try {
			repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));

			if (image != null) {
				repositoryService.addModelEditorSourceExtra(modelData.getId(), image);
			}

		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 复制模型
	 * 
	 * @param newModelData
	 *            目标流程模型
	 * @param modelData
	 *            　源流程模型
	 * @see RepositoryService#saveModel(Model)
	 * @see RepositoryService#addModelEditorSource(String, byte[])
	 * @see RepositoryService#addModelEditorSourceExtra(String, byte[])
	 */
	@Transactional(readOnly = false)
	public void copy(final Model newModelData, final Model modelData) {
		repositoryService.saveModel(newModelData);
		repositoryService.addModelEditorSource(newModelData.getId(),
				repositoryService.getModelEditorSource(modelData.getId()));
		repositoryService.addModelEditorSourceExtra(newModelData.getId(),
				repositoryService.getModelEditorSourceExtra(modelData.getId()));
	}

	/**
	 * 删除模型
	 * 
	 * @param modelId
	 *            流程模型标识
	 */
	public void delete(String modelId) {
		repositoryService.deleteModel(modelId);
	}

}
