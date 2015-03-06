
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<table id="${uuid}"></table>
<div id="p${uuid}"></div>
<br />
<a href="javascript:void(0)" id="g1" onclick="alert(jQuery('#${uuid}').jqGrid('getGridParam','url'));">Get url</a>
<br />
<a href="javascript:void(0)" id="g2" onclick="alert(jQuery('#${uuid}').jqGrid('getGridParam','sortname'));">Get Sort Name</a>
<br />
<a href="javascript:void(0)" id="g3" onclick="alert(jQuery('#${uuid}').jqGrid('getGridParam','sortorder'));">Get Sort Order</a>
<br />
<a href="javascript:void(0)" id="g4" onclick="alert(jQuery('#${uuid}').jqGrid('getGridParam','selrow'));">Get Selected Row</a>
<br />
<a href="javascript:void(0)" id="g5" onclick="alert(jQuery('#${uuid}').jqGrid('getGridParam','page'));">Get Current Page</a>
<br />
<a href="javascript:void(0)" id="g6" onclick="alert(jQuery('#${uuid}').jqGrid('getGridParam','rowNum'));">Get Number of Rows requested</a>
<br />
<a href="javascript:void(0)" id="g7" onclick="alert('See Multi select rows example');">Get Selected Rows</a>
<br />
<a href="javascript:void(0)" id="g8" onclick="alert(jQuery('#${uuid}').jqGrid('getGridParam','datatype'));">Get Data Type requested</a>
<br />
<a href="javascript:void(0)" id="g9" onclick="alert(jQuery('#${uuid}').jqGrid('getGridParam','records'));">Get number of records in Grid</a>


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
	
	onSortCol: function(name,index){ alert("Column Name: "+name+" Column Index: "+index);},
	ondblClickRow: function(id){ alert("You double click row with id: "+id);},
	caption: "发票列表"
	
});
$("#${uuid}").jqGrid('navGrid','#p${uuid}',{edit:false,add:false,del:false});

});
</script>
