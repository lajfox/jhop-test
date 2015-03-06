
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div style="font-size:12px;"  class="add-padding">
    这个例子演示新的操作方法， 请在单击页面上的按键试试.
</div>
<b>响应:</b> <span id="resp"></span>
<br/>
<br/>
<table id="${uuid}"></table>
<div id="p${uuid}"></div>



<br />
<a href="javascript:void(0)" id="sm1">获取记录总数方法</a>
<br />
<a href="javascript:void(0)" id="sm2">选择记录方法</a>
<br />
<a href="javascript:void(0)" id="sm3" >取消选择记录方法</a>  <span> << 请选择一些记录再点击我 </span>

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
	
	multiselect: true,
	caption: "发票列表",
	onPaging : function(but) {
		alert("Button: "+but + " is clicked");
	}	
	
});

jQuery("#${uuid}").jqGrid('navGrid','#p${uuid}',{edit:false,add:false,del:false});

jQuery("#sm1").click( function() {
	alert($("#${uuid}").jqGrid('getGridParam',"records"));
});

jQuery("#sm2").click( function() {
	$("#${uuid}").jqGrid('setGridParam',{
		onSelectRow : function(id) {
			$("#resp").html("I'm row number: "+id +" and setted dynamically").css("color","red");
		}
	});
	var positions = $.jgrid.findPositions(290,50);
	$.jgrid.info_dialog('注意',"请选择记录试试",'',{left:positions[0],top:positions[1]});
});

jQuery("#sm3").click( function() {
	$("#${uuid}").jqGrid('resetSelection');
});

});
</script>
