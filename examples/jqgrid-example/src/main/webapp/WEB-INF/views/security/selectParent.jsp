<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
<c:forEach var="p" items="${permissions}">
	<c:choose>
		<c:when test="${param['_parentid'] == p.id}">
			<option value="${p.id}" selected="selected">${p.displayName}</option>
		</c:when>
		<c:otherwise>
			<option value="${p.id}">${p.displayName}</option>
		</c:otherwise>
	</c:choose>
</c:forEach>

