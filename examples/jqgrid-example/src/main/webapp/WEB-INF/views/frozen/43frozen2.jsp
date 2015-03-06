
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div style="font-size:12px;">
	This example demonstartes the new Frozen Columns<br/>
	Try to scroll horizontally. As can be seen grouping header and rownumbers are supported too.<br/>
	Note that group header is suppored only when useColSpanStyle is false 
</div>
<br />
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
   		{name:'invno',index:'invno', width:55,sorttype:'int', frozen : true},   		
   		{name:'name',index:'name', width:100, frozen : true},
   		{name:'invdate',index:'invdate', width:190, jsonmap:"invdate",sorttype:'date',
   			searchoptions:{dataInit:function (elem) {$(elem).datepicker();}}
   		},   		
   		{name:'amount',index:'amount', width:180, align:"right",sorttype:'double',formatter:'number'},
   		{name:'tax',index:'tax', width:180, align:"right",sorttype:'double',formatter:'number'},		
   		{name:'total',index:'total', width:180,align:"right",sorttype:'double',formatter:'number'},	
  		{name:'closed',index:'closed',width:80,align:'center',
   			editable:true,edittype:"checkbox",editoptions:{value:":选择;Yes:No"},
   			stype:'select',searchoptions:{sopt:['eq','ne'],value:":所有;Yes:是;No:否"},
   			formatter:'checkbox'
   		},
		{name:'shipvia',index:'shipvia',width:100, 
   			editable: true,edittype:"select",editoptions:{value:":选择;FE:FedEx;TN:TNT"},   			
   			stype:'select',formatter:'select',searchoptions:{sopt:['eq','ne'],value:":所有;FE:FedEx;TN:TNT"}
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
	// autowidth:true,
	caption: "发票列表"
	
});

jQuery("#${uuid}").jqGrid('setGroupHeaders', {
	  useColSpanStyle: true, 
	  groupHeaders:[
		{startColumnName: 'invno', numberOfColumns: 2, titleText: 'Client Details'}
	  ]	
	});
	jQuery("#${uuid}").jqGrid('setFrozenColumns');
	
});

</script>
