
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div style="font-size:12px;">
    In 3.6 we have done so that the developer can controll full the ajax requests to the server. For this purpose<br>
    we created a 3 level of settings <br/>
    1. common ajax settings for all modules that use ajax requests <br/>
    2. specific ajax settings for every module<br/>
    3. serialize function which can be used to serialize the data to the server in a way that the developer want<br/>
    In this example we set the grid to use a POST to the server and in the serialize function we set the things so<br>
    that only the first page should be returned<br>
<br />
<table id="jqgajax"></table>
<div id="pjqgajax"></div>

<script  type="text/javascript">
jQuery().ready(function (){
	
$("#jqgajax").jqGrid({
	ajaxGridOptions : {type:"POST"},
	serializeGridData : function(postdata) {
		postdata.page = 1;
		return postdata;
	},	
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
   	pager: '#pjqgajax',
   	sortname: 'invdate',
    viewrecords: true,
    sortorder: "desc",
    rownumbers:true,	
	caption: "发票列表"
	
	
});

jQuery("#jqgajax").jqGrid('navGrid','#pjqgajax',{add:false,edit:false,del:false});

});
</script>
