<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="shirox" uri="http://shirox.techstar.com/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>


<div id="header">
<div class="headerNav">
	<a class="logo" >标志</a> 
	<span style="padding-left:10px;padding-top:10px; position:absolute;">
 		<h1> ${fns:getConfig("productName")}</h1>
 	</span>
	<ul class="nav">
		<li id="switchEnvBox"><a href="javascript:">（<span>北京</span>）切换城市</a>
			<ul>
				<li><a href="javascript:void(0)">北京</a></li>
				<li><a href="javascript:void(0)">上海</a></li>
				<li><a href="javascript:void(0)">南京</a></li>
				<li><a href="javascript:void(0)">深圳</a></li>
				<li><a href="javascript:void(0)">广州</a></li>
				<li><a href="javascript:void(0)">天津</a></li>
				<li><a href="javascript:void(0)">杭州</a></li>
			</ul>
		</li>
		<li><a href="javascript:void(0)" >捐赠</a></li>
		<li><a href="javascript:void(0)" >设置</a></li>
		<li><a href="javascript:void(0)" target="_blank">博客</a></li>
		<li><a href="javascript:void(0)" target="_blank">微博</a></li>
		<li><a href="javascript:void(0)" target="_blank">论坛</a></li>
		<li>
		<shiro:guest>
		<a href="${ctx}/login" class="easyui-linkbutton">登录</a>
		</shiro:guest>
		<shiro:user>
		你好，<shirox:principal property="name"/></span>，欢迎光临！
		&nbsp;&nbsp;<a href="${ctx}/logout">退出</a>
		</shiro:user>		
		</li>
	</ul>

</div>	
</div>	
