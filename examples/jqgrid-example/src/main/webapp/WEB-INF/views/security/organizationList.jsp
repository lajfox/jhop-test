<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/common/taglibs.jsp"%>

<table id="${uuid}" class="autowidth autoheight"></table>
<div id="p${uuid}"></div>

<script  type="text/javascript"> 
var plevel_treeObj;//先把上级树
jQuery().ready(function (){	
$("#${uuid}").jqGrid({        
   	url:'${ctx}/security/organization/search',
	datatype: "json",
	shrinkToFit:false,
   	colNames:['id','名称','编码','上级','排序'],
   	colModel:[
   	    {name: "id",index:'id',width:0,hidden:true,sortable:false,search:false},   		
   		{name:'name',index:'name', width:260,
   	    	editable:true,
   	    	editrules:{required:true,custom:true,
   	    		custom_func:function(value, name) {
   	    			return $.jgrid.checkexists({gid:'${uuid}',value:value,name:name},
   	    					{url:'${ctx}/public/checkExists/com.techstar.security.entity.Organization/name'});
   	    		}
   	    	},
   	    	editoptions:{maxlength:"40"},
   	    	formoptions:{ elmsuffix:"(*)"}
   	    },
   	    {name:'code',index:'code', width:120,
   	   	    	editable:true,
   	   	    	editrules:{required:true,custom:true,
   	   	    		custom_func:function(value, name) {
   	   	    			return $.jgrid.checkexists({gid:'${uuid}',value:value,name:name},
   	   	    					{url:'${ctx}/public/checkExists/com.techstar.security.entity.Organization/code'});
   	   	    		}
   	   	    	},
   	   	    	editoptions:{maxlength:"40"},
   	   	    	formoptions:{ elmsuffix:"(*)"}
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
   		{name:'orderno',index:'orderno', width:120,align:"right",sorttype:'double',
   			editable:true,editrules:{number:true,maxValue:9999.99,minValue:0},searchrules:{number:true}
   		}  			
   	],
   	pager: '#p${uuid}',
   	sortname: 'orderno',
    viewrecords: true,
    sortorder: "asc", 
	editurl: "${ctx}/security/organization/edit",
	autowidth:true,
	treeGrid: true,
    treeGridModel: "adjacency",
    ExpandColumn: "name",
	caption: "组织机构列表"
});

var vh1 = $('#centerPane',window.parent.document).height() - $('.ui-tabs-nav',window.parent.document).height();
var organization_editoptins = {
		modal:true,	
		recreateForm:true,
		reloadAfterSubmit:true,
		bottominfo:"(*) 必须填写",
		viewPagerButtons:false,
		dataheight:vh1-460,
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
			//初始化组织机构数据			
			$.fn.zTree.init($('#parentx','#FrmGrid_${uuid}','#editmod${uuid}'), {
				data: {
					simpleData: {
						enable: true,
						pIdKey:'parent'
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
					url:"${ctx}/security/organization/selectParents",
					otherParam:{"edit_id":id,parentId:pid}
				},
				callback: {
				    onAsyncSuccess: function(){
				    	plevel_treeObj = $.fn.zTree.getZTreeObj("parentx");				    	
				    }    
				}
			});
		},
		beforeSubmit:function(postdata, formid){
			
			var nodes = plevel_treeObj.getCheckedNodes(true);
			if(nodes){
				$.map(nodes, function(n,i){
					postdata['parentx'] = n.id;
				});
			}else{
				postdata['parentx'] = null;
			}
			
			return[true,''];// 提交
		},
		afterSubmit : function(response, postdata){
	   		var res =$.parseJSON(response.responseText);
	   		return [res.success,res.message,res.userdata.id];
	   	},
	   	afterComplete : function (response, postdata, formid) {
			var res =$.parseJSON(response.responseText);			
			if(res.success){
				$('#${uuid}').jqGrid('setRowData',res.userdata.id,res.userdata);
			}
			//$('#${uuid}').trigger("reloadGrid");//后台加载数刷新
	   	 	$('#${uuid}').jqGrid('resetSelection');
// 			$('#${uuid}').jqGrid("setGridParam",{selrow:null});
		}
	};
	
var organization_addoptins = $.extend({},organization_editoptins);	

var organization_options = {add:false,edit:false,del:false,
		addfunc:function(){
			$.jgrid.setDataUrl({gid:"${uuid}",colnames:['parentx']});
			$("#${uuid}").jqGrid('editGridRow',"new",organization_addoptins);
		},
		editfunc:function(rowid){
			$.jgrid.setDataUrl({gid:"${uuid}",colnames:['parentx'],rowid:rowid});
			$("#${uuid}").jqGrid('editGridRow',rowid,organization_editoptins);
		}
};
//编辑 
<shirox:hasAnyPermissions name="admin,organization:create,organization:update">
$.extend(organization_options,{add:true,edit:true});
</shirox:hasAnyPermissions>

//删除 
<shirox:hasAnyPermissions name="admin,organization:delete">
$.extend(organization_options,{del:true});
</shirox:hasAnyPermissions>

jQuery("#${uuid}").jqGrid('navGrid','#p${uuid}',
		organization_options, //options
		organization_editoptins,// edit options
		organization_addoptins, // add options
		{
			reloadAfterSubmit:false,
			url:"${ctx}/security/organization/delete"
		}, // del options
		{} // search options
		);
		
	//设置grid高度
	$("#${uuid}").jqGrid('gridHeight2');  		

});
</script>
