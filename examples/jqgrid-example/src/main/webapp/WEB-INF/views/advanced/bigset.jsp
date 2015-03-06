
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div class="h">Search By:</div>
<div>
	日期<br />
	<input type="text" id="sDs_invdate" onkeydown="doSearch(arguments[0]||event)" />
	<input type="checkbox" id="autosearch" onclick="enableAutosubmit(this.checked)"> Enable Autosearch <br/>
</div>
<div>
	客户<br>
	<input type="text" id="sDs_name" onkeydown="doSearch(arguments[0]||event)" />
	<button onclick="gridReload()" id="submitButton" style="margin-left:30px;">Search</button>
</div>

<br />
<table id="${uuid}" ></table>
<div id="p${uuid}"></div>


<script  type="text/javascript"> 
jQuery().ready(function (){
	
	$( "#sDs_invdate" ).datepicker();
	
$("#${uuid}").jqGrid({        
   	url:'${ctx}/invoice/search',
	datatype: "json",	
   	colNames:['id','单号','日期', '客户', '金额','税费','总计','备注'],
   	colModel:[
   	          {name: "id",width:1,hidden:true, key:true, sortable:false,search:false},
   		{name:'invno',index:'invno', width:55,sorttype:'int'},
   		{name:'invdate',index:'invdate', width:90 
   			,sorttype:'date',
   			searchoptions:{dataInit:function (elem) {$(elem).datepicker();}}},
   		{name:'name',index:'name', width:100},
   		{name:'amount',index:'amount', width:80, align:"right",sorttype:'double'},
   		{name:'tax',index:'tax', width:80, align:"right",sorttype:'double'},		
   		{name:'total',index:'total', width:80,align:"right",sorttype:'double'},		
   		{name:'note',index:'note', width:150, sortable:false,search:false}		
   	],
   	rowNum:10,   
   	pager: '#p${uuid}',
   	sortname: 'invdate',
    viewrecords: true,
    sortorder: "desc",
    rownumbers:true,
	
	 pgbuttons: false,
	 pgtext: false, 
	 pginput:false
	
});

var timeoutHnd;
var flAuto = false;

function doSearch(ev){
	if(!flAuto)
		return;
//	var elem = ev.target||ev.srcElement;
	if(timeoutHnd)
		clearTimeout(timeoutHnd)
	timeoutHnd = setTimeout(gridReload,500)
}

function gridReload(){
	var nm_mask = jQuery("#sDs_invdate").val();
	var cd_mask = jQuery("#sDs_name").val();
	jQuery("#${uuid}").jqGrid('setGridParam',{url:"${ctx}/invoice/search?sDs_invdate="+nm_mask+"&sDs_name="+cd_mask,page:1}).trigger("reloadGrid");
}
function enableAutosubmit(state){
	flAuto = state;
	jQuery("#submitButton").attr("disabled",state);
}

});


</script>
