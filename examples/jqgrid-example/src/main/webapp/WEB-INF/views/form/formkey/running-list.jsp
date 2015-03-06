<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<table id="${uuid}" class="autowidth autoheight"></table>
<div id="p${uuid}"></div>



<script  type="text/javascript">

function activityName(cellvalue, options, rowObject){
	var name = cellvalue;
	$.ajax({
		url: '${ctx}/activity/name/'+rowObject.processDefinitionId+'/'+rowObject.processInstanceId+'/'+cellvalue,
		type: "POST",
		dataType: "json",
		async:false,
		success:function (data){
			name = data.userdata;			
		}
	});
	return name;
}

jQuery().ready(function (){
	
$("#${uuid}").jqGrid({        
   	url:"${ctx}/form/formkey/process-instance/running/list",
	datatype: "json",	
   	colNames:['执行ID','流程实例ID','流程定义ID','当前节点','挂起'],
   	colModel:[
   	    {name: "id", index:'id',width:50,key:true,stype:'number'},
   	    {name: "processInstanceId", index:'processInstanceId',width:100},
   	 	{name:'processDefinitionId',index:'processDefinitionId', width:90},
   	 	
   	 	{
   	 		name:'activityId',index:'activityId', width:150,sortable:false,
   	 		formatter:activityName
   	 	},  
   		
   		
   		{name:'suspended',index:'suspended',
   			align:'center',   			
   			formatter:"select",editoptions:{value:"false:否;true:是"},
   			
   			width:50} 
   				
   	],   
   	rowNum:10,
   	rowList:[10,20,30],
   	pager: '#p${uuid}',
   	sortname: 'id',
    viewrecords: true,
    sortorder: "desc",
    rownumbers:true,   
	caption: "运行中流程(动态)",
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
				data:{actionVariableNames:'deptLeaderPass_text,hrPass_text,reApply_text',reasonVariableNames:'hrBackReason,leaderBackReason',processDefinitionId:ret.processDefinitionId,processInstanceId:ret.processInstanceId,executionId:ret['id'],time:vt}
				
			});			
		} else {			
			$.jgrid.info_dialog2('注意',"请选择记录");	
		}						
	} 
});


//设置grid高度
$("#${uuid}").jqGrid('gridHeight');


});

</script>

