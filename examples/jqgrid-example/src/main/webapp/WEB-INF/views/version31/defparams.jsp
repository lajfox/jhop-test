
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div style="font-size:12px;"  class="add-padding">
    This example demonstartes setting of common grid parameters.
    <br/>
    After setting this parameters a Rows and Loading messages will be changed.
    <br/>
    Reopen the grid to view this.Note that all parameters can be changed.
</div>
<br/>
<table id="${uuid}"></table>
<div id="p${uuid}"></div>


<br />
<a href="javascript:void(0)" id="pp1">Set Common Params</a>



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
   	pager: '#p${uuid}',
   	sortname: 'invdate',
    viewrecords: true,
    sortorder: "desc",
    rownumbers:true,
		
	caption: "发票列表"
	
});

jQuery("#${uuid}").jqGrid('navGrid','#p${uuid}',{edit:false,add:false,del:false});

jQuery("#pp1").click( function() {
	$.extend($.jgrid.defaults,{recordtext: "{0} - {1} of {2} Record(s)",loadtext: "Processing"});
	var positions = $.jgrid.findPositions(290,50);
	$.jgrid.info_dialog('注意',"新参数己经设置，请重新打开grid试试",'',{left:positions[0],top:positions[1]});
});


});
</script>
