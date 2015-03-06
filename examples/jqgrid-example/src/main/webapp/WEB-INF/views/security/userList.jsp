<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/common/taglibs.jsp"%>

<table id="${uuid}" class="autowidth autoheight"></table>
<div id="p${uuid}"></div>

<script  type="text/javascript"> 
var user_treeObj;
var unit_treeObj;

jQuery().ready(function (){	
	
$("#${uuid}").jqGrid({        
   	url:'${ctx}/security/user/search',
	datatype: "json",	
   	colNames:['id','登录名','姓名','密码','确认密码', '电邮','状态','角色','授权','所属单位'],
   	colModel:[
   	    {name: "id",index:'id',width:0,hidden:true,sortable:false,search:false},
   		{name:'loginName',index:'loginName', width:80,
   	    	editable:true,
   	    	editrules:{required:true,custom:true,
   	    		custom_func:function(value, name) {
   	    			return $.jgrid.checkexists({gid:'${uuid}',value:value,name:name},
   	    					{url:'${ctx}/security/user/checkLoginName'});   	    			
   	    		}
   	    	},
   	    	formoptions:{ rowpos:1,colpos:1,elmsuffix:"(*)"},
   	    	editoptions:{maxlength:"100"}
   	    }, 
   	 	{name:'name',index:'name', width:100,
   	    	editable:true,editrules:{required:true},
   	    	formoptions:{ rowpos:1,colpos:2,elmsuffix:"(*)"},
   	    	editoptions:{maxlength:"300"}
   	    },
   	    
   	 	{name: "plainPassword",index:'plainPassword',width:0,hidden:true,sortable:false,search:false,
   	    	editable:true,edittype:'password',editrules:{edithidden:true},
   	    	formoptions:{ rowpos:2,colpos:1},
   	    	editoptions:{maxlength:"50"}
   	    },
   	 	{name: "plainPassword2",index:'plainPassword2',width:0,hidden:true,sortable:false,search:false,
   	    	editable:true,edittype:'password',
   	    	formoptions:{ rowpos:2,colpos:2},
   	    	editrules:{edithidden:true,custom:true,
   	    		custom_func:function(value, name) {
   	    			return $.jgrid.checkPassword({value:value,name:name,password:'plainPassword'});   	    			
   	    		}
   	    	},
   	    	searchrules:{required:true}
   	    },
   		{name:'email',index:'email', width:210,editable:true,editrules:{required:false,email:true},
   	    	formoptions:{ rowpos:3,colpos:1},
   	    	editoptions:{maxlength:"300"}
   		},
   		{name:'status',index:'status',width:80,align:'center', 
   			editable: true,edittype:"select",editoptions:{value:"${allStatus}"},
   			formatter:'select',
   			formoptions:{ rowpos:3,colpos:2},
   			stype:'select',searchoptions:{sopt:['eq','ne'],value:"${allStatus}"}
   		},
   	 	{name: "roleList",index:'roleList',width:240,sortable:false,search:false,
   	    	editable:true,editrules:{edithidden:true},
   	    	edittype:"custom",
   	    	formoptions:{rowpos:4},
   	    	editoptions: {custom_element:function(value, options){
   	    		return '<select id="roleList" name="roleList" multiple="multiple" size="8"></select>';
   	    	},custom_value:function(value){
   	    		return value.val();
   	    	}}   	    	
   	    },   	    
   	 	{name: "permissionList",index:'permissionList',hidden:true,width:200,sortable:false,search:false,
   	    	editable:true,editrules:{edithidden:true},
   	    	edittype:"custom",
   	    	formoptions:{ rowpos:5,colpos:1},
   	    	editoptions: {custom_element:function(value, options){
   	    		return '<ul id="permissionList" class="ztree"></ul>';
   	    	},custom_value:function(value){
   	    		return value.val();
   	    	}}   	    	
   	    },
   	 	{name: "organizationList",index:'organizationList',width:200,sortable:false,search:false,
   	    	editable:true,editrules:{edithidden:true},
   	    	edittype:"custom",
   	    	formoptions:{ rowpos:5,colpos:2},
   	    	editoptions: {custom_element:function(value, options){
   	    		return '<ul id="organizationList" class="ztree"></ul>';
   	    	},custom_value:function(value){
   	    		return value.val();
   	    	}}   	    	
   	    }
   	],
  
   	pager: '#p${uuid}',
   	sortname: 'loginName',
    viewrecords: true,
    sortorder: "asc",
    rownumbers:true,  
	editurl: "${ctx}/security/user/edit",
	autowidth:true,
	hidegrid:false,//设置grid的隐藏/显示按钮是否可用
	//shrinkToFit: false,
	caption: "用户列表"	
});

var setting = {
	data: {
		simpleData: {
			enable: true,
			pIdKey:'parent'
		}
	}
};


var vh1 = $('#centerPane',window.parent.document).height() - $('.ui-tabs-nav',window.parent.document).height();
var user_editoptions = {
		modal:true,	
		reloadAfterSubmit:false,
		recreateForm:true,
		width:700,	
		dataheight:vh1-240,
		viewPagerButtons:false,
		beforeShowForm: function () {
            var $tr = $("#tr_roleList");			            
            $tr.children("td.CaptionTD:last").remove();
            $tr.children("td.DataTD:last").remove();			            
            $tr.children("td.DataTD").attr("colspan", "3");
        },	
		bottominfo:"(*) 必须填写",
		afterShowForm:function(formid){		
			var id = $('#id_g','#FrmGrid_${uuid}','#editmod${uuid}').val();
			id = (id == "_empty") ? "":id;
			
			$.fn.zTree.init($('#permissionList','#FrmGrid_${uuid}','#editmod${uuid}'), $.extend(setting,{check: {
				enable: true
				}},{async: {
					enable: true,
					dataType:"json",
					url:"${ctx}/security/user/selectPermission",
					otherParam:{"edit_id":id}
				},
				callback: {
				    onAsyncSuccess: function(){
				    	user_treeObj = $.fn.zTree.getZTreeObj("permissionList");				    	
				    }    
				}					
			})
			);
			//初始化组织机构数据	
			$.fn.zTree.init($('#organizationList','#FrmGrid_${uuid}','#editmod${uuid}'), $.extend(setting,{check: {
				enable: true
				}},{async: {
					enable: true,
					dataType:"json",
					url:"${ctx}/security/user/selectOrganization",
					otherParam:{"edit_id":id}
				},
				callback: {
				    onAsyncSuccess: function(){
				    	unit_treeObj = $.fn.zTree.getZTreeObj("organizationList");				    	
				    }    
				}					
			})
			);
			
			 
			
			$.ajax({
				url: '${ctx}/security/user/selectRole?edit_id='+id,
				type: "GET",
				dataType: "html",				
				complete : function (req, err) {
					if(parseInt(req.status,10) == 200){
						$('#roleList','#FrmGrid_${uuid}','#editmod${uuid}').append(req.responseText);
						$('#roleList','#FrmGrid_${uuid}','#editmod${uuid}').multiselect2side({minSize:9,moveOptions: false,labelsx: '待选区',labeldx: '已选区' });						
						$('#roleListms2side__sx','#FrmGrid_${uuid}','#editmod${uuid}').addClass('FormElement ui-widget-content ui-corner-all');
						$('select[id="roleListms2side__dx"]','#FrmGrid_${uuid}','#editmod${uuid}').addClass('FormElement ui-widget-content ui-corner-all');									
					}
				}
			});	
			
			
		},
		afterSubmit : function(response, postdata)
	   	{
	   		var res =$.parseJSON(response.responseText);
	   		return [res.success,res.message,res.userdata.id];
	   	},		
		afterComplete : function (response, postdata, formid) {
			var res =$.parseJSON(response.responseText);
			$("#${uuid}").jqGrid('setRowData',res.userdata.id,{roleList:res.userdata.roleList,permissionList:res.userdata.permissionList,organizationList:res.userdata.organizationList});	
			//if(res.userdata.organization){
			//	$("#${uuid}").jqGrid('setRowData',res.userdata.id,{roleList:res.userdata.roleList,permissionList:res.userdata.permissionList,organization:res.userdata.organization.name});	
			//}else{
			//	$("#${uuid}").jqGrid('setRowData',res.userdata.id,{roleList:res.userdata.roleList,permissionList:res.userdata.permissionList});	
			//}
		},
		beforeSubmit:function(postdata, formid){
			//var user_treeObj = $.fn.zTree.getZTreeObj("permissionList");
			var nodes = user_treeObj.getCheckedNodes(true);		
			postdata['permissionList'] = $.map( nodes, function(n,i){
				  return n.id;
			});
			var org = unit_treeObj.getCheckedNodes(true);	
			postdata['organizationList'] = $.map( org, function(n,i){
				  return n.id;
			});
			 
			return[true,''];// 提交
		}		
	};
var user_addoptions = $.extend({},user_editoptions);

var user_options = {
add:false,edit:false,del:false,
addfunc:function(){
	$.jgrid.xeditrules({gid:"${uuid}",colnames:['plainPassword','plainPassword2'],editrules:{required:true}});
	$("#${uuid}").jqGrid('editGridRow',"new",user_addoptions);
},
editfunc:function(rowid){
	$.jgrid.xeditrules({gid:"${uuid}",colnames:['plainPassword','plainPassword2'],editrules:{required:false}});
	$("#${uuid}").jqGrid('editGridRow',rowid,user_editoptions);
}};

//编辑 
<shirox:hasAnyPermissions name="admin,user:create,user:update">
$.extend(user_options,{add:true,edit:true});
</shirox:hasAnyPermissions>

//删除 
<shirox:hasAnyPermissions name="admin,user:delete">
$.extend(user_options,{del:true});
</shirox:hasAnyPermissions>

var positions = $.jgrid.findPositions(290,50);
jQuery("#${uuid}").jqGrid('navGrid','#p${uuid}',
	user_options, //options
	user_editoptions, // edit options
	user_addoptions, // add options
	{
		top:positions[1],
		left:positions[0],
		url:'${ctx}/security/user/delete',
		reloadAfterSubmit:false,
		afterSubmit : function(response, postdata)
	   	{
	   		var res =$.parseJSON(response.responseText);
	   		return [res.success,res.message];
	   	}	
	}, // del options
	{} // search options
);


jQuery("#${uuid}").jqGrid('navButtonAdd','#p${uuid}',{	
	caption:"导出",
	title:'导出数据',
	buttonicon : "ui-icon-star",
	 position : "last",
	onClickButton:function(){		
		jQuery("#${uuid}").jqGrid('excelExport',{url:'${ctx}/security/user/export/xls?jr_url=userreport.jasper'});						
	} 
});
		
//设置grid高度
$("#${uuid}").jqGrid('gridHeight');

$("#${uuid}").jqGrid('bindKeys');

});
</script>