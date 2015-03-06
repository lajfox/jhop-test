
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div style="font-size:12px;">
    From version 3.2 we can hide and show group of columns at once. <br/>
    These methods can accept array as argument as single string too. Try to resize the grid to see how <br/>
    these method works <br/>
</div>
<br />
<table id="hideshow"></table>
<div id="phideshow"></div>
<br />
<a href="javascript:void(0)" id="hcg">Hide column Amount and Tax</a><br/>
<a href="javascript:void(0)" id="scg">Show column Amount and Tax</a>


<script  type="text/javascript"> 

jQuery().ready(function (){
	

	
$("#hideshow").jqGrid({        
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
   	pager: '#phideshow',
   	sortname: 'invdate',
    viewrecords: true,
    sortorder: "desc",
    rownumbers:true,
	
	multiselect: false,
	caption: "发票列表"
	
});

jQuery("#hideshow").jqGrid('navGrid',"#phideshow",{edit:false,add:false,del:false});

jQuery("#hcg").click( function() {
	jQuery("#hideshow").jqGrid('hideCol',["amount","tax"]);
});
jQuery("#scg").click( function() {
	jQuery("#hideshow").jqGrid('showCol',["amount","tax"]);
});

});
</script>
