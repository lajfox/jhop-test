
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div style="font-size:12px;">
	With predefined formatter actions it is quite easy to add inline editing
</div>
<br />
<table id="${uuid}"></table>
<div id="p${uuid}"></div>

<script  type="text/javascript">
jQuery().ready(function (){
	
$("#${uuid}").jqGrid({        
   	url:'${ctx}/invoice/search',
	datatype: "json",

   	colNames:['','id','单号','日期', '客户', '金额','税费','总计','备注'],
   	colModel:[
	{name: 'myac', width:80, fixed:true, sortable:false, resize:false, formatter:'actions',
	formatoptions:{keys:true}},   	          
   	          {name: "id",width:1,hidden:true, key:true, sortable:false,search:false},
   		{name:'invno',index:'invno', width:55,sorttype:'int',
   	       editable:true,editrules:{required:true,integer:true}
   	    },
   		{name:'invdate',index:'invdate', width:90, sorttype:'date',
   			searchoptions:{
   				dataInit:function (elem) {
   					$(elem).datepicker({
		   				showButtonPanel: true,
		   				numberOfMonths: 3,
		   				maxDate:new Date()
	   				});
   				}				
   			},
			editable:true,
			editoptions:{
				dataInit:function(el){
					$(el).datepicker({dateFormat:'yy-mm-dd',numberOfMonths: 3,maxDate:new Date()});
				},
				defaultValue: function(){
					var currentTime = new Date();
					var month = parseInt(currentTime.getMonth() + 1);
					month = month <= 9 ? "0"+month : month;
					var day = currentTime.getDate();
					day = day <= 9 ? "0"+day : day;
					var year = currentTime.getFullYear();
					return year+"-"+month + "-"+day;				
				}
			},
			editrules:{required:true}   
   		},
   		{name:'name',index:'name', width:100,editable:true,editrules:{required:true}},
   		{name:'amount',index:'amount', width:80, align:"right",sorttype:'double',formatter:'number',editable:true,editrules:{number:true}},
   		{name:'tax',index:'tax', width:80, align:"right",sorttype:'double',formatter:'number',editable:true,editrules:{number:true}},		
   		{name:'total',index:'total', width:80,align:"right",sorttype:'double',formatter:'number',editable:true,editrules:{number:true}},		
   		{name:'note',index:'note', width:150, sortable:false,search:false,editable:true}		
   	],    
   	rowNum:10,
   	rowList:[10,20,30],
   	pager: '#p${uuid}',
   	sortname: 'invdate',
    viewrecords: true,
    sortorder: "desc",
    rownumbers:true,	
	caption: "发票列表",	
	editurl: "${ctx}/invoice/edit",
	sortable: true
	
});

jQuery("#${uuid}").jqGrid('navGrid','#p${uuid}',{edit:false,add:false,del:false});



});
</script>
