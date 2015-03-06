
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<table id="${uuid}" class="autowidth autoheight"></table>
<div id="p${uuid}"></div>

<script  type="text/javascript"> 

jQuery().ready(function (){
	
	
$("#${uuid}").jqGrid({        
   	url:'${ctx}/invoice/search',
	datatype: "json",
	
   	colNames:['id','单号','日期', '客户', '金额','税费','总计','Closed','Ship via','备注'],
   	colModel:[
   	          {name: "id",width:1,hidden:true, key:true, sortable:false,search:false,editable:false,editoptions:{readonly:true,size:10}},
   		{name:'invno',index:'invno', width:55,sorttype:'int',editable:true,editoptions:{size:10},editrules:{required:true}},
   		{name:'invdate',index:'invdate', width:90, jsonmap:"invdate",sorttype:'date',
   			searchoptions:{dataInit:function (elem) {$(elem).datepicker();}},
   			editable:true,editrules:{required:true},
   			editoptions:{size:10,dataInit:function (elem) {$(elem).datepicker();}}},
   		{name:'name',index:'name', width:100,editable:true,editoptions:{size:25},editrules:{required:true}},
   		{name:'amount',index:'amount', width:80, align:"right",sorttype:'double',editable:true,editoptions:{size:10},
   			editrules:{required:true,number:true}},
   		{name:'tax',index:'tax', width:80, align:"right",hidden:true,sorttype:'double',editable:true,editoptions:{size:10},
   			editrules:{edithidden:true,required:true,number:true,minValue:40,maxValue:100}},		
   		{name:'total',index:'total', width:80,align:"right",sorttype:'double',editable:true,editoptions:{size:10}},	
   		{name:'closed',index:'closed',width:55,align:'center',
   			editable:true,edittype:"checkbox",editoptions:{value:"Yes:No"},
   			stype:'select',searchoptions:{sopt:['eq','ne'],value:"Yes:Yes;No:No"}
   		},
		{name:'shipvia',index:'shipvia',width:70, 
   			editable: true,edittype:"select",editoptions:{value:"FE:FedEx;TN:TNT"},
   			stype:'select',searchoptions:{sopt:['eq','ne'],value:"FE:FedEx;TN:TNT"}
   		},   		
   		{name:'note',index:'note', width:150, sortable:false,search:false,editable: true,edittype:"textarea", editoptions:{rows:"2",cols:"20"}}		
   	],
   	rowNum:10,
   	rowList:[10,20,30],
   	pager: '#p${uuid}',
   	sortname: 'invdate',
    viewrecords: true,
    sortorder: "desc",
    rownumbers:true,
	
	editurl: "${ctx}/invoice/edit",
	autowidth:true,
	caption: "发票列表"
	
});

jQuery("#${uuid}").jqGrid('navGrid','#p${uuid}',
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
