
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div style="font-size:12px;">
    This example demonstartes a setCell and setLabel methods. We can change the contents <br/>
    and style of the particular header and cell. Again with this we use a loadui option <br/>
    to disable interaction when the data is loaded
</div>
<br/>
<br/>
<table id="method32"></table>
<div id="pmethod32"></div>
<br />
<a href="javascript:void(0)" id="shc">Change the content of header Tax</a>
<br />
<a href="javascript:void(0)" id="scc">Cahnge the tax of row 12</a>
<br />
<a href="javascript:void(0)" id="cdat">Clear the currently loaded data</a>
<br />



<script  type="text/javascript"> 

jQuery().ready(function (){
	

	
$("#method32").jqGrid({        
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
   	pager: '#pmethod32',
   	sortname: 'invdate',
    viewrecords: true,
    sortorder: "desc",
    rownumbers:true,
	
	multiselect: true,
	caption: "发票列表",
	loadui: "block"
	
});

jQuery("#method32").jqGrid('navGrid','#pmethod32',{edit:false,add:false,del:false});

jQuery("#shc").click( function() {
	$("#method32").jqGrid('setLabel',"tax","Tax Amt",{'font-weight': 'bold','font-style': 'italic'});
});

jQuery("#scc").click( function() {
	$("#method32").jqGrid('setCell',"ff8080813beaee71013beaf10c8b0001","tax","",{'font-weight': 'bold',color: 'red','text-align':'center'});
});
jQuery("#cdat").click( function() {
	$("#method32").jqGrid('clearGridData');
});


});
</script>
