<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<table id="${uuid}" class="autowidth autoheight"></table>
<div id="p${uuid}"></div>


<script  type="text/javascript">

jQuery().ready(function (){
	
$("#${uuid}").jqGrid({        
   	url:'${ctx}/workflow/procdef/search',
	datatype: "json",	
   	colNames:['流程定义ID','部署ID','名称','KEY', '版本#','XML','图片', '部署时间','挂起'],
   	colModel:[
   	    {name: "id", index:'a.id_',width:100,key:true},
   	 	{name:'deploymentId',index:'a.deployment_id_', width:90},
   	 	{name:'name',index:'a.name_', width:150},
   		{name:'key',index:'a.key_', width:150 },   		
   		{name:'version',index:'a.version_', width:50,align:'right',sorttype:'int',searchtype:"integer",searchrules:{integer:true, minValue:1}},
   		
   		{
   			name:'resourceName',index:'a.resource_name_', width:100,
   			formatter: "myshowlink", formatoptions:{baseLinkUrl:"${ctx}/workflow/procdef/resource/deployment",pathVariables:["deploymentId"],addParams:["resourceName"]}
   		},
   		{
   			name:'diagramResourceName',index:'a.dgrm_resource_name_', width:100,
   			formatter: "myshowlink", formatoptions:{baseLinkUrl:"${ctx}/workflow/procdef/resource/deployment",pathVariables:["deploymentId"],addParamMap:[{name:"resourceName",value:"diagramResourceName"}]}
   		}, 
   		
   		{name:'deploymentTime',index:'b.deploy_time_', width:80,sorttype:'date',formatter:'mydatetime',
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
   	sortname: 'b.id_',
    viewrecords: true,
    sortorder: "desc",
    rownumbers:true,   
	caption: "流程定义列表",
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
		add:false,edit:false,
		delfunc:function(rowid){
			var ret = $("#${uuid}").jqGrid('getRowData',rowid);
			$("#${uuid}").jqGrid('delGridRow',rowid,{
				url:'${ctx}/workflow/procdef/delete/'+ret.deploymentId,
				reloadAfterSubmit:false				
			});
		}		
	},//options
	{},// edit options
	{},// add options
	{
		url:'${ctx}/workflow/procdef/delete',
		reloadAfterSubmit:false
	}, // del options
	{
	
	}
);


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
$('#state_${uuid}').hide();

jQuery("#${uuid}").jqGrid('navButtonAdd','#p${uuid}',{
	caption:"部署",
	title:'部署流程',
	buttonicon : "ui-icon-circle-triangle-n",
	position : "last",
	onClickButton:function(){
		$.jgrid.form_dialog("${uuid}_deploy",
		{	title:'部署流程',
			width:400,
			height:200 						 						
		},
		{
			url: '${ctx}/workflow/procdef/deployform?uuid=${uuid}_deploy&uuid2=${uuid}'
		});							
	} 
});



//设置grid高度
$("#${uuid}").jqGrid('gridHeight');
$("#${uuid}").jqGrid('qlsearch');

});

</script>

