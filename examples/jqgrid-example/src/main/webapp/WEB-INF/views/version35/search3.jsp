
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div style="font-size:12px;">
    This example demonstartes a new filterToolbar method. When we call this method the search<br>
    is placed above the header columns. This method is similar to filterGrid, but resolves the issue<br>
	related when we resize the coulmns.
</div>
<br />
<table id="s3list"></table>
<div id="s3pager"></div>

<script  type="text/javascript"> 


jQuery().ready(function (){
	
	var mygrid = $("#s3list").jqGrid({        
   	url:'${ctx}/invoice/search',
	datatype: "json",
	
   	colNames:['id','单号','日期', '客户', '金额','税费','总计','关闭','快递','备注'],
   	colModel:[
   	    {name: "id",width:1,hidden:true, key:true, sortable:false,search:false},
   		{name:'invno',index:'invno', width:55,sorttype:'int',
   	    	searchoptions:{sopt:['eq','ne', 'lt', 'le', 'gt', 'ge', 'nu', 'nn'],integer:true}
   	    },
   		{name:'invdate',index:'invdate', width:90, sorttype:'date',
   			searchoptions:{
	   				dataInit:function (elem) {$(elem).datepicker({
	   				showButtonPanel: true,
	   				numberOfMonths: 3,
	   				maxDate:new Date(),
		   			 onClose: function(date) {		   				
		   				$('#s3list').jqGrid('setGridParam',{postData:{invdate:date}});	   				
		   				setTimeout("$('#s3list').trigger('reloadGrid')",1000);	   				
		   			 }
	   				});
   				},
   				searchoptions:{sopt:['eq','ne', 'lt', 'le', 'gt', 'ge', 'nu', 'nn']}
   			}
   		},
   		{name:'name',index:'name', width:100},
   		{name:'amount',index:'amount', width:80, align:"right",sorttype:'double',formatter:'number',
   			searchoptions:{sopt:['eq','ne', 'lt', 'le', 'gt', 'ge', 'nu', 'nn'],number:true}
   		},
   		{name:'tax',index:'tax', width:80, align:"right",sorttype:'double', formatter:'number',
   			edittype:'select', editoptions:{value:":All;0.00:0.00;12:12.00;20:20.00;40:40.00;60:60.00;120:120.00",
   				searchoptions:{sopt:['eq','ne', 'lt', 'le', 'gt', 'ge', 'nu', 'nn'],number:true}
   		}
   		},		
   		{name:'total',index:'total', width:80,align:"right",sorttype:'double',formatter:'number',
   			searchoptions:{sopt:['eq','ne', 'lt', 'le', 'gt', 'ge', 'nu', 'nn'],number:true}
   		},	
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
   	pager: '#s3pager',
   	sortname: 'invdate',
    viewrecords: true,
    gridview : true,
    sortorder: "desc",
    rownumbers:true,		
	caption: "发票列表"
	
});

jQuery("#s3list").jqGrid('navGrid','#s3pager',{edit:false,add:false,del:false,search:false,refresh:false});
jQuery("#s3list").jqGrid('navButtonAdd',"#s3pager",{caption:"展开/收缩",title:"展开/收缩查询工具栏", buttonicon :'ui-icon-pin-s',
	onClickButton:function(){
		mygrid[0].toggleToolbar();
	} 
});
jQuery("#s3list").jqGrid('navButtonAdd',"#s3pager",{caption:"清除",title:"清除查询条件",buttonicon :'ui-icon-refresh',
	onClickButton:function(){
		mygrid[0].clearToolbar();
	} 
});
jQuery("#s3list").jqGrid('filterToolbar',{searchOnEnter : true,stringResult:true});


});
</script>
