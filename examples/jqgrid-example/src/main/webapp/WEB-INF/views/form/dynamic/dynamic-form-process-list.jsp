
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<table id="${uuid}" class="autowidth autoheight"></table>
<div id="p${uuid}"></div>

<link href="${ctx}/static/js/lib/jquery-validation/1.9.0/validate.css" type="text/css" rel="stylesheet" />  
<script src="${ctx}/static/js/lib/jquery-validation/1.9.0/jquery.validate.min.js" type="text/javascript"></script>
<script src="${ctx}/static/js/lib/jquery-validation/1.9.0/messages_bs_cn.js" type="text/javascript"></script>
<script src="${ctx}/static/js/dynamic-process-list.js" type="text/javascript"></script>

<script  type="text/javascript">

jQuery().ready(function (){
	
$("#${uuid}").jqGrid({        
   	url:'${ctx}/form/dynamic/search',
	datatype: "json",	
   	colNames:['流程定义ID','部署ID','名称','KEY', '版本#','XML','图片','状态'],
   	colModel:[
   	    {name: "id", index:'id',width:100,key:true},
   	 	{name:'deploymentId',index:'deploymentId', width:90},
   	 	{name:'name',index:'name', width:150},
   		{name:'key',index:'key', width:150 },   		
   		{name:'version',index:'version', width:50,align:'right',sorttype:'int',searchtype:"integer"},   		
   		{
   			name:'resourceName',index:'a.resource_name_', width:100,
   			formatter: "myshowlink", formatoptions:{baseLinkUrl:"${ctx}/workflow/procdef/resource/deployment",pathVariables:["deploymentId"],addParams:["resourceName"]}
   		},
   		{
   			name:'diagramResourceName',index:'a.dgrm_resource_name_', width:100,
   			formatter: "myshowlink", formatoptions:{baseLinkUrl:"${ctx}/workflow/procdef/resource/deployment",pathVariables:["deploymentId"],addParamMap:[{name:"resourceName",value:"diagramResourceName"}]}
   		},
   		{name:'suspensionState',index:'suspensionState', width:0,hidden:true }
   	],
   
   	pager: '#p${uuid}',
   	sortname: 'id',
    viewrecords: true,
    sortorder: "desc",
    rownumbers:true,   
	caption: "动态Form流程列表",
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
	{add:false,edit:false,search:false,del:false	},//options
	{},// edit options
	{},// add options
	{}, // del options
	{}
);


jQuery("#${uuid}").jqGrid('navButtonAdd','#p${uuid}',{
	id:'start_${uuid}',
	caption:"启动",
	title:'启动流程',
	buttonicon : "ui-icon-play",
	 position : "last",
	onClickButton:function(){		
		var rowid = jQuery("#${uuid}").jqGrid('getGridParam','selrow');				
		if(rowid){
			var ret = $("#${uuid}").jqGrid('getRowData',rowid);			
			showStartupProcessDialog(ret.name,ret.key);	
		} else {		
			$.jgrid.info_dialog2('注意',"请选择记录");	
		}						
	} 
});


jQuery("#${uuid}").jqGrid('navButtonAdd','#p${uuid}',{
	id:'state_${uuid}',
	caption:"挂起",
	title:'挂起流程',
	buttonicon : "ui-icon-pause",
	 position : "last",
	onClickButton:function(){
		
		var gsr = jQuery("#${uuid}").jqGrid('getGridParam','selrow');			
		if(gsr){
			var ret = $("#${uuid}").jqGrid('getRowData',gsr);
			var state = ret.suspensionState;	
			
			$.ajax({
				url: '${ctx}/workflow/procdef/update/'+state+'?processDefinitionId='+gsr,
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

//设置grid高度
$("#${uuid}").jqGrid('gridHeight');

});

</script>
