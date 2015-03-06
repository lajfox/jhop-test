
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<table id="${uuid}"></table>
<div id="pager10"></div>
<br />
<table id="${uuid}_d"></table>
<div id="p${uuid}_d"></div>
<a href="javascript:void(0)" id="ms1">Get Selected id's</a>

<script  type="text/javascript"> 

jQuery().ready(function (){
	
$("#${uuid}").jqGrid({        
   	url:'${ctx}/invoice/search',
	datatype: "json",	
   	colNames:['id','单号','日期', '客户', '金额','税费','总计','备注'],
   	colModel:[
   	          {name: "id",width:1,hidden:true, key:true, sortable:false,search:false},
   		{name:'invno',index:'invno', width:55,sorttype:'int'},
   		{name:'invdate',index:'invdate', width:90, jsonmap:"invdate",sorttype:'date',
   			searchoptions:{dataInit:function (elem) {$(elem).datepicker();}}},
   		{name:'name',index:'name', width:100},
   		{name:'amount',index:'amount', width:80, align:"right",sorttype:'double'},
   		{name:'tax',index:'tax', width:80, align:"right",sorttype:'double'},		
   		{name:'total',index:'total', width:80,align:"right",sorttype:'double'},		
   		{name:'note',index:'note', width:150, sortable:false,search:false}		
   	],
   	rowNum:10,
   	rowList:[10,20,30],
   	pager: '#pager10',
   	sortname: 'invdate',
    viewrecords: true,
    sortorder: "desc",
    rownumbers:true,
	
	multiselect: false,
	caption: "发票列表",
	onSelectRow: function(ids) {
		if(ids == null) {
			ids=0;
			if(jQuery("#${uuid}_d").jqGrid('getGridParam','records') >0 )
			{
				jQuery("#${uuid}_d").jqGrid('setGridParam',{url:"${ctx}/invoice/item/search?sDs_invoice="+ids,page:1});
				jQuery("#${uuid}_d").jqGrid('setCaption',"Invoice Detail: "+ids)
				.trigger('reloadGrid');
			}
		} else {
			jQuery("#${uuid}_d").jqGrid('setGridParam',{url:"${ctx}/invoice/item/search?sDs_invoice="+ids,page:1});
			jQuery("#${uuid}_d").jqGrid('setCaption',"Invoice Detail: "+ids)
			.trigger('reloadGrid');			
		}
	}	
	
});

jQuery("#${uuid}").jqGrid('navGrid','#pager10',{add:false,edit:false,del:false});
jQuery("#${uuid}_d").jqGrid({
	height: 100,
   	url:'${ctx}/invoice/item/search',
	datatype: "json",
   	colNames:['Id','发票Id','No','Item', 'Qty', 'Unit','Line Total'],
   	colModel:[
   	       {name: "id",width:1,hidden:true, key:true, sortable:false,search:false},
   	    {name: "invoice",width:1,hidden:true,  sortable:false,search:false},
   		{name:'num',index:'num', width:55,sorttype:'int'},
   		{name:'item',index:'item', width:180},
   		{name:'qty',index:'qty', width:80, align:"right",sorttype:'double'},
   		{name:'unit',index:'unit', width:80, align:"right",sorttype:'double'},		
   		{name:'linetotal',index:'linetotal', width:80,align:"right", sortable:false, search:false,sorttype:'double'}
   	   
   	],
   	rowNum:5,
   	rowList:[5,10,20],
   	pager: '#p${uuid}_d',
   	sortname: 'num',
    viewrecords: true,
    sortorder: "asc",
	multiselect: true,
	jsonReader: {
		root:"content",
		 page: "number",
	      total: "totalPages",
	      records: "totalElements",		
		repeatitems : false,
		id: "0"
	},	
	caption:"发票详细"
})
jQuery("#${uuid}_d").jqGrid('navGrid','#p${uuid}_d',{add:false,edit:false,del:false});
jQuery("#ms1").click( function() {
	var s;
	s = jQuery("#${uuid}_d").jqGrid('getGridParam','selarrrow');
	alert(s);
});

});
</script>
