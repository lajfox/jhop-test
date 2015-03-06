<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="uuid" value="${param['uuid']}"/>

<span>
	<form method="post" id="Form${uuid}"
		name="Form${uuid}" class="FormGrid"
		 action="${ctx}/security/user/changePassword"
		style="width: 100%; overflow: auto; position: relative; height: auto;">
      
        <input type="hidden" name="id" value="${id}"/>
		<table class="EditTable" cellspacing="0" cellpadding="0" border="0">
			<tbody>
				<tr id="FormError${uuid}" style="display: none;">
					<td id="FormError${uuid}td" class="ui-state-error"
						colspan="2"></td>
				</tr>
				
				<tr class="FormData">
					<td class="CaptionTD">旧密码:</td>
					<td class="DataTD"><input type="password" name="password" id="password"  /></td>
				</tr>
				<tr class="FormData">
					<td class="CaptionTD">新密码:</td>
					<td class="DataTD"><input type="password"  name="plainPassword" id="plainPassword" /></td>
				</tr>
				<tr class="FormData">
					<td class="CaptionTD">确认新密码:</td>
					<td class="DataTD"><input type="password"  name="plainPassword2" id="plainPassword2" /></td>
				</tr>
			</tbody>
		</table>



		<table class="EditTable" cellspacing="0" cellpadding="0" border="0">
			<tbody>
				<tr>
					<td colspan="2">
						<hr class="ui-widget-content" style="margin: 1px">
					</td>
				</tr>
				<tr>
					<td class="navButton"><a
						class="fm-button ui-state-default ui-corner-left"
						href="javascript:void(0)" style="display: none;"> <span
							class="ui-icon ui-icon-triangle-1-w"></span>
					</a> <a class="fm-button ui-state-default ui-corner-right"
						href="javascript:void(0)" style="display: none;"> <span
							class="ui-icon ui-icon-triangle-1-e"></span>
					</a></td>
					<td class="EditButton">
					
					<input type="submit"						
						class="fm-button ui-state-default ui-corner-all "
						value="&nbsp;&nbsp;提交&nbsp;&nbsp;"> 
						
											
						<input type="button" id="cData${uuid}"
						class="fm-button ui-state-default ui-corner-all"
						value="&nbsp;&nbsp;取消&nbsp;&nbsp;">  
					</td>
				</tr> 
			</tbody>
		</table>
	</form>
	

    
</span>

<script src="${ctx}/static/jquery-form/jquery.form.js" type="text/javascript"></script>
<script type="text/javascript">
	jQuery().ready(
			function() {

				var options = {					 
					beforeSubmit : function(formData, jqForm, options) {

						var $password = $.trim($('#password').val());
						if (!$password) {
							$("#FormError${uuid}").show();
							$("#FormError${uuid}td").html('请输入旧密码');
							return false;
						}
						
						var $newpwd = $.trim($('#plainPassword').val());
						if (!$newpwd) {
							$("#FormError${uuid}").show();
							$("#FormError${uuid}td").html('请输入新密码');
							return false;
						} 
						
						if($password == $newpwd){
							$("#FormError${uuid}").show();
							$("#FormError${uuid}td").html('新密码与旧密码不能相同');
							return false;	
						}
						
						var $confirmnewpwd = $.trim($('#plainPassword2').val());
						if (!$confirmnewpwd) {
							$("#FormError${uuid}").show();
							$("#FormError${uuid}td").html('请输入确认密码');
							return false;
						} 
						
						var arr = $.jgrid.checkPassword({value:$newpwd,name:'新密码',password:'plainPassword2'}); 
						if(arr[0] == false){
							$("#FormError${uuid}").show();
							$("#FormError${uuid}td").html(arr[1]);
							return arr[0];
						}					

						
						$("#FormError${uuid}").hide();
						return true;
					},
					success : function(responseText, statusText, xhr, $form) {
						$("#FormError${uuid}").show();
						$("#FormError${uuid}td").html(responseText.message);	
						if(responseText.success){							
							$("#editcnt${uuid}").dialog("close");
							$.jgrid.info_dialog2('注意',responseText.message);	
						}
					},
					
					type : 'post',
					dataType : 'json',
					clearForm : true,
					resetForm : true				
				};

				// bind form using 'ajaxForm' 
				$("#Form${uuid}").ajaxForm(options);							

				$("#cData${uuid}").click(function(e) {
					$("#editcnt${uuid}").dialog("close");					
				});

			});
</script>
