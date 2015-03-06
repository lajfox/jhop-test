<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<table id="${uuid}" class="autowidth autoheight"></table>
<div id="p${uuid}"></div>


	<!-- 下面是每个节点的模板，用来定义每个节点显示的内容 -->
	<!-- 使用DIV包裹，每个DIV的ID以节点名称命名，如果不同的流程版本需要使用同一个可以自己扩展（例如：在DIV添加属性，标记支持的版本） -->

	<!-- 部门领导审批 -->
	<div id="deptLeaderAudit" style="display: none">

		<!-- table用来显示信息，方便办理任务 -->
		<%@include file="view-form.jsp" %>
	</div>

	<!-- HR审批 -->
	<div id="hrAudit" style="display: none">

		<!-- table用来显示信息，方便办理任务 -->
		<%@include file="view-form.jsp" %>
	</div>

	<div id="modifyApply" style="display: none">
		<div class="info" style="display: none"></div>
		
		<div id="radio">
			<input type="radio" id="radio1" name="reApply" value="true" /><label id='reApply_true' for="radio1">调整申请</label>
			<input type="radio" id="radio2" name="reApply" checked="checked" value="false" /><label id='reApply_false' for="radio2">取消申请</label>
		</div>
		<hr />
		<table id="modifyApplyContent" style="display: none">
			<caption>调整请假内容</caption>
			<tr>
				<td>请假类型：</td>
				<td>
					<select id="leaveType" name="leaveType">
						<option value='公休'>公休</option>
						<option value='病假'>病假</option>
						<option value='调休'>调休</option>
						<option value='事假'>事假</option>
						<option value='婚假'>婚假</option>
					</select>
				</td>
			</tr>
			<tr>
				<td>开始时间：</td>
				<td><input type="text" id="startTime" name="startTime" /></td>
			</tr>
			<tr>
				<td>结束时间：</td>
				<td><input type="text" id="endTime" name="endTime" /></td>
			</tr>
			<tr>
				<td>请假原因：</td>
				<td>
					<textarea id="reason" name="reason" style="width: 250px;height: 50px"></textarea>
				</td>
			</tr>
		</table>
	</div>

	<!-- 销假 -->
	<div id="reportBack" style="display: none">
		<!-- table用来显示信息，方便办理任务 -->
		<%@include file="view-form.jsp" %>
		<hr/>
		<table>
			<tr>
				<td>实际请假开始时间：</td>
				<td>
					<input id="realityStartTime" />
				</td>
			</tr>
			<tr>
				<td>实际请假开始时间：</td>
				<td>
					<input id="realityEndTime" />
				</td>
			</tr>
		</table>
	</div>


<script src="${ctx}/static/js/leave-todo.js" type="text/javascript"></script>
<script src="${ctx}/static/js/workflow/workflow.js" type="text/javascript"></script>

<script  type="text/javascript">

function reload(data){
	//$("#${uuid}").trigger('reloadGrid');
	$('#complete_${uuid}').hide();
	$('#unclaim_${uuid}').hide();
	$('#delegate_${uuid}').hide();
	$('#resolve_${uuid}').hide();
	
	$("#${uuid}").jqGrid('setRowData',data.userdata.business.id,data.userdata.business);
	$("#${uuid}").jqGrid('setRowData',data.userdata.process.id,data.userdata.process);	
}

jQuery().ready(function (){
	
$("#${uuid}").jqGrid({        
   	url:"${ctx}/oa/leave/search",
	datatype: "json",	
   	colNames:['ID','假种','申请人','申请时间','开始时间','结束时间', '请假原因','实际开始时间','实际结束时间','当前节点','当前处理人/角色','流程定定义ID','流程定定义KEY','流程实例ID','任务JSON','当前处理任务信息'],
   	colModel:[
   	    {name: "id", index:'id',width:0,key:true,hidden:true},
   	    
   	    {
   	    	name: "leaveType", index:'leaveType',width:100,
   	    	editable:true,edittype:"select",editoptions:{value:"${allTypes}"},
   	    	editrules:{required:true} ,
   	    	formatter:'select',
   	    	stype:'select',searchoptions:{sopt:['eq','ne'],value:"${allTypes}"}
   	    },
   	    
   	 	{name:'userId',index:'userId', width:90},
   	 	{
   	 		name:'applyTime',index:'applyTime', width:150,sorttype:"date",
	   	 	searchoptions:{
				dataInit:function(el){
					$(el).datepicker({						
						showButtonPanel: true,
		   				numberOfMonths: 3	,
		   				maxDate:new Date()
					});
				}
			}	   	 		
   	 	},
   	 	
   	 	{
   	 		name:'startTime',index:'startTime', width:150,sorttype:"date",
   	 		editable:true,
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
   		{
   	 		name:'endTime',index:'endTime', width:150,  sorttype:"date",	 		
	   	 	editable:true,
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
   	 	
   	 	
   		{
   			name:'reason',index:'reason', width:200,
   			editable:true,edittype:'textarea',
 	   		editoptions: {rows:"5",cols:"30"},
 	   		editrules:{required:true} 
   		},
   		
   	 	{
   	 		name:'realityStartTime',index:'realityStartTime', width:150,sorttype:"date",   	 		
			searchoptions:{
				dataInit:function(el){
					$(el).datepicker({						
						showButtonPanel: true,
		   				numberOfMonths: 3	
					});
				}
			}			
   	 	}, 
   		{
   	 		name:'realityEndTime',index:'realityEndTime', width:150,  sorttype:"date", 
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
   	sortname: 'applyTime',
    viewrecords: true,
    sortorder: "desc",
    rownumbers:true,   
	caption: "请假列表",
	autowidth:true,
	editurl: "${ctx}/oa/leave/generic/edit",	
	
	onSelectRow : function(rowid,status,e){			
		$.workflow.decision({gridId:"${uuid}",businessKey:rowid,processDefinitionKey:'leave',start:true,
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
		url:'${ctx}/oa/leave/generic/delete',
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
		$.workflow.procdef.start({gridId:'${uuid}',processDefinitionKey:'leave',path:'oa/leave',buttons:[{name:'start_${uuid}',show:false}] });					
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
		$.workflow.task.claim({gridId:'${uuid}',path:'oa/leave',
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
		$.workflow.task.unclaim({gridId:'${uuid}',path:'oa/leave',
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
		
		var rowid = jQuery("#${uuid}").jqGrid('getGridParam','selrow');					
		if(rowid){
			var ret = $("#${uuid}").jqGrid('getRowData',rowid);
			var task = $.workflow.task.deserializeTasks(ret.taskinfo);
			
			var tkey = task.taskDefinitionKey;
			var tname = task.name;
			var taskId = task.id;
			var pid = ret.processInstanceId;			
			
			handle(tkey,tname,rowid,pid,taskId);			
		} else {			
			$.jgrid.info_dialog2('注意',"请选择记录");	
		}						
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
		$.workflow.task.delegate({gridId:'${uuid}',path:'oa/leave',
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
		$.workflow.task.resolve({gridId:'${uuid}',path:'oa/leave',
			buttons:[{name:'start_${uuid}',show:false},
			         {name:'complete_${uuid}',show:true},
			         {name:'unclaim_${uuid}',show:false},
			         {name:'delegate_${uuid}',show:false},
			         {name:'resolve_${uuid}',show:true},
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
		$.workflow.historic.view("${uuid}",{processDefinitionKey:'leave',actionVariableNames:'deptLeaderAction,hrAction,reApply_text',reasonVariableNames:'hrBackReason,leaderBackReason'});		
	} 
});


//设置grid高度
$("#${uuid}").jqGrid('gridHeight');


});

</script>
