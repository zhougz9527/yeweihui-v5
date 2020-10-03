$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'user/user/list',
        datatype: "json",
        colModel: [
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true },
			{ label: '创建时间', name: 'createTime', index: 'create_time', width: 80, hidden:true },
			{ label: '更新时间', name: 'updateTime', index: 'update_time', width: 80, hidden:true },
			{ label: '小区id', name: 'zoneId', index: 'zone_id', width: 80 },
			{ label: '小区名称', name: 'zoneName', index: 'zone_name', width: 80 },
			{ label: '角色id', name: 'roleId', index: 'role_id', width: 80 },
			{ label: '角色code', name: 'roleCode', index: 'role_code', width: 80 },
			{ label: '角色名称', name: 'roleName', index: 'role_name', width: 80 },
			{ label: 'openid', name: 'openid', index: 'openid', width: 80, hidden:true },
			{ label: '上线uid', name: 'pid', index: 'pid', width: 80, hidden:true },
			{ label: '昵称', name: 'nickname', index: 'nickname', width: 80 },
			{ label: '真实姓名', name: 'realname', index: 'realname', width: 80 },
			{ label: '性别', name: 'gender', index: 'gender', width: 80, formatter:vm.genderFormatter },//1男 2女
			{ label: '市', name: 'city', index: 'city', width: 80 },
			{ label: '省', name: 'province', index: 'province', width: 80 },
			{ label: '国家', name: 'country', index: 'country', width: 80 },
			{ label: '积分', name: 'integral', index: 'integral', width: 80 },
			{ label: '头像', name: 'avatarUrl', index: 'avatar_url', width: 80, formatter: vm.avatarUrlFormatter },
			{ label: '上次登录', name: 'lastLogin', index: 'last_login', width: 80 },
			{ label: '用户名', name: 'username', index: 'username', width: 80 },
			{ label: '密码', name: 'password', index: 'password', width: 80 },
			{ label: '手机', name: 'mobile', index: 'mobile', width: 80 },
			{ label: '状态', name: 'status', index: 'status', width: 80, formatter:vm.statusFormatter },// 0禁用 1正常
			{ label: '部门ID', name: 'deptId', index: 'dept_id', width: 80 },
			{ label: '部门名称', name: 'deptName', index: 'dept_name', width: 80 },
			{ label: '盐', name: 'salt', index: 'salt', width: 80, hidden:true },
			{ label: '邮箱', name: 'email', index: 'email', width: 80, hidden:true }
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
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		user: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.user = {};
		},
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(id)
		},
		saveOrUpdate: function (event) {
			var url = vm.user.id == null ? "user/user/save" : "user/user/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.user),
			    success: function(r){
			    	if(r.code === 0){
						alert('操作成功', function(index){
							vm.reload();
						});
					}else{
						alert(r.msg);
					}
				}
			});
		},
		del: function (event) {
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "user/user/delete",
                    contentType: "application/json",
				    data: JSON.stringify(ids),
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
		},
		getInfo: function(id){
			$.get(baseURL + "user/user/info/"+id, function(r){
                vm.user = r.user;
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                page:page
            }).trigger("reloadGrid");
		},
		// 1男 2女
		genderFormatter: function (cellvalue, options, rowdata) {
			switch (rowdata.gender) {
				case 1:
					return "男";
					break;
				case 2:
					return "女";
					break;
				default:
					return rowdata.gender;
					break;
			}
		},
		// 0禁用 1正常
		statusFormatter: function (cellvalue, options, rowdata) {
			switch (rowdata.status) {
				case 0:
					return "禁用";
					break;
				case 1:
					return "正常";
					break;
				default:
					return rowdata.status;
					break;
			}
		},
		// 头像formatter
		avatarUrlFormatter: function (cellvalue, options, rowdata) {
			return '<img src="' + rowdata.avatarUrl + '" id="img' + rowdata.id + '" style="width:30px;height:30px;" />';
		}

	}
});