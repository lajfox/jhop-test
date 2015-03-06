
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div style="font-size:12px;">
    This example show the new feature of jqGrid forceFit. Try to resize a column. <br>
    We can see that the adjacent column (to the right) resizes so that the overall grid width is maintained <br>
	Again with this we can set the width and height dynamically. <br>
	Another feature here is that we can apply a sort to a certain column instead of clicking on <br>
	another one. Click on date colummn. The sort is not by date, but of client name.
</div>
<br />
<table id="gwidth"></table>
<div id="pgwidth"></div>
<br/>
<input id="setwidth" type="text" /><input type="button" id="snw" value="Set New Width"/>
<br/>
<input id="setheight" type="text" /><input type="button" id="snh" value="Set New Height"/>



<script  type="text/javascript"> 

jQuery().ready(function (){
	

	
$("#gwidth").jqGrid({        
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
   	pager: '#pgwidth',
   	sortname: 'invdate',
    viewrecords: true,
    sortorder: "desc",
    rownumbers:true,
	
	multiselect: false,
	caption: "发票列表",
	forceFit : true,
	onSortCol :function (nm,index) {
		if (nm=='invdate') {
			jQuery("#gwidth").jqGrid('setGridParam',{sortname:'name'});
		}
	},
	onHeaderClick: function (status){
		alert("My status is now: "+ status);
	}
	
});

jQuery("#gwidth").jqGrid('navGrid','#pgwidth',{edit:false,add:false,del:false});
jQuery("#snw").click(function (){
	var nw = parseInt(jQuery("#setwidth").val());
	if(isNaN(nw)) {
		alert("Value must be a number");
	} else if (nw < 200 || nw > 700) {
		alert("Value can be between 200 and 700")
	} else {
		jQuery("#gwidth").jqGrid('setGridWidth',nw);
	}
});
jQuery("#snh").click(function (){
	var nh = jQuery("#setheight").val();
	jQuery("#gwidth").jqGrid('setGridHeight',nh);
});


});
</script>
