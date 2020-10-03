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
            width:22%;
        }
        .td2{
            width:78%;
        }
    </style>

</head>
<body>

<div>
    <h1 style="text-align:center;">${title}投票结果</h1>
</div>
<table class="gridtable" align="center">
    <tr>
        <td class="td1">参会开始时间</td>
        <td class="td2">${meetingStartTime?string('yyyy-MM-dd HH:mm:ss')}</td>
    </tr>
    <tr>
        <td class="td1">参会结束时间</td>
        <td class="td2">${meetingEndTime?string('yyyy-MM-dd HH:mm:ss')}</td>
    </tr>
    <tr>
        <td class="td1">投票开始时间</td>
        <td class="td2">${voteStartTime?string('yyyy-MM-dd HH:mm:ss')}</td>
    </tr>
    <tr>
        <td class="td1">投票结束时间</td>
        <td class="td2">${voteEndTime?string('yyyy-MM-dd HH:mm:ss')}</td>
    </tr>
    <tr>
        <td class="td1" colspan="2">线上投票统计结果</td>
    </tr>
    <tr>
        <td class="td1">总投票户数</td>
        <td class="td2">${bflySubVotes[0].bflyStatVote.totalVoteQuantity}</td>
    </tr>
    <tr>
        <td class="td1">总投票面积(㎡)</td>
        <td class="td2">${bflySubVotes[0].bflyStatVote.totalVoteArea}</td>
    </tr>
    <#assign text = bflySubVotes[0].bflyStatVote.meetingResultJson />
    <#assign json=text?eval />
        <tr>
            <td class="td1">已参会户数</td>
            <td class="td2">${json["已经参会户数"]}</td>
        </tr>
        <tr>
            <td class="td1">已参会面积(㎡)</td>
            <td class="td2">${json["已经参会的面积"]}</td>
        </tr>
    <tr>
        <td class="td1" colspan="2">表决内容</td>
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
        <tr>
            <td class="td1">表决说明</td>
            <td class="td2">${vote.description!'无'}</td>
        </tr>

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

        <#assign text>${vote.bflyStatVote.quantityResultJson}</#assign>
        <#assign json=text?eval />
        <tr>
            <td class="td1">线上投票户数</td>
            <td class="td2">
                <#list json as item>
                    ${item["选项"]}:${item["投票户数"]}
                </#list>
            </td>
        </tr>

        <#assign text>${vote.bflyStatVote.areaResultJson}</#assign>
        <#assign json=text?eval />
        <tr>
            <td class="td1">线上投票面积(㎡)</td>
            <td class="td2">
                <#list json as item>
                    ${item["选项"]}:${item["投票面积"]}
                </#list>
            </td>
        </tr>

    </#list>
    <tr>
        <td class="td1">线下投票结果</td>
        <td class="td2">
            <div style="padding-bottom: 30px; text-align: center">
                <img src="${offlineVoteResultUrl}" class="pic" width="350"/><br></br>
            </div>
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