<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fns" uri="/WEB-INF/tlds/fns.tld" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<select>
<option value="">请选择</option>
<c:forEach var="p" items="${list}">	
<option value="${p.itemname}">${p.itemname}</option>		
</c:forEach>
</select>