<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/common/taglibs.jsp"%>

<table id="${uuid}" class="autowidth autoheight"></table>
<div id="p${uuid}"></div>

<script  type="text/javascript"> 
var menu_treeObj;
jQuery().ready(function (){	
	
$("#${uuid}").jqGrid({        
   	url:'${ctx}/security/permission/search',
	datatype: "json",	
   	colNames:['id','名称','标识', '链接', '菜单','上级菜单','排序'],
   	colModel:[
   	    {name: "id",index:'id',width:0,hidden:true,sortable:false,search:false},
   		{name:'displayName',index:'displayName', width:100,
   	    	editable:true,editrules:{required:true},
   	    	editoptions:{maxlength:"500"},
   	    	formoptions:{ elmsuffix:"(*)"}
   	    },
   		{name:'name',index:'name', width:100,
   	    	editable:true,
   	    	editrules:{required:true,custom:true,
   	    		custom_func:function(value, name) {
   	    			return $.jgrid.checkexists({gid:'${uuid}',value:value,name:name},
   	    					{url:'${ctx}/security/permission/checkName'});
   	    		}
   	    	},
   	    	editoptions:{maxlength:"300"},
   	    	formoptions:{ elmsuffix:"(*)"}
   	    },
   		{name:'url',index:'url', width:80,editable:true,
   	    	editoptions:{maxlength:"500",size:40}   			
   	    },   		
   		{name:'menu',index:'menu',width:55,align:'center',
   			editable:true,edittype:"checkbox",editoptions:{value:"true:false"},
   			formatter:'checkbox',
   			stype:'select',searchoptions:{sopt:['eq','ne'],value:"true:是;false:否"}   			
   		},
   	 	{name: "parentx",index:'parentx.id',width:120,sortable:false,search:false,hidden:true,
   	    	editable:true,editrules:{edithidden:true},
   	    	edittype:"custom",
   	    	editoptions: {custom_element:function(value, options){
   	    		return '<ul id="parentx" class="ztree"></ul>';
   	    	},custom_value:function(value){
   	    		return value.val();
   	    	}}   	    	
   	    },
   		{name:'orderno',index:'orderno', width:50,align:"right",sorttype:'double',
   			editable:true,editrules:{number:true,maxValue:9999.99,minValue:0},
   			searchrules:{number:true}
   		}  			
   	],
   	pager: '#p${uuid}',
   	sortname: 'orderno',
    viewrecords: true,
    sortorder: "asc", 
	editurl: "${ctx}/security/permission/edit",
	autowidth:true,
	treeGrid: true,
    treeGridModel: "adjacency",
    ExpandColumn: "displayName",
	caption: "权限列表"	
});

var vh1 = $('#centerPane',window.parent.document).height() - $('.ui-tabs-nav',window.parent.document).height();
var permission_editoptins = {
		modal:true,	
		recreateForm:true,
		reloadAfterSubmit:true,
		bottominfo:"(*) 必须填写",
		width:400,
		dataheight:vh1-360,
		viewPagerButtons:false,
		afterShowForm:function(formid){
			var id = $('#id_g','#FrmGrid_${uuid}','#editmod${uuid}').val();
			id = (id == "_empty") ? "":id;	
			var pid ;
			if(id){
				var ret = $('#${uuid}').jqGrid('getRowData',id);				
				pid = ret.parent;
			}else{
				pid = $('#${uuid}').jqGrid("getGridParam",'selrow');
			}
			
					
			$.fn.zTree.init($('#parentx','#FrmGrid_${uuid}','#editmod${uuid}'), {
				data: {
					simpleData: {
						enable: true,
						pIdKey:'parent'
					},
					key:{								
						name:'displayName'
					}
				},
				check: {
					enable: true,
					chkStyle: "radio",
					radioType: "all"
				},
				async:{
					enable: true,
					dataType:"json",
					url:"${ctx}/security/permission/selectMenu",
					otherParam:{"edit_id":id,parentId:pid}
				},
				callback: {
				    onAsyncSuccess: function(){
				    	menu_treeObj = $.fn.zTree.getZTreeObj("parentx");				    	
				    }    
				}
			});
			
			var vh1 = $('#centerPane',window.parent.document).height() - $('.ui-tabs-nav',window.parent.document).height();
			var vh2 = formid.height();
			if(vh2 > vh1){
				formid.height(vh1-120);
			}			
		},
		afterSubmit : function(response, postdata)
	   	{
			//$("#${uuid}").jqGrid('resetSelection');
	   		var res =$.parseJSON(response.responseText);
	   		return [res.success,res.message,res.userdata.id];
	   	},
		beforeSubmit:function(postdata, formid){
			
			var nodes2 = menu_treeObj.getCheckedNodes(true);
			if(nodes2){
				$.map(nodes2, function(n,i){
					postdata['parentx'] = n.id;
				});
			}else{
				postdata['parentx'] = null;
			}
			
			return[true,''];// 提交
		},afterComplete : function (response, postdata, formid) {
			var res =$.parseJSON(response.responseText);						
			$("#${uuid}").jqGrid('setRowData',res.userdata.id,res.userdata);
			$('#${uuid}').jqGrid('resetSelection');
		}
	};
	
var permission_addoptins = $.extend({},permission_editoptins);	

var permission_options = {add:false,edit:false,del:false,
		addfunc:function(){
			$.jgrid.setDataUrl({gid:"${uuid}",colnames:['parentx']});
			$("#${uuid}").jqGrid('editGridRow',"new",permission_addoptins);
		},
		editfunc:function(rowid){
			$.jgrid.setDataUrl({gid:"${uuid}",colnames:['parentx'],rowid:rowid});
			$("#${uuid}").jqGrid('editGridRow',rowid,permission_editoptins);
		}
};
//编辑 
<shirox:hasAnyPermissions name="admin,permission:create,permission:update">
$.extend(permission_options,{add:true,edit:true});
</shirox:hasAnyPermissions>

//删除 
<shirox:hasAnyPermissions name="admin,permission:delete">
$.extend(permission_options,{del:true});
</shirox:hasAnyPermissions>


jQuery("#${uuid}").jqGrid('navGrid','#p${uuid}',
		permission_options, //options
		permission_editoptins,// edit options
		permission_addoptins, // add options
		{
			reloadAfterSubmit:false,
			url:"${ctx}/security/permission/delete",
			afterSubmit : function(response, postdata)
		   	{
		   		var res =$.parseJSON(response.responseText);
		   		return [res.success,res.message];
		   	}
		}, // del options
		{} // search options
		);
		
//设置grid高度
$("#${uuid}").jqGrid('gridHeight');  
$("#${uuid}").jqGrid('bindKeys');


});


</script>
