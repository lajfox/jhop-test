
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>


<table id="tabsonetab2" class="autowidth autoheight"></table>
<div id="ptabsonetab2"></div>


<script  type="text/javascript"> 

jQuery().ready(function (){
	
jQuery("#tabsonetab2").jqGrid({
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
	caption: "tabsonetab2 example",
    ExpandColumn: "name",
    autowidth: true,  
    rowNum: 200,  
    autowidth:true,
	pager : "#ptabsonetab2",
	viewrecords: true,
	sortname: 'id',
	sortorder: "asc"
    //treeIcons: {leaf:'ui-icon-document-b'},
	   
});

jQuery("#tabsonetab2").jqGrid('navGrid','#ptabsonetab2',{edit:false,add:false,del:false});


//设置grid高度
$("#tabsonetab2").jqGrid('gridHeight',{height:-5});  

});
</script>
