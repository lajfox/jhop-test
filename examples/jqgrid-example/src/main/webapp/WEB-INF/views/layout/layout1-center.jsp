
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!-- 包含在layout中的grid，autowidth grid宽度跟随窗口大小变化 ,高度随layout的变化而变化-->
<table id="layout1centerlist" class="autowidth"></table>
<div id="playout1centerlist"></div>


<script  type="text/javascript"> 

jQuery().ready(function (){
	
jQuery("#layout1centerlist").jqGrid({
    url: "${ctx}/acc/search",
    datatype: "json", 
    colNames:["id","Account","Acc Num", "Debit", "Credit","Balance"],
   	colModel:[
   		{name:'id',index:'id', width:1,hidden:true,key:true},
   		{name:'name',index:'name', width:180, editable:true,editrules:{required:true}},
   		{name:'num',index:'num', width:80, sorttype:"int", editable:true,editrules:{required:true,integer:true,maxValue:999999,minValue:10000},formatter:'integer'},
   		{name:'debit',index:'debit', width:80, align:"right",sorttype:"double", editable:true,editrules:{required:true,number:true},formatter:'number'},		
   		{name:'credit',index:'credit', width:80,align:"right",sorttype:"double", editable:true,editrules:{required:true,number:true},formatter:'number'},		
   		{name:'balance',index:'balance', width:80,align:"right",sorttype:"double", editable:true,editrules:{required:true,number:true},formatter:'number'}		
   	],
    treeGrid: true,
    treeGridModel: "adjacency",
	caption: "增加树节点例子",
    ExpandColumn: "name",
    autowidth: true,  
    rowNum: 200,   
	
	pager : "#playout1centerlist",
	viewrecords: true,
	editurl:'${ctx}/acc/edit',
	sortname: 'id',
	sortorder: "asc"
	   
});

jQuery("#layout1centerlist").jqGrid('navGrid',"#playout1centerlist",
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
		
 

});
</script>
