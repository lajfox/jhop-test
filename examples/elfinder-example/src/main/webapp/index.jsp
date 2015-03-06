<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>elFinder 2.0</title>

		<!-- jQuery and jQuery UI (REQUIRED) -->
		<link rel="stylesheet" type="text/css" media="screen" href="${ctx}/static/jquery-ui-1.9.2.custom/css/smoothness/jquery-ui-1.9.2.custom.min.css">
		<script type="text/javascript" src="${ctx}/static/jquery-ui-1.9.2.custom/js/jquery-1.8.3.js"></script>
		<script type="text/javascript" src="${ctx}/static/jquery-ui-1.9.2.custom/js/jquery-ui-1.9.2.custom.min.js"></script>

		<!-- elFinder CSS (REQUIRED) -->
		<link rel="stylesheet" type="text/css" media="screen" href="${ctx}/static/elfinder/css/elfinder.min.css">
		<link rel="stylesheet" type="text/css" media="screen" href="${ctx}/static/elfinder/css/theme.css">

		<!-- elFinder JS (REQUIRED) -->
		<script type="text/javascript" src="${ctx}/static/elfinder/js/elfinder.min.js"></script>

		<!-- elFinder translation (OPTIONAL) -->
		<script type="text/javascript" src="${ctx}/static/elfinder/js/i18n/elfinder.zh_CN.js"></script>
		
		<script type="text/javascript" src="${ctx}/static/elfinder/elfinder.extend.js"></script>

		<!-- elFinder initialization (REQUIRED) -->
		<script type="text/javascript" charset="utf-8">
			$().ready(function() {
				var elf = $('#elfinder').elfinder({
					height:500,
					url : '${ctx}/connector' , // connector URL (REQUIRED)
					 lang: 'zh_CN'           // language (OPTIONAL)
				});
			});
		</script>
	</head>
	<body>

		<!-- Element where elFinder will be created (REQUIRED) -->
		<div id="elfinder"></div>

	</body>
</html>
