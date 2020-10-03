$(function () {
    //获取小区列表
    vm.getZonesList();
    vm.getUser();

});

var vm = new Vue({
    el: '#rrapp',
    data: {
        zonesOptions: [],
        user: {},
        vote: {},
        q: {
            zoneId: null,
            username:null,
            voteTitle: null,
            status:null,
        },
        admin:false,
        showList: true,
        viewMode: false,
        title: null,
        groupName: null,
        role: {},
        groupList: [],
        dialogImageUrl: '',
        dialogVisible: false,
        voteInfo:{
            title:'',
            status:null,
            voteSubmitTime:'',
            bflyUserVote:{},
            userVote:{},
            childVote:{}
        },// 用户投票详情
        //日期
        pickerOptions: {
            disabledDate(time) {
                // return time.getTime() > Date.now();
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
    },
    methods: {
        getVoteItemDetail(url){
            window.open(url)
        },
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
                            vm.admin = false
                        }else {
                            vm.admin = true;
                        }
                        $("#jqGrid").jqGrid({
                            url: baseURL + 'bflyVote/userVoteList',
                            mtype: "POST",
                            datatype: "json",
                            postData:{
                                "page":1,
                                "size":10,
                                "zoneId":vm.q.zoneId?vm.q.zoneId:"",

                            },
                            colModel: [
                                {label: 'ID', name: 'id', index: "id", width: 50, key: true},
                                {label: '表决标题', name: 'title', index: "title", width: 80},
                                {label: '小区名称', name: 'name', index: "name", width: 100},
                                {label: '苑', name: 'court', index: "court", width: 40},
                                {label: '楼号', name: 'building', index: "building", width: 40},
                                {label: '单元', name: 'unitName', index: "unitName", width: 40},
                                {label: '房号', name: 'floorName', index: "floorName", width: 80},
                                {label: '业主姓名', name: 'userName', index: "userName", width: 80},
                                {label: '建筑面积', name: 'housingArea', index: "housingArea", width: 80},
                                {label: '投票状态', name: 'status', index: "status", width: 80,formatter: vm.statusFormatter},
                                {label: '投票时间', name: 'voteSubmitTime', index: "voteSubmitTime", width: 140},
                                {label: '操作', name: 'ope', index: 'ope', width: 160, formatter: vm.btnFormatter}
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
                                $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "scroll"});
                            }
                        });
                    }
                },
                error: function () {
                }
            });
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
                type: null,
                status: null,
                happenDate: null,
                zoneId: null,
                copyToMemberEntityList: [],
                //资源上传
                fileList: [],
                childVote: [{options: [{}], content: [{fileList: []}]}],
            };

        },


        saveOrUpdate: function () {
            // console.log(vm.vote, 'vm.vote')
        },

        reload: function () {
            vm.showList = true;
            // vm.voteInfo = {}
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                postData: {
                    // ...param
                    zoneId:vm.q.zoneId,
                    userName:vm.q.userName,
                    status:vm.q.status,
                    title:vm.q.voteTitle,
                },
                page: page
            }).trigger("reloadGrid");
        },
        // 返回
        goBack:function(){
            vm.reload();
            vm.voteInfo = {
                title:'',
                isShow:null,
                status:null,
                voteSubmitTime:'',
                bflyUserVote:{},
                userVote:{},
                childVote:{}
            }
        },

        getInfo: function (userId,voteId) {
            $.ajax({
                type: "post",//方法类型
                dataType: "json",
                url: baseURL + "bflyVote/selectUserVoteDetail",// 需要修改
                data:{bflyUserId:userId,bflyVoteId:voteId},
                success: function (result) {
                    if (result.code == 0) {
                        vm.showList = false;
                        vm.title = "浏览";
                        // vm.voteInfo = result.bflyInfo;
                        vm.voteInfo.title = result.bflyInfo.title;
                        vm.voteInfo.isShow = result.bflyInfo.isShow;
                        vm.voteInfo.status = result.bflyInfo.status;
                        vm.voteInfo.userVote = result.bflyInfo.bflyUserVote.bflyUser;
                        vm.voteInfo.voteSubmitTime = result.bflyInfo.bflyUserVote.voteSubmitTime;
                        vm.voteInfo.bflyUserVote = result.bflyInfo.bflyUserVote;
                        vm.voteInfo.childVote = result.bflyInfo.bflySubVotes
                    }else {
                        alert(result.msg)
                    }
                },
                error: function () {
                }
            });
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
        //操作按钮格式化
        btnFormatter: function (cellvalue, options, rowdata) {
            var result = "";
            result += '<a class="btn btn-primary btn-sm" target="_blank" onclick="onView(' + rowdata.bflyUserId +',' + rowdata.bflyVoteId+')">浏览</a>';
            if (rowdata.status == 2) {// 已投票用户
                result += '&nbsp;&nbsp;<a class="btn btn-primary btn-sm" target="_blank" onclick="viewPdfPrint(' + rowdata.bflyUserId +',' + rowdata.bflyVoteId+')">pdf预览打印</a>';
            }
            return result;
        },
        //0: 未参会 1:已参会,未投票 2:已投票（用户）
        statusFormatter: function (cellvalue, options, rowdata) {
            switch(rowdata.status){
                case 1:
                    return "<span class=\"label label-warning\">未投票</span>";
                    break;
                case 2:
                    return "<span class=\"label label-success\">已投票</span>";
                    break;
                default:
                    return rowdata.status;
                    break;
            }

        }

    }
});

function onView(userId,voteId) {
    if (userId == null||voteId===null) {
        vm.$message.warning('没有找到数据!');
        return;
    }

    vm.getInfo(userId,voteId)
}

//打印pdf
function viewPdfPrint(userId,voteId) {
    if (userId == null||voteId===null) {
        vm.$message.warning('没有找到数据!');
        return;
    }
    window.open(baseURL + `bflyUser/voteDetailExport?voteId=${voteId}&userId=${userId}`)

    // vm.$confirm('是否需要附件?', '提示', {
    //     confirmButtonText: '是',
    //     cancelButtonText: '不需要',
    //     distinguishCancelAndClose: true,
    //     type: 'info'
    // }).then(() => {
    //     window.open(baseURL + `bflyUser/voteDetailExport?voteId=${voteId}&userId=${userId}`)
    // }).catch((action) => {
    //     if (action === 'cancel') {
    //         window.open(baseURL + `operation/bill/viewPdf/${id}/no` );
    //     }
    // });
}