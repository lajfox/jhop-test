
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<table id="${uuid}"></table>
<div id="p${uuid}"></div>
<br>
<a href="javascript:void(0)" id="s${uuid}">获取列表的所有Id's</a><br/>


<script  type="text/javascript">
jQuery().ready(function (){
	
$("#${uuid}").jqGrid({        
   	url:'${ctx}/invoice/search',
	datatype: "json",
	
   	colNames:['id','单号','日期', '客户', '金额','税费','总计','备注'],
   	colModel:[
   	          {name: "id",width:1,hidden:true, key:true, sortable:false,search:false},
   		{name:'invno',index:'invno', width:55,sorttype:'int'},
   		{name:'invdate',index:'invdate', width:90, sorttype:'date',
   			searchoptions:{dataInit:function (elem) {$(elem).datepicker();}}},
   		{name:'name',index:'name', width:100},
   		{name:'amount',index:'amount', width:80, align:"right",sorttype:'double'},
   		{name:'tax',index:'tax', width:80, align:"right",sorttype:'double'},		
   		{name:'total',index:'total', width:80,align:"right",sorttype:'double'},		
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
	
	loadComplete: function(){
		var ret;
		
		var positions = $.jgrid.findPositions(290,50);
		$.jgrid.info_dialog('注意',"This function is executed immediately after\n data is loaded. We try to update data in row 4.",'',{left:positions[0],top:positions[1]});	
		ret = jQuery("#${uuid}").jqGrid('getRowData',"4");
		if(ret.id == "4"){
			jQuery("#${uuid}").jqGrid('setRowData',ret.id,{note:"<font color='red'>Row 4 is updated!</font>"})
		}
	}	
	
});

jQuery("#s${uuid}").click( function() {	
	var positions = $.jgrid.findPositions(400,300);
	$.jgrid.info_dialog('注意',"列表的所有Id's: \n"+jQuery("#${uuid}").jqGrid('getDataIDs'),'',{width:400,height:300,left:positions[0],top:positions[1]});	
});

});
</script>
