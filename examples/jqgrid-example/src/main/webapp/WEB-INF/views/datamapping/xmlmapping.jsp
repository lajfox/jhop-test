
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<table id="${uuid}" class="autowidth autoheight"></table>
<div id="p${uuid}"></div>

<script type="text/javascript"> 
jQuery().ready(function (){
	
$("#${uuid}").jqGrid({
   	url:'${ctx}/book/search.xml',
	datatype: "xml",
   	colNames:['ID','作者','标题', '价格', '出版日期','ASIN'],
   	colModel:[
   		{name:'id',index:'id', width:75,hidden:true, key:true, sortable:false,search:false},
   		{name:'author',index:'author', width:90},
   		{name:'title',index:'title', width:100},
   		{name:'price',index:'price', width:80, align:"right",sorttype:'double'},
   		{name:'datepub',index:'datepub', width:80,
   			sorttype:'date',
   			searchoptions:{dataInit:function (elem) {$(elem).datepicker();}}},		
   		{name:'asin',index:'asin', width:80}   			
   	],
   	rowNum:10,
   	autowidth: true,
   	rowList:[10,20,30],
   	pager: '#p${uuid}',
   	sortname: 'datepub',
    viewrecords: true,
    sortorder: "desc",
    caption:"图书列表"
      
});

$("#${uuid}").jqGrid('navGrid','#p${uuid}',{edit:false,add:false,del:false});

//设置grid高度
$("#${uuid}").jqGrid('gridHeight');   

});
</script>
