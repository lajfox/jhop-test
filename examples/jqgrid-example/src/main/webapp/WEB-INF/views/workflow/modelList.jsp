<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<table id="${uuid}" class="autowidth autoheight"></table>
<div id="p${uuid}"></div>


<script  type="text/javascript">

jQuery().ready(function (){
	
$("#${uuid}").jqGrid({        
   	url:'${ctx}/workflow/model/search',
	datatype: "json",	
   	colNames:['ID','名称','KEY', '版本#', '创建时间','最后更新时间','元数据','描述',''],
   	colModel:[
   	    {name: "id", index:'id_',width:50,key:true},
   	 	{name:'name',index:'name_', width:90,   	    	
   	    	editable:true,		
   	    	formoptions:{  elmsuffix:"(*)" },
			editrules:{required:true}},
   		{name:'key',index:'key_', width:55,editable:true },   		
   		{name:'version',index:'version_', width:100,sorttype:'int',searchtype:"integer",searchrules:{integer:true, minValue:1}},
   		{name:'createTime',index:'create_time_', width:80,sorttype:'date',formatter:'mydatetime',
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
   		{name:'lastUpdateTime',index:'last_update_time_', width:80,sorttype:'date',formatter:'mydatetime',
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
   		{name:'metaInfo',index:'meta_info_', width:90,sortable:false},
   		{name:'description',index:'description', width:0,hidden:true, sortable:false,search:false,   			
   	   		editable:true,   			
   			editrules:{edithidden:true},
   			edittype:"textarea", 
   			editoptions:{rows:"5",cols:"20"}   			
   		},
   		{name: "editorSourceExtraValueId", index:'editorSourceExtraValueId',width:0,hidden:true,sortable:false,search:false }
   		
   	],
   
   	pager: '#p${uuid}',
   	sortname: 'id_',
    viewrecords: true,
    sortorder: "desc",
    rownumbers:true,
    editurl: "${ctx}/workflow/model/create",
	caption: "模型列表",
	autowidth:true
	
});


var activiti_model_addoptions = 
{
	url:'${ctx}/workflow/model/create',
	closeAfterAdd:true,
	reloadAfterSubmit:true,
	afterSubmit : function(response, postdata)
	{
		var res =$.parseJSON(response.responseText);			
		return [res.success,res.message,res.userdata.id];
	},
	afterComplete : function (response, postdata, formid) {
		var res =$.parseJSON(response.responseText);			
		if(res.success){
			$("#${uuid}").jqGrid('setRowData',res.userdata.id,res.userdata);
			window.open('${ctx}/service/editor?id='+res.userdata.id);			
		}			
	}	
};
		
$("#${uuid}").jqGrid('navGrid','#p${uuid}',
	{
		editfunc:function(rowid){
			window.open('${ctx}/service/editor?id='+rowid);				
		}
	},//options
	{},// edit options
	activiti_model_addoptions,// add options
	{
		url:'${ctx}/workflow/model/delete',
		reloadAfterSubmit:false,
		afterSubmit : function(response, postdata)
	   	{
	   		var res =$.parseJSON(response.responseText);
	   		return [res.success,res.message];
	   	}		
	}, // del options
	{
		//multipleSearch:true
	}
);

jQuery("#${uuid}").jqGrid('navButtonAdd','#p${uuid}',{
	caption:"复制",
	title:'复制模型',
	buttonicon : "ui-icon-copy",
	 position : "last",
	onClickButton:function(){
		var gsr = jQuery("#${uuid}").jqGrid('getGridParam','selrow');
		if(gsr){	
			$("#${uuid}").jqGrid('editGridRow',gsr,{
				url :'${ctx}/workflow/model/copy',
				closeAfterEdit:true				
			});
		} else {			
			$.jgrid.info_dialog2('注意',"请选择记录");	
		}							
	} 
});


jQuery("#${uuid}").jqGrid('navButtonAdd','#p${uuid}',{
	caption:"部署",
	title:'部署流程',
	buttonicon : "ui-icon-circle-triangle-e",
	 position : "last",
	onClickButton:function(){
		var gsr = jQuery("#${uuid}").jqGrid('getGridParam','selrow');
		if(gsr){
			var ret = $("#${uuid}").jqGrid('getRowData',gsr);		
			if(ret.editorSourceExtraValueId){
				$.jgrid.info_dialog('注意',"部署所选流程?",'',{buttonalign : 'right',
					buttons : [
						{
							text:"确定",
							onClick:function(){	
								
								var self = this;
								$.ajax({
									url: '${ctx}/workflow/model/deploy/'+gsr,
									type: "POST",
									dataType: "json",
									beforeSend : function(xhr){
										$.jgrid.hideModal(self,{jqm:true});
									},
									error:function(xhr, textStatus, errorThrown){									
										$.jgrid.error_dialog('注意',"发布流程失败");
									},
									success: function(data, textStatus){
										if(data.success){
											$.jgrid.info_dialog('注意',data.message);
										}else{
											$.jgrid.error_dialog('注意',data.message,'',{width:500,align:'left'});
										}
									},
									complete : function (req, err) {
										if(parseInt(req.status,10) == 200){
											var $json = $.parseJSON(req.responseText);										
											if($json.success){
												setTimeout("$.jgrid.hideModal('#info_dialog',{jqm:true})",3000);
											}
										}
									}
								});							
								
								
							}						
						},	
						{
							text:"补充流程资源",
							onClick:function(){	
								
								var self = this;
								$.jgrid.hideModal(self,{jqm:true});
								
								$.jgrid.form_dialog("${uuid}_deploy",
								{	title:'部署流程',
									width:400,
									height:200 						 						
								},
								{
									url: '${ctx}/workflow/model/formkey?uuid=${uuid}_deploy&uuid2=${uuid}&id='+gsr
								});	
								
							}						
						},						
						{
							text:"取消",
							onClick:function(){							
								$.jgrid.hideModal(this,{jqm:true});
							}
						}					
					]
				});	
			}else{				
				$.jgrid.info_dialog2('注意',"该模型还没存在流程文件");				
			}
		} else {			
			$.jgrid.info_dialog2('注意',"请选择记录");	
		}							
	} 
});

jQuery("#${uuid}").jqGrid('navButtonAdd','#p${uuid}',{
	caption:"导出",
	title:'导出流程',
	buttonicon : "ui-icon-circle-triangle-s",
	 position : "last",
	onClickButton:function(){
		var gsr = jQuery("#${uuid}").jqGrid('getGridParam','selrow');
		if(gsr){	
			var ret = $("#${uuid}").jqGrid('getRowData',gsr);		
			if(ret.editorSourceExtraValueId){
				window.open('${ctx}/workflow/model/export/'+gsr);
			}else{				
				$.jgrid.info_dialog2('注意',"该模型还没存在流程文件");					
			}
		} else {			
			$.jgrid.info_dialog2('注意',"请选择记录");	
		}							
	} 
});

jQuery("#${uuid}").jqGrid('navButtonAdd','#p${uuid}',{
	caption:"导入",
	title:'导入流程',
	buttonicon : "ui-icon-circle-triangle-n",
	 position : "last",
	onClickButton:function(){
		$.jgrid.form_dialog("${uuid}_activiti_model_import",
		{	title:'导入流程',
			width:400,
			height:200 						 						
		},
		{
			url: '${ctx}/workflow/model/importform?uuid=${uuid}_activiti_model_import&uuid2=${uuid}'
		});							
	} 
});

//jQuery("#${uuid}").jqGrid('filterToolbar',{searchOnEnter : false,stringResult:false});

//设置grid高度
$("#${uuid}").jqGrid('gridHeight');
$("#${uuid}").jqGrid('qlsearch');

});

</script>

