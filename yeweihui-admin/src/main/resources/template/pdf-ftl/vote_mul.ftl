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
        .pic{
            width: 300%;
        }
    </style>

</head>
<body>

<div>
    <h1 style="text-align:center;">表决标题：${title}</h1>
</div>
<table class="gridtable" align="center">
    <tr>
        <td class="td1">小区名称</td>
        <td class="td2">${zoneName!''}</td>
    </tr>
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
        <#if !verifyType?? || verifyType == 0>
            <td class="td1" colspan="2"><font color='red'>过半数则通过，超过表决截止时间算弃权。</font></td>
        </#if>
    </tr>
    <tr>
        <td class="td1">表决方式</td>
        <td class="td2">${typeCn}</td>
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
            <td class="td1">表决详细 选项1</td>
            <td class="td2">
                ${item1Count}人
                <#list voteMemberItem1EntityList as voteMemberItem1Entity>
                <div>
                    <#if voteMemberItem1Entity.memberRealname?exists>
                        [${voteMemberItem1Entity.memberRealname} - ${voteMemberItem1Entity.updateTime?string('yyyy-MM-dd HH:mm:ss')}]
                    <#else>
                        [用户id：${voteMemberItem1Entity.uid} - ${voteMemberItem1Entity.updateTime?string('yyyy-MM-dd HH:mm:ss')}]
                    </#if>
                </div>
                </#list>
            </td>
        </tr>
        <tr>
            <td class="td1">表决详细 选项2</td>
            <td class="td2">
                ${item2Count}人
                <#list voteMemberItem2EntityList as voteMemberItem2Entity>
                    <div>
                    <#if voteMemberItem2Entity.memberRealname?exists>
                        [${voteMemberItem2Entity.memberRealname} - ${voteMemberItem2Entity.updateTime?string('yyyy-MM-dd HH:mm:ss')}]
                    <#else>
                        [用户id：${voteMemberItem2Entity.uid} - ${voteMemberItem2Entity.updateTime?string('yyyy-MM-dd HH:mm:ss')}]
                    </#if>
                    </div>
                </#list>
            </td>
        </tr>
        <tr>
            <td class="td1">表决详细 选项3</td>
            <td class="td2">
                ${item3Count}人
                <#list voteMemberItem3EntityList as voteMemberItem3Entity>
                    <div>
                    <#if voteMemberItem3Entity.memberRealname?exists>
                        [${voteMemberItem3Entity.memberRealname} - ${voteMemberItem3Entity.updateTime?string('yyyy-MM-dd HH:mm:ss')}]
                    <#else>
                        [用户id：${voteMemberItem3Entity.uid} - ${voteMemberItem3Entity.updateTime?string('yyyy-MM-dd HH:mm:ss')}]
                    </#if>
                    </div>
                </#list>
            </td>
        </tr>
        <tr>
            <td class="td1">表决详细 选项4</td>
            <td class="td2">
                ${item4Count}人
                <#list voteMemberItem4EntityList as voteMemberItem4Entity>
                <div>
                <#if voteMemberItem4Entity.memberRealname?exists>
                        [${voteMemberItem4Entity.memberRealname} - ${voteMemberItem4Entity.updateTime?string('yyyy-MM-dd HH:mm:ss')}]
                    <#else>
                        [用户id：${voteMemberItem4Entity.uid} - ${voteMemberItem4Entity.updateTime?string('yyyy-MM-dd HH:mm:ss')}]
                    </#if>
                </div>
                </#list>
            </td>
        </tr>
        <#if voteMemberEntityList??>
            <tr>
                <td class="td1">表决意见</td>
                <td class="td2">
                    <#list voteMemberEntityList as voteMember>
                        <#if voteMember.remark?exists && voteMember.remark != ''>
                            ${voteMember.remark}
                            <#if voteMember.memberRealname?exists>
                                [${voteMember.memberRealname}]
                            <#else>
                                [用户id：${voteMember.uid}]
                            </#if>
                        </#if>
                    </#list>
                </td>
            </tr>
            <tr>
                <td class="td1">签名</td>
                <td class="td2">
                    <#list voteMemberEntityList as voteMember>
                        <#if voteMember.verifyUrl?exists && voteMember.verifyUrl != ''>
                            <img src="${voteMember.verifyUrl}" width='50' />
                        </#if>
                    </#list>
                </td>
            </tr>
        </#if>
    <#elseif type == 2>

    </#if>

    <#if !verifyType?? || verifyType == 0>
        <tr>
            <td class="td1">状态</td>
            <td class="td2">
                <#if statusCn?exists>
                    ${statusCn}
                <#else>
                    未选出
                </#if>
            </td>
        </tr>
    </#if>
    <tr>
        <td class="td1">内容</td>
        <td class="td2">
            ${content}
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
<#if  (needAttach!true) && (fileList??) && (fileList?size gt 0)>
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