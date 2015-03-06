
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<table id="person1"></table>
<div id="pperson1"></div>

<script  type="text/javascript"> 

jQuery().ready(function (){
	
	$("#person1").jqGrid({
    url: "${ctx}/person/search",
    datatype: "json", 
    colNames:["id","姓名","地址", "电话"],
   	colModel:[
   		{name:'id',index:'id', width:0,hidden:true,search:false},
   		{name:'name',index:'name', width:180},
   		{name:'address',index:'address', width:80},
   		{name:'phone',index:'phone', width:80, sorttype:"int"}   		
   	],
   
	//caption: "个人列表",   
    width: 475,  
    rowNum: 10,   
    rowList:[10,20,30],
    rownumbers:true,
	height:250,
	pager : "#pperson1",
	viewrecords: true,	
	sortname: 'name',
	sortorder: "asc",
	ondblClickRow :function(rowid,iRow,iCol,e){
		personSelrow(rowid);			
	}
	   
});

jQuery("#person1").jqGrid('navGrid','#pperson1',{edit:false,del:false,add:false});	
//jQuery("#person1").jqGrid('gridResize',{minWidth:300,maxWidth:800,minHeight:100, maxHeight:550});

});



</script>
