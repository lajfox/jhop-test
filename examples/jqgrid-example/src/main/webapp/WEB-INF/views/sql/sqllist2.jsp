
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div style="font-size:12px;"  class="add-padding">
SQL查询演示
</div>
<br />
<table id="${uuid}"></table>
<div id="p${uuid}"></div>


<script  type="text/javascript"> 

jQuery().ready(function (){
	
	var sqllist2grid =jQuery("#${uuid}").jqGrid({
    url: "${ctx}/acc/sql/search",
    datatype: "json", 
    colNames:["id","personid","Account","Acc Num", "Debit", "Credit","Balance","Person Name","Address","Phone","Create Time"],
   	colModel:[
   		{name:'id',index:'a.id', width:1,hidden:true,search:false},
   		{name:'personid',index:'b.id', width:0,hidden:true,search:false},
   		{name:'name',index:'a.name', width:180, editable:true,editrules:{required:true}},
   		{name:'num',index:'a.num', width:80, sorttype:"int", 
   			editable:true,editrules:{required:true,integer:true,maxValue:999999,minValue:10000},
   			searchoptions:{sopt:['eq','ne', 'lt', 'le', 'gt', 'ge', 'nu', 'nn'],integer:true}
   		},
   		{name:'debit',index:'a.debit', width:80, align:"right",sorttype:"double", 
   			editable:true,editrules:{required:true,number:true},
   			searchoptions:{sopt:['eq','ne', 'lt', 'le', 'gt', 'ge', 'nu', 'nn'],number:true}
   		},		
   		{name:'credit',index:'a.credit', width:80,align:"right",sorttype:"double", 
   			editable:true,editrules:{required:true,number:true},
   			searchoptions:{sopt:['eq','ne', 'lt', 'le', 'gt', 'ge', 'nu', 'nn'],number:true}
   		},		
   		{name:'balance',index:'a.balance', width:80,align:"right",sorttype:"double",
   			editable:true,editrules:{required:true,number:true},
   			searchoptions:{sopt:['eq','ne', 'lt', 'le', 'gt', 'ge', 'nu', 'nn'],number:true}
   		},
   		
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
   		{name:'phone',index:'b.phone', width:80,sorttype:"int",
   			searchoptions:{sopt:['eq','ne', 'lt', 'le', 'gt', 'ge', 'nu', 'nn'],integer:true} 	
   		},
   		{name:'createtime',index:'a.createtime', width:80,sorttype:"date",
   			editable:true,
			editoptions:{
				dataInit:function(el){
					$(el,"#${uuid}").datetimepicker({
						dateFormat:'yy-mm-dd',
						showButtonPanel: true,
		   				numberOfMonths: 3	,
		   				maxDate:new Date()
		   				//controlType: 'select',
		   				//timeFormat: 'HH:mm'
					});
				}
			},
			editrules:{required:true} ,
			formatter:'mydatetime',
			
			searchoptions:{
				dataInit:function(el){
					$(el).datetimepicker({
						dateFormat:'yy-mm-dd',
						showButtonPanel: true,
		   				numberOfMonths: 3	,
		   				maxDate:new Date(),
		   				controlType: 'select',
		   				timeFormat: 'HH:mm',		   				
		   			 	onClose: function(date) {
		   			 		
			   				$('#${uuid}').jqGrid('setGridParam',{postData:{'a.createtime':date}});	   				
			   				setTimeout("$('#${uuid}').trigger('reloadGrid')",1000);
			   			 }		   				
					});
				},
				sopt:['eq','ne', 'lt', 'le', 'gt', 'ge', 'nu', 'nn']
			}			
   		}
   	],
   
	caption: "JPQL example",
    ExpandColumn: "name",
    autowidth: true,  
    rowNum: 10,   
    rowList:[10,20,30],
    rownumbers:true,
	height:'auto',
	pager : "#p${uuid}",
	viewrecords: true,
	editurl:'${ctx}/acc/edit',
	sortname: 'a.id',
	sortorder: "asc"	   
});

jQuery("#${uuid}").jqGrid('navGrid',"#p${uuid}",
		{search:false}, //options
		{reloadAfterSubmit:true}, // edit options
		{
			reloadAfterSubmit:true,			
 			beforeSubmit:function(postdata, formid){
				
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
		{multipleSearch:true} // search options		
		);
		
//sql查询扩展，自动将colModel的index与sorttype提交到后台
//所以使用sql查询时非string类型数据必须设置sorttype，不设置
//默认为string
$("#${uuid}").jqGrid('qlsearch');

jQuery("#${uuid}").jqGrid('navButtonAdd',"#p${uuid}",{caption:"展开/收缩",title:"展开/收缩查询工具栏", buttonicon :'ui-icon-pin-s',
	onClickButton:function(){
		sqllist2grid[0].toggleToolbar()
	} 
});
jQuery("#${uuid}").jqGrid('navButtonAdd',"#p${uuid}",{caption:"清除",title:"清除查询条件",buttonicon :'ui-icon-refresh',
	onClickButton:function(){
		sqllist2grid[0].clearToolbar()
	} 
});

jQuery("#${uuid}").jqGrid('filterToolbar',{searchOnEnter : false,stringResult:true});

});



</script>
