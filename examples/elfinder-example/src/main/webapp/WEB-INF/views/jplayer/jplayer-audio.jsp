<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset=utf-8 />

<title>Demo : jPlayer </title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="${ctx}/static/jplayer/skin/pink.flag/jplayer.pink.flag.css" rel="stylesheet"	type="text/css" />
<script type="text/javascript" src="${ctx}/static/jquery-ui-1.9.2.custom/js/jquery-1.8.3.js"></script>
<script type="text/javascript" src="${ctx}/static/jplayer/jquery.jplayer.min.js"></script>
<script type="text/javascript">
	      
	$(document).ready(function() {
		

		$("#jquery_jplayer_1").jPlayer(
		{
			ready : function() {
				$(this).jPlayer("setMedia", {
					${extension} : "${ctx}/jplayer/view?target=${param['target']}"
				}).jPlayer("play");
			},							
		
			swfPath : "${ctx}/static/jplayer/",
			supplied : "${extension}",
			wmode : "window",
			keyEnabled: true
		});

		//$(".jp-current-time").unbind("click");
	});
</script>
</head>
<body>
	
	
	<div id="jquery_jplayer_1" class="jp-jplayer"></div>
	<div id="jp_container_1" class="jp-audio">
		<div class="jp-type-single">
			<div class="jp-gui jp-interface">
				<ul class="jp-controls">
					<li><a href="javascript:;" class="jp-play" tabindex="1">play</a></li>
					<li><a href="javascript:;" class="jp-pause" tabindex="1">pause</a></li>
					<li><a href="javascript:;" class="jp-stop" tabindex="1">stop</a></li>
					<li><a href="javascript:;" class="jp-mute" tabindex="1" title="mute">mute</a></li>
					<li><a href="javascript:;" class="jp-unmute" tabindex="1" title="unmute">unmute</a></li>
					<li><a href="javascript:;" class="jp-volume-max" tabindex="1" title="max volume">max volume</a></li>
				</ul>
				<div class="jp-progress">
					<div class="jp-seek-bar">
						<div class="jp-play-bar"></div>

					</div>
				</div>
				<div class="jp-volume-bar">
					<div class="jp-volume-bar-value"></div>
				</div>
				<div class="jp-current-time"></div>
				<div class="jp-duration"></div>
				<ul class="jp-toggles">
					<li><a href="javascript:;" class="jp-repeat" tabindex="1" title="repeat">repeat</a></li>
					<li><a href="javascript:;" class="jp-repeat-off" tabindex="1" title="repeat off">repeat off</a></li>
				</ul>
			</div>
			<div class="jp-title">
				<ul>
					<li>${filename}</li>
				</ul>
			</div>
			<div class="jp-no-solution">
				<span>Update Required</span>
				To play the media you will need to either update your browser to a recent version or update your <a href="http://get.adobe.com/flashplayer/" target="_blank">Flash plugin</a>.
			</div>
		</div>
	</div>
	


</body>

</html>
