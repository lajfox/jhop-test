
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div style="font-size:12px;">
    Thanks to Mark Williams we have another great feature - Column Reordering.<br>
    This is done again just with setting one options - sortable. Click on header column and try to move it to another location.<br>
    This feature depend on jQuery UI sortable widget.<br>
    Since we have other great stuffs which are depend on jQuery UI we have created a separated module<br>
    grid.jqueryui.js
<br />
<table id="colr"></table>
<div id="pcolr"></div>

<script  type="text/javascript">
jQuery().ready(function (){
	
$("#colr").jqGrid({        
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
   	pager: '#pcolr',
   	sortname: 'invdate',
    viewrecords: true,
    sortorder: "desc",
    rownumbers:true,	
	caption: "发票列表",	
	sortable: true
	
});

jQuery("#colr").jqGrid('navGrid','#pcolr',{add:false,edit:false,del:false});

});
</script>
