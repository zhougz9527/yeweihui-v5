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
    </style>

</head>
<body>

<div>
    <h1 style="text-align:center;">${realname}的报销申请</h1>
</div>
<table class="gridtable" align="center">
    <tr>
        <td class="td1">小区名称</td>
        <td class="td2">${zoneName!''}</td>
    </tr>
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
        <td class="td1">审批列表</td>
        <td class="td2">
            <#list verifyMemberEntityList as verifyMemberEntity>
                <div>
                    <#if verifyMemberEntity.memberRealname?exists>
                        ${verifyMemberEntity.memberRealname}[${verifyMemberEntity.statusCn} - ${verifyMemberEntity.updateTime?string('yyyy-MM-dd HH:mm:ss')}]
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
    <#--
    PDF打印输出的格式中，“业委会盖章”这一栏，在“费用报销”这个流程中没有需求，所以，请把这个取消
    <tr>
         <td class="td1" height="100">业委会盖公章处（需纸质公示时）</td>
         <td class="td2"></td>
     </tr>-->
    <tr>
        <td class="td1" colspan="2">打印日期：${printDate?string('yyyy-MM-dd HH:mm:ss')}
            <pre>    </pre>
            打印人：${printer!'sys'}</td>
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