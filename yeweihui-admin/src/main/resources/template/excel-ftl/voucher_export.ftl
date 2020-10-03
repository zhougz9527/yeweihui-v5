<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8"/>
    <title>凭证导出信息</title>
</head>

<body>
<table border=1 style=" border-collapse: collapse;">
	<tr>
		<td class="title">日期</td>
		<td class="title">凭证号</td>
		<td class="title">摘要</td><td>科目</td>
		<td class="title">辅助账</td>
		<td class="title">借方金额</td>
		<td class="title">贷方金额</td>
		<td class="title">附件张数</td>
		<td class="title">制单人</td>
		<td class="title">审核人</td>
	</tr>

	<#list voucherInfos! as voucherInfo > 
		
		<#if voucherInfo?? && voucherInfo.voucher?? && voucherInfo.voucher.accountsFinancialinforms?? && (voucherInfo.voucher.accountsFinancialinforms?size > 0)>
			
			<#assign isSetFirstRow=true/>
			<#assign rowSize=(voucherInfo.voucher.accountsFinancialinforms?size)/>
			<#list voucherInfo.voucher.accountsFinancialinforms as accountsFinancialinform >
				<tr>
				<#if isSetFirstRow >
					<td rowspan="${rowSize}">${voucherInfo.voucher.date!?string('yyyy-MM-dd')}</td>
				</#if>
				<#if isSetFirstRow >
					<td rowspan="${rowSize}">记-${voucherInfo.voucher.tagNumber!?c}</td>
				</#if>
			
				<td>${accountsFinancialinform.digest!}</td>
				<td>${accountsFinancialinform.accountsSubject.name!}</td>
				<td>${accountsFinancialinform.auxiliary!}</td>
				<td>
					<#if accountsFinancialinform.accountsSubject.rdtype == 1>
						${accountsFinancialinform.money!?c}
					</#if>
				</td>
				<td>
					<#if accountsFinancialinform.accountsSubject.rdtype == 2>
						${accountsFinancialinform.money!?c}
					</#if>
				</td>
				<td>${accountsFinancialinform.accessory.fileInfos!?size?c}</td>
			
				<#if isSetFirstRow >
					<td rowspan="${rowSize}">${voucherInfo.voucher.makeUsername!}</td>
				</#if>
				<#if isSetFirstRow >
					<td rowspan="${rowSize}">${voucherInfo.accounts.auditor!}</td>
				</#if>
				</tr>
				<#assign isSetFirstRow=false/>
			</#list>

		<#else>
			<tr>
				<td>${voucherInfo.voucher.date!?string('yyyy-MM-dd')}</td>
				<td>记-${voucherInfo.voucher.tagNumber!?c}</td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td>${voucherInfo.voucher.makeUsername!}</td>
				<td>${voucherInfo.accounts.auditor!}</td>
			</tr>
		</#if>
	</#list>

</table>
</body>

</html>