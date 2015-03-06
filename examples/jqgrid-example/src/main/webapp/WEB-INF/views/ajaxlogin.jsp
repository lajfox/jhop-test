<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<span>
	<form id="ajaxlogin_form" action="${ctx}/login" method="post"
		class="FormGrid"
		style="width: 100%; overflow: auto; position: relative; height: auto;">
		<table align="center" class="EditTable" cellspacing="0"
			cellpadding="0" border="0">
			<tbody>
				<tr>
					<td class="ui-state-error" colspan="2">
						<div align="center" class="ui-widget ui-corner-all">
							<div class="ui-state-error ui-corner-all"
								style="padding: 0 .7em;">
								<p>
									<span class="ui-icon ui-icon-alert"
										style="float: left; margin-right: .3em; text-algin: center;"></span>
									<span id="ajaxlogin_error">登录超时，请重新登录！</span>
								</p>
							</div>
						</div>
					</td>
				</tr>

				<tr class="FormData">
					<td class="CaptionTD" align="right">用户名：</td>
					<td class="DataTD"><input id="username" name="username"
						class="FormElement ui-widget-content ui-corner-all" type="text"></td>
				</tr>
				<tr class="FormData">
					<td class="CaptionTD" align="right">密&nbsp;&nbsp;&nbsp;&nbsp;码：</td>
					<td class="DataTD"><input id="password" name="password"
						class="FormElement ui-widget-content ui-corner-all"
						type="password"></td>
				</tr>
				<tr class="FormData">
					<td class="CaptionTD" align="right">验证码：</td>
					<td class="DataTD"><img id="verifyCodeimg" width="65"
						height="23" title="点击刷新验证码" src="${ctx}/static/captcha.jpg">
						<input id="verifyCode" name="verifyCode" size="8"
						class="FormElement ui-widget-content ui-corner-all" type="text"></td>
				</tr>

				<tr class="FormData">
					<td class="CaptionTD"></td>
					<td class="DataTD"><input id="rememberMe" name="rememberMe"
						type="checkbox"
						class="FormElement ui-widget-content ui-corner-all">记住我</td>
				</tr>

			</tbody>
		</table>
	</form>
	
	<table 
		class="EditTable" cellspacing="0" cellpadding="0" border="0">
		<tbody>
			<tr>
				<td colspan="2">
					<hr class="ui-widget-content" style="margin: 1px">
				</td>
			</tr>
			<tr >
				<td class="navButton"><a 
					class="fm-button ui-state-default ui-corner-left"
					href="javascript:void(0)" style="display: none;"> <span
						class="ui-icon ui-icon-triangle-1-w"></span>
				</a> <a class="fm-button ui-state-default ui-corner-right"
					href="javascript:void(0)" style="display: none;"> <span
						class="ui-icon ui-icon-triangle-1-e"></span>
				</a></td>
				<td class="EditButton"><a id="ajaxlogin_sData"
					class="fm-button ui-state-default ui-corner-all fm-button-icon-left"
					href="javascript:void(0)"> 提交 <span
						class="ui-icon ui-icon-disk"></span>
				</a> <a id="ajaxlogin_cData"
					class="fm-button ui-state-default ui-corner-all fm-button-icon-left"
					href="javascript:void(0)"> 取消 <span
						class="ui-icon ui-icon-close"></span>
				</a></td>
			</tr>
			<tr class="binfo" style="display: none">
				<td class="bottominfo" colspan="2"></td>
			</tr>
		</tbody>
	</table>
		
</span>

<script src="${ctx}/static/js/ajaxlogin.js" type="text/javascript"></script>
