<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<title>Highcharts export server</title>


</head>
<body>

	<div id="wrap">
		<form id="exportForm" action="${ctx}/highcharts/export" method="POST">
			<p>Use this page to experiment with the different options.</p>

			<input id="options" title="Highcharts config object" type="radio"
				name="content" value="options"> <label for="options"
				class="radio">Highcharts config object (JSON)</label> <input
				id="svg" title="svg xml content" type="radio" name="content"
				value="svg"> <label for="svg" class="radio">SVG
				(XML) </label>

			<div id="options">
				<label id="options" for="options">Options</label>
				<div id="oneline" class="info">Specify here your Highcharts
					configuration object.</div>
				<textarea id="options" name="options" rows="12" cols="30"><%@include
						file="/WEB-INF/jspf/config.js"%></textarea>
			</div>
			<div id="svg"></div>
			<label for="type">Image file format</label> <select name="type">
				<option value="image/png">image/png</option>
				<option value="image/jpeg">image/jpeg</option>
				<option value="image/svg+xml">image/svg+xml</option>
				<option value="application/pdf">application/pdf</option>
			</select> <br /> <label for="width">Width</label>
			<div class="info">The exact pixelwidth of the exported image.
				Defaults to chart.width or 600px. Maximum width is set to 2000px</div>
			<input id="width" name="width" type="text" value="" /> <br/> <label
				for="scales">Scale</label> <input id="scale" name="scale"
				type="text" value="" />
			<div class="info">Give in a scaling factor for a higher image
				resolution. Maximum scaling is set to 4x. Remember that the width parameter has a higher
				precedence over scaling.</div>
			<br /> <label for="constructor">Constructor</label>
			<div class="info">
				This will be either 'Chart' or 'StockChart' depending on if <br />you
				want a Highcharts or an HighStock chart.
			</div>
			<select name="constr">
				<option value="Chart">Chart</option>
				<option value="StockChart">StockChart</option>
			</select> </br> <br /> <label for="callback">Callback</label>
			<div id="oneline" class="info">The callback will be fired after
				the chart is created.</div>
			<textarea id="callback" name="callback" rows="12" cols="30" /><%@include
					file="/WEB-INF/jspf/callback.js"%>
			</textarea>
			<input id="submit" type="submit" value="Generate Image">
		</form>
	</div>
	<div id="toggle">
		<label id="svg" for="svg">Svg Content</label>
		<div id="oneline" class="info">Paste in 'raw' svg markup.</div>
		<textarea id="svg" name="svg" rows="12" cols="30"><%@include
				file="/WEB-INF/jspf/lexl.svg"%>
		</textarea>
	</div>
</body>
</html>
