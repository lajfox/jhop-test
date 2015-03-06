<html>
<head>
<title>
${title?if_exists}
</title>
<meta http-equiv="Content-Type" content="${contentType?if_exists}" />
</head>
<body BGCOLOR="#ffffff" LINK="#000099">

<div align='center'>
<br>

<object width="${width}" height="${height}">
  <param name="movie" value="${ctx}/${swf}"/>
  <embed src="${ctx}/${swf}" 
    FlashVars="jrpxml=${ctx}/${report_url}" 
    width="${width}" height="${height}">
  </embed>
</object>

</div>


</body>
</html>
