<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<select>
<c:forEach var="map" items="${shipviaSelect}">
<option value="${map.key}">${map.value}</option>
</c:forEach>
</select>