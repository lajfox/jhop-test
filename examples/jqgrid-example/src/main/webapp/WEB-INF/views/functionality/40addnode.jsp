
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>


<table id="${uuid}"></table>
<div id="p${uuid}"></div>


<script  type="text/javascript"> 

jQuery().ready(function (){
	
jQuery("#${uuid}").jqGrid({
    url: "${ctx}/acc/search",
    datatype: "json", 
    colNames:["id","Account","Acc Num", "Debit", "Credit","Balance"],
   	colModel:[
   		{name:'id',index:'id', width:1,hidden:true,key:true},
   		{name:'name',index:'name', width:180, editable:true,editrules:{required:true}},
   		{name:'num',index:'num', width:80, sorttype:"int", editable:true,editrules:{required:true,integer:true,maxValue:999999,minValue:10000}},
   		{name:'debit',index:'debit', width:80, align:"right",sorttype:"double", editable:true,editrules:{required:true,number:true}},		
   		{name:'credit',index:'credit', width:80,align:"right",sorttype:"double", editable:true,editrules:{required:true,number:true}},		
   		{name:'balance',index:'balance', width:80,align:"right",sorttype:"double", editable:true,editrules:{required:true,number:true}}		
   	],
    treeGrid: true,
    treeGridModel: "adjacency",
	caption: "Add Tree node example",
    ExpandColumn: "name",
    autowidth: true,  
    rowNum: 200,   
    //loadonce: true,
	pager : "#p${uuid}",
	viewrecords: true,
	editurl:'${ctx}/acc/edit',
	sortname: 'orderno',
	sortorder: "asc"
	   
});

jQuery("#${uuid}").jqGrid('navGrid',"#p${uuid}",
		{}, //options
		{reloadAfterSubmit:false}, // edit options
		{
			reloadAfterSubmit:false,
			afterSubmit : function(response, postdata)
		   	{				
		   		var res =$.parseJSON(response.responseText);
		   		return [res.success,res.message,res.userdata.id];
		   	}				
		}, // add options
		{reloadAfterSubmit:false}, // del options
		{} // search options		
		);
		
//设置grid高度
$("#${uuid}").jqGrid('gridHeight');  

});
</script>
