
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>


<table id="treegrid"></table>
<div id="ptreegrid"></div>


<script  type="text/javascript"> 

jQuery().ready(function (){
	
jQuery("#treegrid").jqGrid({
    url: "${ctx}/acc/search",
    datatype: "json",   
    //pager: false,
    //loadui: "disable",
    colNames:["id","Account","Acc Num", "Debit", "Credit","Balance"],
   	colModel:[
   		{name:'id',index:'id', width:1,hidden:true,key:true},
   		{name:'name',index:'name', width:180},
   		{name:'num',index:'num', width:80, align:"center",sorttype:"int"},
   		{name:'debit',index:'debit', width:80, align:"right",sorttype:"double"},		
   		{name:'credit',index:'credit', width:80,align:"right",sorttype:"double"},		
   		{name:'balance',index:'balance', width:80,align:"right",sorttype:"double"}		
   	],
    treeGrid: true,
    treeGridModel: "adjacency",
	caption: "Treegrid example",
    ExpandColumn: "name",
    autowidth: true,  
    rowNum: 200,   
	height:'auto',
	pager : "#ptreegrid",
	viewrecords: true,
	sortname: 'id',
	sortorder: "asc"
    //treeIcons: {leaf:'ui-icon-document-b'},
	   
});

jQuery("#treegrid").jqGrid('navGrid','#ptreegrid',{edit:false,add:false,del:false});


//设置grid高度
$("#treegrid").jqGrid('gridHeight');  

});
</script>
