
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
	
	var mygrid = jQuery("#${uuid}").jqGrid({
    url: "${ctx}/acc/mybatis/search",
    datatype: "json", 
    colNames:["id","Account","Acc Num", "Debit", "Credit","Balance","Person Name","Address","Phone"],
   	colModel:[
   		{name:'id',index:'a.id', width:1,hidden:true,search:false},
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
   		
   		{name:'personname',index:'b.name', width:80},
   		{name:'address',index:'b.address', width:150},
   		{name:'phone',index:'b.phone', width:80,sorttype:"int",
   			searchoptions:{sopt:['eq','ne', 'lt', 'le', 'gt', 'ge', 'nu', 'nn'],integer:true}   			
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

jQuery("#${uuid}").jqGrid('navGrid','#p${uuid}',{search:false});
jQuery("#${uuid}").jqGrid('navButtonAdd',"#p${uuid}",{caption:"展开/收缩",title:"展开/收缩查询工具栏", buttonicon :'ui-icon-pin-s',
	onClickButton:function(){
		mygrid[0].toggleToolbar()
	} 
});
jQuery("#${uuid}").jqGrid('navButtonAdd',"#p${uuid}",{caption:"清除",title:"清除查询条件",buttonicon :'ui-icon-refresh',
	onClickButton:function(){
		mygrid[0].clearToolbar()
	} 
});

jQuery("#${uuid}").jqGrid('filterToolbar',{searchOnEnter : false,stringResult:true});
		
//mybatis查询扩展，自动将colModel的index与sorttype提交到后台
//所以使用mybatis查询时非string类型数据必须设置sorttype，不设置
//默认为string
$("#${uuid}").jqGrid('qlsearch');		

});



</script>
