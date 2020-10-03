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
        <td>序号</td>
        <td>一级科目</td>
        <td>二级科目</td>
        <td>日期</td>
        <td>金额</td>
        <td>备注说明</td>
    </tr>
    <#assign sdate="">
    <#if year=="月">
        <#assign sdate=statisticalDate?string('yyyy-MM')>
    <#else>
        <#assign sdate=statisticalDate?c>
    </#if>
<#assign number =1>
    <#assign ss = 0>
    <#list receiptsDetail! as being>
        <#if being_index < ss>
            <tr>
            <#if being.sname??>
                <td><span onClick="getDetail('${being.sname}',${being.id},'${sdate}')">${being.sname}</span></td>
            <#else>
                <td></td>
            </#if>
            <td></td>
            <#if being.money??>
                <td>${being.money}</td>
            <#else>
                <td></td>
            </#if>
            <td></td>
            </tr>
        <#elseif being.scount??>
            <tr>
            <#assign snumber = being.scount>
            <#assign ss =being_index+snumber>
            <td rowspan="${snumber}">${number}</td>
            <td rowspan="${snumber}">${being.pname}</td>
            <#if being.sname??>
                <td><span onClick="getDetail('${being.sname} ',${being.id},'${sdate}')">${being.sname}</span></td>
            <#else>
                <td></td>
            </#if>
                <td></td>
            <#if being.money??>
                <td>${being.money}</td>
            <#else>
                <td></td>
            </#if>
                <td></td>
            <#assign number =number + 1>
            </tr>
        <#else>
            <tr>
            <td>${number}</td>
            <td ><span onClick="getDetail('${being.pname} ',${being.id},'${sdate}')">${being.pname}</span></td>
            <#if being.sname??>
            <td><span onClick="getDetail('${being.sname}',${being.id},'${sdate}')">${being.sname}</span></td>
            <#else>
            <td></td>
            </#if>
            <td></td>
            <#if being.money??>
            <td>${being.money}</td>
            <#else>
            <td></td>
            </#if>
            <td></td>
            </tr>
            <#assign number =number + 1>
        </#if>
    </#list>

    <tr>
        <td colspan="3" style="background-color: #FFFC72;">本${year}经营收入</td>
        <td style="background-color: #FFFC72;"></td>
        <#if reports.operationsReceipts??>
        <td style="background-color: #FFFC72;">${reports.operationsReceipts}</td>
        <#else>
            <td style="background-color: #FFFC72;"></td>
        </#if>
        <td style="background-color: #FFFC72;"></td>
    </tr>
    <tr>
        <td colspan="3" style="background-color: #FFFC72;">本${year}押金收入</td>
        <td style="background-color: #FFFC72;"></td>
        <#if reports.depositReceipts??>
        <td style="background-color: #FFFC72;">${reports.depositReceipts}</td>
        <#else>
        <td style="background-color: #FFFC72;"></td>
        </#if>
        <td style="background-color: #FFFC72;"></td>
    </tr>
<#assign number =1>
<#assign ss = 0>
<#list disbursementsDetail! as being>
    <#if being_index < ss>
        <tr>
            <#if being.sname??>
                <td><span onClick="getDetail('${being.sname}',${being.id},'${sdate}')">${being.sname}</span></td>
            <#else>
                <td></td>
            </#if>
            <td></td>
            <#if being.money??>
                <td>${being.money}</td>
            <#else>
                <td></td>
            </#if>
            <td></td>
        </tr>
    <#elseif being.scount??>
        <tr>
            <#assign snumber = being.scount>
            <#assign ss =being_index+snumber>
            <td rowspan="${snumber}">${number}</td>
            <td rowspan="${snumber}">${being.pname}</td>
            <#if being.sname??>
                <td><span onClick="getDetail('${being.sname}',${being.id},'${sdate}')">${being.sname}</span></td>
            <#else>
                <td></td>
            </#if>
            <td></td>
            <#if being.money??>
                <td>${being.money}</td>
            <#else>
                <td></td>
            </#if>
            <td></td>
            <#assign number =number + 1>
        </tr>
    <#else>
        <tr>
            <td>${number}</td>
            <td ><span onClick="getDetail('${being.pname}',${being.id},'${sdate}')">${being.pname}</span></td>
            <#if being.sname??>
                <td><span onClick="getDetail('${being.sname}',${being.id},'${sdate}')">${being.sname}</span></td>
            <#else>
                <td></td>
            </#if>
            <td></td>
            <#if being.money??>
                <td>${being.money}</td>
            <#else>
                <td></td>
            </#if>
            <td></td>
        </tr>
        <#assign number =number + 1>
    </#if>
    </#list>
    <tr>
        <td colspan="3" style="background-color: #FFFC72;">本${year}经营支出</td>

        <td style="background-color: #FFFC72;"></td>
    <#if reports.operationsDisbursements??>
        <td style="background-color: #FFFC72;">${reports.operationsDisbursements}</td>
    <#else>
        <td style="background-color: #FFFC72;"></td>
    </#if>
        <td style="background-color: #FFFC72;"></td>
    </tr>
    <tr>
        <td colspan="3" style="background-color: #FFFC72;">本${year}押金支出</td>

        <td style="background-color: #FFFC72;"></td>
    <#if reports.depositDisbursements??>
        <td style="background-color: #FFFC72;">${reports.depositDisbursements}</td>
    <#else>
        <td style="background-color: #FFFC72;"></td>
    </#if>
        <td style="background-color: #FFFC72;"></td>
    </tr>
    <tr>
        <td colspan="3" style="background-color: #EFC25B;">上${year}经营结余</td>

        <td style="background-color: #EFC25B;"></td>
    <#if reports.operationsBlanceL??>
        <td style="background-color: #EFC25B;">${reports.operationsBlanceL}</td>
    <#else>
        <td style="background-color: #EFC25B;"></td>
    </#if>
        <td style="background-color: #EFC25B;"></td>
    </tr>
    <tr>
        <td colspan="3" style="background-color: #EFC25B;">上${year}押金结余</td>
        <td style="background-color: #EFC25B;"></td>
    <#if reports.depositBlanceL??>
        <td style="background-color: #EFC25B;">${reports.depositBlanceL}</td>
    <#else>
        <td style="background-color: #EFC25B;"></td>
    </#if>
        <td style="background-color: #EFC25B;"></td>
    </tr>
    <tr>
        <td colspan="3" style="background-color: #657D47;">本${year}经营结余</td>
        <td style="background-color: #657D47;"></td>
    <#if reports.operationsBlance??>
        <td style="background-color: #657D47;">${reports.operationsBlance}</td>
    <#else>
        <td style="background-color: #657D47;"></td>
    </#if>
        <td style="background-color: #657D47;"></td>
    </tr>
    <tr>
        <td colspan="3" style="background-color: #657D47;">本${year}押金结余</td>

        <td style="background-color: #657D47;"></td>
    <#if reports.depositBlance??>
        <td style="background-color: #657D47;">${reports.depositBlance}</td>
    <#else>
        <td style="background-color: #657D47;"></td>
    </#if>
        <td style="background-color: #657D47;"></td>
    </tr>

</table>
</body>
</html>