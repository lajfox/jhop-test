
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<table id="${uuid}"></table>
<div id="p${uuid}"></div>


<script  type="text/javascript">
jQuery().ready(function (){
	
$("#${uuid}").jqGrid({        
   	url:'${ctx}/invoice/search',
	datatype: "json",

   	colNames:['id','单号','日期', '客户', '金额','税费','总计','备注'],
   	colModel:[
   	          {name: "id",width:1,hidden:true, key:true, sortable:false,search:false},
   		{name:'invno',index:'invno', width:55,sorttype:'int',resizable:false},
   		{name:'invdate',index:'invdate', width:90, sorttype:'date',
   			searchoptions:{dataInit:function (elem) {$(elem).datepicker();}},resizable:false},
   		{name:'name',index:'name', width:100,resizable:false},
   		{name:'amount',index:'amount', width:80, align:"right",sorttype:'double',resizable:false},
   		{name:'tax',index:'tax', width:80, align:"right",sorttype:'double',resizable:false},		
   		{name:'total',index:'total', width:80,align:"right",sorttype:'double',resizable:false},		
   		{name:'note',index:'note', width:150, sortable:false,search:false,resizable:false}		
   	],
   	rowNum:10,
   	rowList:[10,20,30],
   	pager: '#p${uuid}',
   	sortname: 'invdate',
    viewrecords: true,
    sortorder: "desc",
    rownumbers:true,
	
	caption: "发票列表",
	autowidth:true
	
});
$("#${uuid}").jqGrid('navGrid','#p${uuid}',{edit:false,add:false,del:false});

//设置grid高度
$("#${uuid}").jqGrid('gridHeight');  

});
</script>
