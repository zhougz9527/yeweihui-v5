<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>投票标题：${title}</title>
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
    <h1 style="text-align:center;">投票标题：${title}</h1>
</div>
<table class="gridtable" align="center">
    <tr>
        <td class="td1">表决时间</td>

        <td class="td2">
            <#if createAt?exists>
                ${createAt?string('yyyy-MM-dd HH:mm:ss')}
            <#else>
                无
            </#if>
        </td><#--yyyy-MM-dd HH:mm:ss-->
    </tr>
    <tr>
        <td class="td1" colspan="2"><font color='red'>过半数则通过，超过表决截止时间算弃权。</font></td>
<#--        <td class="td2">${typeCn?string('yyyy-MM-dd HH:mm:ss')}</td>-->
    </tr>
    <tr>
        <td class="td1">表决方式</td>
        <td class="td2">${typeCn}</td>
    </tr>
    <tr>
        <td class="td1">表决规则</td>
        <td class="td2">过半数则通过，超过表决截止时间算弃权。</td>
    </tr>
    <tr>
        <td class="td1">选项1</td>
        <td class="td2">${item1}</td>
    </tr>
    <tr>
        <td class="td1">选项2</td>
        <td class="td2">${item2}</td>
    </tr>
    <tr>
        <td class="td1">选项3</td>
        <td class="td2">${item3}</td>
    </tr>
    <tr>
        <td class="td1">选项4</td>
        <td class="td2">${item4}</td>
    </tr>

    <#-- 1实名 2匿名-->
    <#if type == 1>
    <tr>
        <td class="td1">投票详细 选项1</td>
        <td class="td2">
            ${item1Count}人
            <#list voteMemberItem1EntityList as voteMemberItem1Entity>
                <#if voteMemberItem1Entity.memberRealname?exists>
                    [${voteMemberItem1Entity.memberRealname}]
                <#else>
                    [用户id：${voteMemberItem1Entity.uid}]
                </#if>
            </#list>
        </td>
    </tr>
    <tr>
        <td class="td1">投票详细 选项2</td>
        <td class="td2">
            ${item2Count}人
            <#list voteMemberItem2EntityList as voteMemberItem2Entity>
                <#if voteMemberItem2Entity.memberRealname?exists>
                    [${voteMemberItem2Entity.memberRealname}]
                <#else>
                    [用户id：${voteMemberItem1Entity.uid}]
                </#if>
            </#list>
        </td>
    </tr>
    <tr>
        <td class="td1">投票详细 选项3</td>
        <td class="td2">
            ${item3Count}人
            <#list voteMemberItem3EntityList as voteMemberItem3Entity>
                <#if voteMemberItem3Entity.memberRealname?exists>
                    [${voteMemberItem3Entity.memberRealname}]
                <#else>
                    [用户id：${voteMemberItem1Entity.uid}]
                </#if>
            </#list>
        </td>
    </tr>
    <tr>
        <td class="td1">投票详细 选项4</td>
        <td class="td2">
            ${item4Count}人
            <#list voteMemberItem4EntityList as voteMemberItem4Entity>
                <#if voteMemberItem4Entity.memberRealname?exists>
                    [${voteMemberItem4Entity.memberRealname}]
                <#else>
                    [用户id：${voteMemberItem1Entity.uid}]
                </#if>
            </#list>
        </td>
    </tr>
    <#elseif type == 2>

    </#if>
    <tr>
        <td class="td1">状态</td>
        <td class="td2">
            <#if voteItem?exists>
                ${voteItem}
            <#else>
                未选出
            </#if>
        </td>
    </tr>
    <tr>
        <td class="td1">内容</td>
        <td class="td2">
            ${content}
        </td>
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
        <td class="td1" height="100">业委会盖公章处</td>
        <td class="td2"></td>
    </tr>
    <tr>
        <td class="td1" colspan="2">打印日期：${printDate?string('yyyy-MM-dd HH:mm:ss')}</td>
    </tr>
</table>
</body>
</html>