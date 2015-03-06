
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div style="font-size:12px;">
This example shows the tree grid feature of jqGrid with supporting of Adjacency List Model
All you need is to set treeGrid option to true, and to specify which column is expandable.
Of course the data from the server should be ajusted in appropriate way.
</div>
<br />
<table id="treegrid2"></table>
<div id="ptreegrid2"></div>


<script  type="text/javascript"> 

jQuery().ready(function (){
	
jQuery("#treegrid2").jqGrid({
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
	pager : "#ptreegrid2",
	viewrecords: true,
	sortname: 'id',
	sortorder: "asc",
    treeIcons: {leaf:'ui-icon-document-b'}
	   
});

jQuery("#treegrid2").jqGrid('navGrid','#ptreegrid2',{edit:false,add:false,del:false});

//设置grid高度
$("#treegrid2").jqGrid('gridHeight',{height:-30});  

});
</script>
