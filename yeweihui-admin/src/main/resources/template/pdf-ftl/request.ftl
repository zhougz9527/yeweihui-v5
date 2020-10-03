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
    <h1 style="text-align:center;">${uname}的用章申请</h1>
</div>
<table class="gridtable" align="center">
    <tr>
        <td class="td1">小区名称</td>
        <td class="td2">${zoneName!''}</td>
    </tr>
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
                <div>
                    <#if verifyMemberEntity.realname?exists>
                        ${verifyMemberEntity.realname}[${verifyMemberEntity.statusCn}
                        <#if verifyMemberEntity.verifyTime??>
                            ${verifyMemberEntity.verifyTime?string('yyyy-MM-dd HH:mm:ss')}
                        </#if>]
                    <#--                    ${verifyMemberEntity.realname}[${verifyMemberEntity.statusCn}]-->
                    </#if>
                </div>
            </#list>
        </td>
    </tr>
    <tr>
        <td class="td1">签名</td>
        <td class="td2">
            <#list verifyMemberEntityList as verifyMember>
                <#if verifyMember.verifyUrl?exists>
                    <img src="${verifyMember.verifyUrl}" width='50'/>
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
        <td class="td1" height="100">业委会盖公章处<br/>(需纸质公示时)</td>
        <td class="td2"></td>
    </tr>
    <tr>
        <td class="td1" colspan="2">打印日期：${printDate?string('yyyy-MM-dd HH:mm:ss')}</td>
    </tr>
</table>
<#if (needAttach!true) && (fileList??) && (fileList?size gt 0)>
    <div style="page-break-after: always;"></div>
    <div>
        <h3>附件图片</h3>
        <#list fileList as file>
            <div style="padding-bottom: 30px; text-align: center">
                <img src="${file.url}" class="pic" width="350"/><br></br>
            </div>
        </#list>
    </div>
</#if>
</body>
</html>