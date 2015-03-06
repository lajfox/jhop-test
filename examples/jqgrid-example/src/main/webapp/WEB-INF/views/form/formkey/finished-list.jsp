<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<table id="${uuid}" class="autowidth autoheight"></table>
<div id="p${uuid}"></div>



<script  type="text/javascript">

jQuery().ready(function (){
	
$("#${uuid}").jqGrid({        
   	url:"${ctx}/form/formkey/process-instance/finished/list",
	datatype: "json",	
   	colNames:['流程ID','流程定义ID','流程启动时间','流程结束时间','流程结束原因'],
   	colModel:[
   	    {name: "id", index:'id',width:40,key:true,stype:'number'},
   	    {name: "processDefinitionId", index:'processDefinitionId',width:100},
   	 	{name:'startTime',index:'startTime', width:90,formatter:'mydatetime'},
   	 	{name:'endTime',index:'endTime', width:150,formatter:'mydatetime'	},   	 	
   		{name:'deleteReason',index:'deleteReason', width:150	}
   		
   				
   	],   
   	rowNum:10,
   	rowList:[10,20,30],
   	pager: '#p${uuid}',
   	sortname: 'id',
    viewrecords: true,
    sortorder: "desc",
    rownumbers:true,   
	caption: "己结束流程(动态)",
	autowidth:true
	
});



		
$("#${uuid}").jqGrid('navGrid','#p${uuid}',
	{
		add:false,edit:false,del:false	,search:false		
	},//options
	{},// edit options
	{},// add options
	{}, // del options
	{}
);


jQuery("#${uuid}").jqGrid('navButtonAdd','#p${uuid}',{	
	caption:"流程图",
	title:'流程图',
	buttonicon : "ui-icon-star",
	 position : "last",
	onClickButton:function(){		
		var gsr = jQuery("#${uuid}").jqGrid('getGridParam','selrow');
		if(gsr){
			var ret = $("#${uuid}").jqGrid('getRowData',gsr);		
			var vw = document.documentElement.clientWidth;
			var vh = document.documentElement.clientHeight;
			var vt = (new Date()).getTime();
			
			$.jgrid.form_dialog("${uuid}diagram",
			{	title:'流程历史',
				width:vw*0.7,
				height:vh*0.5,	
				closeOnEscape : true 						 						
			},
			{
				url: '${ctx}/workflow/historic',
				data:{actionVariableNames:'deptLeaderPass_text,hrPass_text,reApply_text',reasonVariableNames:'hrBackReason,leaderBackReason',processInstanceId:ret.id,processDefinitionId:ret.processDefinitionId,time:vt}
				
			});			
		} else {			
			$.jgrid.info_dialog2('注意',"请选择记录");	
		}						
	} 
});


//设置grid高度
$("#${uuid}").jqGrid('gridHeight');
//$("#${uuid}").jqGrid('qlsearch');

});

</script>
