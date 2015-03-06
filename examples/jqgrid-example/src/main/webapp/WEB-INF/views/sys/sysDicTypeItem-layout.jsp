<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/common/taglibs.jsp"%>

<!-- autolayoutwidth autolayoutheight layout高度、宽度跟随窗口大小变化 -->	
<div id="sysDic" class="no-padding autolayoutwidth autolayoutheight">
	<div id="sysDic-north" class="ui-layout-north ui-widget-content ui-corner-all no-scrollbar no-padding"> 
		<table id="sysDicTypeGridNorth">
		</table>
		<div id="sysDicTypePagerNorth"></div>
	 </div> 

	<div id="sysDic-center" class="ui-layout-center ui-widget-content ui-corner-all no-scrollbar no-padding"> 
		<table id="sysDicItemGridCenter"></table>
		<div id="sysDicItemPagerCenter"></div>
	</div> 
</div>

<script  type="text/javascript">
function centerHeight(){
	var vh_north =  $('.ui-state-default-north','#sysDic').height() || 0;		
	var vh = $('#sysDic-center').height() 
    - $.jgrid.getGridPagerHeight('sysDicItemGridCenter')
	-$.jgrid.getGridTitleBarHeight('sysDicItemGridCenter') 
	- $.jgrid.getGridHdivHeight('sysDicItemGridCenter')
	- vh_north *2-5;
	return vh;
}	

jQuery().ready(function (){
	
	//在主对象执行完成时，初始化子对象列表 
	function initSubGrid(){	
		jQuery("#sysDicItemGridCenter").jqGrid({
// 		   	url:'${ctx}/zd/sysdicitem/searchTree',
		   	url:'${ctx}/zd/sysdicitem/search',
			datatype: "json",
			mtype:'POST',
			shrinkToFit:false,
		   	colNames:['Id','名称','标识','是否有效', '父节点名称','显示顺序'],
		   	colModel:[
		   	    {name: "id",width:1,hidden:true, key:true, sortable:false,search:false},
		   	 	{name:'itemname',index:'itemname', width:240, sorttype:'string',editable:true,
		   	    	editrules:{required:true,custom:true,
		   	    		custom_func:function(value, name) {
		   	    			var typeid = $("#sysDicTypeGridNorth").jqGrid('getGridParam','selrow');
		   	    			var mainid = $("#sysDicItemGridCenter").jqGrid('getGridParam','selrow');
		   	    			return $.jgrid.checkexists({gid:'sysDicItemGridCenter',value:value,name:name},
		   	    					{url:'${ctx}/zd/sysdicitem/checkExists/itemname?typeid='+typeid+"&mainid="+mainid});   	    			
		   	    		}
		   	    	},
		   	    	editoptions:{maxlength:"100"},
		   	    	formoptions:{ rowpos:1, elmsuffix:"(*)"}},
		   		{name:'ename',index:'ename', width:240, sorttype:'string',editable:true,
		   	    		editrules:{required:true,custom:true,
			   	    		custom_func:function(value, name) {
			   	    			var typeid = $("#sysDicTypeGridNorth").jqGrid('getGridParam','selrow');
			   	    			var mainid = $("#sysDicItemGridCenter").jqGrid('getGridParam','selrow');
			   	    			return $.jgrid.checkexists({gid:'sysDicItemGridCenter',value:value,name:name},
			   	    					{url:'${ctx}/zd/sysdicitem/checkExists/ename?typeid='+typeid+"&mainid="+mainid});   	    			
			   	    		}
			   	    	},
			   	    	editoptions:{maxlength:"40"},
		   	    		formoptions:{ rowpos:2, elmsuffix:"(*)"}},
		   		{name:'valid',index:'valid', width:80, sorttype:'boolean',formatter:sysValidformatter,
		   			editable:true,edittype:"checkbox",
		   			editoptions:{value:"1:0",defaultValue:"1"},
		   			formatter:'checkbox',
		   			stype:'select',searchoptions:{sopt:['eq','ne'],value:"1:有效;0:无效"}
		   		},
		   		{name:'parentx',index:'parent.id',hidden:true,width:0,sortable:false,search:false,
		   			editable:true,edittype:"custom",editrules:{edithidden:true},
		   	   		editoptions: {custom_element:function(value, options){
		   	    		return '<select></select>';
		   	    	},custom_value:function(value){   	    		
		   	    		return value.val();
		   	    	}}, 
		   	   		stype:'select',searchoptions:{sopt:['eq','ne'],dataUrl:'${ctx}/zd/sysdicitem/selectItemParent'}
		   	   		
		   	   	},
		   		{name:'orderno',index:'orderno', width:120,align:"right",sorttype:'double',
		   			editable:true,editrules:{number:true,maxValue:9999.99,minValue:0}
		   		}
		   	   
		   	],
		   	pager: '#sysDicItemPagerCenter',
		   	sortname: 'orderno',
		    viewrecords: true,
		    sortorder: "asc",
			multiselect: false,
	 		rownumbers:true,//行号
	 		rowNum:10,
// 			treeGrid: true,
// 		    treeGridModel: "adjacency",
// 		    ExpandColumn: "itemname",
//	 		jsonReader: {
//	 			root:"content",
//	 			page: "number",
//	 		    total: "totalPages",
//	 		    records: "totalElements",		
//	 			repeatitems : false,
//	 			id: "0"
//	 		},	
			caption:"字典项列表",
			autowidth:true,
			editurl: "${ctx}/zd/sysdicitem/edit",
			beforeRequest:function(){		
				var rowid = $("#sysDicTypeGridNorth").jqGrid('getGridParam','selrow');	
				var ret = $("#sysDicTypeGridNorth").jqGrid('getRowData',rowid);
				$("#sysDicItemGridCenter").jqGrid('setGridParam',{postData:{"sDs_systemDicType.id":rowid,"parentid":rowid},editurl:"${ctx}/zd/sysdicitem/edit?parentid="+rowid+"&sign="+ret.sign});
				return true;
			}		
		});
		
		var sysDicItem_editoptins = {
				recreateForm:true,
				reloadAfterSubmit:false,
				bottominfo:"(*) 必须填写",
				afterShowForm:function(formid){
					var id = $('#id_g','#FrmGrid_sysDicItemGridCenter','#editmodsysDicItemGridCenter').val();
					id = (id == "_empty") ? "":id;	
					var pid ;
					if(id){
						var ret = $('#sysDicItemGridCenter').jqGrid('getRowData',id);				
						pid = ret.parent;
					}else{
						pid = $('#sysDicItemGridCenter').jqGrid("getGridParam",'selrow');
					}
					var typeid = $("#sysDicTypeGridNorth").jqGrid('getGridParam','selrow');
					
					$.ajax({
						url: '${ctx}/zd/sysdicitem/selectItemParent?edit_id='+id+'&_parentid='+pid+'&typeid='+typeid,
						type: "GET",
						dataType: "html",				
						complete : function (req, err) {
							if(parseInt(req.status,10) == 200){
								var $select = $('#parentx','#FrmGrid_sysDicItemGridCenter','#editmodsysDicItemGridCenter');
								$select.append(req.responseText);
								$select.change(function(){								
									$('#parent','#FrmGrid_sysDicItemGridCenter','#editmodsysDicItemGridCenter').val($select.val());
								});															
							}
						}
					});			
				},
				afterComplete : function (response, postdata, formid) {
					var res =$.parseJSON(response.responseText);			
					if(res.success){
						$("#sysDicItemGridCenter").jqGrid('setRowData',res.userdata.id,res.userdata);
					}			
				},
				afterSubmit : function(response, postdata)
			   	{
			   		var res =$.parseJSON(response.responseText);
			   		return [res.success,res.message,res.userdata.id];
			   	}
			};
			
		var sysDicItem_addoptins = $.extend({},sysDicItem_editoptins);

// 		jQuery("#sysDicItemGridCenter").jqGrid('navGrid','#sysDicItemPagerCenter',
// 				{view:true,
// 					addfunc:function(){
// 						$.jgrid.setDataUrl({gid:"sysDicItemGridCenter",colnames:['parentx']});
// 						$("#sysDicItemGridCenter").jqGrid('editGridRow',"new",sysDicItem_addoptins);
// 					},
// 					editfunc:function(rowid){
// 						$.jgrid.setDataUrl({gid:"sysDicItemGridCenter",colnames:['parentx'],rowid:rowid});
// 						$("#sysDicItemGridCenter").jqGrid('editGridRow',rowid,sysDicItem_editoptins);
// 					}
// 				}, //options
// 				sysDicItem_addoptins,// edit options
// 				sysDicItem_addoptins, // add options
// 				{reloadAfterSubmit:false}, // del options
// 				{} // search options
// 		);
		
		//列表直接编辑
	 	jQuery("#sysDicItemGridCenter").jqGrid('navGrid',"#sysDicItemPagerCenter",{edit:false,add:false,del:false,search:false,refresh:false});
	    

		jQuery("#sysDicItemGridCenter").jqGrid('inlineNav',"#sysDicItemPagerCenter",{
	 	    editParams:{
	 	    	key: true,
	 	        aftersavefunc: function( rowid, response ){
	 	        	$("#sysDicItemGridCenter").trigger("reloadGrid");//后台加载数刷新
	 	        	
	 	            //不重新加载数据更新其主键:id
	 	            var $response = $.parseJSON(response.responseText);
	 	            //$("#sysDicItemGridCenter").jqGrid('setRowData',rowid,{id:$response.userdata.id});
	 	            $("#sysDicItemGridCenter").jqGrid('setRowData',$response.userdata.id,$response.userdata);
	 	            return true;
	 	        }
	 	    }
	 	 });
	 	
	 	
	 	jQuery("#sysDicItemGridCenter").jqGrid('navButtonAdd',"#sysDicItemPagerCenter",{
	 		caption : "删除",			
			buttonicon : 'ui-icon-trash',			
			position : "last",
			cursor : 'pointer',
			onClickButton:function(){
				var rowid = $("#sysDicItemGridCenter").jqGrid('getGridParam','selrow');
				if(rowid){	
					$('#sysDicItemGridCenter').jqGrid('delGridRow',rowid,{});
				} else {				
					$.jgrid.info_dialog2('注意',"请选择记录");	
				}			
			} 
		});	
	 	jQuery("#sysDicItemGridCenter").jqGrid('navSeparatorAdd',"#sysDicItemPagerCenter",{
	 		sepclass : "ui-separator",
			sepcontent: '',
            position : "last"
		});	
	 	jQuery("#sysDicItemGridCenter").jqGrid('navButtonAdd',"#sysDicItemPagerCenter",{
	 		caption : "查找",			
			buttonicon : 'ui-icon-search',			
			position : "last",
			cursor : 'pointer',
			onClickButton:function(){
				$('#sysDicItemGridCenter').jqGrid('searchGrid',{});	
			} 
		});	
	 	jQuery("#sysDicItemGridCenter").jqGrid('navButtonAdd',"#sysDicItemPagerCenter",{
	 		caption : "刷新",			
			buttonicon : 'ui-icon-refresh',	
			position : "last",
			cursor : 'pointer',
			onClickButton:function(){
				$('#sysDicItemGridCenter').trigger('reloadGrid');	
			} 
		});	
	 	
	 	//设置grid高度
		$("#sysDicItemGridCenter").jqGrid('setGridHeight',centerHeight()); 
	 	
		$("#sysDicItemGridCenter_iledit").click(function(){
	    	var rowid = $("#sysDicItemGridCenter").jqGrid('getGridParam','selrow');
			if(!rowid){
    			$.jgrid.info_dialog2('提示',"请选择要编辑的记录");
			}
        });
	}
	
	
	var data = $.parseJSON('${sysValidJson}');
	function sysValidformatter(cellvalue){
		if(cellvalue){
			return data[cellvalue];
		}else{
			return '';
		}
	}
	
	function northHeight(){
		var vh_north =  $('.ui-state-default-north','#sysDic').height() || 0;
		var vh = $('#sysDic-north').height() 
		        - $.jgrid.getGridPagerHeight('sysDicTypeGridNorth')
				-$.jgrid.getGridTitleBarHeight('sysDicTypeGridNorth') 
				- $.jgrid.getGridHdivHeight('sysDicTypeGridNorth')
				- vh_north-5;	
		return vh;
	}

	var sysDic;
	var	sysDic_settings = {
			resizerClass: 'ui-state-default',					
			north__size:		'50%' ,					
			center__size:		'50%' ,
			north__onresize: function (pane, $pane, state, options) { 
				$("#sysDicTypeGridNorth").jqGrid('setGridWidth',$pane.innerWidth()-2);
				$("#sysDicTypeGridNorth").jqGrid('setGridHeight',northHeight()); 
			},
			center__onresize: function (pane, $pane, state, options) { 
				$("#sysDicItemGridCenter").jqGrid('setGridWidth',$pane.innerWidth()-2);
				$("#sysDicItemGridCenter").jqGrid('setGridHeight',centerHeight()); 
			}			
	}; 
	
	$("#sysDic").height($('#centerPane',window.parent.document).height()- $('.ui-tabs-nav',window.parent.document).height());
	sysDic = $("#sysDic").layout( sysDic_settings );	
	sysDic.resizeAll(); 
	
	
	$("#sysDicTypeGridNorth").jqGrid({
	   	url:'${ctx}/zd/sysdictype/search',
		datatype: "json",
		mtype:'POST',
		shrinkToFit:false,
	   	colNames:['id','字典名称','字典标识'],
	   	colModel:[
	   	    {name: "id",width:1,hidden:true, key:true, sortable:false,search:false},
	   		{name:'typename',index:'typename', width:120, sorttype:'string',editable:true,
	   	    	editrules:{required:true,custom:true,
	   	    		custom_func:function(value, name) {
	   	    			return $.jgrid.checkexists({gid:'sysDicTypeGridNorth',value:value,name:name},
	   	    					{url:'${ctx}/public/checkExists/com.techstar.sys.entity.SystemDicType/typename'});   	    			
	   	    		}
	   	    	},
	   	    	editoptions:{maxlength:"100"},
	   	    	formoptions:{ rowpos:1, elmsuffix:"(*)"}},
	   		{name:'sign',index:'sign', width:140, sorttype:'string',editable:true,
   	    		editrules:{required:true,custom:true,
	   	    		custom_func:function(value, name) {
	   	    			return $.jgrid.checkexists({gid:'sysDicTypeGridNorth',value:value,name:name},
	   	    					{url:'${ctx}/public/checkExists/com.techstar.sys.entity.SystemDicType/sign'});   	    			
	   	    		}
	   	    	},
	   	    	editoptions:{maxlength:"100"},
   	    		formoptions:{ rowpos:2, elmsuffix:"(*)"}}
	   	],
	   	rowNum:10,
	   	rowList:[10,20,30],
	   	pager: '#sysDicTypePagerNorth',
	   	sortname: 'typename',
	    viewrecords: true,
	    sortorder: "asc",
	    rownumbers:true,//行号
	    editurl: "${ctx}/zd/sysdictype/edit",
		caption: "字典类型列表",
		gridComplete:function(){
            if(jQuery("#sysDicTypeGridNorth").jqGrid('getDataIDs').length>0){
            	var ids = jQuery("#sysDicTypeGridNorth").jqGrid('getDataIDs')[0];
            	jQuery("#sysDicTypeGridNorth").jqGrid('setSelection',ids);//默认选中第一行          	
            }
        },
		onSelectRow: function(ids) {
			if($('#gview_sysDicItemGridCenter')[0]){
				jQuery("#sysDicItemGridCenter").trigger('reloadGrid');
			}else{
				initSubGrid();
			}
		},
		autowidth:true //Grid宽度根据窗口大小自动伸缩
	});
	var systemDicType_editoptions = $.extend({},
			{jqModal:true,
			savekey: [true,13], 
			navkeys: [true,38,40],
			modal:true,	
			reloadAfterSubmit:false, 
			recreateForm:true,
			afterComplete : function (response, postdata, formid) {
				var res =$.parseJSON(response.responseText);			
				if(res.success){
					$("#sysDicTypeGridNorth").jqGrid('setRowData',res.userdata.id,res.userdata);
				}			
			},
			afterSubmit : function(response, postdata)
		   	{
		   		var res =$.parseJSON(response.responseText);
		   		return [res.success,res.message,res.userdata.id];
		   	},
			bottominfo:"标识 (*)是必填项"}	
	);
	
	var systemDicType_addoptions =$.extend({},systemDicType_editoptions);
	var systemDicType_options = {add:false,edit:false,del:false};
	//编辑 
	<shirox:hasAnyPermissions name="admin,systemDicType:create,systemDicType:update">
	$.extend(systemDicType_options,{add:true,edit:true});
	</shirox:hasAnyPermissions>

	//删除 
	<shirox:hasAnyPermissions name="admin,systemDicType:delete">
	$.extend(systemDicType_options,{del:true});
	</shirox:hasAnyPermissions>
	
	jQuery("#sysDicTypeGridNorth").jqGrid('navGrid','#sysDicTypePagerNorth', 
			systemDicType_options,//options 
			systemDicType_editoptions, // edit options 
			systemDicType_addoptions, // add options 
			{reloadAfterSubmit:false}, // del options 
			{closeOnEscape:true}, // search options 
			{navkeys: [true,38,40], height:250,jqModal:false,closeOnEscape:true} // view options 
	);

	//设置grid高度		
	$("#sysDicTypeGridNorth").jqGrid('setGridHeight',northHeight());  

});
</script>