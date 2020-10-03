$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'user/appuser/list',
        datatype: "json",
        colModel: [
            {label: 'id', name: 'id', index: 'id', width: 50, key: true},
            {label: '用户头像url', name: 'headIconUrl', index: 'head_icon_url', width: 80, formatter: vm.headIconUrlFormatter},
            {label: '昵称', name: 'nickname', index: 'nickname', width: 80},
            {label: '用户类型', name: 'type', index: 'type', width: 80, formatter: vm.userTypeFormatter}, /*（游客 GUEST、普通用户 NORMAL_USER、业委会达人 DQ_EXPERT、业委会歌手 DQ_SINGER、业委会号 DQ_OFFICIAL_ACCOUNT、管理员 ADMIN）*/
            {label: '手机', name: 'phone', index: 'phone', width: 80},
            {label: '介绍', name: 'description', index: 'description', width: 80},
            {label: '生日', name: 'birthday', index: 'birthday', width: 80},
            {label: '性别', name: 'gender', index: 'gender', width: 80, formatter: vm.userGenderFormatter},
            {label: '地区', name: 'area', index: 'area', width: 80},
            {label: '注册时间', name: 'registerTime', index: 'register_time', width: 80},
            {label: '最后一次登陆时间', name: 'lastLoginTime', index: 'last_login_time', width: 80},
            {label: '操作', name: 'ope', index: 'ope', width: 100, formatter: vm.userBtnFormatter}
            /*{ label: '登陆账号', name: 'account', index: 'account', width: 80 },
             { label: '密码', name: 'password', index: 'password', width: 80 },
             { label: '申请成为 type中的一个角色,默认NONE,不申请', name: 'applyFor', index: 'apply_for', width: 80 },
             { label: '升级申请时间', name: 'applyForTime', index: 'apply_for_time', width: 80 },
             { label: '累计浏览次数', name: 'viewTimes', index: 'view_times', width: 80 },
             { label: '累计评论次数', name: 'commentTimes', index: 'comment_times', width: 80 },
             { label: '累计发微读数', name: 'weReadTimes', index: 'we_read_times', width: 80 },
             { label: '累计小视频数', name: 'tinyVideoCount', index: 'tiny_video_count', width: 80 },
             { label: '累计视频数', name: 'videoCount', index: 'video_count', width: 80 },
             { label: '累计歌曲数', name: 'songCount', index: 'song_count', width: 80 },
             { label: '累计文章数', name: 'articleCount', index: 'article_count', width: 80 },
             { label: '累计图集数', name: 'picGroupCount', index: 'pic_group_count', width: 80 },
             { label: '手机唯一识别码', name: 'phoneUniqueCode', index: 'phone_unique_code', width: 80 },
             { label: 'token', name: 'token', index: 'token', width: 80 },
             { label: '是否冻结 1 是 0否', name: 'isFreeze', index: 'is_freeze', width: 80 },
             { label: '冻结原因', name: 'freezeReason', index: 'freeze_reason', width: 80 },
             { label: '是否禁言 1 是 0否', name: 'isMute', index: 'is_mute', width: 80 },
             { label: '禁言原因', name: 'muteReason', index: 'mute_reason', width: 80 },
             { label: 'app转发次数', name: 'appShareCount', index: 'app_share_count', width: 80 },
             { label: '微信转发次数', name: 'wechatShareCount', index: 'wechat_share_count', width: 80 },
             { label: '朋友圈转发次数', name: 'momentsShareCount', index: 'moments_share_count', width: 80 },
             { label: '微博转发次数', name: 'microBlogShareCount', index: 'micro_blog_share_count', width: 80 },
             { label: 'qq转发次数', name: 'qqShareCount', index: 'qq_share_count', width: 80 },
             { label: 'app今日阅读次数', name: 'todayReadCount', index: 'today_read_count', width: 80 },
             { label: 'app累计阅读次数', name: 'totalReadCount', index: 'total_read_count', width: 80 },
             { label: 'app平均日阅读次数', name: 'avgDayReadCount', index: 'avg_day_read_count', width: 80 },
             { label: '今日停留（分钟）', name: 'todayStayMinutes', index: 'today_stay_minutes', width: 80 },
             { label: '累计停留（分钟）', name: 'totalStayMinutes', index: 'total_stay_minutes', width: 80 },
             { label: '使用app天数', name: 'totalAppUseDays', index: 'total_app_use_days', width: 80 },
             { label: '性别 MALE FEMALE', name: 'gender', index: 'gender', width: 80 },
             { label: '创建时间', name: 'createTime', index: 'create_time', width: 80 },
             { label: '修改时间', name: 'modifyTime', index: 'modify_time', width: 80 }		*/
        ],
        viewrecords: true,
        height: 630,
        rowNum: 10,
        rowList: [10, 30, 50],
        rownumbers: true,
        rownumWidth: 25,
        autowidth: true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader: {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames: {
            page: "page",
            rows: "limit",
            order: "order"
        },
        gridComplete: function () {
            //隐藏grid底部滚动条
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
        }
    });

    //用户头像上传绑定
    // initAliyunUpload("userHeadIconPictureUploadBtn ", "userHeadIconPictureUploadInputFile", "userHeadIconPictureUrl", "userHeadIconPictureImg");
    initAliyunUpload("userHeadIconPictureUploadBtn ", "userHeadIconPictureUploadInputFile", "userHeadIconPictureSetValue");

    //获取用户分类大类多选数据
    vm.getUserMainCategoryList();
});

var vm = new Vue({
    el: '#rrapp',
    data: {
        showList: true,
        //用户详情
        showUser: false,
        //新增虚拟用户
        showAddVirtualUser: false,
        title: null,
        appUser: {
            userHeadIconPictureUrl:null,
            gender:null,
            userMainCategoryIdList:null,

        },
        q:{
            nickname:null,
            userType:null
        },
        //用户类型选择
        options: [{
            value: '',
            label: '全部'
        }, {
            value: 'VIRTUAL_USER',
            label: '虚拟用户'
        }, {
            value: 'GUEST',
            label: '游客'
        }, {
            value: 'NORMAL_USER',
            label: '普通用户'
        }, {
            value: 'DQ_EXPERT',
            label: '业委会达人'
        }, {
            value: 'DQ_SINGER',
            label: '业委会歌手'
        }, {
            value: 'DQ_OFFICIAL_ACCOUNT',
            label: '业委会号'
        }, {
            value: 'ADMIN',
            label: '管理员'
        }],
        value: '',
        //歌手类型字典
        userGenderList: [
            {
                value: 'MALE',
                label: '男'
            },{
                value: 'FEMALE',
                label: '女'
            },
        ],
        //用户分类大类多选
        userMainCategoryIdListStr:null,
        userMainCategoryIdList:[]
    },
    methods: {
        query: function () {
            vm.reload();
        },
        add: function () {
            vm.showList = false;
            vm.title = "新增";
            vm.appUser = {
                userMainCategoryIdList:[]
            };

            //用户分类大类多选清空
            $('#userMainCategorySelectPage').selectPageClear();
        },
        //新增虚拟用户
        addVirtualUser: function () {
            vm.showList = false;
            vm.showUser = false;
            vm.showAddVirtualUser = true;
            vm.title = "新增虚拟用户";
            vm.appUser = {
                type:'VIRTUAL_USER',
                headIconUrl:null,
                gender:null,
                userMainCategoryIdList:[]
            };
        },
        update: function (event) {
            var id = getSelectedRow();
            if (id == null) {
                return;
            }
            vm.showList = false;
            vm.title = "修改";

            vm.getInfo(id);
        },
        //修改虚拟用户弹框
        updateVirtualUser: function (event) {
            var id = getSelectedRow();
            if (id == null) {
                return;
            }

            // vm.getInfo(id);
            $.get(baseURL + "user/appuser/info/" + id, function (r) {
                if (r.appUser.type == 'VIRTUAL_USER'){
                    vm.appUser = r.appUser;

                    vm.showList = false;
                    vm.showUser = false;
                    vm.showAddVirtualUser = true;
                    vm.title = "修改虚拟用户";
                }else{
                    alert("当前选择的用户不是虚拟用户，不能进行修改");
                }
            });
        },
        saveOrUpdate: function (event) {
            //歌曲封面url赋值
            vm.appUser.headIconUrl = $("#userHeadIconPictureUrl").val();

            var url = vm.appUser.id == null ? "user/appuser/save" : "user/appuser/update";
            $.ajax({
                type: "POST",
                url: baseURL + url,
                contentType: "application/json",
                data: JSON.stringify(vm.appUser),
                success: function (r) {
                    if (r.code === 0) {
                        alert('操作成功', function (index) {
                            vm.reload();
                        });
                    } else {
                        alert(r.msg);
                    }
                }
            });
        },
        del: function (event) {
            var ids = getSelectedRows();
            if (ids == null) {
                return;
            }

            confirm('确定要删除选中的记录？', function () {
                $.ajax({
                    type: "POST",
                    url: baseURL + "user/appuser/delete",
                    contentType: "application/json",
                    data: JSON.stringify(ids),
                    success: function (r) {
                        if (r.code == 0) {
                            alert('操作成功', function (index) {
                                $("#jqGrid").trigger("reloadGrid");
                            });
                        } else {
                            alert(r.msg);
                        }
                    }
                });
            });
        },
        getInfo: function (id) {
            //用户分类大类多选清空
            $('#userMainCategorySelectPage').selectPageClear();
            
            $.get(baseURL + "user/appuser/info/" + id, function (r) {
                vm.appUser = r.appUser;
            });

            //用户分类大类多选
            vm.userMainCategoryIdListStr = vm.appUser.userMainCategoryIdList.join(",");
            $('#userMainCategorySelectPage').val(vm.userMainCategoryIdListStr);
            $('#userMainCategorySelectPage').selectPageRefresh();
        },
        reload: function (event) {
            vm.showList = true;
            vm.showAddVirtualUser = false;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                postData:{
                    'nickname': vm.q.nickname,
                    'userType': vm.q.userType
                },
                page: page
            }).trigger("reloadGrid");
        },
        headIconUrlFormatter: function (cellvalue, options, rowdata) {
            return '<img src="' + rowdata.headIconUrl + '" id="img' + rowdata.id + '" style="width:30px;height:30px;" />';
        },
        userBtnFormatter: function (cellvalue, options, rowdata) {
            var result = "";
            if(rowdata.isFreeze == 1){
                result += '<a class="btn btn-primary btn-sm" onclick="unFreeze(' + rowdata.id + ')">解冻</a>';/*<i class="fa fa-plus"></i>*/
            }else{
                result += '<a class="btn btn-primary btn-sm" onclick="freeze(' + rowdata.id + ')">冻结</a>';/*<i class="fa fa-plus"></i>*/
            }
            if(rowdata.isMute == 1){
                result += '&nbsp;<a class="btn btn-primary btn-sm" onclick="unMute(' + rowdata.id + ')">解除禁言</a>';/*<i class="fa fa-plus"></i>*/
            }else{
                result += '&nbsp;<a class="btn btn-primary btn-sm" onclick="mute(' + rowdata.id + ')">禁言</a>';/*<i class="fa fa-plus"></i>*/
            }
            result += '&nbsp;<a class="btn btn-primary btn-sm" onclick="detail(' + rowdata.id + ')">详情</a>'
            return result;
        },
        //查看详情
        detail: function (id) {
            alert(id);
            vm.showList = false;
            vm.showUser = true;
            vm.showAddVirtualUser = false;
            vm.title = "详情";

            vm.getInfo(id)
        },
        userTypeFormatter: function (cellvalue, options, rowdata) {
            var result = "";
            switch (rowdata.type) {//（游客 GUEST、普通用户 NORMAL_USER、业委会达人 DQ_EXPERT、业委会歌手 DQ_SINGER、业委会号 DQ_OFFICIAL_ACCOUNT、管理员 ADMIN）
                case "GUEST":
                    result += '游客';
                    break;
                case "NORMAL_USER":
                    result += '普通用户';
                    break;
                case "DQ_EXPERT":
                    result += '业委会达人';
                    break;
                case "DQ_SINGER":
                    result += '业委会歌手';
                    break;
                case "DQ_OFFICIAL_ACCOUNT":
                    result += '业委会号';
                    break;
                case "ADMIN":
                    result += '管理员';
                    break;
                case "VIRTUAL_USER":
                    result += '虚拟用户';
                    break;
                default:break;
            }
            return result;
        },
        //性别格式化
        userGenderFormatter: function (cellvalue, options, rowdata) {
            var result = "";
            switch (rowdata.gender) {
                case "MALE":
                    result += '男';
                    break;
                case "FEMALE":
                    result += '女';
                    break;
                case "UNKNOWN":
                    result += '未知';
                    break;
                default:break;
            }
            return result;
        },
        //获取用户分类大类多选数据
        getUserMainCategoryList: function () {
            $.ajax({
                type: "post",//方法类型
                dataType: "json",
                url: baseURL + "common/category/simpleList",
                contentType: "application/json; charset=utf-8",
                data: JSON.stringify({parentCategoryId:0}),
                success: function (result) {
                    if (result.code == 0) {
                        $('#userMainCategorySelectPage').selectPage({
                            showField: 'categoryName',
                            keyField: 'id',
                            pageSize: 10,
                            data: result.list,
                            multiple: true,
                            multipleControlbar: false,
                            eSelect: function (data) {
                                vm.appUser.userMainCategoryIdList.push(data.id);
                                console.log("userMainCategoryIdList eSelect:" + vm.appUser.userMainCategoryIdList);
                            },
                            eTagRemove: function (datas) {
                                if (datas.length > 0) {
                                    for (var i = 0; i < vm.appUser.userMainCategoryIdList.length; i++) {
                                        if (vm.appUser.userMainCategoryIdList[i] == datas[0].id) {
                                            console.log("datas[0].id:" + datas[0].id + "" );
                                            vm.appUser.userMainCategoryIdList.splice(i, 1)
                                        }
                                    }
                                }
                                /*if(datas && datas.length){
                                    for(var i=0;i<datas.length;i++){
                                        vm.appUser.vSingerCategoryIdList.splice(i, 1);
                                        console.log(datas[i].id  + '(' + datas[i].categoryName + ') item removed');
                                    }
                                }*/
                                console.log("getTagList eTagRemove:" + vm.appUser.userMainCategoryIdList);
                            }
                        });
                        /*console.log(result.tagList.join());
                         vm.tagListArrStr = result.tagList.join();*/
                    }
                },
                error: function () {
                }
            });
        }
    }
});

// 冻结用户
function freeze(id) {
    if (null != id) {
        vm.appUser.id = id;
    }
    confirm('确定要冻结用户？', function () {
        $.ajax({
            type: "POST",
            url: baseURL + "user/appuser/freeze",
            contentType: "application/json",
            data: JSON.stringify(vm.appUser),
            success: function (r) {
                if (r.code == 0) {
                    alert('操作成功', function (index) {
                        $("#jqGrid").trigger("reloadGrid");
                        vm.getInfo(vm.appUser.id);
                    });
                } else {
                    alert(r.msg);
                }
            }
        });
    });
}

// 解冻用户
function unFreeze(id) {
    if (null != id) {
        vm.appUser.id = id;
    }
    confirm('确定要解冻用户？', function () {
        $.ajax({
            type: "POST",
            url: baseURL + "user/appuser/unFreeze",
            contentType: "application/json",
            data: JSON.stringify(vm.appUser),
            success: function (r) {
                if (r.code == 0) {
                    alert('操作成功', function (index) {
                        $("#jqGrid").trigger("reloadGrid");
                        vm.getInfo(vm.appUser.id);
                    });
                } else {
                    alert(r.msg);
                }
            }
        });
    });
}

// 禁言用户
function mute(id) {
    if (null == id) {
        id = vm.appUser.id;
    }
    confirm('确定要禁言用户？', function () {
        $.ajax({
            type: "POST",
            url: baseURL + "user/appuser/mute",
            contentType: "application/json",
            data: JSON.stringify(vm.appUser),
            success: function (r) {
                if (r.code == 0) {
                    alert('操作成功', function (index) {
                        $("#jqGrid").trigger("reloadGrid");
                        vm.getInfo(id);
                    });
                } else {
                    alert(r.msg);
                }
            }
        });
    });
}

// 解除禁言用户
function unMute(id) {
    if (null == id) {
        id = vm.appUser.id;
    }
    confirm('确定要解除禁言用户？', function () {
        $.ajax({
            type: "POST",
            url: baseURL + "user/appuser/unMute",
            contentType: "application/json",
            data: JSON.stringify(vm.appUser),
            success: function (r) {
                if (r.code == 0) {
                    alert('操作成功', function (index) {
                        $("#jqGrid").trigger("reloadGrid");
                        vm.getInfo(id);
                    });
                } else {
                    alert(r.msg);
                }
            }
        });
    });
}

// 用户详情
function detail(id) {
    vm.showList = false;
    vm.showUser = true;
    vm.showAddVirtualUser = false;
    vm.title = "详情";

    vm.getInfo(id);
}

/**
 * 用户头像上传的时候设置值，必传
 * @param returnUrl
 */
function userHeadIconPictureSetValue(returnUrl){
    //用户头像上传的时候设置值
    if (null != vm.appUser){
        vm.appUser.headIconUrl = returnUrl;
    }
}