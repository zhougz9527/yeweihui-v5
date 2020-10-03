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

		.fontStyle{
            font-size:12px;
            font-weight: normal;}

        .pos {
            position: absolute;
            left: 100px;
            top: 150px
        }

        table {
            border-collapse: collapse;
            width: 100%;
            height: 100%;
            border: 1px solid #666666;
        }
        
        table tbody{}
        td {
            border: 1px solid #666666;
            width: 20%;
            text-align: center;
            padding: 10px;
        }

        .slip {
            width: 100%;
            height:800px;
            margin:  0 auto;
            text-align: center;
            padding-top: 20px;
        }

        .header {
			margin:10px 0px;
			padding:10px 0px;
        }
		.header table{border:0 none; table-layout:fixed; border-collapse: collapse;}
		.header td{border:0 none; padding-top:5px;}

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
        <div>
            <h2> 记账凭证 </h2>
            <span>${voucherInfo.voucher.date!?string('yyyy-MM-dd')}</span>
        </div>
        <div class="header">
        	<table border="0" cellpadding="0" cellspacing="0">
	        	<tr>
	        		<td></td><td class="w_50_px textR">凭证号：</td><td class="w_50_px textL">记-${voucherInfo.voucher.tagNumber!"0"?c}</td>
	        	</tr>
	        	<tr>
	        		<td class="textL">&nbsp;&nbsp;&nbsp;&nbsp;单位：${voucherInfo.zoneName!"无"}</td><td class="textR">附件：</td><td class="textL">&nbsp;&nbsp;${voucherInfo.accessoryCount!?c}</td>
	        	</tr>
        	</table>
        </div>
        <div>
             <table>
                 <tr>
                     <td class="w_24_p">摘要</td>
                     <td class="w_24_p">科目</td>
                     <td class="w_24_p">辅助账</td>
                     <td class="w_14_p">借方</td>
                     <td class="w_14_p">贷方</td>
                 </tr>
                 <#list voucherInfo.voucher.accountsFinancialinforms! as accountsFinancialinform > 
    			 	<tr class="fontStyle">
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
                     <td>人民币（大写）</td>
                     <td colspan="2"></td>
                     <td></td>
                     <td></td>
                 </tr>
             </table>
        </div>
        <div class="footer">
			<div class="inputBox"><span>记账/制单：</span>${voucherInfo.voucher.makeUsername!}</div>
			<div class="inputBox"><span>审核：${voucherInfo.accounts.auditor!}</span></div>
			<div class="inputBox"><span>复核：</span></div>
        </div>
    </div>
</#list>
</body>

</html>