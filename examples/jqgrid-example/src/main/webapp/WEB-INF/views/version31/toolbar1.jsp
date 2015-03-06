
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<table id="toolbar1" class="autowidth autoheight50"></table>
<div id="pgtoolbar1"></div>
<br/>
<table id="toolbar2" class="autowidth autoheight50"></table>
<div id="pgtoolbar2"></div>


<script  type="text/javascript">
jQuery().ready(function (){
	
$("#toolbar1").jqGrid({        
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
   	pager: '#pgtoolbar1',
   	sortname: 'invdate',
    viewrecords: true,
    sortorder: "desc",
    rownumbers:true,
	
	caption: "发票列表",
	toolbar: [true,"top"],
	autowidth:true
	
});

$("#toolbar1").jqGrid('navGrid','#pgtoolbar1',{edit:false,add:false,del:false});

$("#t_toolbar1").append("<input type='button' value='Click Me' style='height:20px;font-size:-3'/>");
$("input","#t_toolbar1").click(function(){
	alert("Hi! I'm added button at this toolbar");
});


$("#toolbar2").jqGrid({        
   	url:'${ctx}/invoice/search/userdata',
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
   	pager: '#pgtoolbar2',
   	sortname: 'invdate',
    viewrecords: true,
    sortorder: "desc",
    rownumbers:true,
	jsonReader: {
		root:"content",
		 page: "number",
	      total: "totalPages",
	      records: "totalElements",		
		repeatitems : false,
		id: "0"
	},
	caption: "发票列表",
	toolbar: [true,"bottom"],
	autowidth:true,
	loadComplete: function() {
		var udata = $("#toolbar2").jqGrid('getGridParam','userData');
		$("#t_toolbar2").css("text-align","right").html("总金额:"+udata.amount+" 税率: "+udata.tax+" 总计: "+udata.total+ "&nbsp;&nbsp;&nbsp;");
	}	
	
});

jQuery("#toolbar2").jqGrid('navGrid','#pgtoolbar2',{edit:false,add:false,del:false});


$("#toolbar1").jqGrid('gridHeight',{ogridid:"toolbar2",rate:0.5}); 
$("#toolbar2").jqGrid('gridHeight',{ogridid:"toolbar1",rate:0.5}); 

});
</script>
