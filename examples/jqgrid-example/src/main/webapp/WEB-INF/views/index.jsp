<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/common/taglibs.jsp"%>

<!DOCTYPE html>
<html >
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>XXX系统</title> 
<link rel="shortcut icon" href="${ctx}/static/images/favicon.png">
  
<link rel="stylesheet" type="text/css" media="screen" href="${ctx}/static/js/lib/jquery-ui-1.9.2.custom/css/cupertino/jquery-ui-1.9.2.custom.min.css" id="jquery-ui-css" />
<link rel="stylesheet" type="text/css" media="screen" href="${ctx}/static/js/lib/jqgrid/plugins/ui.multiselect.css" />

<link rel="stylesheet" type="text/css" media="screen" href="${ctx}/static/js/lib/jqgrid/css/ui.jqgrid.css" />

<link rel="stylesheet" type="text/css" media="screen" href="${ctx}/static/css/sun-ui.css" />

<script type="text/javascript">
	var ctx = '${ctx}';	
</script>
<script data-main="static/js/main" src="${ctx}/static/js/lib/require.js"></script>


</head>
<body>

	<div id="northPane" class="ui-layout-north ui-widget-content no-scrollbar">
	<%@ include file="/WEB-INF/common/head.jsp"%>
	</div>
	
  	<div id="westPane" class="ui-layout-west ui-widget ui-widget-content no-scrollbar">
		<table id="west-grid"></table>
	</div> <!-- #westPane -->
	<div id="centerPane" class="ui-layout-center  ui-widget-content no-scrollbar" ><!-- Tabs pane -->
   
		<div id="tabs" class="jqgtabs">
			<ul>
				<li><a href="#tabs-1">首页</a></li>
			</ul>
			<div id="tabs-1" style="font-size:12px;"  class="add-padding">  
			<ul>
			<li>2013-3-5 增加系统管理</li>
			<li>2013-3-5 Mybatis 从3.1.1升级到3.2.0</li>
			</ul>
			</div>
		</div>
	</div> <!-- #centerPane -->
	
	<div id="southPane" class="ui-layout-south ui-widget-content">
	<%@ include file="/WEB-INF/common/tailer.jsp"%>
	</div>

<!-- jquery-ui-timepicker-->
<link rel="stylesheet" type="text/css" media="screen" href="${ctx}/static/js/lib/timepicker/jquery-ui-timepicker-addon.css" />

<!-- multiselect2side-->
<link rel="stylesheet" type="text/css" media="screen" href="${ctx}/static/js/lib/jquery.multiselect2side/css/jquery.multiselect2side.css" />

<!-- zTree树控件 -->
<link rel="stylesheet" href="${ctx}/static/js/lib/jquery.zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">


	
</body>


</html>