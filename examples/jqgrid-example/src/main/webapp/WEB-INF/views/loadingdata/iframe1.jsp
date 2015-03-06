<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/common/taglibs.jsp"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Iframe 例子1</title>

<link rel="stylesheet" type="text/css" media="screen" href="${ctx}/static/js/lib/jquery-ui-1.9.2.custom/css/cupertino/jquery-ui-1.9.2.custom.min.css" id="jquery-ui-css" />
<link rel="stylesheet" type="text/css" media="screen" href="${ctx}/static/js/lib/jqgrid/css/ui.jqgrid.css" />
<link rel="stylesheet" type="text/css" media="screen" href="${ctx}/static/css/sun-ui.css" />

<script src="${ctx}/static/js/lib/jquery/jquery-1.8.3.js" type="text/javascript"></script>
<script src="${ctx}/static/js/lib/jquery-ui-1.9.2.custom/js/jquery-ui-1.9.2.custom.min.js" type="text/javascript"></script>
<script src="${ctx}/static/js/lib/jquery-ui-1.9.2.custom/development-bundle/ui/i18n/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>

<script src="${ctx}/static/js/lib/jqgrid/js/i18n/grid.locale-cn.js" type="text/javascript"></script>

<script type="text/javascript">
	var ctx = '${ctx}';
</script>
<script src="${ctx}/static/js/lib/jqgrid/plugins/ui.multiselect.js" type="text/javascript"></script>
<script src="${ctx}/static/js/lib/jqgrid/js/jquery.jqGrid.min.js" type="text/javascript"></script>
<script src="${ctx}/static/js/lib/jqgrid/jqGrid.extend.js" type="text/javascript"></script

<script src="${ctx}/static/js/lib/jquery-layout-1.3/themeswitchertool.js" type="text/javascript"></script>

</head>
<body>

<table id="${uuid}" class="autowidth autoheight"></table>
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
	autowidth:true
	
});
$("#${uuid}").jqGrid('navGrid','#p${uuid}',{edit:false,add:false,del:false});


//设置grid高度
$("#${uuid}").jqGrid('gridHeight2');  

});


</script>


</body>
</html>
