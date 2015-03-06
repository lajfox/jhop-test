<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<script type="text/javascript">
	jQuery().ready(function() {

		var options = {

			success : function(responseText, statusText, xhr, $form) {
				if (responseText.success) {
					$.jgrid.info_dialog2('提示', '邮件发送成功');
				} else {
					$.jgrid.error_dialog2('提示', '邮件发送佚败');
				}
			},
			type : 'post',
			dataType : 'json'
		};

		$('#sData2', '#mailButton2').click(function(e) {
			$("#mailForm2").ajaxSubmit(options);
		});

	});
</script>

<div class="add-padding">
	发送html邮件并带附件演示，采用freemark模板，模板参见/WEB-INF/freemark/NotifyUser.ftl</div>

<div class="add-padding">

	<span>
		<form method="post" name="mailForm2" id="mailForm2" class="FormGrid"
			action="${ctx}/mail/send2" enctype="multipart/form-data"
			style="width: 100%; overflow: auto; position: relative; height: auto;">


			<table class="EditTable" cellspacing="0" cellpadding="0" border="0">
				<tbody>
					<tr class="FormData">
						<td class="CaptionTD">收件人</td>
						<td class="DataTD"><input type="text"
							class="FormElement ui-widget-content ui-corner-all" name="to"
							id="to" value="sundoctor@21cn.com" /></td>
					</tr>

					<tr class="FormData">
						<td class="CaptionTD">抄送</td>
						<td class="DataTD"><input type="text"
							class="FormElement ui-widget-content ui-corner-all" name="cc"
							id="cc" /></td>
					</tr>

					<tr class="FormData">
						<td class="CaptionTD">暗送</td>
						<td class="DataTD"><input type="text"
							class="FormElement ui-widget-content ui-corner-all" name="bcc"
							id="bcc" /></td>
					</tr>

					<tr class="FormData">
						<td class="CaptionTD">标题</td>
						<td class="DataTD"><input type="text"
							class="FormElement ui-widget-content ui-corner-all"
							name="subject" id="subject" value="HTML测试" /></td>
					</tr>


					<tr class="FormData">
						<td class="CaptionTD">附件</td>
						<td class="DataTD"><input type="file"
							class="FormElement ui-widget-content ui-corner-all"
							name="attachment_file" id="attachment_file"  /></td>
					</tr>


				</tbody>
			</table>

		</form>

		<table id="mailButton2" class="EditTable" cellspacing="0"
			cellpadding="0" border="0">
			<tbody>
				<tr>
					<td colspan="2">
						<hr class="ui-widget-content" style="margin: 1px">
					</td>
				</tr>
				<tr id="Act_Buttons">
					<td class="navButton"><a id="pData"
						class="fm-button ui-state-default ui-corner-left"
						href="javascript:void(0)" style="display: none;"> <span
							class="ui-icon ui-icon-triangle-1-w"></span>
					</a> <a id="nData" class="fm-button ui-state-default ui-corner-right"
						href="javascript:void(0)" style="display: none;"> <span
							class="ui-icon ui-icon-triangle-1-e"></span>
					</a></td>
					<td class="EditButton"><a id="sData2"
						class="fm-button ui-state-default ui-corner-all fm-button-icon-left"
						href="javascript:void(0)"> 发送 <span
							class="ui-icon ui-icon-disk"></span>
					</a></td>
				</tr>
				<tr class="binfo" style="display: none">
					<td class="bottominfo" colspan="2"></td>
				</tr>
			</tbody>
		</table>

	</span>

</div>
