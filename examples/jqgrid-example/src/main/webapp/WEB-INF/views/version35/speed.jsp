
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<p style="font-size:12px;">
This sample demonstrates the new rendering engine of jqGrid.<br>
In the versions before 3.5 beta jqGrid renders a big data sets slow. This is changed in 3.5 beta.<br>
By default jqGrid renders a data 3-5 time faster compared with previous releses, but there is a more.<br>
When we use the option griedview set to true it is possibe to speed the reading process from 5-10 times.<br>
This is achieved by reading the data at once. This mode has some limitations - it is not possible to use <br>
treeGrid, subGrid and afterInsertRow (event). Enjoy the speed!
<br />
<div id="speed_div"></div>
<br/>
<table id="speed"></table>
<div id="speedp"></div>


<script  type="text/javascript"> 
jQuery().ready(function (){

	
$("#speed").jqGrid({        
   	url:'${ctx}/invoice/search',
	datatype: "json",	
   	colNames:['id','单号','日期', '客户', '金额','税费','总计','备注'],
   	colModel:[
   	          {name: "id",width:1,hidden:true, key:true, sortable:false,search:false},
   		{name:'invno',index:'invno', width:55,sorttype:'int'},
   		{name:'invdate',index:'invdate', width:90 
   			,sorttype:'date',
   			searchoptions:{dataInit:function (elem) {$(elem).datepicker();}}},
   		{name:'name',index:'name', width:100},
   		{name:'amount',index:'amount', width:80, align:"right",sorttype:'number',formatter:'number'},
   		{name:'tax',index:'tax', width:80, align:"right",sorttype:'number',formatter:'number'},		
   		{name:'total',index:'total', width:80,align:"right",sorttype:'number',formatter:'number'},		
   		{name:'note',index:'note', width:150, sortable:false,search:false}		
   	],
   	rowNum:1000,
   	rowList:[200,300,500,1000,2000],
   	pager: '#speedp',
   	sortname: 'invdate',
    viewrecords: true,
    sortorder: "desc",
    rownumbers: true,
	rownumWidth: 40,
	height:300,
	gridview: true,	
	caption: "发票列表",
	gridComplete : function() {
		var tm = jQuery("#speed").jqGrid('getGridParam','totaltime');
		$("#speed_div").html("Render time: "+ tm+" ms ");
	}
		
	
});

});


</script>
