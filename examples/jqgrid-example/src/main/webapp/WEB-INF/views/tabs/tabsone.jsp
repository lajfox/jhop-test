
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div id="tabsone">
<ul>
<li><a href="${ctx}/invoice/tabs/tabsonetab1?_ajax=true">Tab 1</a></li>
<li><a href="${ctx}/acc/tabs/tabsonetab2?_ajax=true">Tab 2</a></li>
<li><a href="${ctx}/invoice/tabs/tabsonetab3?_ajax=true">Tab 3</a></li>
</ul>
</div>


<script  type="text/javascript"> 

jQuery().ready(function (){
	//cache 默认为false，无缓存。这个选项用于ajax调用，简单的说无缓存，就是每次发送
	//请求都刷新；有缓存就是第一次请求刷新，以后就不刷新了	
	 $( "#tabsone" ).tabs({ 
		 cache: true,
		 //一个远程(ajax)选项卡的内容被加载完成后触发该事件
		 load:function(event, ui){
            var grids= $.jgrid.getGridNames($('.ui-jqgrid','#'+ui.panel.id,"#tabsone"));
            if(grids && grids.length > 0){
            	var tabid = $('#'+ui.panel.id,"#tabsone").parent().parent().attr("id");
            	tabgrids.push({"tabid":tabid,"grids":grids});
            }            
		 }
	 });	

});
</script>
