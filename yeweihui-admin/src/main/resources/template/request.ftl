<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>${uname}的用章申请</title>
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
    <h1 style="text-align:center;">${uname}的用章申请</h1>
</div>
<table class="gridtable" align="center">
    <!-- <tr>
        <th class="td1">Info Header 1</th>
        <th class="td2">Info Header 2</th>
    </tr> -->
    <tr>
        <td class="td1">状态</td>
        <td class="td2">${statusCn}</td>
    </tr>
    <tr>
        <td class="td1">经办人</td>
        <td class="td2">${uname}</td>
    </tr>
    <tr>
        <td class="td1">用章日期</td>
        <td class="td2">${useDate?string('yyyy-MM-dd')}</td><#--yyyy-MM-dd HH:mm:ss-->
    </tr>
    <tr>
        <td class="td1">用章文件名</td>
        <td class="td2">${documentName}</td>
    </tr>
    <tr>
        <td class="td1">文件份数</td>
        <td class="td2">${num}</td>
    </tr>
    <tr>
        <td class="td1">文件类别</td>
        <td class="td2">${fileTypeCn}</td>
    </tr>
    <tr>
        <td class="td1">加盖公章</td>
        <td class="td2">${sealCn}</td>
    </tr>
    <tr>
        <td class="td1" height="100">备注</td>
        <td class="td2">${notice!''}</td>
    </tr>
    <tr>
        <td class="td1">审批人【过半通过，超过24小时默认同意】</td>
        <td class="td2">
            <#list verifyMemberEntityList as verifyMemberEntity>
                <#if verifyMemberEntity.realname?exists>
                    ${verifyMemberEntity.realname}[${verifyMemberEntity.statusCn} - ${verifyMemberEntity.verifyTime?string('yyyy-MM-dd HH:mm:ss')}]
                </#if>
            </#list>
        </td>
    </tr>
    <tr>
        <td class="td1">抄送人</td>
        <td class="td2">
            <#list copyToMemberEntityList as copyToMemberEntity>
                <#if copyToMemberEntity.realname?exists>
                    ${copyToMemberEntity.realname}
                </#if>
            </#list>
        </td>
    </tr>
    <tr>
        <td class="td1" height="100">业委会盖公章处（需纸质公示时）</td>
        <td class="td2"></td>
    </tr>
    <tr>
        <td class="td1" colspan="2">打印日期：${printDate?string('yyyy-MM-dd HH:mm:ss')}</td>
    </tr>
</table>
</body>
</html>