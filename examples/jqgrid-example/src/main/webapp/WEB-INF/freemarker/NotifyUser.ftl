
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
<title>用户角色</title>
</head>
<body>
<p>尊敬的${user.name}用户,你好！你具有角色详情如下:</p>
<table>
<tr><td>角色名称</td></tr>
<#list user.roles as item>
<tr>
<td>《${item.name}》</td>
</tr>
</#list>
</table>
</body>
</html>