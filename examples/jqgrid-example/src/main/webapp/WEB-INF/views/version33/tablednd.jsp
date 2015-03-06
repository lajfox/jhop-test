
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div>
    This example show how we can use The Table Drag and Drop plugin provided from Denis Howlett <br>
    Try to drag a row whitin table.
</div>
<br />
<table id="listdnd"></table>
<div id="pagerdnd"></div>


<script  type="text/javascript"> 

jQuery().ready(function (){

jQuery("#listdnd").tableDnD({scrollAmount:0});
$("#listdnd").jqGrid({        
   	url:'${ctx}/invoice/search',
	datatype: "json",
	
   	colNames:['id','单号','日期', '客户', '金额','税费','总计','备注'],
   	colModel:[
   	          {name: "id",width:1,hidden:true, key:true, sortable:false,search:false},
   		{name:'invno',index:'invno', width:55,sorttype:'int'},
   		{name:'invdate',index:'invdate', width:90, jsonmap:"invdate",sorttype:'date',
   			searchoptions:{dataInit:function (elem) {$(elem).datepicker();}}},
   		{name:'name',index:'name', width:100},
   		{name:'amount',index:'amount', width:80, align:"right",sorttype:'double'},
   		{name:'tax',index:'tax', width:80, align:"right",sorttype:'double'},		
   		{name:'total',index:'total', width:80,align:"right",sorttype:'double'},		
   		{name:'note',index:'note', width:150, sortable:false,search:false}		
   	],
   	rowNum:10,
   	rowList:[10,20,30],
   	pager: '#pagerdnd',
   	sortname: 'invdate',
    viewrecords: true,
    sortorder: "desc",
    rownumbers:true,
	
	multiselect: false,
	gridComplete: function() {
    	$("#_empty","#listdnd").addClass("nodrag nodrop");
    	jQuery("#listdnd").tableDnDUpdate();
    },  	
	caption: "发票列表"
	
});

jQuery("#listdnd").jqGrid('navGrid','#pagerdnd',{edit:false,add:false,del:false});



});
</script>
