
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div  style="font-size:12px;" class="add-padding">	
	可以通过自定义查询模板让查询更方便快速.	
</div>
<br />
<table id="${uuid}"></table>
<div id="p${uuid}"></div>

<script  type="text/javascript">
jQuery().ready(function (){
	
	var template1 =
	{ "groupOp": "AND",
	      "rules": [
	        { "field": "name", "op": "bw", "data": "Client1" },
	        { "field": "amount", "op": "gt", "data": "20"}
	      ]
	};

	var template2 =
	{ "groupOp": "AND",
	      "rules": [
	        { "field": "name", "op": "eq", "data": "Client2" },
	        { "field": "invno", "op": "le", "data": "1000"}
	      ]
	} ;	
	
$("#${uuid}").jqGrid({        
   	url:'${ctx}/invoice/search',
	datatype: "json",

   	colNames:['id','单号','日期', '客户', '金额','税费','总计','备注'],
   	colModel:[
   	          {name: "id",width:1,hidden:true, key:true, sortable:false,search:false},
   		{name:'invno',index:'invno', width:55,sorttype:'int',searchtype:"integer", searchrules:{required:true, integer:true, minValue:13}},
   		{name:'invdate',index:'invdate', width:90, sorttype:'date',
   			searchoptions:{dataInit:function (elem) {
					$(elem).datepicker({
							showButtonPanel: true,
			   				numberOfMonths: 3,
			   				maxDate:new Date() 
					});
				}
			}
   		},
   		{name:'name',index:'name', width:100},
   		{name:'amount',index:'amount', width:80, align:"right",sorttype:'float',formatter:'number', searchtype:"number"},
   		{name:'tax',index:'tax', width:80, align:"right",sorttype:'float',formatter:'number', searchtype:"number"},		
   		{name:'total',index:'total', width:80,align:"right",sorttype:'float',formatter:'number', searchtype:"number"},		
   		{name:'note',index:'note', width:150, sortable:false}		
   	],
   	rowNum:10,
   	rowList:[10,20,30],
   	pager: '#p${uuid}',
   	sortname: 'invdate',
    viewrecords: true,
    sortorder: "desc",
    rownumbers:true,	
	caption: "发票列表",	   
   	gridview: true
	
	
});

jQuery("#${uuid}").jqGrid('navGrid','#p${uuid}',{del:false,add:false,edit:false},{},{},{},
		{multipleSearch:true, 
		multipleGroup:true, 
		showQuery: true,
		// set the names of the template 
		tmplNames : ["模板１", "模板2"], 
		// set the template contents 
		tmplFilters: [template1, template2]	
		}
	);

});
</script>
