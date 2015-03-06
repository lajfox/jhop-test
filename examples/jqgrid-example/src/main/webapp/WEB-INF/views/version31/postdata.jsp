
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div style="font-size:12px;" class="add-padding">
	这个下例子演示使用postData数组提交数据的新方法。   
    <br/>
    如果你安装了FireBug,当你提交数据时就可以看到效果。
    <br/>
    <b>Request: </b> <span id="postdata"></span>   
</div>


<br/>
<table id="${uuid}"></table>
<div id="p${uuid}"></div>
<br />
<a href="javascript:void(0)" id="ps1">Set Post Data</a>
<br />
<a href="javascript:void(0)" id="ps2">Get Post Data</a>
<br />
<a href="javascript:void(0)" id="ps3" >Append Post Data</a>
<br />
<a href="javascript:void(0)" id="ps4" >Set Post Data Item</a>
<br />
<a href="javascript:void(0)" id="ps5" >Get Post Data Item</a>
<br />
<a href="javascript:void(0)" id="ps6" >Remove Post Data Item</a>


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
	
	postData:{q:1},
	multiselect: true,
	caption: "发票列表"
	
});

jQuery("#${uuid}").jqGrid('navGrid','#p${uuid}',{edit:false,add:false,del:false});

jQuery("#ps1").click( function() {
	$("#${uuid}").jqGrid('setGridParam',{postData:{p:1,param1:"p1"}});
	$("#${uuid}").trigger("reloadGrid");
});

jQuery("#ps2").click( function() {
	var pd =$("#${uuid}").jqGrid('getGridParam','postData');
	var r ="";
	$.each(pd,function(i){
		r += i+": "+pd[i]+",";
	})
	$("#postdata").html(r).css("background-color","yellow");
});

jQuery("#ps3").click( function() {
	$("#${uuid}").jqGrid('setGridParam',{postData:{param2:"p2"}});
	$("#${uuid}").trigger("reloadGrid");
	var pd =$("#${uuid}").jqGrid('getGridParam','postData');
	var r ="";
	$.each(pd,function(i){
		r += i+": "+pd[i]+",";
	})
	$("#postdata").html(r).css("background-color","yellow");
});
jQuery("#ps4").click( function() {
	$("#${uuid}").jqGrid('setGridParam',{postData:{"param2":"I'w new value"}});
	$("#${uuid}").trigger("reloadGrid");
	var pd =$("#${uuid}").jqGrid('getGridParam','postData');
	var r ="";
	$.each(pd,function(i){
		r += i+": "+pd[i]+",";
	})
	$("#postdata").html(r).css("background-color","yellow");
});
jQuery("#ps5").click( function() {
	alert( "rows : "+$("#${uuid}").jqGrid('getGridParam', 'postData')['rows']);
});
jQuery("#ps6").click( function() {
	$("#${uuid}").jqGrid('setGridParam',{postData:{param1:null}});
	$("#${uuid}").trigger("reloadGrid");
	var pd =$("#${uuid}").jqGrid('getGridParam','postData');
	var r ="";
	$.each(pd,function(i){
		r += i+": "+pd[i]+",";
	})
	$("#postdata").html(r).css("background-color","yellow");
});

});
</script>
