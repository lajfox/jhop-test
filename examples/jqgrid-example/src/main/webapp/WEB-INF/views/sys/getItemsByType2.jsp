<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="_selected" value="${param['_selected']}"/>
<c:set var="_emptytext" value="${param['_emptytext']}"/>

<c:if test="${not empty _emptytext}">
<option role="option" value="">${_emptytext}</option>
</c:if>

<c:forEach var="p" items="${list}">	
	<c:choose>
	<c:when test="${empty param['_selected'] }">
		<option role="option" value="${p.itemname}">${p.itemname}</option>
	</c:when>
	<c:otherwise>
	
		<c:if test='${fn:indexOf(_selected, p.itemname) != -1}'>
			<option role="option" value="${p.itemname}" selected="selected">${p.itemname}</option>
		</c:if>
		<c:if test='${fn:indexOf(_selected, p.itemname) == -1}'>
			<option role="option" value="${p.itemname}" >${p.itemname}</option>
		</c:if>		
	
	</c:otherwise>
	</c:choose>		
</c:forEach> 

