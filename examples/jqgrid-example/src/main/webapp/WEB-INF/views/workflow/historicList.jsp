<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<table id="${uuid}" ></table>



<script  type="text/javascript">

jQuery().ready(function (){
	
$("#${uuid}").jqGrid({        
   	url:"${ctx}/workflow/historic/search/${param['processInstanceId']}",
	datatype: "json",	
   	colNames:['ID','流程实例ID','流程节点','处理人','处理结果','处理时间', '处理意见'],
   	colModel:[
   	    {name: "id", width:100,key:true,hidden:true,sortable:false},
   	 	{name:'processInstanceId',index:'processInstanceId', width:0,hidden:true,sortable:false},
   	 	{name:'activityName',index:'activityName', width:100,sortable:false},
   	 	{name:'assignee',index:'assignee', width:80,sortable:false},  
   	 	{name:'action',index:'action', width:50,sortable:false},   	 	
   		{name:'endTime',index:'endTime', width:120,formatter:'mydatetime',sortable:false},
   		{name:'reason',index:'reason', width:200,sortable:false}		
   	],
   	sortable:false,
   	rowNum:300,
   	sortname: 'id',
    viewrecords: true,
    sortorder: "desc",
    rownumbers:true,   
	caption: "流程历史列表",
	autowidth:true,
	height:'auto',	
	beforeRequest:function(){
		
		<c:if test="${empty param['processInstanceId']}">
		return false;
		</c:if>
		
		$("#${uuid}").jqGrid('setGridParam',{postData:{actionVariableNames:"${param['actionVariableNames']}",reasonVariableNames:"${param['reasonVariableNames']}",_time:"${param['time']}"}});
		return true;
	}	
});



});

</script>

