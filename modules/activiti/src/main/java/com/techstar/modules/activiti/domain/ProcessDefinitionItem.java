package com.techstar.modules.activiti.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.impl.db.HasRevision;
import org.activiti.engine.impl.db.PersistentObject;
import org.activiti.engine.impl.persistence.entity.SuspensionState;
import org.activiti.engine.repository.ProcessDefinition;

public class ProcessDefinitionItem implements ProcessDefinition, HasRevision,PersistentObject {

	private String id;
	private String category;
	private String name;
	private String key;
	private String description;
	private int version;
	private String resourceName;
	private String deploymentId;
	private String diagramResourceName;
	private boolean hasStartFormKey;
	private int revision = 1;

	private Date deploymentTime;

	private int suspensionState = SuspensionState.ACTIVE.getStateCode();

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public String getCategory() {
		return this.category;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getKey() {
		return this.key;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public int getVersion() {
		return this.version;
	}

	@Override
	public String getResourceName() {
		return this.resourceName;
	}

	@Override
	public String getDeploymentId() {
		return this.deploymentId;
	}

	@Override
	public String getDiagramResourceName() {
		return this.diagramResourceName;
	}

	@Override
	public boolean hasStartFormKey() {
		return this.hasStartFormKey;
	}

	@Override
	public boolean isSuspended() {
		return suspensionState == SuspensionState.SUSPENDED.getStateCode();
	}

	public boolean isHasStartFormKey() {
		return hasStartFormKey;
	}

	public void setHasStartFormKey(boolean hasStartFormKey) {
		this.hasStartFormKey = hasStartFormKey;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public void setDeploymentId(String deploymentId) {
		this.deploymentId = deploymentId;
	}

	public void setDiagramResourceName(String diagramResourceName) {
		this.diagramResourceName = diagramResourceName;
	}

	public int getSuspensionState() {
		return suspensionState;
	}

	public void setSuspensionState(int suspensionState) {
		this.suspensionState = suspensionState;
	}

	public Date getDeploymentTime() {
		return deploymentTime;
	}

	public void setDeploymentTime(Date deploymentTime) {
		this.deploymentTime = deploymentTime;
	}

	@Override
	public void setRevision(int revision) {
		this.revision = revision;
	}

	@Override
	public int getRevision() {
		return this.revision;
	}

	@Override
	public int getRevisionNext() {
		return revision + 1;
	}

	@Override
	public Object getPersistentState() {
	    Map<String, Object> persistentState = new HashMap<String, Object>();  
	    persistentState.put("suspensionState", this.suspensionState);
	    persistentState.put("category", this.category);
	    return persistentState;
	}

}
