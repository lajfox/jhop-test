
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div style="font-size:12px">
This a real world example. Here we demonstarte a new feature of the formatter where the check box can be edited.<br>
Point to column Enabled and right click a mouse. Mark or unmark child nodes.<br>
We achive this using the context menu plugin from Chris Domigan.<br>
Note: This will not work in Opera browsers, since Opera does not support context menu 
</div>
<br />
<table id="treegridadv"></table>
<div id="ptreegridadv"></div>
<div class="contextMenu" id="myMenu1" style="display: none;">
	<ul>
   		<li id="mchild">Mark Childs</li>
        <li id="umchild">Unmark Child</li>
    </ul>
</div>


<script  type="text/javascript"> 

jQuery().ready(function (){
	
jQuery("#treegridadv").jqGrid({
    url: "${ctx}/acc/search",
    datatype: "json",
    colNames:["id","Account","Acc Num", "Debit", "Credit","Balance","Enabled"],
   	colModel:[
   		{name:'id',index:'id', width:1,hidden:true,key:true},
   		{name:'name',index:'name', width:180},
   		{name:'num',index:'num', width:80, align:"center",sorttype:"int"},
   		{name:'debit',index:'debit', width:80, align:"right",sorttype:"double"},		
   		{name:'credit',index:'credit', width:80,align:"right",sorttype:"double"},		
   		{name:'balance',index:'balance', width:80,align:"right",sorttype:"double"},
   		{name:'enbl', index:'enbl', width: 60, align:'center', formatter:'checkbox', editoptions:{value:'1:0'}, formatoptions:{disabled:false}}
   	],
    treeGrid: true,
    treeGridModel: "adjacency",
	caption: "Treegrid example",
    ExpandColumn: "name",
    autowidth: true,  
    rowNum: 200,   
	height:'auto',
	pager : "#ptreegridadv",
	viewrecords: true,
	sortname: 'id',
	sortorder: "asc",
    treeIcons: {leaf:'ui-icon-document-b'}
	   
});

var ci,rowid,ptr,td;
$('#treegridadv').contextMenu('myMenu1', {
      bindings: {
        'mchild': function(t) {
        	if(ptr && rowid && ci >=1) {
        		var gcn = $("#treegridadv").jqGrid('getFullTreeNode',ptr);
				$(gcn).each(function(i,v){
					$("td:eq("+ci+")",this).each(function(){
						if(!$("input[type='checkbox']",this).attr("checked")) {
							$(this).toggleClass("changed");
							$("input[type='checkbox']",this).attr("defaultChecked",true).attr("checked","checked");
						}
					});
				});
				ptr = rowid=ci=null;
        	}
        },
        'umchild': function(t) {
        	if(ptr && rowid && ci >=1) {
        		var gcn = $("#treegridadv").jqGrid('getFullTreeNode',ptr);
				$(gcn).each(function(){
					$("td:eq("+ci+")",this).each(function(){
						if($("input[type='checkbox']",this).attr("checked")) {
							$(this).toggleClass("changed");
							$("input[type='checkbox']",this).removeAttr("checked").attr("defaultChecked","");
						}
					});
				});
				ptr = rowid=ci=null;
        	}
        }
      },
      onContextMenu: function(e, menu) {
		td = e.target || e.srcElement;
		ptr = $(td).parents("tr.jqgrow")[0];
		ci = !$(td).is('td') ? $(td).parents("td:first")[0].cellIndex : td.cellIndex;
		if($.browser.msie) {
			ci = $.jgrid.getCellIndex(td);
		}
		if( ci >=1 ) {
			rowid = ptr.id;
			$('#treegridadv').jqGrid('setSelection',rowid,false);
			return true;
		} else {
		//alert(ptr.id+" : "+ptr.rowIndex+" : "+ci);
			return false;
		}
	  }
});
$("#jqContextMenu").addClass("ui-widget ui-widget-content").css("font-size","12px");

//设置grid高度
$("#treegridadv").jqGrid('gridHeight',{height:-80});  

});
</script>
