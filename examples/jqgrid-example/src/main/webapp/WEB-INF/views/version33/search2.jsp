
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div style="font-size:12px;">
    This example demonstartes a form search of multi search<br>
    Just click on search button.<br>
    <h2>需要添加plugins/grid.addons.js，不再推荐使用</h2>
</div>
<br />
<table id="s2list"></table>
<div id="s2pager"></div>
<div id="filter" style="margin-left:30%;display:none">查询发票</div>

<script src="${ctx}/static/jqgrid/plugins/grid.addons.js" type="text/javascript"></script>
<script  type="text/javascript"> 

jQuery().ready(function (){
	

	
$("#s2list").jqGrid({        
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
   		{name:'tax',index:'tax', width:80, align:"right",sorttype:'double', 
   			edittype:'select', editoptions:{value:":All;0.00:0.00;12:12.00;20:20.00;40:40.00;60:60.00;120:120.00"}},		
   		{name:'total',index:'total', width:80,align:"right",sorttype:'double'},		
   		{name:'note',index:'note', width:150, sortable:false,search:false}		
   	],
   	rowNum:10,
   	rowList:[10,20,30],
   	pager: '#s2pager',
   	sortname: 'invdate',
    viewrecords: true,
    sortorder: "desc",
   
	
	caption: "发票列表",
    onHeaderClick: function (stat) {
    	if(stat == 'visible' ){
    		jQuery("#filter").css("display","none");
    	}
    }	
	
});

jQuery("#s2list").jqGrid('navGrid','#s2pager',{edit:false,add:false,del:false,search:false,refresh:false});
jQuery("#s2list").jqGrid('navButtonAdd',"#s2pager",{caption:"查找",title:"查找",
	onClickButton:function(){ 
		if(jQuery("#filter").css("display")=="none") {
			jQuery(".HeaderButton","#gbox_s2list").trigger("click");
			jQuery("#filter").show();
		}
	} 
});
jQuery("#s2list").jqGrid('navButtonAdd',"#s2pager",{caption:"清除",title:"清除查询条件",buttonicon:'ui-icon-refresh',
	onClickButton:function(){
		var stat = jQuery("#s2list").jqGrid('getGridParam','search');
		if(stat) {
			var cs = jQuery("#filter")[0];
			cs.clearSearch();
		}
	} 
});
jQuery("#filter").jqGrid('filterGrid',"s2list",
	{
		gridModel:true,
		gridNames:true,
		formtype:"vertical",
		enableSearch:true,		
		autosearch: false,
		searchButton:'查询',
		enableClear:true,
		clearButton:'清除',
		afterSearch : function() {
			jQuery(".HeaderButton","#gbox_s2list").trigger("click");
			jQuery("#filter").css("display","none");
		}
	}
);
jQuery("#sg_invdate","#filter").datepicker({dateFormat:"yy-mm-dd"});


});
</script>
