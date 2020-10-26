<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>${uname}</title>
    <style>
        body {
            font-family: SimHei;
        }

        .pos {
            position: absolute;
            left: 100px;
            top: 150px
        }
    </style>

    <style type="text/css">
        table.gridtable {
            /*font-family: verdana,arial,sans-serif;*/
            font-size: 16px;
            color: #333333;
            border-width: 1px;
            border-color: #666666;
            border-collapse: collapse;
            width: 90%;
        }

        table.gridtable th {
            border-width: 1px;
            padding: 8px;
            border-style: solid;
            border-color: #666666;
            background-color: #dedede;
        }

        table.gridtable td {
            border-width: 1px;
            padding: 8px;
            border-style: solid;
            border-color: #666666;
            background-color: #ffffff;

        }

        .td1 {
            width: 20%;
        }

        .td2 {
            width: 80%;
        }

        .pic {
            width: 300%;
        }
    </style>
</head>
<body>
<div>
    <h1 style="text-align:center;">${uname}</h1>
</div>
<table class="gridtable" align="center" >
            <tr>
                <td>日期</td>
                <td>记账字号</td>
                <td>摘要</td>
                <td>科目</td>
                <td>辅助账</td>
                <td>收入</td>
                <td>支出</td>
                <td>附件张数</td>
                <td>制单人</td>
                <td>审核人</td>
            </tr>
        <#list flist as being>
            <tr>
                <td>${being.date?string('yyyy-MM-dd')}</td>
                <td>${being.tagNumber!}</td>
                <td>${being.digest!}</td>
                <td>${being.accountsSubject.name!}</td>
                <td>${being.auxiliary!}</td>
                <td>
                    <#if being.accountsSubject.rdtype! == 1 >
                        	${being.money!}
						</#if>
                </td>
                <td>
                    <#if being.accountsSubject.rdtype! == 2 >
                        	${being.money!}
						</#if>
                </td>
                <td>${being.accessory.fileInfos?size} </td>
                <td>${being.makeUsername!}</td>
                <td>${being.auditor!}</td>
            </tr>
        </#list>
        </table>
</body>

</html>