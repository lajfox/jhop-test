
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div style="font-size:12px;">
    Another great feature in 3.6 version is supporting of Right To Left languages<br>
    This is done just with one option direction in jqGrid options. <br>
    Note that in a page one grid can have direction set to rtl, while another can have direction set to ltr.<br>
    Support of RTL is provided only in FireFox 3.x, Internet Explorer 7/8 and partially in Opera > 9.x browsers. <br>
    To enjoy the full of this feature we have created a demo page with all the examples <a href="http://www.trirand.com/jqgrid/rtl/jqgrid.html" target="_blank">here</a><br>
<br />
<table id="jqrtl"></table>
<div id="pjqrtl"></div>

<script  type="text/javascript">
jQuery().ready(function (){
	
$("#jqrtl").jqGrid({        
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
   	pager: '#pjqrtl',
   	sortname: 'invdate',
    viewrecords: true,
    sortorder: "desc",
    rownumbers:true,	
	caption: "发票列表",
	editurl: "${ctx}/invoice/edit",
	direction: "rtl",
	recordpos : "left"
	
});

jQuery("#jqrtl").jqGrid('navGrid','#pjqrtl',{position:"right"},{reloadAfterSubmit:false},{reloadAfterSubmit:false}); 

});
</script>
