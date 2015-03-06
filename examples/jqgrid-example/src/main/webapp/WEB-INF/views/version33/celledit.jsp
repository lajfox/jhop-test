
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div style="font-size:12px;">
    This example show the new Cell editing feature of jqGrid. Select some cell. <br>
    The fields date, amout and tax are editable. When select a cell you can <br>
	navigate with left, right, up and down keys. The Enter key save the content. The esc does not save the content.<br>
	Try to change the values of amount or tax and see that the total changes.<br>
	To enable cell editing you should just set cellEdit: true and ajust the colModel in appropriate way.
</div>
<br />
<table id="celltbl"></table>
<div id="pcelltbl"></div>


<script  type="text/javascript"> 

jQuery().ready(function (){
	
	$("#celltbl").jqGrid({        
	   	url:'${ctx}/invoice/search',
		datatype: "json",
		
	   	colNames:['id','单号','日期', '客户', '金额','税费','总计','备注'],
	   	colModel:[
	   	          {name: "id",width:1,hidden:true, key:true, sortable:false,search:false},
	   		{name:'invno',index:'invno', width:55,sorttype:'int'},
	   		{name:'invdate',index:'invdate', width:90, jsonmap:"invdate",sorttype:'date',
	   			searchoptions:{dataInit:function (elem) {$(elem).datepicker();}},editable:true},
	   		{name:'name',index:'name', width:100},
	   		{name:'amount',index:'amount', width:80, align:"right",sorttype:'double',editable:true,editrules:{number:true}},
	   		{name:'tax',index:'tax', width:80, align:"right",sorttype:'double',editable:true,editrules:{number:true}},		
	   		{name:'total',index:'total', width:80,align:"right",sorttype:'double'},		
	   		{name:'note',index:'note', width:150, sortable:false,search:false}		
	   	],
	   	rowNum:10,
	   	rowList:[10,20,30],
	   	pager: '#pcelltbl',
	   	sortname: 'invdate',
	    viewrecords: true,
	    sortorder: "desc",
	    rownumbers:true,
		
		multiselect: false,
		caption: "发票列表",		
		forceFit : true,
		cellEdit: true,//表示表格可编辑 
		cellsubmit: 'remote',//clientArray表示在本地进行修改
		cellurl: "${ctx}/invoice/edit",
		afterEditCell: function (id,name,val,iRow,iCol){
			if(name=='invdate') {
				jQuery("#"+iRow+"_invdate","#celltbl").datepicker({dateFormat:"yy-mm-dd"});
			}
		},
		afterSaveCell : function(rowid,name,val,iRow,iCol) {
			if(name == 'amount') {
				var taxval = jQuery("#celltbl").jqGrid('getCell',rowid,iCol+1);
				jQuery("#celltbl").jqGrid('setRowData',rowid,{total:parseFloat(val)+parseFloat(taxval)});
			}
			if(name == 'tax') {
				var amtval = jQuery("#celltbl").jqGrid('getCell',rowid,iCol-1);
				jQuery("#celltbl").jqGrid('setRowData',rowid,{total:parseFloat(val)+parseFloat(amtval)});
			}
		}		
		
	});

	jQuery("#celltbl").jqGrid('navGrid','#pcelltbl',{edit:false,add:false,del:false});


});
</script>
