
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<p style="width:750px;">
	Using the new event <b>cellattr</b> in colModel we can easy configure data span.
	Note that with cellattribute we can set any valid attribute in the cell including style one
</p>
</div>
<br />
<table id="${uuid}"></table>
<div id="p${uuid}"></div>

<script  type="text/javascript">
jQuery().ready(function (){
	
$("#${uuid}").jqGrid({        
   	url:'${ctx}/invoice/search',
	datatype: "json",

   	colNames:['id','单号', '客户','日期', '金额','税费','总计','备注'],
   	colModel:[
   	          {name: "id",width:1,hidden:true,  sortable:false,search:false},
   		
   		{
			name:'invno',			
			index:'invno',
			width:55,
			cellattr: function(rowId, value, rowObject, colModel, arrData) {
				return ' ${uuid}=2';
			},
			formatter : function(value, options, rData){
				return "Invoce: "+value + " - "+rData['name'];
			},
			sorttype:'integer'
		},
   		{name:'name', index:'name',	width:100,
			cellattr: function(rowId, value, rowObject, colModel, arrData) {
				return " style=display:none; ";
			}
		},
		{name:'invdate',index:'invdate', width:90, sorttype:'date',
   			searchoptions:{dataInit:function (elem) {$(elem).datepicker();}}},		
   		{name:'amount',index:'amount', width:80, align:"right",sorttype:'number',formatter:'number'},
   		{name:'tax',index:'tax', width:80, align:"right",sorttype:'double',formatter:'number'},		
   		{name:'total',index:'total', width:80,align:"right",sorttype:'double',formatter:'number'},		
   		{name:'note',index:'note', width:150, sortable:false,search:false}		
   	],
   	rowNum:10,
   	rowList:[10,20,30],
   	pager: '#p${uuid}',
   	sortname: 'invdate',
    viewrecords: true,
    sortorder: "desc",
    rownumbers:true,	
	caption: "发票列表",	
	sortable: true
	
});

jQuery("#${uuid}").jqGrid('navGrid','#p${uuid}',{add:false,edit:false,del:false});

});
</script>
