<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/common/taglibs.jsp"%>

<table id="${uuid}" class="autowidth autoheight"></table>
<div id="p${uuid}"></div>

<script  type="text/javascript"> 
var role_treeObj;
jQuery().ready(function (){	
	
$("#${uuid}").jqGrid({        
   	url:'${ctx}/security/role/search',
	datatype: "json",	
   	colNames:['id','名称','授权','描述'],
   	colModel:[
   	    {name: "id",width:0,hidden:true,sortable:false,search:false},   		   		
   		{name:'name',index:'name', width:100,
   	    	editable:true,editrules:{required:true,custom:true,
   	    		custom_func:function(value, name) {
   	    			return $.jgrid.checkexists({gid:'${uuid}',value:value,name:name},
   	    					{url:'${ctx}/security/role/checkName'});   	    			
   	    		}
   	    	},
   	    	editoptions:{maxlength:"500"},
   	    	formoptions:{ elmsuffix:"(*)"}},
   	    {name: "permissionList${uuid}",index:'permissionList',hidden:true,width:300,sortable:false,search:false,
   	    	editable:true,editrules:{edithidden:true},
   	    	edittype:"custom",
   	    	editoptions: {custom_element:function(value, options){
   	    		return '<ul id="permissionList${uuid}" class="ztree"></ul>';
   	    	},custom_value:function(value){   	    		
   	    		return value.val();
   	    	}}   	  	    	
   	    }, 
 	    {name:'note',index:'note', width:300,
 	   	    editable:true,
 	   	    edittype:'textarea',
 	   		editoptions: {rows:"5",cols:"50",maxlength:"2000"} 	   	   
   	    }   	    	
   	],
   
   	pager: '#p${uuid}',
   	sortname: 'name',
    viewrecords: true,
    sortorder: "asc",
    rownumbers:true,  
	editurl: "${ctx}/security/role/edit",
	autowidth:true,
	caption: "角色列表"	
});

var setting = {
	check: {
		enable: true
	},
	data: {
		simpleData: {
			enable: true,
			pIdKey:'parent'
		}
	}
};

var vh1 = $('#centerPane',window.parent.document).height() - $('.ui-tabs-nav',window.parent.document).height();
var role_editoptions = {
		modal:true,	
		recreateForm:true,
		width:500,
		dataheight:vh1-320,
		viewPagerButtons:false,
		reloadAfterSubmit:false,
		bottominfo:"(*) 必须填写",
		afterShowForm:function(formid){
			var id = $('#id_g','#FrmGrid_${uuid}','#editmod${uuid}').val();
			id = (id == "_empty") ? "":id;
			
			$.fn.zTree.init($('#permissionList${uuid}','#TblGrid_${uuid}'), $.extend(setting,{async: {
					enable: true,
					dataType:"json",
					url:"${ctx}/security/role/selectPermission",
					otherParam:{"edit_id":id}
				},
				callback: {
				    onAsyncSuccess: function(){
				    	role_treeObj = $.fn.zTree.getZTreeObj("permissionList${uuid}");				    	
				    }    
				}				
			})
			);
			
			var vh1 = $('#centerPane',window.parent.document).height() - $('.ui-tabs-nav',window.parent.document).height();
			var vh2 = formid.height();
			if(vh2 > vh1){
				formid.height(vh1-120)
			}			
		},
		
		afterComplete : function (response, postdata, formid) {
			var res =$.parseJSON(response.responseText);			
			if(res.success){
				$("#${uuid}").jqGrid('setRowData',res.userdata.id,{permissionList:res.userdata.permissionList});
			}			
		},
		beforeSubmit:function(postdata, formid){
			//var role_treeObj = $.fn.zTree.getZTreeObj("permissionList${uuid}");
			var nodes = role_treeObj.getCheckedNodes(true);
			postdata['permissionList'] = $.map( nodes, function(n,i){
				  return n.id;
			});                         
			return[true,''];// 提交
		},
		afterSubmit : function(response, postdata)
	   	{
	   		var res =$.parseJSON(response.responseText);
	   		return [res.success,res.message,res.userdata.id];
	   	}
	};
var role_addoptions = $.extend({},role_editoptions);
	
var role_options = {add:false,edit:false,del:false};
//编辑 
<shirox:hasAnyPermissions name="admin,role:create,role:update">
$.extend(role_options,{add:true,edit:true});
</shirox:hasAnyPermissions>

//删除 
<shirox:hasAnyPermissions name="admin,role:delete">
$.extend(role_options,{del:true});
</shirox:hasAnyPermissions>

jQuery("#${uuid}").jqGrid('navGrid','#p${uuid}',
		role_options, //options
		role_editoptions,// edit options		
		role_addoptions, // add options
		{
			reloadAfterSubmit:false,
			url:"${ctx}/security/role/delete"
		}, // del options
		{} // search options
		);
		
//设置grid高度
$("#${uuid}").jqGrid('gridHeight');  	
$("#${uuid}").jqGrid('bindKeys');

});
</script>
