
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div style="font-size:12px;">
    The groupig also works with remoute data. If the option groupDataSorted is turned on then to the sidx parameter is<br/>
    added the grouping column name. This way the server know that it should first sort by the grouping column and then <br/>
    to the sort column.<br/>
</div>
<br />
<table id="${uuid}"></table>
<div id="p${uuid}"></div>

<script  type="text/javascript">
jQuery().ready(function (){
	
$("#${uuid}").jqGrid({        
   	url:'${ctx}/invoice/search/userdata',
	datatype: "json",

   	colNames:['id','单号','日期', '客户', '金额','税费','总计','备注'],
   	colModel:[
   	          {name: "id",width:1,hidden:true, key:true, sortable:false,search:false},
   		{name:'invno',index:'invno', width:55,sorttype:'int',summaryType:'count', summaryTpl : '({0}) total'},
   		{name:'invdate',index:'invdate', width:90, sorttype:'date',
   			searchoptions:{dataInit:function (elem) {$(elem).datepicker();}}},
   		{name:'name',index:'name', width:100},
   		{name:'amount',index:'amount', width:80, align:"right",sorttype:'number',formatter:'number',summaryType:'sum'},
   		{name:'tax',index:'tax', width:80, align:"right",sorttype:'number',formatter:'number',summaryType:'sum'},		
   		{name:'total',index:'total', width:80,align:"right",sorttype:'number',formatter:'number', summaryType:'sum'},		
   		{name:'note',index:'note', width:150, sortable:false,editable:true}		
   	],
   	rowNum:30,
   	rowList:[10,20,30],
   	pager: '#p${uuid}',
   	sortname: 'name',
    viewrecords: true,
    sortorder: "desc",
    rownumbers:true,	
	caption: "发票列表",	
	 grouping: true,
   	groupingView : {
   		groupField : ['name','invdate'],
   		groupColumnShow : [true,true],
   		groupText : ['<b>{0}</b> Sum of totaly: {total}', '{0} Sum of totaly: {total}'],
   		groupCollapse : false,
		groupOrder: ['asc','desc'],
		groupSummary : [false,false],		
		groupDataSorted : true
   	},
    footerrow: true,
    userDataOnFooter: true
	
});

jQuery("#${uuid}").jqGrid('navGrid','#p${uuid}',{add:false,edit:false,del:false});


//设置grid高度
$("#${uuid}").jqGrid('gridHeight',{height:-200});   

});
</script>
