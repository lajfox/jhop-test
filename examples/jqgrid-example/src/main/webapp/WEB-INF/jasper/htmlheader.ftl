<html>
<head>
  <title></title>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
  <style type="text/css">
    a {text-decoration: none}
    .add-disabled{ disabled:disabled; }
  </style>

<link href="${ctx}/static/js/lib/wijmo-open/themes/rocket/jquery-wijmo.css" rel="stylesheet" type="text/css" />  
<link href="${ctx}/static/js/lib/wijmo-open/themes/wijmo/jquery.wijmo.wijsuperpanel.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/js/lib/wijmo-open/themes/wijmo/jquery.wijmo.wijmenu.css" rel="stylesheet" type="text/css" />

<script src="${ctx}/static/js/lib/jquery/jquery-1.8.3.min.js" type="text/javascript"></script>
<script src="${ctx}/static/js/lib/jquery-ui-1.9.2.custom/js/jquery-ui-1.9.2.custom.min.js" type="text/javascript"></script>

<script src="${ctx}/static/js/lib/wijmo-open/wijmo/minified/jquery.wijmo.wijutil.min.js" type="text/javascript"></script>
<script src="${ctx}/static/js/lib/wijmo-open/wijmo/minified/jquery.wijmo.wijsuperpanel.min.js" type="text/javascript"></script>
<script src="${ctx}/static/js/lib/wijmo-open/wijmo/minified/jquery.wijmo.wijmenu.min.js" type="text/javascript"></script>
<script type="text/javascript">
    $(document).ready(function () {
        $("#downloadMappingsmenu").wijmenu();       
    });
</script>  
</head>
<body text="#000000" link="#000000" alink="#000000" vlink="#000000">
<table width="98%" cellpadding="0" cellspacing="0" border="0">

<tr>

<td width="50%">&nbsp;</td>
<td >

<#if downloadMappings?exists || (showpage && pageSize gt 1) >

<ul id="downloadMappingsmenu">
	
    <li><a href="#"><span class="ui-icon ui-icon-arrowthick-1-s wijmo-wijmenu-icon-left"></span><span class="wijmo-wijmenu-text">导出</span></a>
        <ul>
			<#list downloadMappings as item>
			<li><a href="${ctx}/jasper/multi/${item}?${queryString}">${item}</a></li>	
			</#list>
        </ul>
    </li> 
    
<#if showpage && pageSize gt 1 && pageIndex?exists>	
	
	<#if firstPage?exists>
	<li><a href="${requestURI}?${firstPage}"><span class="ui-icon ui-icon-person wijmo-wijmenu-icon-left"></span><span class="wijmo-wijmenu-text">首页</span></a></li>	
	<#else>
	<li><a calss="add-disabled"><span class="ui-icon ui-icon-person wijmo-wijmenu-icon-left"></span><span class="wijmo-wijmenu-text">首页</span></a></li>
	</#if>
	
	<#if nextPage?exists>	
	<li><a href="${requestURI}?${nextPage}"><span class="ui-icon ui-icon-arrowthick-1-e wijmo-wijmenu-icon-left"></span><span class="wijmo-wijmenu-text">下页</span></a></li>
	<#else>
	<li><a calss="add-disabled"><span class="ui-icon ui-icon-arrowthick-1-e wijmo-wijmenu-icon-left"></span><span class="wijmo-wijmenu-text">下页</span></a></li>
	</#if>
	
	<#if previousPage?exists>	
	<li><a href="${requestURI}?${previousPage}"><span class="ui-icon ui-icon-arrowthick-1-w wijmo-wijmenu-icon-left"></span><span class="wijmo-wijmenu-text">上页</span></a></li>
	<#else>
	<li><a calss="add-disabled"><span class="ui-icon ui-icon-arrowthick-1-w wijmo-wijmenu-icon-left"></span><span class="wijmo-wijmenu-text">上页</span></a></li>
	</#if>
	
	<#if lastPage?exists>	
	<li><a href="${requestURI}?${lastPage}"><span class="ui-icon ui-icon ui-icon-bookmark wijmo-wijmenu-icon-left"></span><span class="wijmo-wijmenu-text">末页</span></a></li>   	
	<#else>
	<li><a calss="add-disabled"><span class="ui-icon ui-icon ui-icon-bookmark wijmo-wijmenu-icon-left"></span><span class="wijmo-wijmenu-text">末页</span></a></li>
	</#if>
			
</#if>    
    
</ul> 
</#if>

</td>
<td width="50%">&nbsp;</td>
</tr>

<tr>
<td width="50%">&nbsp;</td>
<td align="center">