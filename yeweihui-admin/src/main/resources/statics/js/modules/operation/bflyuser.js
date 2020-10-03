$(function () {
    //获取小区列表
    vm.getZonesList();
    vm.getUser();



});

var vm = new Vue({
    el: '#rrapp',
    data: {
        totalHouse:0,
        totalArea:0,
        zonesOptions: [],
        user: {},
        q: {
            zoneId:null,
            groupId: null,
            userName: null,
            phoneNumber: null,
            status:null,
        },
        admin:false,//判断是否为超级管理员
        showList: true,
        title: null,
        groupName: null,
        role: {},
        groupList: [],
        currUser:{},// 当前编辑用户
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
                            vm.admin = false
                            vm.queryTotal(result.user.zoneId)
                        }else {
                            vm.admin = true
                            vm.queryTotal("")
                        }
                        $("#jqGrid").jqGrid({
                            mtype:"POST",
                            postData:{
                                "page":1,
                                "size":10,
                                "zoneId":vm.q.zoneId?vm.q.zoneId:"",
                            },
                            url: baseURL + 'bflyUser/selectOwnerList',
                            // url: baseURL + 'bflyUser/list',
                            datatype: "json",
                            colModel: [
                                {label: 'ID', name: 'ID', index: "ID", width: 50, key: true},
                                {label: '小区名称', name: 'zoneName', index: "group_name", width: 100},
                                {label: '苑', name: 'court', index: "court", width: 100,},
                                {label: '楼号', name: 'building', index: "building", width: 40},
                                {label: '单元', name: 'unitName', index: "unitName", width: 40,},
                                {label: '房号', name: 'floorName', index: "floorName", width: 80},
                                {label: '姓名', name: 'userName', index: "userName", width: 80},
                                {label: '手机', name: 'phoneNum', index: "phoneNum", width: 100},
                                {label: '身份证号', name: 'idCard', index: "idCard", width: 150},
                                {label: '建筑面积', name: 'housingArea', index: "housingArea", width: 80},
                                {label: '状态', name: 'isValid', index: "isValid", width: 80, formatter: vm.verifyStatusFormatter},
                                {label: '审核状态', name: 'status', index: "status", width: 80, formatter: vm.statusFormatter},
                                {label: '认证头像', name: 'headerUrl', index: "headerUrl", width: 80, formatter: vm.avatarUrlFormatter},
                                {label: '认证时间', name: 'lastCertTime', index: "lastCertTime", width: 150},
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
                            // shrinkToFit: false,
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

        query: function (data) {
            vm.reload();
        },
        queryTotal: function(id){
            // 获取统计数据
            $.ajax({
                type: "post",
                dataType: "json",
                url: baseURL + `bflyUser/queryUserNumAndAllRoomArea?zoneId=${id}`,
                success: function (result) {
                    if (result.code == 0) {
                        vm.totalHouse = result.result.userNum
                        vm.totalArea = result.result.allArea
                    }else{
                        alert(result.msg);
                    }
                },
                error: function () {
                }
            });

        },

        // update: function () {
        //     var id = getSelectedRow();
        //     if (id == null) {
        //         return;
        //     }
        //     if (vm.user.zoneId === 0 && !vm.q.zoneId) {
        //         alert("请在下拉框中选择要修改的小区");
        //         return;
        //     }
        //     var zoneId = vm.user.zoneId > 0 ? vm.user.zoneId : vm.q.zoneId;
        //
        //     vm.groupList.map(g => {
        //         if (g.id == id) {
        //             vm.groupName = g.groupName;
        //         }
        //     });
        //     vm.showList = false;
        //     vm.title = "修改";
        //
        // },
        // saveOrUpdate: function () {
        //     var id = getSelectedRow();
        //     if (id == null) {
        //         return;
        //     }
        //     if (vm.user.zoneId === 0 && !vm.q.zoneId) {
        //         alert("请在下拉框中选择要修改的小区");
        //         return;
        //     }
        //     var zoneId = vm.user.zoneId > 0 ? vm.user.zoneId : vm.q.zoneId;
        //
        //     vm.groupList.map(g => {
        //         if (g.id == id) {
        //             vm.groupName = g.groupName;
        //         }
        //     });
        //     vm.showList = false;
        //     vm.title = "修改";
        //
        // },

        getInfo: function(id){
            $.get(baseURL + "bflyUser/info/"+id, function(r){
                vm.currUser =r.result;
            });
        },
        reload: function () {
            vm.showList = true;
            vm.currUser = {}
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            const param = {};
            // vm.q.zoneId ? param.zoneId = vm.q.zoneId : null;
            // vm.q.userName ? param.nickname = vm.q.userName : null;
            // vm.q.phoneNumber ? param.phoneNumber = vm.q.phoneNumber : null;
            $("#jqGrid").jqGrid('setGridParam', {
                postData: {
                    zoneId:vm.q.zoneId,
                    ownerName:vm.q.userName,
                    phoneNum:vm.q.phoneNumber,
                    status:vm.q.status,
                },
                page: page
            }).trigger("reloadGrid");
        },
        // 头像formatter
        avatarUrlFormatter: function (cellvalue, options, rowdata) {
            return '<img src="' + rowdata.headerUrl + '" id="img' + rowdata.ID + '" style="width:30px;height:30px;" />';
        },
        // 0:无效（撤销认证） 1： 有效（审核通过）
        verifyStatusFormatter: function (cellvalue, options, rowdata) {
            switch (rowdata.isValid) {
                case 0:
                    return "<span class=\"label label-danger\">无效</span>";
                    break;
                case 1:
                    return "<span class=\"label label-success\">有效</span>";
                    break;
                default:
                    return rowdata.status;
                    break;
            }
        },
        // 0:未审核, 1:已通过 2:未通过 3审核中
        statusFormatter: function (cellvalue, options, rowdata) {
            switch (rowdata.status) {
                // case 0:
                //     return "<span class=\"label label-warning\">未审核</span>";
                //     break;
                case 1:
                    return "<span class=\"label label-success\">已认证</span>";
                    break;
                case 2:
                    return "<span class=\"label label-warning\">未通过</span>";
                    break;
                case 3:
                    return "<span class=\"label label-danger\">审核中</span>";
                    break;
                case 0:
                    return "<span class=\"label label-danger\">已撤销</span>";
                    break;
                default:
                    return rowdata.status;
                    break;
            }
        },
        //操作按钮格式化
        btnFormatter: function (cellvalue, options, rowdata) {
            var result = "";
            //1 有效 0 撤销
            // 0:未审核, 1:已通过 2:未通过 3审核中
            if (rowdata.status == 1) {
                result += '<a class="btn btn-primary btn-sm" target="_blank" onclick="onView(' + rowdata.ID + ')">编辑</a>';
                result += '&nbsp;<a class="btn btn-primary btn-sm" onclick="revokeCert(' + rowdata.ID + ')">撤销认证</a>';
            }
            return result;
        },
        saveOrUpdate: function (event) {
            confirm('确定要修改用户的相关信息？',function(){
                const params = {
                    id:vm.currUser.id,
                    username:vm.currUser.username,
                    phoneNum:vm.currUser.phoneNum,
                    idCard:vm.currUser.idCard,
                    housingArea:vm.currUser.housingArea
                }
                $.ajax({
                    type: "post",
                    dataType: "json",
                    contentType: "application/json",
                    url: baseURL + `bflyUser/update`,
                    data:JSON.stringify(params),
                    success: function (result) {
                        if (result.code == 0) {
                            alert('操作成功', function(index){
                                vm.reload();
                            });
                        }else{
                            alert(result.msg);
                        }
                    },
                    error: function () {
                    }
                });
                vm.reload();
            })

        },
    }
});

function onView(id) {
    vm.showList = false;
    vm.title = "编辑用户信息";

    vm.getInfo(id)
}

// 撤销认证
function revokeCert(id) {
    if(id == null){
        vm.$message.warning('没有找到数据!');
        return;
    }
    confirm('确定要撤销该用户的认证？',function(){
        $.ajax({
            type: "post",
            dataType: "json",
            url: baseURL + `bflyUser/delete/${id}`,
            success: function (result) {
                if (result.code == 0) {
                    if(result.msg == "撤销认证前请先完成已参会投票"){
                        alert(result.msg)
                    }else {
                        alert('操作成功', function(index){
                            vm.reload();
                        });
                    }
                }else{
                    alert(result.msg);
                }
            },
            error: function () {
            }
        });
        vm.reload();
    })

}