
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div style="font-size:12px;">
    This example show the custom formatter options per field. Options are set in column model <br/>
    <br/>    
</div>
<br />
<table id="cfrmgrid"></table>
<div id="pcfrmgrid"></div>

<script  type="text/javascript"> 

jQuery().ready(function (){
	
	

$("#cfrmgrid").jqGrid({        
   	url:'${ctx}/invoice/search',
	datatype: "json",
	
   	colNames:['id','单号','日期', '客户', '金额','税费','总计','Closed','Ship via','备注'],
   	colModel:[
   	          {name: "id",width:1,hidden:true, key:true, sortable:false,search:false},
   		{name:'invno',index:'invno', width:55,sorttype:'int'},
   		{name:'invdate',index:'invdate', width:90, sorttype:'date',
   			searchoptions:{dataInit:function (elem) {$(elem).datepicker();}},
   			formatter:'date', formatoptions:{srcformat:"Y-m-d",newformat:"d-M-Y"}},
   		{name:'name',index:'name', width:100, formatter: 'link'},
   		{name:'amount',index:'amount', width:80, align:"right",sorttype:'double',formatter:'currency',formatoptions:{thousandsSeparator:","}},
   		{name:'tax',index:'tax', width:80, align:"right",sorttype:'double',formatter:'currency'},		
   		{name:'total',index:'total', width:80,align:"right",sorttype:'double',formatter:'currency', formatoptions:{prefix:"&euro;"}},	
   		{name:'closed',index:'closed',width:55,align:'center',
   			editable:true,edittype:"checkbox",editoptions:{value:"Yes:No"},
   			stype:'select',searchoptions:{sopt:['eq','ne'],value:"Yes:Yes;No:No"},
   			formatter:'checkbox'
   		},
		{name:'shipvia',index:'shipvia',width:70, 
   			editable: true,edittype:"select",editoptions:{value:"FE:FedEx;TN:TNT"},
   			formatter:'select',
   			stype:'select',searchoptions:{sopt:['eq','ne'],value:"FE:FedEx;TN:TNT"}
   			},   		
   		{name:'note',index:'note', width:150, sortable:false,search:false}		
   	],
   	rowNum:10,
   	rowList:[10,20,30],
   	pager: '#pcfrmgrid',
   	sortname: 'invdate',
    viewrecords: true,
    sortorder: "desc",
    rownumbers:true,
	
	multiselect: false,
	caption: "发票列表",
	forceFit : true		
});

jQuery("#cfrmgrid").jqGrid('navGrid','#pcfrmgrid',{edit:false,add:false,del:false});


});
</script>
