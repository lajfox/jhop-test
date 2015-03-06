
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div style="font-size:12px;">
It is possible to load the subgrid data only once and then just to hide/show it, <br/>
without to make additional ajax call to the server<br/>
This is done just with one option - see the code. <br/>
</div>
<br />
<table id="${uuid}"></table>
<div id="p${uuid}"></div>


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
   		{name:'amount',index:'amount', width:80, align:"right",sorttype:'double',formatter:'number'},
   		{name:'tax',index:'tax', width:80, align:"right",sorttype:'double',formatter:'number'},		
   		{name:'total',index:'total', width:80,align:"right",sorttype:'double',formatter:'number'},		
   		{name:'note',index:'note', width:150, sortable:false,search:false}		
   	],
   	rowNum:8,
   	rowList:[8,10,20,30],
   	pager: '#p${uuid}',
   	sortname: 'invdate',
    viewrecords: true,
    sortorder: "desc",
    
	caption: "发票列表",
	autowidth:true,
	
	multiselect: false, 
	subGrid : true, 
	
	// define the icons in subgrid
	subGridOptions: {
        "plusicon"  : "ui-icon-triangle-1-e",
        "minusicon" : "ui-icon-triangle-1-s",
        "openicon"  : "ui-icon-arrowreturn-1-e",
     // load the subgrid data only once
		// and the just show/hide
		"reloadOnExpand" : false,
		// select the row when the expand column is clicked 
		"selectOnExpand" : true
	},	
	subGridRowExpanded: function(subgrid_id, row_id) {
		// we pass two parameters
		// subgrid_id is a id of the div tag created whitin a table data
		// the id of this elemenet is a combination of the "sg_" + id of the row
		// the row_id is the id of the row
		// If we wan to pass additinal parameters to the url we can use
		// a method getRowData(row_id) - which returns associative array in type name-value
		// here we can easy construct the flowing
		var subgrid_table_id, pager_id;
		subgrid_table_id = subgrid_id+"_t";
		pager_id = "p_"+subgrid_table_id;
		$("#"+subgrid_id).html("<table id='"+subgrid_table_id+"' class='scroll'></table><div id='"+pager_id+"' class='scroll'></div>");
		jQuery("#"+subgrid_table_id).jqGrid({
			url:"${ctx}/invoice/item/search?sDs_invoice="+row_id,
			datatype: "json",
			colNames: ['Id','No','Item','Qty','Unit','Line Total'],
			colModel: [
	   	       {name: "id",width:1,hidden:true, key:true, sortable:false,search:false},			      	  
	      		{name:'num',index:'num', width:55,sorttype:'int'},
	      		{name:'item',index:'item', width:180},
	      		{name:'qty',index:'qty', width:80, align:"right",sorttype:'double',formatter:'number'},
	      		{name:'unit',index:'unit', width:80, align:"right",sorttype:'double'},		
	      		{name:'linetotal',index:'linetotal', width:80,align:"right", sortable:false, search:false,sorttype:'double'}
			],
		   	rowNum:20,
		   	rowList:[8,10,20,30],
		   	pager: pager_id,
		   	sortname: 'num',
		    sortorder: "asc",
		    height: '100%',
		    viewrecords: true
		    	    
		});
		jQuery("#"+subgrid_table_id).jqGrid('navGrid',"#"+pager_id,{edit:false,add:false,del:false})
	},
	subGridRowColapsed: function(subgrid_id, row_id) {
		// this function is called before removing the data
		//var subgrid_table_id;
		//subgrid_table_id = subgrid_id+"_t";
		//jQuery("#"+subgrid_table_id).remove();
	}	
	
});
$("#${uuid}").jqGrid('navGrid','#p${uuid}',{edit:false,add:false,del:false});

//设置grid高度
$("#${uuid}").jqGrid('gridHeight',{height:-200});  

});
</script>
