
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>


<table id="${uuid}"></table>
<div id="p${uuid}"></div>


<script  type="text/javascript"> 

jQuery().ready(function (){
	

	
$("#${uuid}").jqGrid({        
   	url:'${ctx}/invoice/search',
	datatype: "json",
	
   	colNames:['id','单号','客户','日期',  '金额','税费','总计','关闭','快递','备注'],
   	colModel:[
   	          {name: "id",width:1,hidden:true, key:true, sortable:false,search:false, frozen : true},
   		{name:'invno',index:'invno', width:155,sorttype:'int', frozen : true},   		
   		{name:'name',index:'name', width:200, frozen : true},
   		{name:'invdate',index:'invdate', width:290, jsonmap:"invdate",sorttype:'date',
   			searchoptions:{dataInit:function (elem) {$(elem).datepicker();}}
   		},   		
   		{name:'amount',index:'amount', width:280, align:"right",sorttype:'double',formatter:'number'},
   		{name:'tax',index:'tax', width:180, align:"right",sorttype:'double',formatter:'number'},		
   		{name:'total',index:'total', width:180,align:"right",sorttype:'double',formatter:'number'},	
  		{name:'closed',index:'closed',width:180,align:'center',
   			editable:true,edittype:"checkbox",editoptions:{value:":选择;Yes:No"},
   			stype:'select',searchoptions:{sopt:['eq','ne'],value:":所有;Yes:是;No:否"},
   			formatter:'checkbox'
   		},
		{name:'shipvia',index:'shipvia',width:100, 
   			editable: true,edittype:"select",editoptions:{value:":选择;FE:FedEx;TN:TNT"},
   			formatter:'select',
   			stype:'select',searchoptions:{sopt:['eq','ne'],value:":所有;FE:FedEx;TN:TNT"}
   		},   		
   		{name:'note',index:'note', width:250, sortable:false,search:false}		
   	],
   	rowNum:10,
   	rowList:[10,20,30],
   	pager: '#p${uuid}',
   	sortname: 'invdate',
    viewrecords: true,
    sortorder: "desc",
    rownumbers:true,
	height:'auto',
	multiselect: true,
	width:700,
	shrinkToFit: false,
	autowidth:true,
	caption: "发票列表"
	
});

jQuery("#${uuid}").jqGrid('setFrozenColumns');

//设置grid高度
$("#${uuid}").jqGrid('gridHeight'); 

});

</script>
