<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8"/>
    <title>凭证导出信息</title>
</head>

<body>
<table border=1 style=" border-collapse: collapse;">
	<tr>
		<td>日期</td>
		<td>记账号</td>
		<td>摘要</td>
		<td>科目</td>
		<td>辅助账</td>
		<td>收入金额</td>
		<td>支出金额</td>
		<td>附件张数</td>
		<td>制单人</td>
		<td>审核人</td>
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
			
				<td>${accountsFinancialinform.digest!}&nbsp;</td>
				<td>${accountsFinancialinform.accountsSubject.levelInfo!?replace("_","/")}</td>
				<td>${accountsFinancialinform.auxiliary!}&nbsp;</td>
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