<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>XXX系统 - 用户登录</title>		
 
 	<link rel="stylesheet" type="text/css" media="screen" href="${ctx}/static/js/lib/jquery-ui-1.9.2.custom/css/redmond/jquery-ui-1.9.2.custom.min.css" />
	<link href="${ctx}/static/js/lib/jquery-validation/1.9.0/validate.css" type="text/css" rel="stylesheet" />   	
    <link rel="stylesheet" type="text/css" href="${ctx}/static/css/user_login.css">
    
	</head>
		
	<body id=userlogin_body>
	<form id="loginForm" action="${ctx}/login" method="post">
		<c:if test="${not empty shiroLoginFailure }">
		<table width="600" align="center">
		<tr><td >
		<div align="center" class="ui-widget" >
			<div class="ui-state-error ui-corner-all" style="padding: 0 .7em;">
				<p><span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
				<strong>错误:</strong> 
				${shiroLoginFailure}
			</p>
			</div>
		</div>
		</td></tr>
		</table>
		<br>
		</c:if>	
		
		<div id=user_login>
			<dl>
				<dd id=user_top>
					<ul>
						<li class=user_top_l></li>
						<li class=user_top_c>						
						
						</li>
						<li class=user_top_r></li>
					</ul>
				</dd>
				<dd id=user_main>
					<ul>
						<li class=user_main_l></li>
						<li class=user_main_c>
												
							<div class=user_main_box>
								
								<ul>
									<li class=user_main_text>
										用户名：
									</li>
									<li class=user_main_input>
										<input class="txtusernamecssclass required" name="username" id="username" type="text"  value="${username}"  >
									</li>
								</ul>
								<ul>
									<li class=user_main_text>
										密&nbsp;&nbsp;&nbsp;&nbsp;码：
									</li>
									<li class=user_main_input>
										<input class="txtpasswordcssclass required" name="password" id="password" type="password" >
									</li>
								</ul>
								
								<ul>
									<li class=user_main_text>
										验证码：
									</li>
									<li class=user_main_input>
										<img id="verifyCodeimg" class="vc-pic" width="65" height="23"
										 title="看不清，点击换一张" alt="看不清，点击换一张" src="${ctx}/static/captcha.jpg">
										
										<input class="vc-text required" id="verifyCode" name="verifyCode" type="text">
									</li>
								</ul>
								
								<ul>
									<li class=user_main_text>										
									</li>
									<li class=user_main_input>
									<input type="checkbox" id="rememberMe" ame="rememberMe" />记住我									
									</li>
								</ul>
																
							</div>
						</li>											
						<li class=user_main_r>
							<input class="ibtnentercssclass"
								style="border-top-width: 0px; border-left-width: 0px; border-bottom-width: 0px; border-right-width: 0px"
								type=image src="${ctx}/static/css/img/login/user_botton.gif">
						</li>
					</ul>
					</dd>
				<dd id=user_bottom>
					<ul>
						<li class=user_bottom_l></li>
						<li class=user_bottom_c>
							<span style="margin-top: 40px" id="messageBox">							
							请输入你的帐号和密码进行登录。										
							</span>	
						</li>
						<li class=user_bottom_r></li>
					</ul>
				</dd>
			</dl>
		</div>
		<div></div>
		</form>

		<script src="${ctx}/static/js/lib/jquery/jquery-1.8.3.min.js" type="text/javascript"></script>
		<script src="${ctx}/static/js/lib/jquery-validation/1.9.0/jquery.validate.min.js" type="text/javascript"></script>
		<script src="${ctx}/static/js/lib/jquery-validation/1.9.0/messages_bs_cn.js" type="text/javascript"></script>
		
		<script>
			
			jQuery.extend(jQuery.validator.messages, {
	        	required: "必填"			
			});
		
			$(document).ready(function() {
				
				$('#verifyCodeimg').click(function(){				
					$(this).attr('src','${ctx}/static/captcha.jpg?_time='+new Date().getTime());
				});	
				
				$("#loginForm").validate({
					submitHandler:function(form){
			            var rememberMe = false;
			            if($('#rememberMe').attr("checked")){
			            	rememberMe = true;
			            }
			           	form.action=form.action+'?rememberMe='+rememberMe;
			            form.submit();
			        }   					
				});
			});
		</script>
		
	</body>
</html>
