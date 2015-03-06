
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

var _personid;
function personSelrow(rowid){
	var ret = $("#person1").jqGrid('getRowData',rowid);
	_personid = ret.id;						
	$('#personname','#TblGrid_${uuid}').val(ret.name);
	$('#address','#TblGrid_${uuid}').val(ret.address);
	$('#phone','#TblGrid_${uuid}').val(ret.phone);
	$( '#person1_dialog' ).dialog( "close" ); 
}

jQuery().ready(function (){	
	
jQuery("#${uuid}").jqGrid({
    url: "${ctx}/acc/mybatis/search",
    datatype: "json", 
    colNames:["id","personid","Account","Acc Num", "Debit", "Credit","Balance","Person Name","Address","Phone"],
   	colModel:[
   		{name:'id',index:'a.id', width:0,hidden:true,search:false},
   		{name:'personid',index:'b.id', width:0,hidden:true,search:false},
   		{name:'name',index:'a.name', width:180, editable:true,editrules:{required:true}},
   		{name:'num',index:'a.num', width:80, sorttype:"int", editable:true,editrules:{required:true,integer:true,maxValue:999999,minValue:10000}},
   		{name:'debit',index:'a.debit', width:80, align:"right",sorttype:"double", editable:true,editrules:{required:true,number:true}},		
   		{name:'credit',index:'a.credit', width:80,align:"right",sorttype:"double", editable:true,editrules:{required:true,number:true}},		
   		{name:'balance',index:'a.balance', width:80,align:"right",sorttype:"double", editable:true,editrules:{required:true,number:true}}	,
   		
   		{name:'personname',index:'b.name', width:80,
   			editable: true,
   			editoptions:{dataEvents:[
      			  { type: 'click', data: { i: 7 }, 
   	   				fn: function(e) {    	   					
	   	   			 	  $.jgrid.grid_dialog("person1",
	   	   					{	title:'选择人员',
	   	   						width:500,
	   	   						height:400,	   	   					
	   	   						buttons: [ 
	   	   						  { text: "确定", 
	   	   							  click: function() {
	   	   								  var id = $("#person1").jqGrid('getGridParam','selrow');
	   	   									if (id)	{
	   	   										personSelrow(id);	   	   										 
	   	   									} else { 
	   	   										var positions = $.jgrid.findPositions(290,50);
	   	   										$.jgrid.info_dialog2('注意','请选择记录');
	   	   									}				  
	   	   							  } 
	   	   						  },
	   	   						  { text: "取消", 
	   	   							  click: function() { 
	   	   								  $( this ).dialog( "close" ); 
	   	   							  } 
	   	   						  } 		  
	   	   						]	   	   						
	   	   					},
	   	   					{
	   	   						url: '${ctx}/person/mybatis/person'	
	   	   					});     
	   	   					
	   	   				   					
   	   				} 
      			    }],
      			    readonly:true
      			},
   			editrules:{required:true}
   		},
   		{name:'address',index:'b.address', width:150,editable: true,editoptions: {readonly:true}},
   		{name:'phone',index:'b.phone', width:80,sorttype:"int",editable: true,editoptions: {readonly:true}}
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
		{reloadAfterSubmit:false,
			afterShowForm:function(formid){				
				var id = $("#${uuid}").jqGrid('getGridParam','selrow');
				if (id)	{
					var ret = $("#${uuid}").jqGrid('getRowData',id);
					_personid = ret.personid;					
				}
			},
			beforeSubmit:function(postdata, formid){				
				postdata['personid'] = _personid;
				return [true];
			}		
		}, // edit options
		{
			reloadAfterSubmit:false,	
			beforeSubmit:function(postdata, formid){				
				//postdata['personname'] = _personid;
				postdata['personid'] = _personid;
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
		
//mybatis查询扩展，自动将colModel的index与sorttype提交到后台
//所以使用mybatis查询时非string类型数据必须设置sorttype，不设置
//默认为string
$("#${uuid}").jqGrid('qlsearch');		

});



</script>
