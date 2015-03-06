
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div style="font-size:12px;">
    This example demonstartes the entairley rewriten searchGrid method. <br>
	From now on jqGrid use a new s4list engine thanks to wonderful plugin provided from Kasey Speakman <br>
	Note that the old behaviour of the s4list is saved. <br>
	I will just say Enjoy! <br>
</div>
<br />
<table id="s4list"></table>
<div id="s4pager"></div>

<script  type="text/javascript"> 

jQuery().ready(function (){
	
$("#s4list").jqGrid({        
   	url:'${ctx}/invoice/search',
	datatype: "json",
	
   	colNames:['id','单号','日期', '客户', '金额','税费','总计','备注'],
   	colModel:[
   	          {name: "id",width:1,hidden:true, key:true, sortable:false,s4list:false},
   		{name:'invno',index:'invno', width:55,sorttype:'int', searchoptions:{sopt:['eq','ne','lt','le','gt','ge']}},
   		{name:'invdate',index:'invdate', width:90,sorttype:'date',
   			searchoptions:{dataInit:function (elem) {$(elem).datepicker(
   					{showButtonPanel: true,
   						maxDate:new Date(),
   				numberOfMonths: 3}
   					);}}},
   		{name:'name',index:'name', width:100},
   		{name:'amount',index:'amount', width:80, align:"right",sorttype:'double'},
   		{name:'tax',index:'tax', width:80, align:"right",sorttype:'double'},		
   		{name:'total',index:'total', width:80,align:"right",sorttype:'double'},		
   		{name:'note',index:'note', width:150, sortable:false}		
   	],
   	rowNum:10,
   	rowList:[10,20,30],
   	pager: '#s4pager',
   	sortname: 'invdate',
    viewrecords: true,
    sortorder: "desc",
    rownumbers:true,	
	caption: "发票列表"
	
});

jQuery("#s4list").jqGrid('navGrid','#s4pager',
		{
			edit:false,add:false,del:false,search:true,refresh:true
		},
		{}, // edit options
		{}, // add options
		{}, //del options
		{multipleSearch:true} // search options
		);

});
</script>
