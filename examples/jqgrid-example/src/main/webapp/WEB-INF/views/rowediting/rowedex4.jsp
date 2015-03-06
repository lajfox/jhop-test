
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<table id="${uuid}"></table>
<div id="p${uuid}"></div>
<br />
<input type="BUTTON" id="ed4" value="Edit row 4" />
<input type="BUTTON" id="sved4" disabled='true' value="Save row 4" />


<script  type="text/javascript"> 

jQuery().ready(function (){
	
$("#${uuid}").jqGrid({        
   	url:'${ctx}/invoice/search',
	datatype: "json",
	
   	colNames:['Id','单号','日期', '客户', '金额','税费','总计','备注'],
   	colModel:[
   	          {name: "id",width:50,hidden:false, key:true, sortable:false,search:false},
   		{name:'invno',index:'invno', width:55,sorttype:'int', editable:true},
   		{name:'invdate',index:'invdate', width:90, jsonmap:"invdate",sorttype:'date',
   			searchoptions:{dataInit:function (elem) {$(elem).datepicker();}}, 
   			editoptions:{dataInit:function (elem) {$(elem).datepicker();}},
   			editable:true
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
	
	editurl: "${ctx}/invoice/edit",
	caption: "发票列表"
	
});

jQuery("#ed4").click( function() {
	jQuery("#${uuid}").jqGrid('editRow',"4",true);
	this.disabled = 'true';
	jQuery("#sved4").attr("disabled",false);
});
jQuery("#sved4").click( function() {
	jQuery("#${uuid}").jqGrid('saveRow',"4", checksave);
	jQuery("#sved4").attr("disabled",true);
	jQuery("#ed4").attr("disabled",false);
});

function checksave(result) {
	var $success = $.parseJSON(result.responseText).success;
	if (!$success) {
		alert("更新失败!");		
	}
	return $success;
}



});
</script>
