
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<table id="${uuid}"></table>
<div id="p${uuid}"></div>


<script  type="text/javascript"> 



function editRow(id) {
	jQuery("#${uuid}").jqGrid('editRow',id,true);	
}



jQuery().ready(function (){
	
$("#${uuid}").jqGrid({        
   	url:'${ctx}/invoice/search',
	datatype: "json",
	
   	colNames:['操作','Id','单号','日期', '客户', '金额','税费','总计','备注'],
   	colModel:[
		{name:'act',index:'act', width:150,align:"center",sortable:false,search:false},
   	    {name: "id",width:1,hidden:true, key:true, sortable:false,search:false},
   		{name:'invno',index:'invno', width:55,sorttype:'int', editable:true},
   		{name:'invdate',index:'invdate', width:90,
   			searchoptions:{dataInit:function (elem) {$(elem).datepicker();}}, 
   			editoptions:{dataInit:function (elem) {$(elem).datepicker();}},
   			editable:true, sorttype:'date'
   		},
   		{name:'name',index:'name', width:100, editable:true},
   		{name:'amount',index:'amount', width:80, align:"right",sorttype:'double', editable:true,formatter:'number'},
   		{name:'tax',index:'tax', width:80, align:"right",sorttype:'double', editable:true,formatter:'number'},		
   		{name:'total',index:'total', width:80,align:"right",sorttype:'double', editable:true,formatter:'number'},		
   		{name:'note',index:'note', width:150, sortable:false,search:false, editable:true}		
   	],
   	rowNum:10,
   	rowList:[10,20,30],
   	pager: '#p${uuid}',
   	sortname: 'invdate',
    viewrecords: true,
    sortorder: "desc",
    rownumbers:true,
	
	gridComplete: function(){
		var ids = jQuery("#${uuid}").jqGrid('getDataIDs');
		for(var i=0;i<ids.length;i++){
			var cl = ids[i];
			be = "<input style='height:22px;width:40px;' type='button' value='编辑' onclick=\"editRow('"+cl+"');\"  />"; 
			se = "<input style='height:22px;width:40px;' type='button' value='保存' onclick=\"jQuery('#${uuid}').jqGrid('saveRow','"+cl+"');\"  />"; 
			ce = "<input style='height:22px;width:40px;' type='button' value='取消' onclick=\"jQuery('#${uuid}').jqGrid('restoreRow','"+cl+"');\" />"; 
			jQuery("#${uuid}").jqGrid('setRowData',ids[i],{act:be+se+ce});
		}	
	},	
	editurl: "${ctx}/invoice/edit",
	caption: "发票列表"
	
});

jQuery("#${uuid}").jqGrid('navGrid',"#p${uuid}",{edit:false,add:false,del:false});



});
</script>
