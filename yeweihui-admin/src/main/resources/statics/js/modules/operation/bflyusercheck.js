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
        q: {
            zoneId: null,
            groupId: null,
            userName: null,
            phoneNumber: null
        },
        admin:false,
        showList: true,
        title: null,
        groupName: null,
        role: {},
        groupList: [],
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
                        }else {
                            vm.admin = true
                        }
                        $("#jqGrid").jqGrid({
                            url: baseURL + 'bflyUser/selectOwnerCertList',
                            mtype: "POST",
                            postData:{
                                "page":1,
                                "size":10,
                                "zoneId":vm.q.zoneId?vm.q.zoneId:"",
                            },
                            datatype: "json",
                            colModel: [
                                {label: 'ID', name: 'id', index: "id", width: 50, key: true},
                                {label: '小区名称', name: 'zoneName', index: "zoneName", width: 100},
                                {label: '苑', name: 'court', index: "court", width: 100},
                                {label: '楼号', name: 'building', index: "building", width: 40},
                                {label: '单元', name: 'unitName', index: "unitName", width: 40},
                                {label: '房号', name: 'floorName', index: "floorName", width: 80},
                                {label: '真实姓名', name: 'checkUserName', index: "checkUserName", width: 80},
                                {label: '手机', name: 'checkPhoneNum', index: "checkPhoneNum", width: 140},
                                {label: '身份证号', name: 'checkIdCard', index: "checkIdCard", width: 160},
                                {label: '建筑面积', name: 'checkHousingArea', index: "checkHousingArea", width: 80},
                                {
                                    label: '房产证信息',
                                    name: 'checkHouseCertificateUrl',
                                    index: "checkHouseCertificateUrl",
                                    width: 120,
                                    formatter: vm.buildingUrlFormatter
                                },
                                {label: '身份证信息', name: 'checkIdCardUrl', index: "checkIdCardUrl", width: 120, formatter: vm.idCardUrlFormatter},
                                {label: '认证头像', name: 'checkHeaderUrl', index: "checkHeaderUrl", width: 80, formatter: vm.avatarUrlFormatter},
                                {label: '状态', name: 'status', index: "status", width: 80, formatter: vm.verifyStatusFormatter},
                                {label: '操作', name: 'ope', index: 'ope', width: 100, formatter: vm.btnFormatter}
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

        reload: function () {
            vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            const param = {};
            // vm.q.zoneId ? param.zoneId = vm.q.zoneId : null;
            // vm.q.userName ? param.ownerName = vm.q.userName : null;
            // vm.q.phoneNumber ? param.phoneNumber = vm.q.phoneNumber : null;
            $("#jqGrid").jqGrid('setGridParam', {
                postData: {
                    zoneId:vm.q.zoneId,
                    ownerName:vm.q.userName,
                    phoneNum:vm.q.phoneNumber
                },
                page: page
            }).trigger("reloadGrid");
        },
        // 头像formatter
        avatarUrlFormatter: function (cellvalue, options, rowdata) {
            return '<a target="_blank" href="' + rowdata.checkHeaderUrl + '" id="avatar' + rowdata.id + '" style="cursor:pointer;color:#337ab7" download="身份证信息"><img src="' + rowdata.checkHeaderUrl + '" id="img' + rowdata.id + '" style="width:30px;height:30px;"/></a>';
        },
        // 0:未审核, 1:已通过 2:未通过 3审核中
        verifyStatusFormatter: function (cellvalue, options, rowdata) {
            switch (rowdata.status) {
                case 0:
                    return "<span class=\"label label-warning\">未审核</span>";
                    break;
                case 1:
                    return "<span class=\"label label-success\">已通过</span>";
                    break;
                case 2:
                    return "<span class=\"label label-warning\">未通过</span>";
                    break;
                case 3:
                    return "<span class=\"label label-danger\">审核中</span>";
                    break;
                case 4:
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
            //审核状态 0审核中 1审核通过 2撤销 3未通过
            if (rowdata.status == 0) {
                result += '&nbsp;<a class="btn btn-primary btn-sm" onclick="verifyUser(' + rowdata.id + ', 1)">通过</a>';
                result += '&nbsp;<a class="btn btn-primary btn-sm" onclick="verifyUser(' + rowdata.id + ', 3)">拒绝</a>';
            }
            return result;
        },
        // 身份证formatter
        idCardUrlFormatter: function (cellvalue, options, rowdata) {
            if (rowdata.checkIdCardUrl) {
                if(rowdata.checkIdCardUrl && rowdata.checkIdCardUrlExtra1){
                    return '<a target="_blank" href="' + rowdata.checkIdCardUrl + '" id="idcard' + rowdata.id + '" style="cursor:pointer;color:#337ab7" download="身份证信息1">下载1</a>' +
                        '<a target="_blank" href="' + rowdata.checkIdCardUrlExtra1 + '" id="idcard' + rowdata.id + '" style="cursor:pointer;color:#337ab7" download="身份证信息2">下载2</a>';
                }
                return '<a target="_blank" href="' + rowdata.checkIdCardUrl + '" id="idcard' + rowdata.id + '" style="cursor:pointer;color:#337ab7" download="身份证信息">下载</a>';
            } else {
                return '';
            }
        },
        // 房产证formatter
        buildingUrlFormatter: function (cellvalue, options, rowdata) {
            console.log(rowdata,'rowdata')
            if (rowdata.checkHouseCertificateUrl) {
                if(rowdata.checkHouseCertificateUrl && rowdata.checkHouseCertificateUrlExtra1){
                    return '<a target="_blank" href="' + rowdata.checkHouseCertificateUrl + '" id="building' + rowdata.id + '" style="cursor:pointer;color:#337ab7" download="test" download="房产证信息1">下载1</a>' +
                        '<a target="_blank" href="' + rowdata.checkHouseCertificateUrlExtra1 + '" id="building' + rowdata.id + '" style="cursor:pointer;color:#337ab7" download="test" download="房产证信息2">下载2</a>';
                }
                return '<a target="_blank" href="' + rowdata.checkHouseCertificateUrl + '" id="building' + rowdata.id + '" style="cursor:pointer;color:#337ab7" download="test" download="房产证信息">下载</a>';
            } else {
                return '';
            }
        },


    }
});

function verifyUser(id,type){
if(id == null){
		vm.$message.warning('没有找到数据!');
		return;
	}

let confirmText;
let certStatus;
if(type===1){// 通过
    confirmText='确定要通过该用户的审核？'
    certStatus= true
}else if(type===3){
    confirmText='确定要拒绝该用户的审核？'
    certStatus= false
}

    confirm(confirmText, function(){
        $.ajax({
            type: "POST",
            url: baseURL + "bflyUser/certOwner",
            data: {userCertId:id,cert:certStatus},
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