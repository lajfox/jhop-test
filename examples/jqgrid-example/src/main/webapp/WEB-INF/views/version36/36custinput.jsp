
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div style="font-size:12px;">
    Another great feature which is requested many times from the community - custom cretion of editable elements<br>
    This is done with the following settings in colModel - edittype:'custom' and two functions - one to create the element and <br>
    another to get the value from it<br>
    In this exmple click on row to edit it and see what is happen when you perform saving in the column Client..
    <br/>
<br />
<table id="cinput"></table>
<div id="pcinput"></div>


<script  type="text/javascript"> 

jQuery().ready(function (){
	
var lastsel;
function my_input(value, options) {
	return $("<input type='text' size='10' value='"+value+"'/>");
}
function my_value(value) {
	return "My value: "+value.val();
}
	
$("#cinput").jqGrid({        
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
   			
   		{name:'name',index:'name', width:100, 
   				editable:true, edittype:'custom', 
   				editoptions:{custom_element:my_input,custom_value:my_value}
   		},
   		{name:'amount',index:'amount', width:80, align:"right",sorttype:'double', editable:true},
   		{name:'tax',index:'tax', width:80, align:"right",sorttype:'double', editable:true},		
   		{name:'total',index:'total', width:80,align:"right",sorttype:'double', editable:true},		
   		{name:'note',index:'note', width:150, sortable:false,search:false, editable:true}		
   	],
   	rowNum:10,
   	rowList:[10,20,30],
   	pager: '#pcinput',
   	sortname: 'invdate',
    viewrecords: true,
    sortorder: "desc",
    rownumbers:true,
	
    onSelectRow: function(id){
		if(id && id!==lastsel){
			jQuery('#cinput').jqGrid('restoreRow',lastsel);
			jQuery('#cinput').jqGrid('editRow',id,true);
			lastsel=id;
		}
	},
	
	editurl: "${ctx}/invoice/edit",
	caption: "发票列表"
	
});

jQuery("#cinput").jqGrid('navGrid',"#pcinput",{edit:false,add:false,del:false});



});
</script>
