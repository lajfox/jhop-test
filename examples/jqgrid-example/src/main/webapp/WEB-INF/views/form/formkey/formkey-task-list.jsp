<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<table id="${uuid}" class="autowidth autoheight"></table>
<div id="p${uuid}"></div>

<!-- 办理任务对话框 -->
<div id="handleTemplate" class="template ui-jqdialog-content"></div>


<link href="${ctx}/static/js/lib/jquery-validation/1.9.0/validate.css" type="text/css" rel="stylesheet" />  
<script src="${ctx}/static/js/lib/jquery-validation/1.9.0/jquery.validate.min.js" type="text/javascript"></script>
<script src="${ctx}/static/js/lib/jquery-validation/1.9.0/messages_bs_cn.js" type="text/javascript"></script>
<script src="${ctx}/static/js/formkey-form-handler.js" type="text/javascript"></script>

<script  type="text/javascript">

function reload(){
	$("#${uuid}").trigger('reloadGrid');
}

jQuery().ready(function (){
	
$("#${uuid}").jqGrid({        
   	url:"${ctx}/form/formkey/task/list",
	datatype: "json",	
   	colNames:['任务ID','任务Key','任务名称','任务处理人','流程定义ID','流程实例ID','优先级', '任务创建日期','任务逾期日','任务描述','任务所属人','执行ID'],
   	colModel:[
   	    {name: "id", index:'id',width:50,key:true},
   	    {name: "taskDefinitionKey", index:'taskDefinitionKey',width:100},
   	 	{name:'name',index:'name', width:90},
   	 	{name:'assignee',index:'assignee', width:150 		},
   	 	{name:'processDefinitionId',index:'processDefinitionId', width:150},
   	 	{name:'processInstanceId',index:'processInstanceId', width:150}, 
   		{name:'priority',index:'priority', width:150}, 
   		{name:'createTime',index:'createTime',  width:150}, 
   		{name:'dueDate',index:'dueDate', width:150		},
   		{name:'description', index:'description',width:100,sortable:false},
   		{name:'owner', width:50,sortable:false},
   		{name:'executionId', width:0,sortable:false,hidden:true}   		
   	],   
   	
   
   	pager: '#p${uuid}',
   	sortname: 'createTime',
    viewrecords: true,
    sortorder: "desc",
    rownumbers:true,   
	caption: "请假待办任务列表",
	autowidth:true,
	
	
	onSelectRow : function(rowid,status,e){
		var ret = $("#${uuid}").jqGrid('getRowData',rowid);		
		if(ret['assignee']){
			$('#complete_${uuid}').show();
			$('#claim_${uuid}').hide();				
		}else{
			$('#claim_${uuid}').show();
			$('#complete_${uuid}').hide();					
		}
	}	
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
	id:'claim_${uuid}',
	caption:"签收",
	title:'签收任务',
	buttonicon : "ui-icon-key",
	 position : "last",
	onClickButton:function(){		
		var rowid = jQuery("#${uuid}").jqGrid('getGridParam','selrow');
		var ret = $("#${uuid}").jqGrid('getRowData',rowid);		
		if(rowid){	
			$.ajax({
				url: '${ctx}/form/dynamic/task/claim/'+ret.id,
				type: "POST",
				dataType: "json",				
				success : function (data) {
					if(data.success){
						$.jgrid.info_dialog2('提示',data.message);	
						$("#${uuid}").jqGrid('setRowData',rowid,{'assignee':data.userdata});
						$('#complete_${uuid}').show();
						$('#claim_${uuid}').hide();						
					}
				}
			});			
		} else {			
			$.jgrid.info_dialog2('注意',"请选择记录");	
		}						
	} 
});
$('#claim_${uuid}').hide();

jQuery("#${uuid}").jqGrid('navButtonAdd','#p${uuid}',{
	id:'complete_${uuid}',
	caption:"办理",
	title:'办理任务',
	buttonicon : "ui-icon-key",
	 position : "last",
	onClickButton:function(){
		
		var rowid = jQuery("#${uuid}").jqGrid('getGridParam','selrow');
		var ret = $("#${uuid}").jqGrid('getRowData',rowid);			
		if(rowid){
			var tkey = ret['taskDefinitionKey'];
			var tname = ret['name'];			
			
			handle(rowid,tkey,tname);			
		} else {			
			$.jgrid.info_dialog2('注意',"请选择记录");	
		}						
	} 
});
$('#complete_${uuid}').hide();


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
				data:{actionVariableNames:'deptLeaderPass_text,hrPass_text,reApply_text',reasonVariableNames:'hrBackReason,leaderBackReason',processDefinitionId:ret.processDefinitionId,processInstanceId:ret.processInstanceId,executionId:ret['executionId'],time:vt}
				
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
