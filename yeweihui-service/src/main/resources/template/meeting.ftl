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
    <h1 style="text-align:center;">会议主题：${title}</h1>
</div>
<table class="gridtable" align="center">
    <!-- <tr>
        <th class="td1">Info Header 1</th>
        <th class="td2">Info Header 2</th>
    </tr> -->
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
                <#if meetingMemberEntity.memberRealname?exists && meetingMemberEntity.signTime?exists
                && (meetingMemberEntity.status == 1 || meetingMemberEntity.status == 2)><#-- 名字不为空且签到了 -->
                    ${meetingMemberEntity.memberRealname}
                </#if>
            </#list>
        </td>
    </tr>
    <tr>
        <td class="td1">会议纪要签字</td>
        <td class="td2">
            <#list meetingMemberEntityList as meetingMemberEntity>
                <#if meetingMemberEntity.memberRealname?exists && meetingMemberEntity.signNameTime?exists
                && meetingMemberEntity.status == 2>
                    ${meetingMemberEntity.memberRealname}
                </#if>
            </#list>
        </td>
    </tr>
    <tr>
        <td class="td1">会议纪要内容</td>
        <td class="td2">
            <#list meetingLogEntityList as meetingLogEntity>
                <#if meetingLogEntity.memberRealname?exists>
                    <div style="display: block;height:20px;">
                        ${meetingLogEntity.memberRealname}
                        ${meetingLogEntity.createTime?string('yyyy-MM-dd HH:mm:ss')}
                        ${meetingLogEntity.content}
                    </div>

                    <br></br>
                    <#list meetingLogEntity.fileList as file>
                        <div style="height:100px;display: block;margin-top: 100px;">
                            <img src="${file.url}"/><br></br>
                        </div>
                    </#list>
                    <#--<img src="https://wx.zaijiawangluo.com/image/asset/2018/12/09/a727ff62a410cc7523ef626d2ee1088f.jpg" />-->
                </#if>
                <br></br>
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