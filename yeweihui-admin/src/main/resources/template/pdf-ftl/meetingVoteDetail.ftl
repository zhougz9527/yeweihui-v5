<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>表决标题：${title}</title>
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
    <h1 style="text-align:center;">${title}参会信息</h1>
</div>
<table class="gridtable" align="center">
    <tr>
        <td class="td1">业主姓名</td>
        <td class="td2">${bflyUserVote.bflyUser.username}</td>
    </tr>
    <tr>
        <td class="td1">业主房号</td>
        <td class="td2">${bflyUserVote.bflyUser.address!'--'}</td>
    </tr>
    <tr>
        <td class="td1">建筑面积</td>
        <td class="td2">${bflyUserVote.bflyUser.housingArea!'--'}</td>
    </tr>
    <tr>
        <td class="td1">确认参会时间</td>
        <td class="td2">${bflyUserVote.meetingSubmitTime?string('yyyy-MM-dd HH:mm:ss')}</td>
    </tr>
    <#list bflySubVotes as vote>
        <tr>
            <td class="td1">子表决名称</td>
            <td class="td2">${vote.title}</td>
        </tr>
        <tr>
            <td class="td1">表决事项</td>
            <td class="td2">表决详情</td>
        </tr>
        <#list vote.bflyVoteItems as voteItem>
            <tr>
                <td class="td1">${voteItem.matter}</td>
                <td class="td2">${voteItem.content}</td>
            </tr>
        </#list>
        <#assign text>${vote.options}</#assign>
        <#assign json=text?eval />
        <tr>
            <td class="td1">选项</td>
            <td class="td2">
                <#list json as item>
                    ${item}
                </#list>
            </td>
        </tr>

        <tr>
            <td class="td1">表决说明</td>
            <td class="td2">${vote.description!'--'}</td>
        </tr>
    </#list>

    <tr>
        <td class="td1">业主参会签名</td>
        <td class="td2">
            <#if bflyUserVote.meetingSignatureUrl?exists>
               <img src="${bflyUserVote.meetingSignatureUrl}" height='150' />
            </#if>
        </td>
    </tr>
    <tr>
        <td class="td1">业主参会自拍认证</td>
        <td class="td2">
            <#if bflyUserVote.meetingSignatureHeaderUrl?exists>
                <img src="${bflyUserVote.meetingSignatureHeaderUrl}" height='150' />
            </#if>
        </td>
    </tr>
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