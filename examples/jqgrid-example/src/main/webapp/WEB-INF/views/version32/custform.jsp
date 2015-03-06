<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<script type="text/javascript">
	jQuery().ready(function() {
		$('#invdate', '#order').datepicker({
			numberOfMonths : 3,
			maxDate : new Date()
		});
		
		$('#sData','#orderButton').click(function(e){
			var invid = jQuery("#id").val();
			if(invid) {
				jQuery("#custbutt").jqGrid('FormToGrid',invid,"#order");
				$( '#editcntcustbutt' ).dialog( "close" ); 
			}			
		});
		
		$('#cData','#orderButton').click(function(e){			
			$( '#editcntcustbutt' ).dialog( "close" );						
		});
		
	});
</script>


<span>
	<form method="post" name="order" id="order" class="FormGrid"
		style="width: 100%; overflow: auto; position: relative; height: auto;">


		<table class="EditTable" cellspacing="0" cellpadding="0" border="0">
			<tbody>
				<tr id="tr_invno" class="FormData">
					<td class="CaptionTD">单号</td>
					<td class="DataTD"><input type="hidden" name="id" id="id" /> <input
						type="text" class="FormElement ui-widget-content ui-corner-all"
						name="invno" id="invno" /></td>
				</tr>
				<tr class="FormData">
					<td class="CaptionTD">日期</td>
					<td class="DataTD"><input type="text"
						class="FormElement ui-widget-content ui-corner-all" name="invdate"
						id="invdate" readonly="true" /></td>
				</tr>
				<tr class="FormData">
					<td class="CaptionTD">客户</td>
					<td class="DataTD"><input type="text"
						class="FormElement ui-widget-content ui-corner-all" id="name" name="name"/></td>
				</tr>
				<tr class="FormData">
					<td class="CaptionTD">金额</td>
					<td class="DataTD"><input type="text"
						class="FormElement ui-widget-content ui-corner-all" id="amount" name="amount"/></td>
				</tr>
				<tr class="FormData">
					<td class="CaptionTD">税费</td>
					<td class="DataTD"><input type="text"
						class="FormElement ui-widget-content ui-corner-all" id="tax" name="tax"/></td>
				</tr>
				<tr class="FormData">
					<td class="CaptionTD">总计</td>
					<td class="DataTD"><input type="text"
						class="FormElement ui-widget-content ui-corner-all" id="total" name="total"/></td>
				</tr>
				<tr class="FormData">
					<td class="CaptionTD">备注</td>
					<td class="DataTD"><input type="text"
						class="FormElement ui-widget-content ui-corner-all" id="note" name="note"/></td>
				</tr>

			</tbody>
		</table>

	</form>

	<table id="orderButton"
		class="EditTable" cellspacing="0" cellpadding="0" border="0">
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
				<td class="EditButton"><a id="sData"
					class="fm-button ui-state-default ui-corner-all fm-button-icon-left"
					href="javascript:void(0)"> 提交 <span
						class="ui-icon ui-icon-disk"></span>
				</a> <a id="cData"
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


