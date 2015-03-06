
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<table id="${uuid}"></table>
<div id="p${uuid}"></div>
<br />
<a href="javascript:void(0)" id="s1">Set new url</a>
<br />
<a href="javascript:void(0)" id="s2">Set Sort to amount column</a>
<br />
<a href="javascript:void(0)" id="s3" >Set Sort new Order</a>
<br />
<a href="javascript:void(0)" id="s4">Set to view second Page</a>
<br />
<a href="javascript:void(0)" id="s5">Set new Number of Rows(15)</a>
<br />
<a href="javascript:void(0)" id="s6" >Set Data Type from json to xml</a>
<br />
<a href="javascript:void(0)" id="s7" >Set new Caption</a>
<br />
<a href="javascript:void(0)" id="s8" >Sort by Client</a>

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
	
	caption: "发票列表"
	
});
jQuery("#${uuid}").jqGrid('navGrid','#p${uuid}',{edit:true,add:false,del:false,refresh:false});

jQuery("#s1").click( function() {
	jQuery("#${uuid}").jqGrid('setGridParam',{url:"${ctx}/invoice/search"}).trigger("reloadGrid");
});

jQuery("#s2").click( function() {
	jQuery("#${uuid}").jqGrid('setGridParam',{sortname:"amount",sortorder:"asc"}).trigger("reloadGrid");
});

jQuery("#s3").click( function() {
	var so = jQuery("#${uuid}").jqGrid('getGridParam','sortorder');
	so = so=="asc"?"desc":"asc";
	jQuery("#${uuid}").jqGrid('setGridParam',{sortorder:so}).trigger("reloadGrid");
});

jQuery("#s4").click( function() {
	jQuery("#${uuid}").jqGrid('setGridParam',{page:2}).trigger("reloadGrid");
});

jQuery("#s5").click( function() {
	jQuery("#${uuid}").jqGrid('setGridParam',{rowNum:15}).trigger("reloadGrid");
});

jQuery("#s6").click( function() {
	jQuery("#${uuid}").jqGrid('setGridParam',{url:"${ctx}/invoice/search.xml",datatype:"xml"}).trigger("reloadGrid");
});

jQuery("#s7").click( function() {
	jQuery("#${uuid}").jqGrid('setCaption',"New Caption");
});

jQuery("#s8").click( function() {
	jQuery("#${uuid}").jqGrid('sortGrid',"name",false);
});

});
</script>
