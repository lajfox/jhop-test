
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div style="font-size:12px;">
Another very usefull addition in grid.jqueryui.js module is adding a possibility for sortable rows.<br>
In order to use this feature a jQuery UI sortable widget is used.<br/>
Try to sort the rows in the grid<br>
Note that all available options and evenents from sortable widget can be used <br>
<br />
<table id="sortrows"></table>
<div id="psortrows"></div>

<script  type="text/javascript">
jQuery().ready(function (){
	
$("#sortrows").jqGrid({        
   	url:'${ctx}/invoice/search',
	datatype: "json",
   	colNames:['id','单号','日期', '客户', '金额','税费','总计','备注'],
   	colModel:[
   	          {name: "id",width:1,hidden:true, key:true, sortable:false,search:false},
   		{name:'invno',index:'invno', width:55,sorttype:'int'},
   		{name:'invdate',index:'invdate', width:90, sorttype:'date',
   			searchoptions:{dataInit:function (elem) {$(elem).datepicker();}}},
   		{name:'name',index:'name', width:100},
   		{name:'amount',index:'amount', width:80, align:"right",sorttype:'double'},
   		{name:'tax',index:'tax', width:80, align:"right",sorttype:'double'},		
   		{name:'total',index:'total', width:80,align:"right",sorttype:'double'},		
   		{name:'note',index:'note', width:150, sortable:false,search:false}		
   	],
   	rowNum:10,
   	rowList:[10,20,30],
   	pager: '#psortrows',
   	sortname: 'invdate',
    viewrecords: true,
    sortorder: "desc",
    rownumbers:true,	
	caption: "发票列表",	
	deepempty : true
	
});

jQuery("#sortrows").jqGrid('navGrid','#psortrows',{edit:false,add:false,del:false}).jqGrid('sortableRows');

});
</script>
