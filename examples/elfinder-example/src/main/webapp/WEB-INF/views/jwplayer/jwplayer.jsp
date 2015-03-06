<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/common/taglibs.jsp"%>
<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml" >	
<head>
	<title>jwplayer JSP Example</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="initial-scale=1,user-scalable=no,maximum-scale=1,width=device-width" />
	<script type="text/javascript" src="${ctx}/static/jwplayer/jwplayer.js"></script>
</head>
<body>
	
	<div id="myElement" align='center'>Loading the player...</div>

	<script type="text/javascript">
	    jwplayer("myElement").setup({
	    	//file: "${ctx}/static/jwplayer/165122135266.mp3",
	        file: "${ctx}/jwplayer/view?target=${param['target']}",
	        type:'${extension}',
	       /*  playlist: [{
	            file: '${ctx}/jwplayer/view?target=${param['target']}',	           
	            type:'${extension}'
	        }], */
	        image: "${ctx}/static/jwplayer/start.jpg",
	        //stretching : 'uniform',    
            //streamer:"start",    
            //provider:"http",  
	        //fallback: 'false',
	        primary: 'flash',
	        autostart:true,
	        overstretch:true,	       
	        width: '800',
	        height: '600'
	        //aspectratio: '4:3'
	    });
	</script>

</body>
</html>