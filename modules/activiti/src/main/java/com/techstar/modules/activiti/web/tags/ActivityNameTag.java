package com.techstar.modules.activiti.web.tags;

import java.io.IOException;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import com.techstar.modules.activiti.util.ProcessDefinitionCache;

/**
 * Activiti工作流　当前节点名称taglib
 * 
 * @author sundoctor
 * 
 */
public class ActivityNameTag extends TagSupport {

	private static final long serialVersionUID = -699119763021130557L;
	private String processDefinitionId;// 流程定义ID
	private String activityId;// 当前节点ID

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public int doEndTag() throws JspTagException {
		try {
			pageContext.getOut().write(ProcessDefinitionCache.getActivityName(processDefinitionId, activityId));
		} catch (IOException ex) {
			throw new JspTagException("Error!");
		}

		return EVAL_PAGE;

	}

}
