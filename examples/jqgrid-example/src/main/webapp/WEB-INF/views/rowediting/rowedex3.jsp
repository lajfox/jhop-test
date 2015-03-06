
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<table id="${uuid}"></table>
<div id="p${uuid}"></div>


<script  type="text/javascript"> 

jQuery().ready(function (){

	var lastsel;
$("#${uuid}").jqGrid({        
   	url:'${ctx}/invoice/search',
	datatype: "json",
	
   	colNames:['Id','单号','日期', '客户', '金额','税费','总计','备注'],
   	colModel:[		
   	    {name: "id",width:1,hidden:true, key:true, sortable:false,search:false},
   		{name:'invno',index:'invno', width:55,sorttype:'int', editable:true},
   		{name:'invdate',index:'invdate', width:90, sorttype:'date',
   			searchoptions:{dataInit:function (elem) {$(elem).datepicker();}},
   			editoptions:{dataInit:function (elem) {$(elem).datepicker();}},
   			editable:true
   		},
   		{name:'name',index:'name', width:100, editable:true},
   		{name:'amount',index:'amount', width:80, align:"right",sorttype:'double', editable:true,formatter:'number',editrules:{required:true,number:true}},
   		{name:'tax',index:'tax', width:80, align:"right",sorttype:'double', editable:true,formatter:'number'},		
   		{name:'total',index:'total', width:80,align:"right",sorttype:'double', editable:true,formatter:'number'},		
   		{name:'note',index:'note', width:150, sortable:false,search:false, editable:true}		
   	],
   	rowNum:10,
   	rowList:[10,20,30],
   	pager: '#p${uuid}',
   	sortname: 'invdate',
    viewrecords: true,
    sortorder: "desc",
    rownumbers:true,
	
	onSelectRow: function(id){
		if(id && id!==lastsel){
			jQuery('#${uuid}').jqGrid('saveRow',lastsel);
			jQuery('#${uuid}').jqGrid('editRow',id,true);
			lastsel=id;
		}
	},	
	editurl: "${ctx}/invoice/edit",
	caption: "发票列表"
	
});

jQuery("#${uuid}").jqGrid('navGrid',"#p${uuid}",{edit:false,add:false,del:false});



});
</script>
