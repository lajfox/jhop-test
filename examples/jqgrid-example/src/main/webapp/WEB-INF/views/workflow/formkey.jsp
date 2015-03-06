<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<span>
	<form method="post" id="Form${param['uuid']}"
		name="Form${param['uuid']}" class="FormGrid"
		enctype="multipart/form-data" action="${ctx}/workflow/model/deploy/${param['id']}"
		style="width: 100%; overflow: auto; position: relative; height: auto;">

		<table class="EditTable"  cellspacing="0" cellpadding="0" border="0">
			<tbody id="model_deploy_tbody">
				<tr id="FormError${param['uuid']}" style="display: none;">
					<td id="FormError${param['uuid']}td" class="ui-state-error"
						colspan="2"></td>
				</tr>
				
				<tr >
					<td  colspan="2">
					<a id="aData${param['uuid']}"
					class="fm-button ui-state-default ui-corner-all fm-button-icon-left"
					href="javascript:void(0)"> 增加资源 <span
						class="ui-icon ui-icon-add"></span>
					</a> 					
					</td>
				</tr>
								
				<tr class="FormData">
					<td class="CaptionTD">资源文件</td>
					<td class="DataTD">
					<input type="file"
						class="FormElement ui-widget-content ui-corner-all"
						name="model_deploy_file" id="model_deploy_file" /></td>
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
					
					<a id="sData${param['uuid']}"
					class="fm-button ui-state-default ui-corner-all fm-button-icon-left"
					href="javascript:void(0)"> 提交 <span
						class="ui-icon ui-icon-disk"></span>
					</a> 
					<a id="cData${param['uuid']}"
						class="fm-button ui-state-default ui-corner-all fm-button-icon-left"
						href="javascript:void(0)"> 取消 <span
							class="ui-icon ui-icon-close"></span>
					</a>					
					 
					</td>
				</tr>
				<tr class="binfo">
					<td class="bottominfo" colspan="2"></td>
				</tr>
			</tbody>
		</table>
	</form>
</span>

<script type="text/javascript">
jQuery().ready(
	function() {

		var options = {	
		success : function(responseText, statusText, xhr, $form) {
			if(responseText.success){							
				$("#FormError${param['uuid']}").show();
				$("#FormError${param['uuid']}td").html(responseText.message);
				$("#${param['uuid2']}").trigger('reloadGrid');
			}else{
				alert(responseText.message);
				$("#FormError${param['uuid']}").show();
				$("#FormError${param['uuid']}td").html(responseText.message);
			}
		},
		type : 'post',
		dataType : 'json',
		clearForm : true,
		resetForm : true				 
	};

	// bind form using 'ajaxSubmit' 
	$("#sData${param['uuid']}").click(function(){
		$("#Form${param['uuid']}").ajaxSubmit(options);
	});							

	$("#cData${param['uuid']}").click(function(e) {
		$("#editcnt${param['uuid']}").dialog("close");
	});
	
	$("#aData${param['uuid']}").click(function(e) {
		var row = '<tr class="FormData">';
		row += '<td class="CaptionTD">资源文件</td>';
		row += '<td class="DataTD">';
		row += '<input type="file"';
		row += 'class="FormElement ui-widget-content ui-corner-all"';
		row += 'name="model_deploy_file" id="model_deploy_file" /></td>';
		row += '</tr>';
		
		$('#model_deploy_tbody').append(row);
		$("#editcnt${param['uuid']}").height($("#editcnt${param['uuid']}").height()+30);
		
	});

});
</script>
