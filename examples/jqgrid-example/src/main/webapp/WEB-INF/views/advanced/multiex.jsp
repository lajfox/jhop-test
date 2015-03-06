
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<table id="${uuid}"></table>
<div id="p${uuid}"></div>
<br />
<a href="javascript:void(0)" id="m1">Get Selected id's</a><br/>
<a href="javascript:void(0)" id="m1s">Select(Unselect) row 5</a>

<script  type="text/javascript"> 

jQuery().ready(function (){
	
$("#${uuid}").jqGrid({        
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
   	pager: '#p${uuid}',
   	sortname: 'invdate',
    viewrecords: true,
    sortorder: "desc",
    rownumbers:true,	
	recordpos: 'left',
	multiselect: true,
	caption: "发票列表"
	
});

jQuery("#${uuid}").jqGrid('navGrid','#p${uuid}',{add:false,del:false,edit:false,position:'right'});
jQuery("#m1").click( function() {
	var s;
	s = jQuery("#${uuid}").jqGrid('getGridParam','selarrrow');
	alert(s);
});
jQuery("#m1s").click( function() {
	jQuery("#${uuid}").jqGrid('setSelection',"5");
});

});
</script>
