
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div style="font-size:12px;">
Toolbar search on local data works too on local data.<br>
<br><br>

</div>
<br />
<table id="toolbar"></table>
<div id="ptoolbar" ></div>

<script  type="text/javascript">
jQuery().ready(function (){


	
$("#toolbar").jqGrid({        
   	url:'${ctx}/invoice/search',
	datatype: "json",

   	colNames:['id','单号','日期', '客户', '金额','税费','总计','关闭','快递','备注'],
   	colModel:[
   	          {name: "id",width:1,hidden:true, key:true, sortable:false,search:false},
   		{name:'invno',index:'invno', width:55,sorttype:'int'},
   		{name:'invdate',index:'invdate', width:90, sorttype:'date',
   			searchoptions:{
   				dataInit:function (elem) {
   					$(elem).datepicker({
	   		   			 onClose: function(date) {		   				
	 		   				//$('#toolbar').jqGrid('setGridParam',{postData:{invdate:date}});	 		   				
	 		   				//setTimeout("$('#toolbar').trigger('reloadGrid')",1000);	   				
	 		   			 }
   					});
   				}
   			}
   		},
   		{name:'name',index:'name', width:100},
   		{name:'amount',index:'amount', width:80, align:"right",sorttype:'float'},
   		{name:'tax',index:'tax', width:80, align:"right",sorttype:'float'},		
   		{name:'total',index:'total', width:80,align:"right",sorttype:'float'},
   		{name:'closed',index:'closed',width:55,align:'center',
   			editable:true,edittype:"checkbox",editoptions:{value:":选择;Yes:No"},
   			stype:'select',searchoptions:{sopt:['eq','ne'],value:":所有;Yes:是;No:否"},
   			formatter:'checkbox'
   		},
		{name:'shipvia',index:'shipvia',width:70, 
   			editable: true,edittype:"select",editoptions:{value:":选择;FE:FedEx;TN:TNT"},
   			formatter:'select',
   			stype:'select',searchoptions:{sopt:['eq','ne'],value:":所有;FE:FedEx;TN:TNT"}
   			},    		
   		{name:'note',index:'note', width:150, sortable:false}		
   	],
   	rowNum:10,
   	rowList:[10,20,30],
   	pager: '#ptoolbar',
   	sortname: 'invdate',
    viewrecords: true,
    sortorder: "desc",
    rownumbers:true,	
	caption: "发票列表",	
   	rowTotal: 2000,
   	gridview: true,	
	loadonce:true
	
});

jQuery("#toolbar").jqGrid('navGrid','#ptoolbar',{del:false,add:false,edit:false,search:false});
jQuery("#toolbar").jqGrid('filterToolbar',{stringResult: true,searchOnEnter : false});


});
</script>
