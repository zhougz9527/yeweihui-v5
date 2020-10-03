<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>${realname}的公示记录</title>
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
    <h1 style="text-align:center;">${realname}的公示记录</h1>
</div>
<table class="gridtable" align="center">
    <tr>
        <td class="td1">小区名称</td>
        <td class="td2">${zoneName!''}</td>
    </tr>
    <tr>
        <td class="td1">创建时间</td>
        <td class="td2">${createTime?string('yyyy-MM-dd HH:mm:ss')}</td>
    </tr>
    <tr>
        <td class="td1">公示标题</td>
        <td class="td2">${title}</td>
    </tr>
    <tr>
        <td class="td1">公示内容</td>
        <td class="td2">${content}</td>
    </tr>
    <tr>
        <td class="td1">开始时间</td>
        <td class="td2">${startTime?string('yyyy-MM-dd')}</td>
    </tr>
    <tr>
        <td class="td1">结束时间</td>
        <td class="td2">${endTime?string('yyyy-MM-dd')}</td>
    </tr>
    <tr>
        <td class="td1">接收列表</td>
        <td class="td2">
            <#list memberEntityList as member>
                <div>
                    <#if member.realname?exists>
                        ${member.realname}[${member.statusCn} - ${member.updateTime?string('yyyy-MM-dd HH:mm:ss')}]
                    </#if>
                </div>
            </#list>
        </td>
    </tr>
<#--    <tr>-->
<#--        <td class="td1">签名</td>-->
<#--        <td class="td2">-->
<#--            <#list paperMemberEntityList as paperMember>-->
<#--                <#if paperMember.verifyUrl?exists>-->
<#--                    <img src="${paperMember.verifyUrl}" width='150' />-->
<#--                </#if>-->
<#--            </#list>-->
<#--        </td>-->
<#--    </tr>-->
    <tr>
        <td class="td1" colspan="2">打印日期：${printerDate?string('yyyy-MM-dd HH:mm:ss')}<pre>    </pre>打印人：${printer!'sys'}</td>
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