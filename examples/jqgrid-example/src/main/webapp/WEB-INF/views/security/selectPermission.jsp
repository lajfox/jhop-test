<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<c:forEach var="p" items="${list}">
	<c:choose>
		<c:when test="${p.cnt > 0}">
			<option value="${p.id}" selected="selected">${p.name}</option>
		</c:when>
		<c:otherwise>
			<option value="${p.id}">${p.name}</option>
		</c:otherwise>
	</c:choose>
</c:forEach>

