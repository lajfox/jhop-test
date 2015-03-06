
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div id="workflowHistoricTabs" name="workflowHistoricTabs" calss="no-padding" >
<ul>
<li><a href="#workflowHistoricTabs-1">流程图</a></li>
<li><a href="${ctx}/workflow/historic/list?processInstanceId=${param['processInstanceId']}&actionVariableNames=${param['actionVariableNames']}&reasonVariableNames=${param['reasonVariableNames']}&time=${param['time']}">流程列表</a></li>
</ul>

<div id="workflowHistoricTabs-1" >  
<p align='center'>
<img alt="流程图" border='0' src="${ctx}/workflow/historic/diagram/${param['processDefinitionKey']}?processDefinitionId=${param['processDefinitionId']}&processInstanceId=${param['processInstanceId']}&executionId=${param['executionId']}&_time=${param['time']}">
</p>
</div>


			
</div>

<script  type="text/javascript">

jQuery().ready(function (){
	 $( "#workflowHistoricTabs" ).tabs({ 
		 cache: true 
	 });
});

</script>

