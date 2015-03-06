<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<c:set var="id" value="${param['id']}"/>
<c:set var="url" value="${param['url']}"/>
<c:set var="frameborder" value="${param['frameborder']}"/>
<c:set var="scrolling" value="${param['scrolling']}"/>
<c:set var="width" value="${param['width']}"/>
<c:set var="height" value="${param['height']}"/>

<iframe id="${id}" name="${id}" src="${url}" frameborder="${frameborder}" scrolling="${scrolling}" width="${width}" height="${height}"></iframe>

