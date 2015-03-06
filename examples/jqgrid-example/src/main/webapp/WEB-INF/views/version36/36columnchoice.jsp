
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div style="font-size:12px;">
    Thanks again to Mark Williams we have other great feature - Column Chooser and Reordering.<br>
    This is done with a simple method call like jQuery("#colch").jqGrid('columnChooser');<br>
    This feature depend on jQuery UI sortable widget, jQuery UI dilog widget <br/>
    and multiselect plugin provided from Michael Aufreiter and Yanick Rochon.<br/>
    Click on Columns button in the navigator and enjoy.<br/>
<br />
<table id="colch"></table>
<div id="pcolch"></div>

<script  type="text/javascript">
jQuery().ready(function (){
	
$("#colch").jqGrid({        
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
   	pager: '#pcolch',
   	sortname: 'invdate',
    viewrecords: true,
    sortorder: "desc",
    rownumbers:true,	
	caption: "发票列表",	
	sortable: true
	
});

jQuery("#colch").jqGrid('navGrid','#pcolch',{add:false,edit:false,del:false,search:false,refresh:false});
jQuery("#colch").jqGrid('navButtonAdd','#pcolch',{
    caption: "显示/隐藏列",
    title: "显示/隐藏列",
    onClickButton : function (){
        jQuery("#colch").jqGrid('columnChooser');
    }
});

});
</script>
