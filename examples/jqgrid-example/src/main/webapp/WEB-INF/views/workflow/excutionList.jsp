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
   	url:'${ctx}/workflow/execution/search',
	datatype: "json",	
   	colNames:['执行ID','流程实例ID','流程定义ID','流程定义名称','当前节点', '部署时间','挂起'],
   	colModel:[
   	    {name: "id", index:'a.id_',width:100,key:true},
   	 	{name:'processInstanceId',index:'a.proc_inst_id_', width:90},
   	 	{name:'processDefinitionId',index:'a.proc_def_id_', width:150},
   	 	{name:'processDefinition.name',index:'b.name_', width:150},
   	 
   	 	
   		{
   	 		name:'activityId',index:'a.act_id_', width:150,sortable:false,
   	 		formatter:activityName
   	 	}, 
   		
   		{name:'processDefinition.deploymentTime',index:'c.deploy_time_', width:80,sorttype:'date',formatter:'mydatetime',
			searchoptions:{
				dataInit:function(el){
					$(el).datetimepicker({						
						showButtonPanel: true,
		   				numberOfMonths: 3	,
		   				maxDate:new Date()		   				
					});
				}
			}   			
   		},
   		
   		{name:'suspensionState',index:'a.suspension_state_',
   			align:'center',   			
   			formatter:"select",editoptions:{value:"1:否;2:是"},
   			stype:"select",searchoptions:{sopt:['eq','ne'],value:"1:否;2:是"},
   			width:50}   		
   	],
   
   	pager: '#p${uuid}',
   	sortname: 'a.id_',
    viewrecords: true,
    sortorder: "desc",
    rownumbers:true,   
	caption: "运行中流程",
	autowidth:true,	
	
	onSelectRow : function(rowid,status,e){
		var ret = $("#${uuid}").jqGrid('getRowData',rowid);		
		if(parseInt(ret.suspensionState,10) == 1){
			$('#state_${uuid}').show();
			$('#state_${uuid}').attr('title','挂起流程');
			var $html = $('.ui-pg-div','#state_${uuid}').html();			
			$('.ui-pg-div','#state_${uuid}').html($html.replace("激活","挂起"));
			
		}else{
			$('#state_${uuid}').show();
			$('#state_${uuid}').attr('title','激活流程');
			var $html = $('.ui-pg-div','#state_${uuid}').html();			
			$('.ui-pg-div','#state_${uuid}').html($html.replace("挂起","激活"));			
		}
	}
	
});



		
$("#${uuid}").jqGrid('navGrid','#p${uuid}',
	{
		add:false,edit:false,del:false			
	},//options
	{},// edit options
	{},// add options
	{}, // del options
	{}
);


jQuery("#${uuid}").jqGrid('navButtonAdd','#p${uuid}',{
	id:'state_${uuid}',
	caption:"挂起",
	title:'挂起流程',
	buttonicon : "ui-icon-key",
	 position : "last",
	onClickButton:function(){
		
		var gsr = jQuery("#${uuid}").jqGrid('getGridParam','selrow');
		var ret = $("#${uuid}").jqGrid('getRowData',gsr);
		var state = ret.suspensionState;		
		if(gsr){	
			$.ajax({
				url: '${ctx}/workflow/execution/update/'+state+'?processInstanceId='+ret.processInstanceId,
				type: "GET",
				dataType: "json",				
				success : function (data) {
					if(data.success){						
						$("#${uuid}").jqGrid('setRowData',gsr,{suspensionState:data.userdata});
						if(data.userdata == 1){
							$('#state_${uuid}').attr('title','挂起流程');
							var $html = $('.ui-pg-div','#state_${uuid}').html();			
							$('.ui-pg-div','#state_${uuid}').html($html.replace("激活","挂起"));							
						}else{
							$('#state_${uuid}').attr('title','激活流程');
							var $html = $('.ui-pg-div','#state_${uuid}').html();			
							$('.ui-pg-div','#state_${uuid}').html($html.replace("挂起","激活"));							
						}
					}
				}
			});			
		} else {			
			$.jgrid.info_dialog2('注意',"请选择记录");	
		}						
	} 
});
$('#state_${uuid}').hide();

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
				url: '${ctx}/workflow/historic/diagram',
				data:{processDefinitionId:ret.processDefinitionId,processInstanceId:ret.processInstanceId,executionId:ret.id,time:vt}
				
			});			
		} else {			
			$.jgrid.info_dialog2('注意',"请选择记录");	
		}						
	} 
});


//设置grid高度
$("#${uuid}").jqGrid('gridHeight');
$("#${uuid}").jqGrid('qlsearch');

});

</script>

