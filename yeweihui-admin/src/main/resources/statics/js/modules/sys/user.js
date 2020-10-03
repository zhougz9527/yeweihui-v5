$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'sys/user/list',
        datatype: "json",
        colModel: [			
			// { label: '用户ID', name: 'userId', index: "user_id", width: 45, key: true },
            { label: '用户ID', name: 'id', index: 'id', width: 50, key: true },
            { label: '创建时间', name: 'createTime', index: 'create_time', width: 80, hidden:true },
            { label: '更新时间', name: 'updateTime', index: 'update_time', width: 80, hidden:true },
            { label: '小区id', name: 'zoneId', index: 'zone_id', width: 80, hidden:true },
            { label: '小区名称', name: 'zoneName', index: 'zone_name', width: 80 },
            { label: '角色id', name: 'roleId', index: 'role_id', width: 80, hidden:true },
            { label: '角色code', name: 'roleCode', index: 'role_code', width: 80, hidden:true },
            { label: '角色名称', name: 'roleName', index: 'role_name', width: 80, hidden:true },
            { label: 'openid', name: 'openid', index: 'openid', width: 80, hidden:true },
            { label: '上线uid', name: 'pid', index: 'pid', width: 80, hidden:true },
            { label: '昵称', name: 'nickname', index: 'nickname', width: 80, hidden:true },
            { label: '真实姓名', name: 'realname', index: 'realname', width: 80 },
            { label: '性别', name: 'gender', index: 'gender', width: 80, formatter:vm.genderFormatter },//1男 2女
            { label: '市', name: 'city', index: 'city', width: 80, hidden:true },
            { label: '省', name: 'province', index: 'province', width: 80, hidden:true },
            { label: '国家', name: 'country', index: 'country', width: 80, hidden:true },
            { label: '积分', name: 'integral', index: 'integral', width: 80, hidden:true },
            { label: '头像', name: 'avatarUrl', index: 'avatar_url', width: 80, formatter: vm.avatarUrlFormatter },
            { label: '上次登录', name: 'lastLogin', index: 'last_login', width: 80, hidden:true },
            { label: '用户名', name: 'username', index: 'username', width: 80 },
            { label: '密码', name: 'password', index: 'password', width: 80, hidden:true },
            { label: '手机', name: 'mobile', index: 'mobile', width: 80 },
            { label: '状态', name: 'status', index: 'status', width: 80, formatter:vm.statusFormatter },// 0禁用 1正常
            { label: '部门ID', name: 'deptId', index: 'dept_id', width: 80, hidden:true },
            { label: '部门名称', name: 'deptName', index: 'dept_name', width: 80, hidden:true },
            { label: '盐', name: 'salt', index: 'salt', width: 80, hidden:true },
            { label: '邮箱', name: 'email', index: 'email', width: 80, hidden:true },
            { label: '角色', name: 'roleNameList', index: 'role_name_list', width: 80 },
            { label: '角色id', name: 'roleId', index: 'role_id', width: 80, hidden:true },
            { label: '角色名称', name: 'roleName', index: 'role_name', width: 80, hidden:true },
            { label: '审核状态', name: 'verifyStatus', index: 'verify_status', width: 80, formatter:vm.verifyStatusFormatter },// 审核状态 0审核中 1审核通过 2撤销 3未通过
            { label: '操作', name: 'ope', index: 'ope', width: 100, formatter: vm.btnFormatter }
        ],
		viewrecords: true,
        height: 630,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader : {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames : {
            page:"page", 
            rows:"limit", 
            order: "order"
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	//$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
        }
    });

    //获取小区列表
    vm.getZonesList();
});
var setting = {
    data: {
        simpleData: {
            enable: true,
            idKey: "deptId",
            pIdKey: "parentId",
            rootPId: -1
        },
        key: {
            url:"nourl"
        }
    }
};
var ztree;

var vm = new Vue({
    el:'#rrapp',
    data:{
        q:{
            keyword: null,
            zoneId:null
        },
        showList: true,
        title:null,
        roleList:{},
        user:{
            status:1,
            deptId:null,
            deptName:null,
            roleIdList:[]
        },
        //性别
        genderList:[
            {
                id:1,
                gender:'男'
            },
            {
                id:2,
                gender:'女'
            },
            {
                id:0,
                gender:'未知'
            }
        ],
        zonesOptions:[],
    },
    methods: {
        query: function () {
            vm.reload();
        },
        add: function(){
            vm.showList = false;
            vm.title = "新增";
            vm.roleList = {};
            vm.user = {deptName:null, deptId:null, status:1, roleIdList:[], zoneId:null, gender:null};

            //获取角色信息
            this.getRoleList();

            vm.getDept();
        },
        getDept: function(){
            //加载部门树
            $.get(baseURL + "sys/dept/list", function(r){
                console.log(r);
                ztree = $.fn.zTree.init($("#deptTree"), setting, r);
                var node = ztree.getNodeByParam("deptId", vm.user.deptId);
                if(node != null){
                    ztree.selectNode(node);

                    vm.user.deptName = node.name;
                }
            })
        },
        update: function () {
            var id = getSelectedRow();
            if(id == null){
                return ;
            }

            vm.showList = false;
            vm.title = "修改";

            vm.getUser(id);
            //获取角色信息
            this.getRoleList();
        },
        del: function () {
            var userIds = getSelectedRows();
            if(userIds == null){
                return ;
            }

            confirm('确定要删除选中的记录？', function(){
                $.ajax({
                    type: "POST",
                    url: baseURL + "sys/user/delete",
                    contentType: "application/json",
                    data: JSON.stringify(userIds),
                    success: function(r){
                        if(r.code == 0){
                            alert('操作成功', function(){
                                vm.reload();
                            });
                        }else{
                            alert(r.msg);
                        }
                    }
                });
            });
        },
        saveOrUpdate: function () {
            var url = vm.user.id == null ? "sys/user/save" : "sys/user/update";
            console.log(vm.user);
            $.ajax({
                type: "POST",
                url: baseURL + url,
                contentType: "application/json",
                data: JSON.stringify(vm.user),
                success: function(r){
                    if(r.code === 0){
                        alert('操作成功', function(){
                            vm.reload();
                        });
                    }else{
                        alert(r.msg);
                    }
                }
            });
        },
        getUser: function(userId){
            $.get(baseURL + "sys/user/info/"+userId, function(r){
                vm.user = r.user;
                vm.user.password = null;

                vm.getDept();
            });
        },
        getRoleList: function(){
            $.get(baseURL + "sys/role/select", function(r){
                vm.roleList = r.list;
            });
        },
        deptTree: function(){
            layer.open({
                type: 1,
                offset: '50px',
                skin: 'layui-layer-molv',
                title: "选择部门",
                area: ['300px', '450px'],
                shade: 0,
                shadeClose: false,
                content: jQuery("#deptLayer"),
                btn: ['确定', '取消'],
                btn1: function (index) {
                    var node = ztree.getSelectedNodes();
                    //选择上级部门
                    vm.user.deptId = node[0].deptId;
                    vm.user.deptName = node[0].name;

                    layer.close(index);
                }
            });
        },
        reload: function () {
            vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam','page');
            $("#jqGrid").jqGrid('setGridParam',{
                postData:{
                    'keyword': vm.q.keyword,
                    'zoneId': vm.q.zoneId
                },
                page:page
            }).trigger("reloadGrid");
        },
        // 1男 2女
        genderFormatter: function (cellvalue, options, rowdata) {
            switch (rowdata.gender) {
                case 1:
                    return "<span class=\"label label-danger\">男</span>";
                    break;
                case 2:
                    return "<span class=\"label label-success\">女</span>";
                    break;
                case 0:
                    return "<span class=\"label label-warning\">未知</span>";
                    break;
                default:
                    return rowdata.gender;
                    break;
            }
        },
        // 审核状态 0审核中 1审核通过 2撤销 3未通过
        verifyStatusFormatter: function (cellvalue, options, rowdata) {
            switch (rowdata.verifyStatus) {
                case 0:
                    return "<span class=\"label label-warning\">审核中</span>";
                    break;
                case 1:
                    return "<span class=\"label label-success\">审核通过</span>";
                    break;
                case 2:
                    return "<span class=\"label label-warning\">撤销</span>";
                    break;
                case 3:
                    return "<span class=\"label label-danger\">未通过</span>";
                    break;
                default:
                    return rowdata.verifyStatus;
                    break;
            }
        },
        // 0禁用 1正常
        statusFormatter: function (cellvalue, options, rowdata) {
            switch (rowdata.status) {
                case 0:
                    return '<span class="label label-danger">禁用</span>';
                    break;
                case 1:
                    return '<span class=\"label label-success\">正常</span>';
                    break;
                default:
                    return rowdata.status;
                    break;
            }
        },
        // 头像formatter
        avatarUrlFormatter: function (cellvalue, options, rowdata) {
            return '<img src="' + rowdata.avatarUrl + '" id="img' + rowdata.id + '" style="width:30px;height:30px;" />';
        },
        //获取小区列表
        getZonesList: function () {
            //获取下拉框数据
            $.ajax({
                type: "get",//方法类型
                dataType: "json",
                url: baseURL + "user/zones/simpleList",
                contentType: "application/x-www-form-urlencoded; charset=utf-8",
                data: {

                },
                success: function (result) {
                    if (result.code == 0) {
                        vm.zonesOptions = result.zonesEntityList;
                        console.log(vm.zonesOptions);
                    }
                },
                error: function () {
                }
            });
        },
        //操作按钮格式化
        btnFormatter: function (cellvalue, options, rowdata) {
            var result = "";
            //审核状态 0审核中 1审核通过 2撤销 3未通过
            if (rowdata.verifyStatus == 0){
                result += '&nbsp;<a class="btn btn-primary btn-sm" onclick="verifyUser('+ rowdata.id +', 1)">通过</a>';
                result += '&nbsp;<a class="btn btn-primary btn-sm" onclick="verifyUser('+ rowdata.id +', 3)">拒绝</a>';
            }

            return result;
        }


    }
});

//审核  审核状态 0审核中 1审核通过 2撤销 3未通过
function verifyUser(id, verifyStatus) {
    $.ajax({
        type: "post",//方法类型
        dataType: "json",
        url: baseURL + "/user/user/verifyUser",
        // contentType: "application/json; charset=utf-8",
        contentType: "application/x-www-form-urlencoded; charset=utf-8",
        // data: JSON.stringify({parentId:0}),
        data: {
            userId:id,
            verifyStatus:verifyStatus
        },
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
}