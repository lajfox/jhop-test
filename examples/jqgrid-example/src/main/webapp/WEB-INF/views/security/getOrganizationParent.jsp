<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/common/taglibs.jsp"%>

<option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
<c:forEach var="p" items="${organizations}">
	<c:choose>
		<c:when test="${param['_parentid'] == p.id}">
			<option value="${p.id}" selected="selected">${fn:length(p.name)>20?fn:substring(p.name,0,20):p.name}</option>
		</c:when>
		<c:otherwise>
			<option value="${p.id}">${fn:length(p.name)>20?fn:substring(p.name,0,20):p.name}</option>
		</c:otherwise>
	</c:choose>
</c:forEach>


