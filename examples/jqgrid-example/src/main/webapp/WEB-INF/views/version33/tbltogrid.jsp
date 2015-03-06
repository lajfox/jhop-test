
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div style="font-size:12px;">
    This module is contributed from Peter Romianowski <br>
    The module convert existing html table to Grid<br>
</div>
<br />
<br/>
<a href="#" id="ttogrid">Convert To Grid</a>
<br/><br>
<div style="width:500px;">
<table  id="mytable" border="1">
<thead>
<tr>
<th>Header1</th>
<th>Header2</th>
<th>Header3</th>
</tr>
</thead>
<tbody>
<tr>
<td>Cell 11</td>
<td>Cell 12</td>
<td>Cell 13</td>
</tr>
<tr>
<td>Cell 21</td>
<td>Cell 22</td>
<td>Cell 23</td>
</tr>
<tr>
<td>Cell 31</td>
<td>Cell 32</td>
<td>Cell 33</td>
</tr>
</tbody>
</table>
</div>


<script  type="text/javascript"> 

jQuery().ready(function (){
	
	jQuery("#ttogrid").click(function (){
		tableToGrid("#mytable");
	});

});
</script>
