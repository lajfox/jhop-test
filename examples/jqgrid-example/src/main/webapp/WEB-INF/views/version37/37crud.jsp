
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div style="font-size:12px;">
    Adding,Updating and Deleteing is possible on local data too. In this example we use a form edit.<br/>
    <br/><br/>
</div>
<br />
<table id="crud"></table>
<div id="pcrud"></div>

<script  type="text/javascript">
jQuery().ready(function (){


	
$("#crud").jqGrid({        
   	url:'${ctx}/invoice/search',
	datatype: "json",

   	colNames:['id','单号','日期', '客户', '金额','税费','总计','关闭','快递','备注'],
   	colModel:[
   	          {name: "id",width:1,hidden:true, key:true, sortable:false,search:false},
   		{name:'invno',index:'invno', width:55,sorttype:'int',editable:true,editrules:{required:true,integer:true}},
   		{name:'invdate',index:'invdate', width:90, sorttype:'date',
   			searchoptions:{
   				dataInit:function (elem) {
   					$(elem).datepicker({
		   				showButtonPanel: true,
		   				numberOfMonths: 3	,
		   				maxDate:new Date()
	   				});
   				}				
   			},
   			editable:true,
			editoptions:{size:12,
				dataInit:function(el){
					$(el).datepicker({
						dateFormat:'yy-mm-dd',
						showButtonPanel: true,
		   				numberOfMonths: 3	,
		   				maxDate:new Date()						
					});
				},
				defaultValue: function(){
					var currentTime = new Date();
					var month = parseInt(currentTime.getMonth() + 1);
					month = month <= 9 ? "0"+month : month;
					var day = currentTime.getDate();
					day = day <= 9 ? "0"+day : day;
					var year = currentTime.getFullYear();
					return year+"-"+month + "-"+day;				
				}
			}, 
			editrules:{required:true}    			
   		},
   		{name:'name',index:'name', width:100,editable:true,editrules:{required:true}},
   		{name:'amount',index:'amount', width:80, align:"right",sorttype:'float',editable:true,editrules:{required:true,number:true}},
   		{name:'tax',index:'tax', width:80, align:"right",sorttype:'float',editable:true,editrules:{required:true,number:true}},		
   		{name:'total',index:'total', width:80,align:"right",sorttype:'float',editable:true,editrules:{number:true}},
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
   		{name:'note',index:'note', width:150, sortable:false,editable: true,edittype:"textarea", 
   	   			editoptions:{rows:"2",cols:"20"}}		
   	],
   	rowNum:10,
   	rowList:[10,20,30],
   	pager: '#pcrud',
   	sortname: 'invdate',
    viewrecords: true,
    sortorder: "desc",
    rownumbers:true,	
	caption: "发票列表",	
   	rowTotal: 2000,
   	gridview: true,	
    editurl: "${ctx}/invoice/edit",
	loadonce:true
	
});

jQuery("#crud").jqGrid('navGrid','#pcrud',
		{view:true},
		{}, // edit options
		{jqModal:true,
			 reloadAfterSubmit:false,			
			afterSubmit : function(response, postdata)
		   	{
		   		var res =$.parseJSON(response.responseText);		   		
		   		return [res.success,res.message,res.userdata.id];
		   	}				
		} // add options		
);

});
</script>
