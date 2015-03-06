
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div style="font-size:12px;">
    Here we demonstrate more form editing improvements. Thanks again to Faserline  <code>www.faserline.com</code><br/>
    In this demo: <br/>
    Navigation with up and down keys when in edit and view mode;<br/>
	submit the cahnges pressing the Enter key; <br/>
	check if something is changed in the form and ask the user to confirm. Try to change something in edit mode and submit.
</div>
<br />
<table id="navgrid4"></table>
<div id="pagernav4"></div>

<script  type="text/javascript"> 



jQuery().ready(function (){
	
	$("#navgrid4").jqGrid({        
   	url:'${ctx}/invoice/search',
	datatype: "json",	
   	colNames:['id','单号','日期', '客户', '金额','税费','总计','关闭','快递','备注'],
   	colModel:[
   	          {name: "id",width:1,hidden:true, key:true, sortable:false,search:false},
   		{name:'invno',index:'invno', width:55,sorttype:'int',
   	        	editable:true,editoptions:{size:10},   	        	
   	        	editrules:{required:true,integer:true}
   	    },
   		{name:'invdate',index:'invdate', width:90, sorttype:'date',
   			searchoptions:{
   				dataInit:function (elem) {
   					$(elem).datepicker({
		   				showButtonPanel: true,
		   				numberOfMonths: 3	   			
	   				});
   				}				
   			},
			editable:true,
			editoptions:{size:12,
				dataInit:function(el){
					$(el).datepicker({dateFormat:'yy-mm-dd'});
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
   			formoptions:{ rowpos:3, elmsuffix:"(*)  yyyy-mm-dd" },
			editrules:{required:true}   			
   		},
   		{name:'name',index:'name', width:100,
   			editable:true,editoptions:{size:25}, 
   			formoptions:{ rowpos:2, label: "Name", elmsuffix:"(*)"},
   			editrules:{required:true}
   		},
   		{name:'amount',index:'amount', width:80, align:"right",sorttype:'double',
   			editable:true,editoptions:{size:10}, 
   			formoptions:{ rowpos:6},
   			editrules:{number:true}
   		},
   		{name:'tax',index:'tax', width:80, align:"right",sorttype:'double',    			 
   			editable:true,editoptions:{size:10},
   			formoptions:{ rowpos:7},
   			editrules:{number:true}
   		},		
   		{name:'total',index:'total', width:80,align:"right",sorttype:'double',
   			editable:true,editoptions:{size:10}, 
   			formoptions:{ rowpos:8 },
   			editrules:{number:true}
   		},	
   		{name:'closed',index:'closed',width:55,align:'center',
   			editable:true,edittype:"checkbox",editoptions:{value:":选择;Yes:No",defaultValue:"Yes"},
   			stype:'select',searchoptions:{sopt:['eq','ne'],value:":所有;Yes:是;No:否"},
   			formatter:'checkbox',
   			formoptions:{ rowpos:5 }
   		},
		{name:'shipvia',index:'shipvia',width:70, 
   			editable: true,edittype:"select",editoptions:{value:"${shipviaValue}", defaultValue:'Intime'},   			
   			stype:'select',searchoptions:{sopt:['eq','ne'],dataUrl:'${ctx}/invoice/shipviaSelect'},	
   			formatter:'select',
			formoptions:{ rowpos:4}   			
   		},     		
   		{name:'note',index:'note', width:150, sortable:false,
   			editable: true,edittype:"textarea", 
   			editoptions:{rows:"2",cols:"20"}, 
   			formoptions:{ rowpos:9 }
   		}		
   	],
   	rowNum:10,
   	rowList:[10,20,30],
   	pager: '#pagernav4',
   	sortname: 'invdate',
    viewrecords: true,
    gridview : true,
    sortorder: "desc",
    rownumbers:true,	
    editurl: "${ctx}/invoice/edit",
	caption: "发票列表"
	
});

jQuery("#navgrid4").jqGrid('navGrid','#pagernav4',
		{view:true}, //options
		{jqModal:true,checkOnUpdate:true,savekey: [true,13], navkeys: [true,38,40], checkOnSubmit : true, reloadAfterSubmit:false, closeOnEscape:true, bottominfo:"Fields marked with (*) are required"}, // edit options
		{jqModal:true,checkOnUpdate:true,savekey: [true,13], 
			navkeys: [true,38,40], checkOnSubmit : true, 
			reloadAfterSubmit:false, closeOnEscape:true,
			bottominfo:"Fields marked with (*) are required",
			afterSubmit : function(response, postdata)
		   	{
		   		var res =$.parseJSON(response.responseText);
		   		return [res.success,res.message,res.userdata.id];
		   	}				
		}, // add options
		{reloadAfterSubmit:false,jqModal:false, closeOnEscape:true}, // del options
		{closeOnEscape:true}, // search options
		{navkeys: [true,38,40], height:250,jqModal:false,closeOnEscape:true} // view options
		);


});
</script>
