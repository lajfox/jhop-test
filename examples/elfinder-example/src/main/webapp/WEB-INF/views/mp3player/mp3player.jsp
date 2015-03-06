<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/common/taglibs.jsp"%>
<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml" >	
<head>
	<title>cuPlayer JSP Example</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="initial-scale=1,user-scalable=no,maximum-scale=1,width=device-width" />
	<script type="text/javascript" src="${ctx}/static/mp3player/images/swfobject.js"></script>
</head>
<body>
	
<div class="video" id="CuPlayer">

<script type="text/javascript">
var so = new SWFObject("${ctx}/static/mp3player/mp3CutePlayer.swf","player","120","110","9","#000000");
so.addParam("allowfullscreen","true");
so.addParam("allowscriptaccess","always");
so.addParam("wmode","transparent");
so.addParam("quality","high");
so.addParam("scale","noscale");
so.addParam("salign","lt");
so.addVariable("xmlFile","${ctx}/mp3Player/songs.xml?target=${param['target']}");//播放器配置文件可以是xml文件，也可以是程序文件asp,php,jsp,aspx
so.write("CuPlayer");
</script>

</body>
</html>