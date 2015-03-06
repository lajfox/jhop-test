
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div style="font-size:12px;">
We can make a virtual scroll to a relative big local data - i.e scroll option set to 1 <br>
<br>

</div>
<br />
<table id="scroll37"></table>
<div id="pscroll37" ></div>

<script  type="text/javascript">
jQuery().ready(function (){
	
$("#scroll37").jqGrid({        
   	url:'${ctx}/invoice/search',
	datatype: "json",

   	colNames:['id','单号','日期', '客户', '金额','税费','总计','备注'],
   	colModel:[
   	          {name: "id",width:1,hidden:true, key:true, sortable:false,search:false},
   		{name:'invno',index:'invno', width:55,sorttype:'int'},
   		{name:'invdate',index:'invdate', width:90, sorttype:'date',
   			searchoptions:{dataInit:function (elem) {$(elem).datepicker();}}},
   		{name:'name',index:'name', width:100},
   		{name:'amount',index:'amount', width:80, align:"right",sorttype:'float'},
   		{name:'tax',index:'tax', width:80, align:"right",sorttype:'float'},		
   		{name:'total',index:'total', width:80,align:"right",sorttype:'float'},		
   		{name:'note',index:'note', width:150, sortable:false,search:false}		
   	],
   	rowNum:10,
   	rowList:[10,20,30],
   	pager: '#pscroll37',
   	sortname: 'invdate',
    viewrecords: true,
    sortorder: "desc",
    rownumbers:true,	
	caption: "发票列表",	
   	rowTotal: 2000,
   	gridview: true,	
   	scroll:1,
	loadonce:true
	
});

jQuery("#scroll37").jqGrid('navGrid','#pscroll37',{del:false,add:false,edit:false},{},{},{},{multipleSearch:true});

});
</script>
