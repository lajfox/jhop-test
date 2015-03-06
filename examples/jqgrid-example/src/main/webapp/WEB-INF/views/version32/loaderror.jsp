
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div style="font-size:12px;">
    This example demonstartes how we can control the error from the server.In this case we <br/>
    point to not existing file on the server.
</div>
<b>Response:</b> <span id="rsperror" style="color:red"></span>
<br/>
<br/>
<table id="loaderr"></table>
<div id="ploaderr"></div>


<script  type="text/javascript"> 

jQuery().ready(function (){
	

	
$("#loaderr").jqGrid({        
   	url:'${ctx}/invoice/errorurl',
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
   	pager: '#ploaderr',
   	sortname: 'invdate',
    viewrecords: true,
    sortorder: "desc",
    rownumbers:true,
	
	multiselect: false,
	caption: "发票列表",
	loadError : function(xhr,st,err) {
    	jQuery("#rsperror").html("Type: "+st+"; Response: "+ xhr.status + " "+xhr.statusText);
    }	
	
});

jQuery("#loaderr").jqGrid('navGrid','#ploaderr',{edit:false,add:false,del:false});


});
</script>
