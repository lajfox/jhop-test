
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<table id="${uuid}" class="autowidth autoheight"></table>
<div id="p${uuid}"></div>
<br />
<a href="#" id="a1">获取行记录</a>
<br />
<a href="#" id="a2">删除行12</a>
<br />
<a href="#" id="a3">更新行11</a>
<br />
<a href="#" id="a4">增加行99</a>


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
$("#${uuid}").jqGrid('navGrid','#p${uuid}',{edit:false,add:false,del:false});

jQuery("#a1").click( function(){
	var id = jQuery("#${uuid}").jqGrid('getGridParam','selrow');
	if (id)	{
		var ret = jQuery("#${uuid}").jqGrid('getRowData',id);		
		var positions = $.jgrid.findPositions(290,50);
		$.jgrid.info_dialog('注意',"id="+ret.id+" invdate="+ret.invdate+"...",'',{left:positions[0],top:positions[1]});		
	} else { 
		var positions = $.jgrid.findPositions(290,50);
		$.jgrid.info_dialog('注意',"请选择记录",'',{left:positions[0],top:positions[1]});		
	}
});
jQuery("#a2").click( function(){
	var su=jQuery("#${uuid}").jqGrid('delRowData',12);
	if(su) {		
		var positions = $.jgrid.findPositions(290,50);
		$.jgrid.info_dialog('注意',"删除记录成功",'',{left:positions[0],top:positions[1]});		
	}
	else {		
		var positions = $.jgrid.findPositions(290,50);
		$.jgrid.info_dialog('注意',"记录早己删除或在列表中不存在",'',{left:positions[0],top:positions[1]});		
	}
});
jQuery("#a3").click( function(){
	var su=jQuery("#${uuid}").jqGrid('setRowData',11,{amount:"333.00",tax:"33.00",total:"366.00",note:"<img src='images/user1.gif'/>"});
	if(su) {
		var positions = $.jgrid.findPositions(290,50);
		$.jgrid.info_dialog('注意',"更新记录成功",'',{left:positions[0],top:positions[1]});
	}
	else {	
		var positions = $.jgrid.findPositions(290,50);
		$.jgrid.info_dialog('注意',"更新记录失败",'',{left:positions[0],top:positions[1]});		
	}
});
jQuery("#a4").click( function(){
	var datarow = {id:"99",invno:"2880",invdate:"2007-09-01",name:"test3",note:"note3",amount:"400.00",tax:"30.00",total:"430.00"};
	var su=jQuery("#${uuid}").jqGrid('addRowData',99,datarow);
	if(su) {
		var positions = $.jgrid.findPositions(290,50);
		$.jgrid.info_dialog('注意',"新增记录成功",'',{left:positions[0],top:positions[1]});
	}
	else {
		var positions = $.jgrid.findPositions(290,50);
		$.jgrid.info_dialog('注意',"新增记录失败",'',{left:positions[0],top:positions[1]});
	}
});

});
</script>
