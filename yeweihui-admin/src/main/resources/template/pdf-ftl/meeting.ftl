<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>会议主题：${title}</title>
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
    <h1 style="text-align:center;">会议主题：${title}</h1>
</div>
<table class="gridtable" align="center">
    <tr>
        <td class="td1">小区名称</td>
        <td class="td2">${zoneName!''}</td>
    </tr>
    <tr>
        <td class="td1">参会人数</td>
        <td class="td2">${num}</td>
    </tr>
    <tr>
        <td class="td1">开始时间</td>
        <td class="td2">${startAt?string('yyyy-MM-dd HH:mm:ss')}</td><#--yyyy-MM-dd HH:mm:ss-->
    </tr>
    <tr>
        <td class="td1">会议地点</td>
        <td class="td2">${location}</td>
    </tr>
    <tr>
        <td class="td1">估计时长</td>
        <td class="td2">${spendTime}</td>
    </tr>
    <tr>
        <td class="td1">会议签到</td>
        <td class="td2">
            <#list meetingMemberEntityList as meetingMemberEntity>
                <div>
                    <#if meetingMemberEntity.memberRealname?exists && meetingMemberEntity.signTime?exists
                    && (meetingMemberEntity.status == 1 || meetingMemberEntity.status == 2)><#-- 名字不为空且签到了 -->
                        [${meetingMemberEntity.memberRealname} - ${meetingMemberEntity.signTime?string('yyyy-MM-dd HH:mm:ss')}]
                    </#if>
                </div>
            </#list>
        </td>
    </tr>
    <tr>
        <td class="td1">会议纪要签字</td>
        <td class="td2">
            <#list meetingMemberEntityList as meetingMemberEntity>
                <div>
                    <#if meetingMemberEntity.memberRealname?exists && meetingMemberEntity.signNameTime?exists
                    && meetingMemberEntity.status == 2>
                        [${meetingMemberEntity.memberRealname} - ${meetingMemberEntity.signNameTime?string('yyyy-MM-dd HH:mm:ss')}]
                    </#if>
                </div>
            </#list>
        </td>
    </tr>
    <tr>
        <td class="td1">会议纪要内容</td>
        <td class="td2">
            <#list meetingLogEntityList as meetingLogEntity>
                <#if meetingLogEntity.memberRealname?exists>
                <#--                    <div style="display: block">-->
                    <div style="margin-top: 20px">
                        ${meetingLogEntity.memberRealname}
                        ${meetingLogEntity.createTime?string('yyyy-MM-dd HH:mm:ss')}
                        ${meetingLogEntity.content}
                        <br></br>
                    </div>
<#--                    <#if (needAttach!true)>-->
<#--                        <#list meetingLogEntity.fileList as file>-->
<#--                            <div style="margin-top: 40px">-->
<#--                                <img src="${file.url}" height="300"/><br></br>-->
<#--                            </div>-->
<#--                        </#list>-->
<#--                    </#if>-->
                <#--<img src="https://wx.zaijiawangluo.com/image/asset/2018/12/09/a727ff62a410cc7523ef626d2ee1088f.jpg" />-->
                </#if>
                <br></br>
            </#list>
        </td>
    </tr>
    <tr>
        <td class="td1" height="100">业委会盖公章处(需纸质公示时)</td>
        <td class="td2"></td>
    </tr>
    <tr>
        <td class="td1" colspan="2">打印日期：${printDate?string('yyyy-MM-dd HH:mm:ss')}</td>
    </tr>
</table>

<#if (needAttach!true)>
    <div style="page-break-after: always;"></div>
    <div>
        <h3>纪要上传图片</h3>
        <div>
            <#list meetingLogEntityList as meetingLogObj>
                <#if meetingLogObj.memberRealname?exists>
                    <#list meetingLogObj.fileList as file>
                        <#if file.url?exists>
                            <div style="margin-top: 40px">
                                <img src="${file.url}" height="300"/><br></br>
                            </div>
                        </#if>
                    </#list>
                </#if>
            </#list>
        </div>

    </div>
</#if>

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