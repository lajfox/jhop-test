
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div style="font-size:12px;">
    This example show how we can load XML Grid Configuration from external xml file <br>
    A JSON import is also available. Press a Convert link to perform the conversion<br>
</div>
<br />
<table id="xmlist1"></table>
<div id="xmlpager1"></div>

<br />
<a id="cnvxml" href="#">转换</a>
<br>
<br>
<br/>
<a  href="${ctx}/static/testxml.xml">查看例子XML文件</a>
<br>
<br>

<script  type="text/javascript"> 

jQuery().ready(function (){

	$("#cnvxml").click(function(){
		$("#xmlist1").jqGrid('jqGridImport',{impurl:"${ctx}/static/testxml.xml"});
		$(this).hide();
	});


});
</script>
