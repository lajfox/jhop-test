<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/common/taglibs.jsp"%>
<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml" >	
<head>
	<title>cuPlayer JSP Example</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="initial-scale=1,user-scalable=no,maximum-scale=1,width=device-width" />
	<script type="text/javascript" src="${ctx}/static/cuplayer/images/swfobject.js"></script>
</head>
<body>
	
<div class="video" id="CuPlayer">

<script type="text/javascript">
var so = new SWFObject("${ctx}/static/cuplayer/CuPlayerMiniV4.swf","CuPlayerV4","600","410","9","#000000");
so.addParam("allowfullscreen","true");
so.addParam("allowscriptaccess","always");
so.addParam("wmode","opaque");
so.addParam("quality","high");
so.addParam("salign","lt");
so.addVariable("CuPlayerSetFile","${ctx}/static/cuplayer/CuPlayerSetFile.xml"); //播放器配置文件地址,例SetFile.xml、SetFile.asp、SetFile.php、SetFile.aspx
so.addVariable("CuPlayerFile","${ctx}/cuPlayer/view?target=${param['target']}"); //视频文件地址
so.addVariable("CuPlayerImage","${ctx}/static/cuplayer/images/start.jpg");//视频略缩图,本图片文件必须正确
so.addVariable("CuPlayerWidth","600"); //视频宽度
so.addVariable("CuPlayerHeight","410"); //视频高度
so.addVariable("CuPlayerAutoPlay","yes"); //是否自动播放
//so.addVariable("CuPlayerLogo","${ctx}/static/cuplayer/images/Logo.png"); //Logo文件地址
so.addVariable("CuPlayerPosition","bottom-right"); //Logo显示的位置
so.write("CuPlayer");
</script>

</body>
</html>