<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
<title><sitemesh:title/></title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />

<%@ include file="/WEB-INF/common/ui.css.jsp"%>
<%@ include file="/WEB-INF/common/jqgrid.css.jsp"%>
<%@ include file="/WEB-INF/common/app.css.jsp"%>

<%@ include file="/WEB-INF/common/jquery.js.jsp"%>
<%@ include file="/WEB-INF/common/ui.js.jsp"%>
<%@ include file="/WEB-INF/common/jqgrid.js.jsp"%>

<sitemesh:head />

</head>

<body>
	
<sitemesh:body />	

<script src="${ctx}/static/jqgrid/plugins/jquery.tablednd.js" type="text/javascript"></script>
<script src="${ctx}/static/jqgrid/plugins/jquery.contextmenu.js" type="text/javascript"></script>

<script src="${ctx}/static/jquery-ui-1.9.2.custom/development-bundle/external/jquery.bgiframe-2.1.2.js" type="text/javascript"></script>
<script src="${ctx}/static/jquery-ui-1.9.2.custom/development-bundle/external/jquery.cookie.js" type="text/javascript"></script>	

<script src="${ctx}/static/jquery-form/jquery.form.js" type="text/javascript"></script>

<%@ include file="/WEB-INF/common/timepicker.jsp"%>
<%@ include file="/WEB-INF/common/m2side.jsp"%>
<%@ include file="/WEB-INF/common/ztree.jsp"%>
	
</body>
</html>