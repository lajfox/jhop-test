
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>


<div style="font-size:12px;">
	This example demonstartes the new Group Header feature.<br/>
	In this example : the colSpanStyle option is set to false
</div>
<br />
<table id="${uuid}"></table>
<div id="p${uuid}"></div>



<script  type="text/javascript"> 

jQuery().ready(function (){
	

	
$("#${uuid}").jqGrid({        
   	url:'${ctx}/invoice/search',
	datatype: "json",
	
   	colNames:['id','单号','日期', '客户', '金额','税费','总计','关闭','快递','备注'],
   	colModel:[
   	          {name: "id",width:1,hidden:true, key:true, sortable:false,search:false},
   		{name:'invno',index:'invno', width:55,sorttype:'int'},
   		{name:'invdate',index:'invdate', width:90, jsonmap:"invdate",sorttype:'date',
   			searchoptions:{dataInit:function (elem) {$(elem).datepicker();}}},
   		{name:'name',index:'name', width:100},
   		{name:'amount',index:'amount', width:80, align:"right",sorttype:'double',formatter:'number'},
   		{name:'tax',index:'tax', width:80, align:"right",sorttype:'double',formatter:'number'},		
   		{name:'total',index:'total', width:80,align:"right",sorttype:'double',formatter:'number'},	
  		{name:'closed',index:'closed',width:55,align:'center',
   			editable:true,edittype:"checkbox",editoptions:{value:":选择;Yes:No"},
   			stype:'select',searchoptions:{sopt:['eq','ne'],value:":所有;Yes:是;No:否"},
   			formatter:'checkbox'
   		},
		{name:'shipvia',index:'shipvia',width:70, 
   			editable: true,edittype:"select",editoptions:{value:":选择;FE:FedEx;TN:TNT"},   			
   			stype:'select',formatter:'select',searchoptions:{sopt:['eq','ne'],value:":所有;FE:FedEx;TN:TNT"}
   		},   		
   		{name:'note',index:'note', width:150, sortable:false,search:false}		
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
	 autowidth:true,
	caption: "发票列表"
	
});

jQuery("#${uuid}").jqGrid('setGroupHeaders', {
	  useColSpanStyle: false, 
	  groupHeaders:[
		{startColumnName: 'amount', numberOfColumns: 3, titleText: '<em>Price</em>'},
		{startColumnName: 'closed', numberOfColumns: 2, titleText: 'Shiping'}
	  ]	
	});
	
});

</script>
