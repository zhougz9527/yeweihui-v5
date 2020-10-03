$(function () {
    vm.getZonesList();
    vm.getUser();
    $('#file-upload-input').change(function () {
        // if(!vm.zoneId){
        //     console.log('111')
        //    return  alert('请先选择需要导入业主的小区！')
        // }
        // console.log('2222')
        importExcel();
    });
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
        zoneId:null,// 选择导入业主的小区id
        usersListResult:[],// 业主导入成功结果
        importSuccess:false,// 导入预览弹窗
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
                            vm.zoneId = result.user.zoneId;
                            vm.admin = false
                        }else {
                            vm.admin = true
                        }
                        $("#jqGrid").jqGrid({
                            datatype: "local",
                            colModel: [
                                {label: 'ID', name: 'id', index: "id", width: 50, key: true},
                                // {label: '小区名称', name: 'zoneName', index: "group_name", width: 100},
                                {label: '苑', name: 'court', index: "court", width: 40},
                                {label: '楼号', name: 'building', index: "building", width: 40},
                                {label: '单元', name: 'unitName', index: "unitName", width: 40},
                                {label: '房号', name: 'floorName', index: "floorName", width: 80},
                                {label: '真实姓名', name: 'userName', index: "userName", width: 80},
                                {label: '手机', name: 'phoneNum', index: "phoneNum", width: 80},
                                {label: '身份证号', name: 'idCard', index: "idCard", width: 80},
                                {label: '建筑面积', name: 'housingArea', index: "housingArea", width: 80},
                            ],
                            viewrecords: true,
                            height: 630,
                            rowNum: 10,
                            rowList: [10, 30, 50],
                            rownumbers: true,
                            rownumWidth: 50,
                            autowidth: true,
                            // multiselect: true,
                            // jsonReader: {
                            //     root: "page.list",
                            //     page: "page.currPage",
                            //     total: "page.totalPage",
                            //     records: "page.totalCount"
                            // },
                            // prmNames: {
                            //     page: "page",
                            //     rows: "limit",
                            //     order: "order"
                            // },
                            gridComplete: function () {
                                //隐藏grid底部滚动条
                                $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
                            }
                        })



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
            vm.q.zoneId ? param.zoneId = vm.q.zoneId : null;
            vm.q.userName ? param.nickname = vm.q.userName : null;
            vm.q.phoneNumber ? param.phoneNumber = vm.q.phoneNumber : null;
            $("#jqGrid").jqGrid('setGridParam', {
                postData: {
                    ...param
                },
                page: page
            }).trigger("reloadGrid");
        },
        // 头像formatter
        avatarUrlFormatter: function (cellvalue, options, rowdata) {
            return '<img src="' + rowdata.avatarUrl + '" id="img' + rowdata.id + '" style="width:30px;height:30px;" />';
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
            //审核状态 0审核中 1审核通过 2撤销 3未通过
            if (rowdata.status == 0) {
                result += '&nbsp;<a class="btn btn-primary btn-sm" onclick="verifyUser(' + rowdata.id + ', 1)">通过</a>';
                result += '&nbsp;<a class="btn btn-primary btn-sm" onclick="verifyUser(' + rowdata.id + ', 3)">拒绝</a>';
            }
            return result;
        }
    }
});

//审核  审核状态 0审核中 1审核通过 2撤销 3未通过
function verifyUser(id, verifyStatus) {
    // $.ajax({
    //     type: "post",//方法类型
    //     dataType: "json",
    //     url: baseURL + "/user/user/verifyUser",
    //     // contentType: "application/json; charset=utf-8",
    //     contentType: "application/x-www-form-urlencoded; charset=utf-8",
    //     // data: JSON.stringify({parentId:0}),
    //     data: {
    //         userId:id,
    //         verifyStatus:verifyStatus
    //     },
    //     success: function (result) {
    //         if (result.code == 0) {
    //             alert('操作成功', function(index){
    //                 vm.reload();
    //             });
    //         }else{
    //             alert(result.msg);
    //         }
    //     },
    //     error: function () {
    //     }
    // });
    vm.reload();
}

function importExcel(event) {
    var file = document.getElementById('file-input');

    if(!vm.zoneId){
        file.value = '';
        return alert('请先选择需要上传业主信息的小区！')
    }

    const formData = new FormData();
    // let file = $('#file-input').get(0).files[0];
    let newfile = event.target.files[0]

    formData.append("file", newfile);
    if (formData === "" || formData === null) {
        return;
    }
    formData.append("zoneId", vm.zoneId);
    $.ajax({
        type: 'POST',
        url: baseURL + 'bflyRoomTemp/importExcel ',
        data: formData,
        cache: false,
        action: 'upload',
        contentType: false,
        processData: false,

        success: function (data) {
            if (data && data.code == 0) {
                file.value = '';
                vm.usersListResult = data.result;
                // var usersList = vm.usersListResult;
                console.log(data.result,'dataresult');
                // for(var i=0;i<=usersList.length;i++){
                //     jQuery("#jqGrid").jqGrid('addRowData',i+1,usersList[i]);
                // }
                vm.importSuccess = true
            } else {
                alert(data.msg);
                file.value = '';
            }
        },
        error: function (data) {
            alert(data.msg);
            file.value = '';
        }
    });
}

function btnExport (){
    const aoa = [
        ['小区','苑','幢','单元','房号','建筑面积（㎡）','姓名','手机','身份证号'],
        ['钱江苑','3苑','1','2','202','129.9','王帅','15257621312','150421197623135213'],
        ['钱江苑','','商铺','2','202','129.9','王帅','15257621312','150421197623135213'],
        ['钱江苑','3苑','排屋','2','202','129.9','王帅','15257621312','15042119762313521X'],
    ];
    var sheet = XLSX.utils.aoa_to_sheet(aoa);
    openDownloadDialog(sheet2blob(sheet), '业主信息导入模板.xlsx');

}
// 确认导入信息
function confirmImport (){
    const userList = vm.usersListResult
    let userIds = [];

    userList.forEach(item => {
        userIds.push(item.id)
    });
    console.log(userIds,'userIds');
    $.ajax({
        type: "POST",
        url: baseURL + "bflyRoomTemp/saveAll",
        data: {zoneId:vm.zoneId,tempIds:userIds},
        dataType: "json",
        traditional: true,
        success: function(result){
            if(result.code == 0){
                vm.importSuccess = false;
                vm.usersListResult = [];
                vm.zoneId = '';
                jQuery("#jqGrid").jqGrid("clearGridData");
                alert('导入成功！')
            }else{
                vm.importSuccess = false;
                vm.usersListResult = [];
                vm.zoneId = '';
                jQuery("#jqGrid").jqGrid("clearGridData");
                alert(result.msg)
            }
        }
    });
}

// 取消导入
function cancelImport(){
    confirm('确认取消导入这些业主信息？', function(){
        vm.importSuccess = false
        vm.usersListResult = [];
        vm.zoneId = '';
        jQuery("#jqGrid").jqGrid("clearGridData");
        alert('取消成功！')
    });
}