
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div style="font-size:12px;">
With this example we show the new very usefull feature of jqGrid - load all the needed data from server at once and then <br>
operate on it locally - i.e - pagging, sorting, searching and etc. <br><br>
For this purpose we introduce a new grid parameter - rowTotal, when set this paramater can instruct the server <br>
to load the total number of rows needed to work on.<br>
Note that rowNum determines the total records displayed in the grid, while rowTotal the total rows on wich we operate.<br>
When this parameter is set we send a aditional parameter to server named totalrows. You can check for this<br>
parameter and if it is available you can replace the rows parameter with this one.<br>
You can see how fast is the grid when loading 2000 recs.

</div>
<br />
<table id="list27"></table>
<div id="pager27" ></div>

<script  type="text/javascript">
jQuery().ready(function (){
	
$("#list27").jqGrid({        
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
   	pager: '#pager27',
   	sortname: 'invdate',
    viewrecords: true,
    sortorder: "desc",
    rownumbers:true,	
	caption: "发票列表",	
   	rowTotal: 2000,
   	gridview: true,	
	loadonce:true
	
});

jQuery("#list27").jqGrid('navGrid','#pager27',{add:false,edit:false,del:false});

});
</script>
