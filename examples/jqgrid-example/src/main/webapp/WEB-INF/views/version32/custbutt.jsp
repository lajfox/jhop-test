
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div style="font-size:12px;" class="add-padding">
    这个例子演示三个新方法 <br/>
    1. navButtonAdd - 可以在分页栏上增加我们自己的按钮. <br/>
    2. GridToForm - 这个方法通过列表上的记录Id与表单Id将数据填充到表单中. <br/>
    3. FormToGrid - 这个方法与GridToForm相反，将表单上的数据填充到列表中. <br/>
    在列表上选中一行. 然后单击编辑按钮. 在表单上修改某些数据后单击保存按钮看效果.<br/>
    注意: 数据并没有保存到服务器上.
</div>
<br/>
<br/>
<table id="custbutt"></table>
<div id="pcustbutt"></div>


<script  type="text/javascript"> 

jQuery().ready(function (){
	
$("#custbutt").jqGrid({        
   	url:'${ctx}/invoice/search',
	datatype: "json",
	
   	colNames:['id','单号','日期', '客户', '金额','税费','总计','备注'],
   	colModel:[
   	          {name: "id",width:1,hidden:true, key:true, sortable:false,search:false},
   		{name:'invno',index:'invno', width:55,sorttype:'int',formatter:'integer'},
   		{name:'invdate',index:'invdate', width:90, jsonmap:"invdate",sorttype:'date',
   			searchoptions:{dataInit:function (elem) {$(elem).datepicker();}}},
   		{name:'name',index:'name', width:100},
   		{name:'amount',index:'amount', width:80, align:"right",sorttype:'double',formatter:'number'},
   		{name:'tax',index:'tax', width:80, align:"right",sorttype:'double',formatter:'number'},		
   		{name:'total',index:'total', width:80,align:"right",sorttype:'double',formatter:'number'},		
   		{name:'note',index:'note', width:150, sortable:false,search:false}		
   	],
   	rowNum:10,
   	rowList:[10,20,30],
   	pager: '#pcustbutt',
   	sortname: 'invdate',
    viewrecords: true,
    sortorder: "desc",
    rownumbers:true,
	
	multiselect: false,
	height: '100%',
	caption: "发票列表"
	
});

jQuery("#custbutt").jqGrid('navGrid','#pcustbutt',{edit:false,add:false,del:false});

jQuery("#custbutt").jqGrid('navButtonAdd','#pcustbutt',{
	caption:"编辑",
	buttonicon : "ui-icon-pencil",
	 position : "first",
	onClickButton:function(){
		var gsr = jQuery("#custbutt").jqGrid('getGridParam','selrow');
		if(gsr){
			
				$.jgrid.form_dialog("custbutt",
 					{	title:'修改记录',
 						width:380,
 						height:280,	
 						closeOnEscape : true 						 						
 					},
 					{
 						url: '${ctx}/invoice/version32/custform',
 							async:false
 					});	
				
				$("#custbutt").jqGrid('GridToForm',gsr,"#order");
		} else {
			var positions = $.jgrid.findPositions(290,50);
			$.jgrid.info_dialog('注意',"请选择记录",'',{left:positions[0],top:positions[1]});	
		}							
	} 
});


});
</script>
