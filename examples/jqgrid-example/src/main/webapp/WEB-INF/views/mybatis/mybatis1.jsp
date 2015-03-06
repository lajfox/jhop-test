
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div style="font-size:12px;">
使用MyBatis演示多表关联查询
</div>
<br />
<table id="${uuid}"></table>
<div id="p${uuid}"></div>




<script  type="text/javascript"> 

jQuery().ready(function (){
	
jQuery("#${uuid}").jqGrid({
    url: "${ctx}/acc/mybatis/search",
    datatype: "json", 
    colNames:["id","personid","Account","Acc Num", "Debit", "Credit","Balance","Person Name","Address","Phone","Create Time"],
   	colModel:[
   		{name:'id',index:'a.id', width:1,hidden:true,search:false},
   		{name:'personid',index:'b.id', width:0,hidden:true,search:false},
   		{name:'name',index:'a.name', width:180, editable:true,editrules:{required:true},editoptions: {title:'请输入个人帐户'}},
   		{name:'num',index:'a.num', width:80, sorttype:"int", editable:true,editrules:{required:true,integer:true,maxValue:999999,minValue:10000}},
   		{name:'debit',index:'a.debit', width:80, align:"right",sorttype:"double", editable:true,editrules:{required:true,number:true}},		
   		{name:'credit',index:'a.credit', width:80,align:"right",sorttype:"double", editable:true,editrules:{required:true,number:true}},		
   		{name:'balance',index:'a.balance', width:80,align:"right",sorttype:"double", editable:true,editrules:{required:true,number:true}}	,
   		
   		{name:'personname',index:'b.name', width:80,
   			editable: true,edittype:"select",
   			editoptions:{dataUrl:'${ctx}/person/select',
   				buildSelect:function(response){
   					var res =$.parseJSON(response);
   					var vh = '<select >';
   					vh += '<option value="">请选择</option>'; 
   					$.each(res.content,function(i,item){   						
   						vh += '<option value="'+this.id+'">'+this.name+'</option>';
   					});
   					vh += '</select>';   					
   					return vh;
   				}
   			},
   			editrules:{required:true}
   		},
   		{name:'address',index:'b.address', width:150},
   		{name:'phone',index:'b.phone', width:80,sorttype:"int"},
   		{name:'createtime',index:'a.createtime', width:80,sorttype:"date",
   			editable:true,
			editoptions:{
				dataInit:function(el){
					$(el).datetimepicker({
						dateFormat:'yy-mm-dd',
						showButtonPanel: true,
		   				numberOfMonths: 3	,
		   				maxDate:new Date()
		   				//controlType: 'select',
		   				//timeFormat: 'hh:mm tt'
					});
				}
			},
			editrules:{required:true} ,
			formatter:'mydatetime'			
   		}
   	],
   
	caption: "MyBatis example",
    ExpandColumn: "name",
    autowidth: true,  
    rowNum: 10,   
    rowList:[10,20,30],
    rownumbers:true,
	height:'auto',
	pager : "#p${uuid}",
	viewrecords: true,
	editurl:'${ctx}/acc/edit',
	sortname: 'id',
	sortorder: "asc"	
    //treeIcons: {leaf:'ui-icon-document-b'},
	   
});

jQuery("#${uuid}").jqGrid('navGrid',"#p${uuid}",
		{}, //options
		{reloadAfterSubmit:true}, // edit options
		{
			reloadAfterSubmit:true,			
 			beforeSubmit:function(postdata, formid){
				//var val = $('#personname').children('option:selected').val();
				postdata['personid'] = postdata['personname'];
				return [true];
			}, 
			afterSubmit : function(response, postdata)
		   	{				
		   		var res =$.parseJSON(response.responseText);
		   		return [res.success,res.message,res.userdata.id];
		   	}				
		}, // add options
		{reloadAfterSubmit:false}, // del options
		{} // search options		
		);
		
//mybatis查询扩展，自动将colModel的index与sorttype提交到后台
//所以使用mybatis查询时非string类型数据必须设置sorttype，不设置
//默认为string
$("#${uuid}").jqGrid('qlsearch');




});



</script>
