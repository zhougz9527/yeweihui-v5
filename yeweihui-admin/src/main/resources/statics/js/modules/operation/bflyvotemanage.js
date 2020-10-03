$(function () {

    //获取小区列表
    vm.getZonesList();
    vm.getUser();

    $('#upload-file-form').change(function () {
        importExcel();
    });
    // vm.pieWidth = document.getElementById("pieContent").offsetWidth;

});

var vm = new Vue({
    el: '#rrapp',
    data: {
        pieWidth:null,// 饼图容器宽度
        zonesOptions: [],
        user: {},
        vote: {},
        status:null,//表决状态 1: 未开始 2:参会中 3: 参会结束 4:投票中 5:投票结束（表决）
        q: {
            zoneId: null,
            groupId: null,
            voteTitle: null,
            creatDate: null,
        },
        personalZoneId:null,
        admin:false,
        showList: true,
        viewMode: false,
        showUpload:false,//上传线下结果展示
        title: null,
        groupName: null,
        role: {},
        groupList: [],
        dialogImageUrl: '',
        dialogVisible: false,
        curVoteId:null,// 当前操作表决id
        voteDetail:{},// 浏览详情
        offlineVoteResultUrl:'',// 线下结果
        offlineFileList:[],//线下结果列表
        testShow:false,
        voteSavable:false,// 创建表决按钮
        fullscreenLoading:false,// loading
        progress:false,// 是否有上传中图片
        //日期
        pickerOptions: {
            shortcuts: [{
                text: '今天',
                onClick(picker) {
                    picker.$emit('pick', new Date());
                }
            }, {
                text: '昨天',
                onClick(picker) {
                    const date = new Date();
                    date.setTime(date.getTime() - 3600 * 1000 * 24);
                    picker.$emit('pick', date);
                }
            }, {
                text: '一周前',
                onClick(picker) {
                    const date = new Date();
                    date.setTime(date.getTime() - 3600 * 1000 * 24 * 7);
                    picker.$emit('pick', date);
                }
            }]
        },
        createPickeroptions:{
            disabledDate(time) {
                return time.getTime() < Date.now() - 1000*60*60*24;
            },
            shortcuts: [{
                text: '今天',
                onClick(picker) {
                    picker.$emit('pick', new Date());
                }
            }, {
                text: '昨天',
                onClick(picker) {
                    const date = new Date();
                    date.setTime(date.getTime() - 3600 * 1000 * 24);
                    picker.$emit('pick', date);
                }
            }, {
                text: '一周前',
                onClick(picker) {
                    const date = new Date();
                    date.setTime(date.getTime() - 3600 * 1000 * 24 * 7);
                    picker.$emit('pick', date);
                }
            }]
        },
        totalQuantity:0, // 小区总户数
        totalArea:0, // 小区总面积
        optionNum:0, // 选项数量
    },
    methods: {
        //获取小区列表
        getZonesList: function () {
            //获取下拉框数据
            $.ajax({
                type: "get",//方法类型
                dataType: "json",
                url: baseURL + "user/zones/simpleList",
                contentType: "application/x-www-form-urlencoded; charset=utf-8",
                data: {},
                success: function (result) {
                    if (result.code == 0) {
                        vm.zonesOptions = result.zonesEntityList;
                    }
                },
                error: function () {
                }
            });
        },
        getUser: function () {
            $.ajax({
                type: "get",//方法类型
                dataType: "json",
                url: baseURL + "sys/user/info",
                contentType: "application/x-www-form-urlencoded; charset=utf-8",
                data: {},
                success: function (result) {
                    if (result.code == 0) {
                        vm.user = result.user;
                        if(result.user.zoneId!==0){
                            vm.q.zoneId = result.user.zoneId;
                            vm.vote.zoneId = result.user.zoneId;
                            vm.personalZoneId = result.user.zoneId;
                            vm.admin = false
                        }else {
                            vm.admin = true
                        }
                        $("#jqGrid").jqGrid({
                            url: baseURL + 'bflyVote/selectVoteList',
                            mtype: "POST",
                            postData:{
                                "page":1,
                                "size":10,
                                "zoneId":vm.q.zoneId?vm.q.zoneId:"",
                            },
                            datatype: "json",
                            colModel: [
                                {label: 'ID', name: 'id', index: "id", width: 50, key: true},
                                {label: '小区名称', name: 'zoneName', index: "zoneName", width: 80},
                                {label: '会议主题', name: 'title', index: "title", width: 100},
                                {label: '开始时间', name: 'voteStartTime', index: "voteStartTime", width: 80},
                                {label: '截止时间', name: 'voteEndTime', index: "voteEndTime", width: 80},
                                {label: '投票状态', name: 'status', index: "status", width: 40,formatter: vm.voteStatusFormatter},
                                {label: '是否显示', name: 'phoneNum', index: "phoneNum", width: 40,formatter: vm.showStatusFormatter},
                                {label: '操作', name: 'ope', index: 'ope', width: 200, formatter: vm.btnFormatter}

                            ],
                            viewrecords: true,
                            height: 630,
                            rowNum: 10,
                            rowList: [10, 30, 50],
                            rownumbers: true,
                            rownumWidth: 50,
                            autowidth: true,
                            // multiselect: true,
                            pager: "#jqGridPager",
                            jsonReader: {
                                root: "page.list",
                                page: "page.currPage",
                                total: "page.totalPage",
                                records: "page.totalCount"
                            },
                            prmNames: {
                                page: "page",
                                rows: "size",
                                order: "order"
                            },
                            gridComplete: function () {
                                //隐藏grid底部滚动条
                                $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
                            }
                        });
                    }
                },
                error: function () {
                }
            });
        },

        // 表决状态 0:隐藏 1: 未开始  4:投票中 5:投票结束  ，2:参会中 3: 参会结束归入投票中状态
        voteStatusFormatter: function (cellvalue, options, rowdata) {
            switch (rowdata.status) {
                case 0:
                case 1:
                    return "<span class=\"label label-info\">未开始</span>";
                    break;
                case 2:
                    // return "<span class=\"label label-success\">参会中</span>";
                    // break;
                case 3:
                    // return "<span class=\"label label-danger\">参会结束</span>";
                    // break;
                case 4:
                    return "<span class=\"label label-success\">进行中</span>";
                    break;
                case 5:
                    return "<span class=\"label label-danger\">已结束</span>";
                    break;
                default:
                    return rowdata.status;
                    break;
            }
        },
        // 是否显示
        showStatusFormatter:function (cellvalue, options, rowdata) {
            switch (rowdata.isShow) {
                case 0:
                    return "<span>隐藏</span>";
                    break;
                case 1:
                    return "<span>显示</span>";
                    break;
                default:
                    return '--';
                    break;
            }
        },


        query: function () {
            vm.reload();
        },
        recovery: function () {
            vm.reload();
        },
        hide: function () {
            vm.reload();
        },
        add: function () {
            vm.showList = false;
            vm.title = "新增";
            vm.vote = {
                status: 1,
                isShow:1,
                title:"",
                meetingStartTime:null,
                meetingEndTime:null,
                voteStartTime:null,
                voteEndTime:null,
                zoneId: vm.personalZoneId?vm.personalZoneId:"",
                // copyToMemberEntityList: [],
                //资源上传
                // fileList: [],
                bflySubVotes: [{voteType:0,options: ["","",""],bflyVoteItems:[{matter:'',content:'',attachmentUrls:''}] }],
                // bflySubVotes: [{options: [{}], content: [{fileList: []}]}],
            };

        },
        addChild: function () {
            // vm.vote.bflySubVotes.push({options: [{}], content: [{fileList: []}]});
            vm.vote.bflySubVotes.push({voteType:0,options: ["","",""], bflyVoteItems:[{matter:'',content:'',attachmentUrls:''}]});
        },
        deleteChild: function (index) {
            vm.vote.bflySubVotes.splice(index, 1);
        },
        addChildContent: function (index) {
            vm.vote.bflySubVotes[index].bflyVoteItems.push({matter:'',content:'',attachmentUrls:''});
        },
        deleteChildContent:function (index, key) {
            vm.vote.bflySubVotes[index].bflyVoteItems.splice(key, 1);
        },
        addChildItem: function (index) {
            vm.vote.bflySubVotes[index].options.push("");
        },
        deleteChildItem: function (index, key) {
            vm.vote.bflySubVotes[index].options.splice(key, 1);
        },
        getPlaceHolder:function(index){
            switch(index){
                case 0 : return "每栏仅限自定义一个表决意见，如填写：“同意”，或者“满意”";
                case 1 : return "每栏仅限自定义一个表决意见，如填写：“不同意”，或者“不满意”";
                case 2 : return "每栏仅限自定义一个表决意见，如填写：“弃权”";
            }
        },
        saveOrUpdate: function () {
            const voteData = JSON.parse(JSON.stringify(vm.vote));
            voteData.bflySubVotes.forEach(item=>{
                item.options = getVoteOptions(item.options)
            });

            if (vm.offlineFileList && vm.offlineFileList.length > 0) {
                for (let i = 0, len = vm.offlineFileList.length; i < len; i++) {
                    if (vm.offlineFileList[i].status !== "success") {
                        return vm.$confirm("您还有附件正在上传中，请稍后再点击“确定”提交");
                    }
                }
            }

            if(vm.progress){
                vm.$confirm("您还有附件正在上传中，请稍后再点击“确定”提交");
            }else{
                vm.voteSavable = true;
                vm.fullscreenLoading = true;
                $.ajax({
                    type: "POST",
                    dataType:'json',
                    url: baseURL + 'bflyVote/save',
                    contentType: "application/json",
                    data: JSON.stringify(voteData),
                    success: function(r){
                        vm.voteSavable = false;
                        vm.fullscreenLoading = false;
                        if(r.code === 0){
                            alert('操作成功', function(){
                                vm.reload();
                            });
                        }else{
                            alert(r.msg);
                        }
                    }
                });
            }



        },

        // 上传线下投票结果
        handleUpload:function(){
            if(vm.offlineFileList&&vm.offlineFileList.length>0){
                    if (vm.offlineFileList && vm.offlineFileList.length > 0) {
                        for (let i = 0, len = vm.offlineFileList.length; i < len; i++) {
                            if (vm.offlineFileList[i].status !== "success") {
                                return vm.$confirm("您还有附件正在上传中，请稍后再点击“确定”提交");
                            }
                        }
                    }
                confirm('确定要上传该线下投票结果？', function(){
                    vm.fullscreenLoading = true;
                    // console.log(vm.offlineFileList,'offlinelist')
                    $.ajax({
                        type: "POST",
                        url: baseURL + "bflyVote/updateVote",
                        contentType: "application/json",
                        data: JSON.stringify({id:vm.curVoteId,offlineVoteResultUrl:vm.offlineFileList[0].url}),
                        success: function(r){
                            vm.fullscreenLoading = false;
                            if(r.code == 0){
                                alert('操作成功', function(index){
                                    vm.reload();
                                    $("#jqGrid").trigger("reloadGrid");
                                });
                            }else{
                                alert(r.msg);
                            }
                        }
                    });
                });
            }else {
                alert("请上传线下投票结果")
            }



        },
        reload: function () {
            vm.showList = true;
            vm.viewMode = false;
            vm.showUpload = false;
            vm.curVoteId = '';
            vm.offlineVoteResultUrl = '';
            vm.offlineFileList = [];
            vm.voteDetail={};
            vm.vote={};
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                postData: {
                    zoneId: vm.q.zoneId,
                    startDate:vm.q.creatDate,
                    title:vm.q.voteTitle,
                    // endDate:
                },
                page: page
            }).trigger("reloadGrid");
        },
        getInfo: function (id) {
            let _this  = this;

            $.get(baseURL + "bflyVote/info/" + id, function (r) {
                vm.voteDetail = r.result;
                vm.status = r.result.status;
                _this.$nextTick(function(){// dom更新完成后再对chart初始化
                if(vm.status === 5){
                    let voteHouseChart =  echarts.init(document.getElementById(`voteHousePie`));
                    let voteAreaChart =  echarts.init(document.getElementById(`voteAreaPie`));
                    const result = vm.voteDetail.bflySubVotes[0].bflyStatVote;
                    const mettingResult = JSON.parse(result.meetingResultJson);
                    const houseNum = Number(mettingResult["已经参会户数"]);
                    const areaNum = Number(mettingResult["已经参会的面积"]);
                    const unCertResult = JSON.parse(result.uncertResultJson);
                    const unCertHouse =  Number(unCertResult[0]["未认证户数"]);
                    const unCertArea =  Number(unCertResult[0]["未认证面积"]);
                    const voteHouseData = [
                        {name:'已确认参会',value:houseNum},
                        {name:'已认证未参会',value:Number(result.totalVoteQuantity) - houseNum - unCertHouse>0?Number(result.totalVoteQuantity) - houseNum - unCertHouse:0},
                        {name:'未认证未参会',value:unCertHouse},
                    ];
                    const voteAreaData = [
                        {name:'已确认参会',value:areaNum},
                        {name:'已认证未参会',value:Number(Number(result.totalVoteArea) - areaNum - unCertArea).toFixed(2)>0?Number(Number(result.totalVoteArea) - areaNum - unCertArea).toFixed(2):0},
                        {name:'未认证未参会',value:unCertArea},
                    ];

                    // totalVoteQuantity:小区总户数  totalVoteArea：小区总面积
                    const pieTitle1 = {name:'小区总户数',value:result.totalVoteQuantity};
                    const pieTitle2 = {name:'小区总面积',value:result.totalVoteArea};
                    voteHouseChart.setOption(getOption(pieTitle1,voteHouseData,'result',unCertHouse));
                    voteAreaChart.setOption(getOption(pieTitle2,voteAreaData,'result',unCertArea));

                    vm.totalQuantity = result.totalVoteQuantity;
                    vm.totalArea = result.totalVoteArea;

                    vm.voteDetail.bflySubVotes.forEach((item,index)=>{
                        let houseChart =  echarts.init(document.getElementById(`housePie${index}`));
                        let areaChart =  echarts.init(document.getElementById(`areaPie${index}`));
                        var houseData = JSON.parse(item.bflyStatVote.quantityResultJson);
                        var areaData = JSON.parse(item.bflyStatVote.areaResultJson);
                        vm.optionNum =houseData ? houseData.length : 0;
                        houseChart.resize({height:200 + vm.optionNum * 30});
                        areaChart.resize({height:200 + vm.optionNum * 30});
                        houseChart.setOption(getOption(pieTitle1,houseData,'house',unCertHouse));
                        areaChart.setOption(getOption(pieTitle2,areaData,'area',unCertArea));
                    })
                }
                })
            });
        },
        getResultInfo:function(id){
            $.get(baseURL + "bflyVote/info/" + id, function (r) {
                if(r.result.offlineVoteResultUrl){
                    vm.offlineFileList = [
                        {
                            name:'已上传投票结果',
                            url:r.result.offlineVoteResultUrl,
                            status:'success'
                        }
                    ]
                }
            })
        },

        // 0:未审核, 1:已通过 2:未通过 3审核中
        verifyStatusFormatter: function (cellvalue, options, rowdata) {
            switch (rowdata.isValid) {
                case 0:
                    return "<span class=\"label label-danger\">失效</span>";
                    break;
                case 1:
                    return "<span class=\"label label-success\">有效</span>";
                    break;
                case 2:
                    return "<span class=\"label label-warning\">未通过</span>";
                    break;
                case 3:
                    return "<span class=\"label label-danger\">审核中</span>";
                    break;
                default:
                    return rowdata.status;
                    break;
            }
        },
        //操作按钮格式化 表决状态 0:隐藏 1: 未开始 2:参会中 3: 参会结束 4:投票中 5:投票结束（表决）
        btnFormatter: function (cellvalue, options, rowdata) {
            var result = "";
            result += '<a class="btn btn-primary btn-sm" target="_blank" onclick="onView(' + rowdata.id + ',1)">投票结果总览</a>';
            if(rowdata.isShow===0){
                result += '&nbsp;&nbsp;<a class="btn btn-primary btn-sm" target="_blank" onclick="handleShow(' + rowdata.id + ',1)">显示</a>';
            }else if(rowdata.isShow===1){
                result += '&nbsp;&nbsp;<a class="btn btn-primary btn-sm" target="_blank" onclick="handleShow(' + rowdata.id + ',0)">隐藏</a>';
            }
            if(rowdata.status == 5) {
                result += '&nbsp;&nbsp;<a class="btn btn-primary btn-sm" target="_blank" onclick="viewPdfPrint(' + rowdata.id + ')">pdf预览打印</a>'
                result += '&nbsp;&nbsp;<a class="btn btn-primary btn-sm" target="_blank" onclick="onView(' + rowdata.id + ',2)">上传线下结果</a>';
            }

            return result;
        },
        handlePictureCardPreview(file) {
            window.open(file.response.url);
            // if (suffix.indexOf('doc') >= 0 || suffix.indexOf('xls') >= 0 || suffix.indexOf('ppt') >= 0) {
            //     window.open(officeViewUrl + file.url);
            // } else if (suffix.indexOf('pdf') >= 0) {
            //     window.open(file.url);
            // } else {
            //     this.dialogImageUrl = file.response.url;
            //     this.dialogVisible = true;
            // }
        },
        //上传图片
        //移除图片
        handleRemove(res,key,k) {
            // vm.paper.fileList.remove(file);

            vm.vote.bflySubVotes[key].bflyVoteItems[k].attachmentUrls= '';
        },
        handlePreview(file) {
            console.log("handlePreview", file);
        },
        //图片上传成功
        handleSuccess(res,key,k) {
            // console.log(res,'res')
            vm.vote.bflySubVotes[key].bflyVoteItems[k].attachmentUrls = res.url;
            vm.progress = false
        },
        handleProgress(){
            vm.progress = true
        },
        handleBeforeUpload(file) {
            var isAllowedImg = file.type === 'image/jpeg' || file.type === 'image/png' || file.type === 'image/gif' ||
                file.type.indexOf('application/vnd') >= 0 || file.type.indexOf('application/ms') >= 0 ||
                file.type.indexOf('application/pdf') >= 0;
            var isLt2M = file.size / 1024 / 1024 < 5;// 测试

            if (!isAllowedImg) {
                this.$message.error('上传图片只能是word,ppt,excel,pdf,图片格式!');
            }
            if (!isLt2M) {
                this.$message.error('上传图片大小不能超过 5MB!');
            }
            return isAllowedImg && isLt2M;
        },
        //关闭查看大图
        closeDialog() {
            this.dialogVisible = false;
        },
        // 线下结果相关
        handleRemoveOfflineUrl(res,file,fileList){
            vm.offlineFileList=[]
            $.ajax({
                type: "POST",
                url: baseURL + "bflyVote/deleteVoteOfflineUrl",
                contentType: "application/json",
                data: JSON.stringify({id:vm.curVoteId,offlineVoteResultUrl:""}),
                success: function(r){
                    if(r.code == 0){
                        alert('已删除上传结果');
                    }else{
                        alert(r.msg);
                    }
                }
            });

        },
        handleOfflineUrlSuccess(res,file,fileList){

            if(res.code == 0){
                vm.offlineFileList = [
                    {
                        name:file.name,
                        url:file.response.url,
                        status:'success'
                    }
                ]
            }else {
                vm.offlineFileList = [
                    {
                        name:file.name,
                        url:file.response.url,
                        status:'fail'
                    }
                ]
                vm.$confirm(`${file.name}，请删除后重新操作`)
            }
        },
        getVoteItemDetail(url){
            window.open(url)
        },
        // 导出参会信息
        exportMettingData(){
            window.location = baseURL + "bflyVote/exportMeeting/" + vm.voteDetail.id
        },
        // 导出线上投票结果
        exportVoteresult(){
            window.location = baseURL + "bflyVote/exportVote/" + vm.voteDetail.id
        },
    },
});

function onView(id,type) { // type ： 1 ->浏览  2->上传线下结果
    if (id == null) {
        vm.$message.warning('没有找到数据!');
        return;
    }
    vm.showList = false;
    vm.curVoteId = id;
    if(type===1){
        vm.pieWidth = document.getElementById("pieContent").offsetWidth/2;
        vm.viewMode = true;
        vm.title = "浏览";
        vm.getInfo(id)

    }else {
        vm.title = "上传线下结果";
        vm.getResultInfo(id)
        vm.showUpload = true
    }

}

//打印pdf
function viewPdfPrint(id) {
    window.open(baseURL+`bflyUser/voteManageDetailExport?id=${id}`)
}

function handleShow(id,isShow) {
    if (id == null) {
        vm.$message.warning('没有找到数据!');
        return;
    }
    let confirmText;
    if(isShow===0){
        confirmText="确定要隐藏该表决？"
    }else {
        confirmText="确定要显示该表决？"
    }

    confirm(confirmText, function(){
        $.ajax({
            type: "POST",
            // dataType:'json',
            url: baseURL + "bflyVote/updateVote",
            contentType: "application/json",
            data: JSON.stringify({id,isShow}),
            success: function(r){
                if(r.code == 0){
                    alert('操作成功', function(index){
                        $("#jqGrid").trigger("reloadGrid");
                    });
                }else{
                    alert(r.msg);
                }
            }
        });
    });

}

function getOption(pie,data,type,unCert){
    let legendData=[];
    let newData = [];
    let totalVotedArea = 0;
    let totalVotedQuantity = 0;
    if(type==='result'){
        data.forEach(item=>{
            legendData.push(item['name']);
        });
        newData = data
    }else {
        data.forEach(item => {
            legendData.push(item['选项']);
            let obj = {
                name:item['选项']
            };
            if (type==='area') {
                totalVotedArea += item['投票面积']
                obj.value = item['投票面积']===0?null:Number(item['投票面积']).toFixed(2)//过滤为0 的数据
            } else if(type==='house') {
                totalVotedQuantity += item['投票户数']
                obj.value = item['投票户数']===0?null:item['投票户数']
            }
            obj.itemStyle = {
                normal: {
                    label: {
                        show: true,
                    },
                    labelLine: {
                        show:true,
                    },
                }
            };
            newData.push(obj)
        });
        // 饼图基数为小区总户数和小区总面积  手动计算未投票的户数和面积
        // legendData.push("未投票");
        //         // const noVoteNum = type === 'area' ? Number(vm.totalArea - totalVotedArea).toFixed(2) : Number(vm.totalQuantity - totalVotedQuantity).toFixed(2)
        //         // newData.push({name:"未投票",value:noVoteNum});
        legendData.push("已认证未参会");
        legendData.push("未认证未参会");
        const noVoteNum = type === 'area' ? Number(vm.totalArea - totalVotedArea - unCert).toFixed(2) : Number(vm.totalQuantity - totalVotedQuantity-unCert)
        newData.push({name:"已认证未参会",value:noVoteNum>0?noVoteNum:0});
        newData.push({name:"未认证未参会",value:unCert});
    }

    const option ={
        title: {
            text: pie.name,
            subtext: pie.value,
            left: 'center'
        },
        tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b} : {c} ({d}%)'
        },
        legend: {
            orient: "vertical",
            left: "center",
            bottom:0,
            data:legendData,
        },
        series: [
            {
                name: '选项',
                type: 'pie',
                radius: '40%',
                center:["50%","120"],
                data:newData,
                label: {
                    fontSize: 12,
                    formatter: "{c}",
                    position:"inner"
                },
                labelLine: {
                    length: 4,
                    length2: 6
                },
                emphasis: {
                    itemStyle: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            },
            {
                name: '选项',
                type: 'pie',
                radius: '40%',
                center:["50%","120"],
                data:newData,
                label: {
                    fontSize: 12,
                    formatter: "{d}%",
                    position:"outside"
                },
                labelLine: {
                    length: 4,
                    length2: 6
                },
                emphasis: {
                    itemStyle: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }
        ]
    };
    return option

}

// 拼接option
function getVoteOptions (data){
    let result  = '[';
    for(let i=0,len = data.length;i<len;i++){
        if(i===len-1){
            result += `\"${data[i]}\"]`
        }else {
            result += `\"${data[i]}\",`
        }
    }
    return result
}
