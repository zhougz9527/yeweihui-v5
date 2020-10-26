<!--
 * @Author: xingjing
 * @Date: 2020-09-02 15:21:46
 * @LastEditors: xingjing
 * @LastEditTime: 2020-09-02 16:38:40
 * @Description: 9527
-->
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8" />
    <title>凭证打印信息</title>
    <style>
        body {
            font-family: SimHei;
        }
        table {
            border-collapse: collapse;
            width: 100%;
            height: 100%;
            border: 1px solid #666666;
        }
        
        .fontSize{font-size:12px;}
        .fontWeight{font-weight:bold;}
        td {
            border: 1px solid #666666;
            width: 20%;
            text-align: center;
            padding: 5px;
        }

        .slip {
            width: 100%;
            height:800px;
            margin:  0 auto;
            text-align: center;
            padding-top: 0px;
        }
		.title{font-size:20px; font-weight:bold; padding-bottom:10px;}
        .header {
			margin:5px 0px;
			padding:5px 0px;
        }
		.header table{border:0 none; table-layout:fixed; border-collapse: collapse;}
		.header td{border:0 none; padding-top:5px; padding:5px 0px;}

		.footer{padding:15px 0px;}
		
		.inputBox{width:33%; float:left; text-align:left;}
		.w_24_p{width:24%;}
		.w_14_p{width:14%;}
		.w_50_p{width:50%;}
		.w_30_p{width:30%;}
		.w_70_p{width:70%;}
		
		.w_50_px{width:50px;}
		.w_70_px{width:70px;}
		.w_150_px{width:150px;}
		
		
		.floatL{float:left;}
		.floatR{float:right;}
		.textL{text-align: left;}
		.textR{text-align: right;}
		.textC{text-align: center;}
		
		
		
    </style>

</head>

<body>
<#list voucherInfos! as voucherInfo > 
    <div class="slip">
        <div class="title">收支记账</div>
        <div class="header">
        	<table border="0" cellpadding="0" cellspacing="0" class="fontSize">
	        	<tr>
	        		<td class="textL"><b>日期：</b>${voucherInfo.voucher.date!?string('yyyy-MM-dd')}</td><td class="w_50_px textR"><b>记账号：</b></td><td class="w_50_px textL">记-${voucherInfo.voucher.tagNumber!"0"?c}</td>
	        	</tr>
	        	<tr>
	        		<td class="textL">&nbsp;&nbsp;&nbsp;&nbsp;<b>单位：</b>${voucherInfo.zoneName!"无"}</td><td class="textR"><b>附件：</b></td><td class="textL">&nbsp;&nbsp;${voucherInfo.accessoryCount!?c}</td>
	        	</tr>
        	</table>
        </div>
        <div>
             <table class="fontSize">
                 <tr>
                     <td class="w_24_p"><b>摘要</b></td>
                     <td class="w_24_p"><b>科目</b></td>
                     <td class="w_24_p"><b>辅助账</b></td>
                     <td class="w_14_p"><b>收入</b></td>
                     <td class="w_14_p"><b>支出</b></td>
                 </tr>
                 <#list voucherInfo.voucher.accountsFinancialinforms! as accountsFinancialinform > 
    			 	<tr>
                        <td>${accountsFinancialinform.digest!}</td>
                        <td>${accountsFinancialinform.accountsSubject.levelInfo!}</td>
                        <td>${accountsFinancialinform.auxiliary!}</td>
                        <td>
                        <#if accountsFinancialinform.accountsSubject.rdtype! == 1 >
                        	${accountsFinancialinform.money!?c}
						</#if>
                        </td>
                    	<td>
                        <#if accountsFinancialinform.accountsSubject.rdtype! == 2 >
                        	${accountsFinancialinform.money!?c}
						</#if>
                        </td>
                 	</tr>
				 </#list>
                 <tr>
                     <td colspan="5">&nbsp;</td>
                 </tr>
                 <tr>
                     <td><b>人民币（大写）</b></td>
                     <td colspan="2">${voucherInfo.aggregateChinese!"无数据"}</td>
                     <td class="fontStyle">${voucherInfo.earningSum!?c}</td>
                     <td class="fontStyle">${voucherInfo.expenditureSum!?c}</td>
                 </tr>
             </table>
        </div>
        <div class="footer fontSize">
			<div class="inputBox"><span><b>记账/制单：</b></span>${voucherInfo.voucher.makeUsername!}</div>
			<div class="inputBox"><span><b>审核：</b>${voucherInfo.accounts.auditor!}</span></div>
			<div class="inputBox"><span><b>复核：</b></span></div>
        </div>
    </div>
</#list>
</body>

</html>