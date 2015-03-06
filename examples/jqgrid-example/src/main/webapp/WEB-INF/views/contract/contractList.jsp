<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<table id="${uuid}" class="autowidth autoheight"></table>
<div id="p${uuid}"></div>

<link href="${ctx}/static/js/lib/jquery-validation/1.9.0/validate.css" type="text/css" rel="stylesheet" />  
<script src="${ctx}/static/js/lib/jquery-validation/1.9.0/jquery.validate.min.js" type="text/javascript"></script>
<script src="${ctx}/static/js/lib/jquery-validation/1.9.0/messages_bs_cn.js" type="text/javascript"></script>
<script src="${ctx}/static/js/workflow/workflow.js" type="text/javascript"></script>

<script  type="text/javascript">



jQuery().ready(function (){
	
$("#${uuid}").jqGrid({        
   	url:"${ctx}/oa/contract/search",
	datatype: "json",	
   	colNames:['ID','名称','甲方','乙方','总金额','创建时间','当前节点','当前处理人/角色','流程定定义ID','流程定定义KEY','流程实例ID','任务JSON','当前处理任务信息'],
   	colModel:[
   	    {name: "id", index:'id',width:0,key:true,hidden:true}, 
   	 	{name:'name',index:'name', width:150,editable:true,editrules:{required:true} },
   	 	{name:'firstParty',index:'firstParty', width:150,editable:true,editrules:{required:true} },
   		{name:'secondParty',index:'secondParty', width:150,editable:true,editrules:{required:true} },   	 	
   		{name:'amount',index:'amount', width:100,editable:true,editrules:{required:true,number:true}},
   	 	{
   	 		name:'createTime',index:'createTime', width:150,sorttype:"date",   	 		
			editoptions:{
				dataInit:function(el){
					$(el).datetimepicker({						
						showButtonPanel: true,
		   				numberOfMonths: 3	,
		   				minDate:new Date()		   				
					});
				}
			},
			editrules:{required:true} ,
			searchoptions:{
				dataInit:function(el){
					$(el).datepicker({						
						showButtonPanel: true,
		   				numberOfMonths: 3	
					});
				}
			}			
   	 	}, 
   	 	  		
   		{name:'activityNames',width:150,sortable:false,
   			formatter : function(value, options, rData){ 
   				return $.workflow.task.getActivityNames(value, options, rData);
   			},
   			search:false
   		},  
   		{name:'assignees', width:150,sortable:false,
   			formatter : function(value, options, rData){
 				return $.workflow.task.getAssignees(value, options, rData);
			},
			search:false
   		},
   		{name:'processDefinitionId', width:0,sortable:false,hidden:true},
   		{name:'processDefinitionKey', width:0,sortable:false,hidden:true},
   		{name:'processInstanceId', width:0,sortable:false,hidden:true},   		
   		{name:'tasks', width:0,sortable:false,hidden:true},
   		{name:'taskinfo', width:0,sortable:false,hidden:true}    	
   	],   
   	
   
   	pager: '#p${uuid}',
   	sortname: 'createTime',
    viewrecords: true,
    sortorder: "desc",
    rownumbers:true,   
	caption: "合同列表",
	autowidth:true,
	editurl: "${ctx}/oa/contract/generic/edit",	
	
	onSelectRow : function(rowid,status,e){		
		$.workflow.decision({gridId:"${uuid}",businessKey:rowid,processDefinitionKey:'leave-formkey',start:false,
			buttons:[
		         { name:'start_${uuid}',type:'start'},
				 { name:'complete_${uuid}',type:'complete'},
				 { name:'unclaim_${uuid}',type:'unclaim'},
				 { name:'delegate_${uuid}',type:'delegate'},
				 { name:'resolve_${uuid}',type:'resolve'},
				 { name:'claim_${uuid}',type:'claim'}			         
			]
		});			
	}
		
});



		
$("#${uuid}").jqGrid('navGrid','#p${uuid}',
	{
			
	},//options
	{},// edit options
	{},// add options
	{
		url:'${ctx}/oa/contract/generic/delete',
		reloadAfterSubmit:false
	}, // del options
	{multipleSearch:true}// search options	
);

jQuery("#${uuid}").jqGrid('navButtonAdd','#p${uuid}',{
	id:'start_${uuid}',
	caption:"启动",
	title:'启动流程',
	buttonicon : "ui-icon-disk",
	 position : "last",
	onClickButton:function(){								
		$.workflow.formkey.showStartupProcessDialog({gridId:"${uuid}",path:'oa/contract',processDefinitionKey:'leave-formkey',
			buttons:[{name:'start_${uuid}',show:false}],dialogopts:{width:400}
		});								
	} 
});
$('#start_${uuid}').hide();


jQuery("#${uuid}").jqGrid('navButtonAdd','#p${uuid}',{
	id:'claim_${uuid}',
	caption:"签收",
	title:'签收任务',
	buttonicon : "ui-icon-check",
	 position : "last",
	onClickButton:function(){		
		$.workflow.task.claim({gridId:'${uuid}',path:'oa/contract',
			buttons:[{name:'start_${uuid}',show:false},
			         {name:'complete_${uuid}',show:true},
			         {name:'unclaim_${uuid}',show:true},
			         {name:'delegate_${uuid}',show:true},
			         {name:'resolve_${uuid}',show:false},
			         {name:'claim_${uuid}',show:false}
			]
		});						
	} 
});
$('#claim_${uuid}').hide();


jQuery("#${uuid}").jqGrid('navButtonAdd','#p${uuid}',{
	id:'unclaim_${uuid}',
	caption:"取消签收",
	title:'取消签收任务',
	buttonicon : "ui-icon-close",
	 position : "last",
	onClickButton:function(){		
		$.workflow.task.unclaim({gridId:'${uuid}',path:'oa/contract',
			buttons:[{name:'start_${uuid}',show:false},
			         {name:'complete_${uuid}',show:false},
			         {name:'unclaim_${uuid}',show:false},
			         {name:'delegate_${uuid}',show:false},
			         {name:'resolve_${uuid}',show:false},
			         {name:'claim_${uuid}',show:true}
			]
		});						
	} 
});
$('#unclaim_${uuid}').hide();


jQuery("#${uuid}").jqGrid('navButtonAdd','#p${uuid}',{
	id:'complete_${uuid}',
	caption:"办理",
	title:'办理任务',
	buttonicon : "ui-icon-check",
	 position : "last",
	onClickButton:function(){
		$.workflow.formkey.showTaskDialog({gridId:"${uuid}",path:'oa/contract',dialogopts:{width:400,height:300},
			buttons:[{name:'start_${uuid}',show:false},
			         {name:'complete_${uuid}',show:false},
			         {name:'unclaim_${uuid}',show:false},
			         {name:'delegate_${uuid}',show:false},
			         {name:'resolve_${uuid}',show:false},
			         {name:'claim_${uuid}',show:false}
			]
		});						
	} 
});
$('#complete_${uuid}').hide();


jQuery("#${uuid}").jqGrid('navButtonAdd','#p${uuid}',{
	id:'delegate_${uuid}',
	caption:"委派",
	title:'委派任务，将任务委派给别一用户办理',
	buttonicon : "ui-icon-check",
	position : "last",
	onClickButton:function(){		
		$.workflow.task.delegate({gridId:'${uuid}',path:'oa/contract',
			buttons:[{name:'start_${uuid}',show:false},
			         {name:'complete_${uuid}',show:false},
			         {name:'unclaim_${uuid}',show:false},
			         {name:'delegate_${uuid}',show:false},
			         {name:'resolve_${uuid}',show:true},
			         {name:'claim_${uuid}',show:false}
			]
		});					
	} 
});
$('#delegate_${uuid}').hide();


jQuery("#${uuid}").jqGrid('navButtonAdd','#p${uuid}',{
	id:'resolve_${uuid}',
	caption:"取消委派",
	title:'取消委派任务',
	buttonicon : "ui-icon-close",
	 position : "last",
	onClickButton:function(){		
		$.workflow.task.resolve({gridId:'${uuid}',path:'oa/contract',
			buttons:[{name:'start_${uuid}',show:false},
			         {name:'complete_${uuid}',show:true},
			         {name:'unclaim_${uuid}',show:false},
			         {name:'delegate_${uuid}',show:true},
			         {name:'resolve_${uuid}',show:false},
			         {name:'claim_${uuid}',show:false}
			]
		});					
	} 
});
$('#resolve_${uuid}').hide();


jQuery("#${uuid}").jqGrid('navButtonAdd','#p${uuid}',{	
	caption:"流程图",
	title:'流程图',
	buttonicon : "ui-icon-star",
	 position : "last",
	onClickButton:function(){		
		$.workflow.historic.view("${uuid}",{processDefinitionKey:'leave-formkey',actionVariableNames:'deptLeaderPass_text,hrPass_text,reApply_text',reasonVariableNames:'hrBackReason,leaderBackReason'});		
	} 
});


//设置grid高度
$("#${uuid}").jqGrid('gridHeight');


});

</script>
