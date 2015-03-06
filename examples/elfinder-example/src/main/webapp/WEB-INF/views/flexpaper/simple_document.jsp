<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/common/taglibs.jsp"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" >
    <head>
        <title>FlexPaper AdaptiveUI JSP Example</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta name="viewport" content="initial-scale=1,user-scalable=no,maximum-scale=1,width=device-width" />
        <style type="text/css" media="screen">
			html, body	{ height:100%; }
			body { margin:0; padding:0; overflow:auto; }
			#flashContent { display:none; }
        </style>

		<link rel="stylesheet" type="text/css" href="${ctx}/static/flexpaper/css/flexpaper.css" />
		<script type="text/javascript" src="${ctx}/static/flexpaper/js/jquery.min.js"></script>
		<script type="text/javascript" src="${ctx}/static/flexpaper/js/jquery.extensions.min.js"></script>
		<script type="text/javascript" src="${ctx}/static/flexpaper/js/flexpaper.js"></script>
		<script type="text/javascript" src="${ctx}/static/flexpaper/js/flexpaper_handlers.js"></script>
    </head>
    <body>
		<div id="documentViewer" class="flexpaper_viewer" style="position:absolute;left:10px;top:10px;width:770px;height:500px"></div>
		
		<script type="text/javascript">
			function getDocumentUrl(document){
				return "${ctx}/flexPaper/view?target=${param['target']}&doc={doc}&format={format}&page={page}".replace("{doc}",document);     
			}
			function getDocQueryServiceUrl(document){
				return "${ctx}/flexPaper/view?target=${param['target']}&doc={doc}&format=swfdump&page={page}".replace("{doc}",document);
			}
			var startDocument = "${doc}";

			
			String.format = function() {
				var s = arguments[0];
				for (var i = 0; i < arguments.length - 1; i++) {
					var reg = new RegExp("\\{" + i + "\\}", "gm");
					s = s.replace(reg, arguments[i + 1]);
				}
			
				return s;
			};

			$('#documentViewer').FlexPaperViewer({
				 config : {
					 DOC : escape(getDocumentUrl(startDocument)),
					 Scale : 0.6, 
					 ZoomTransition : 'easeOut',
					 ZoomTime : 0.5, 
					 ZoomInterval : 0.1,
					 FitPageOnLoad : true,
					 FitWidthOnLoad : false, 
					 FullScreenAsMaxWindow : false,
					 ProgressiveLoading : false,
					 MinZoomSize : 0.2,
					 MaxZoomSize : 5,
					 SearchMatchAll : false,
					 RenderingOrder : 'flash,flash',

					 ViewModeToolsVisible : true,
					 ZoomToolsVisible : true,
					 NavToolsVisible : true,
					 CursorToolsVisible : true,
					 SearchToolsVisible : true,

					 DocSizeQueryService : escape(getDocQueryServiceUrl(startDocument)),
					 jsDirectory : '${ctx}/static/flexpaper/js/',
					 localeDirectory : '${ctx}/static/flexpaper/locale/',

					 JSONDataType : 'json',
					

					 WMode : 'window',
					 localeChain: 'zh_CN'
					 }
			});
		</script>
   </body>
</html>