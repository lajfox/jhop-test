<#if data?exists>
<select>
<option value="">请选择</option>
<#list data as item>
<option value="${item.itemname}">${item.itemname}</option>		
</#list>
</select>
</#if>