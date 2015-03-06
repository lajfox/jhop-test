
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>


<!-- autolayoutwidth autolayoutheight layout高度、宽度跟随窗口大小变化 -->	
<div id="layout1" class="no-padding autolayoutwidth autolayoutheight">


	<div id="layout1-north" class="ui-layout-north ui-widget-content ui-corner-all no-scrollbar no-padding"> 
		
		<c:import url="/invoice/layout/layout1-north?_ajax=true" charEncoding="UTF-8"></c:import>
		
	 </div> 

	<div id="layout1-center" class="ui-layout-center ui-widget-content ui-corner-all no-scrollbar no-padding"> 
		
		<c:import url="/acc/layout/layout1-center?_ajax=true" charEncoding="UTF-8"></c:import>
		
	</div> 

	
</div>


	


<script  type="text/javascript">
jQuery().ready(function (){
	
	function northHeight(){
		var vh_north =  $('.ui-state-default-north').height() || 0;
		var vh = $('#layout1-north').height() 
		        - $.jgrid.getGridPagerHeight('layout1northlist')
				-$.jgrid.getGridTitleBarHeight('layout1northlist') 
				- $.jgrid.getGridHdivHeight('layout1northlist')
				- vh_north;	
		return vh;
	}
	
	function centerHeight(){
		var vh_north =  $('.ui-state-default-north').height() || 0;
		var vh_south =  $('.ui-state-default-south').height() || 0;
		var vh = $('#layout1-center').height() 
	    - $.jgrid.getGridPagerHeight('layout1centerlist')
		-$.jgrid.getGridTitleBarHeight('layout1centerlist') 
		- $.jgrid.getGridHdivHeight('layout1centerlist')
		- vh_north - vh_south;
		return vh;
	}	

	var layout1;
	var	layout1_settings = {
			resizerClass: 'ui-state-default',					
			north__size:		'50%' ,					
			center__size:		'50%' ,
			north__onresize: function (pane, $pane, state, options) { 
				$("#layout1northlist").jqGrid('setGridWidth',$pane.innerWidth()-2);
				$("#layout1northlist").jqGrid('setGridHeight',northHeight()); 
			},
			center__onresize: function (pane, $pane, state, options) { 
				$("#layout1centerlist").jqGrid('setGridWidth',$pane.innerWidth()-2);
				$("#layout1centerlist").jqGrid('setGridHeight',centerHeight()); 
			}			
		}; 

	
	layout1 = $("#layout1").layout( layout1_settings );	
	$("#layout1").height($('#centerPane').height()- $.jgrid.getTabsNavHeight('layout1'));
	layout1.resizeAll();	
	
	//将inner layout加入innerLayouts
	var tabid = $('#layout1').parent().attr("id");
	innerLayouts.push({"tabid":tabid,"layout":layout1});
	

	//设置grid高度		
	$("#layout1northlist").jqGrid('setGridHeight',northHeight());  

	//设置grid高度
	$("#layout1centerlist").jqGrid('setGridHeight',centerHeight()); 

});
</script>
