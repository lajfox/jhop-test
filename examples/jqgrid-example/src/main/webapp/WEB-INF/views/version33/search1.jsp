
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div style="font-size:12px;">
    This example demonstartes the new multi serach feature of jqGrid.<br>
    The search fields are in the toolbar. Click on search button  to toggle the search and enjoy<br>
     <h2>需要添加plugins/grid.addons.js，不再推荐使用</h2>
</div>
<br />
<table id="s1list"></table>
<div id="s1pager"></div>

<script src="${ctx}/static/jqgrid/plugins/grid.addons.js" type="text/javascript"></script>
<script  type="text/javascript"> 

jQuery().ready(function (){
	

	
$("#s1list").jqGrid({        
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
   	pager: '#s1pager',
   	sortname: 'invdate',
    viewrecords: true,
    sortorder: "desc",
    
	toolbar : [true,"top"],
	caption: "发票列表"
	
});

jQuery("#t_s1list").height(25).hide().jqGrid('filterGrid',"s1list",
		{gridModel:true,gridToolbar:true});
jQuery("#sg_invdate").datepicker({dateFormat:"yy-mm-dd"});

jQuery("#s1list").jqGrid('navGrid','#s1pager',{edit:false,add:false,del:false,search:false,refresh:true});
jQuery("#s1list").jqGrid('navButtonAdd',"#s1pager",{caption:"查找",title:"查找",
	onClickButton:function(){ 
		if(jQuery("#t_s1list").css("display")=="none") {
			jQuery("#t_s1list").css("display","");
		} else {
			jQuery("#t_s1list").css("display","none");
		}
		
	} 
});


});
</script>
