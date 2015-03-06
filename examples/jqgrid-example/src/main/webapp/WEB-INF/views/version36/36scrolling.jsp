
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<p style="font-size:12px;">
Thanks again to Mark Williams creating this wonderfull feature - loading data while scrolling <br>
Note how the data is loaded and the requests to the server!<br>
Enjoy the feature and speed!

<table id="scrolling"></table>
<div id="pscrolling"></div>

<script  type="text/javascript">
jQuery().ready(function (){
	
$("#scrolling").jqGrid({
	
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
   	rowNum:100,
   	scroll: 1,
   	pager: '#pscrolling',
   	sortname: 'invdate',
    viewrecords: true,
    sortorder: "desc",
    rownumbers: true,
	rownumWidth: 40,
	gridview: true,	
	caption: "发票列表"
	
	
});



});
</script>
