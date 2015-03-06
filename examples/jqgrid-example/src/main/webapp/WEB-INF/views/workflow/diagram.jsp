<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<p align='center'>
<img alt="流程图" border='0' src="${ctx}/workflow/historic/diagram/${param['processDefinitionId']}/${param['processInstanceId']}?executionId=${param['executionId']}&_time=${param['time']}">
</p>