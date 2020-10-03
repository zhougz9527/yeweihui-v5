<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>${realname}的报销申请</title>
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
            font-size:16px;
            color:#333333;
            border-width: 1px;
            border-color: #666666;
            border-collapse: collapse;
            width:90%;
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
        .td1{
            width:20%;
        }
        .td2{
            width:80%;
        }
    </style>

</head>
<body>

<div>
    <h1 style="text-align:center;">${realname}的报销申请</h1>
</div>
<table class="gridtable" align="center">
    <!-- <tr>
        <th class="td1">Info Header 1</th>
        <th class="td2">Info Header 2</th>
    </tr> -->
    <tr>
        <td class="td1">审核状态</td>
        <td class="td2">${statusCn}</td>
    </tr>
    <tr>
        <td class="td1">费用发生</td>
        <td class="td2">${happenDate?string('yyyy-MM-dd HH:mm:ss')}</td><#--yyyy-MM-dd HH:mm:ss-->
    </tr>
    <tr>
        <td class="td1">申请时间</td>
        <td class="td2">${createTime?string('yyyy-MM-dd HH:mm:ss')}</td>
    </tr>
    <tr>
        <td class="td1">报销金额</td>
        <td class="td2">${money}元</td>
    </tr>
    <tr>
        <td class="td1">详情说明</td>
        <td class="td2">${content}</td>
    </tr>
    <tr>
        <td class="td1">图片</td>
        <td class="td2">
            <#list fileList as file>
                <img src="${file.url}" />
            </#list>

        </td>
    </tr>
    <tr>
        <td class="td1">审批列表</td>
        <td class="td2">
            <#list verifyMemberEntityList as verifyMemberEntity>
                <#if verifyMemberEntity.memberRealname?exists>
                    ${verifyMemberEntity.memberRealname}[${verifyMemberEntity.statusCn}]
                </#if>
            </#list>
        </td>
    </tr>
    <tr>
        <td class="td1" height="100">业委会盖公章处</td>
        <td class="td2"></td>
    </tr>
    <tr>
        <td class="td1" colspan="2">打印日期：${printDate?string('yyyy-MM-dd HH:mm:ss')}</td>
    </tr>
</table>
</body>
</html>