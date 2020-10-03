$(function () {
    function addCellAttr(rowId, val, rawObject, cm, rdata) {
        if(rawObject.recordStatus == 1 ){
            return "style='color:red'";
        }
    }
    $("#jqGrid").jqGrid({
        url: baseURL + 'operation/meeting/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true, cellattr: addCellAttr },
			{ label: '创建时间', name: 'createTime', index: 'create_time', width: 80, hidden:true },
			{ label: '更新时间', name: 'updateTime', index: 'update_time', width: 80, hidden:true },
            { label: '发起人id', name: 'uid', index: 'uid', width: 80, hidden : true },
			{ label: '小区id', name: 'zoneId', index: 'zone_id', width: 80, hidden:true },

            { label: '小区名称', name: 'zoneName', index: 'zone_name', width: 80, cellattr: addCellAttr },
            { label: '发起人', name: 'realname', index: 'realname', width: 80, cellattr: addCellAttr },
            { label: '会议主题', name: 'title', index: 'title', width: 80, cellattr: addCellAttr },
            { label: '参加人数', name: 'num', index: 'num', width: 80, cellattr: addCellAttr },
            { label: '签到人数', name: 'signNum', index: 'sign_num', width: 80, cellattr: addCellAttr },
            { label: '会议地点', name: 'location', index: 'location', width: 80, cellattr: addCellAttr },
            { label: '开始时间', name: 'startAt', index: 'start_at', width: 80, cellattr: addCellAttr },
            { label: '估计时长', name: 'spendTime', index: 'spend_time', width: 80, cellattr: addCellAttr },
            { label: '内容', name: 'content', index: 'content', width: 80, cellattr: addCellAttr },
            { label: '状态', name: 'status', index: 'status', width: 80, formatter:vm.statusFormatter },//状态 0待签到 1进行中 2待签字 3结束 4取消
            { label: '记录状态', name: 'recordStatus', index: 'record_status', width: 80, formatter:recordStatusFormatter},//状态 0已删除 1已隐藏 2正常
            { label: '操作', name: 'ope', index: 'ope', width: 130, formatter: vm.btnFormatter }
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
        	// $("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
        }
    });

    //获取小区列表
    vm.getZonesList();
});

var vm = new Vue({
	el:'#rrapp',
	data:{
	    progress:false,
	    viewMode: false,
		showList: true,
		title: null,
		meeting: {
	        type: 0,
			//会议参与成员
			meetingMemberEntityList:[],
			//会议抄送成员
			copy2MeetingMemberEntityList:[]
		},
        zonesOptions:[],
        //状态 0待签到 1进行中 2待签字 3结束 4取消
        statusOptions:[
            {
                value:0,
                label:"待签到"
            }, {
                value:1,
                label:"进行中"
            }, {
                value:2,
                label:"待签字"
            }, {
                value:3,
                label:"结束"
            }, {
                value:4,
                label:"取消"
            }
        ],
        //日期时间
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
		//会议日志记录
		meetingLog: {
			content: null,
			//资源上传
			fileList: [],
			//会议id
			mid:null
		},
        meetingOptions: [
            {
                "value": 0,
                "label": "预约开会"
            },
            {
                "value": 1,
                "label": "现场开会"
            },
        ],
		//图片
		dialogImageUrl: '',
		dialogVisible: false,
        courierQuery:{
            keyword:null,
            zoneId:null
        },
        q:{
            keyword:null,
            zoneId:null
        },
        zonesOptions:[],
        memberList: [],
        dialogShow: false
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.meeting = {
                startAt:null,
                zoneId:null,
                status:null,
                type: 0,
                meetingMemberEntityList:[],
                copy2MeetingMemberEntityList:[]
            };
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
            if (vm.meeting.meetingMemberEntityList.length == 0){
            	alert("请选择参会人员");
            	return;
			}
            // vm.meeting.copy2MeetingMemberEntityList = vm.meeting.copy2MeetingMemberEntityList;

			var url = vm.meeting.id == null ? "operation/meeting/save" : "operation/meeting/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.meeting),
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
				    url: baseURL + "operation/meeting/delete",
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
        hide: function (event) {
            var ids = getSelectedRows();
            if(ids == null){
                return ;
            }

            confirm('确定要隐藏选中的记录？', function(){
                $.ajax({
                    type: "POST",
                    url: baseURL + "operation/meeting/hide",
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
        recovery: function (event) {
            var ids = getSelectedRows();
            if(ids == null){
                return ;
            }

            confirm('确定要恢复选中的记录？', function(){
                $.ajax({
                    type: "POST",
                    url: baseURL + "operation/meeting/recovery",
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
			$.get(baseURL + "operation/meeting/info/"+id, function(r){
                vm.meeting = r.meeting;
            });
		},
        checkMemberStatus(id) {
            $.get(baseURL + "operation/meeting/info/"+id, function(r){
                vm.meeting = r.meeting;
                if (r.meeting && r.meeting.meetingMemberEntityList) {
                    vm.memberList = r.meeting.meetingMemberEntityList;
                    vm.dialogShow = true;
                } else {
                    vm.$message.warning("未查询到信息！");
                }
            });
        },
		reload: function (event) {
		    vm.viewMode = false;
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
		//状态 0待签到 1进行中 2待签字 3结束 4取消
		statusFormatter: function (cellvalue, options, rowdata) {
			switch (rowdata.status) {
				case 0:
					return '<span class="label label-warning">待签到</span>';
					break;
				case 1:
					return '<span class="label label-success">进行中</span>';
					break;
				case 2:
					return '<span class="label label-warning">待签字</span>';
					break;
				case 3:
					return '<span class="label label-danger">结束</span>';
					break;
				case 4:
					return '<span class="label label-default">取消</span>';
					break;
				default:
					return rowdata.status;
					break;
			}
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
                        console.log(result.zonesEntityList);
                        vm.zonesOptions = result.zonesEntityList;
                    }
                },
                error: function () {
                }
            });
        },
		//按钮格式化
		btnFormatter: function (cellvalue, options, rowdata) {
			var result = "";
            result += '<a class="btn btn-primary btn-sm" target="_blank" onclick="onView(' + rowdata.id + ')">浏览</a>&nbsp;&nbsp;';
			//状态 0待签到 1进行中 2待签字 3结束 4取消
			if(rowdata.status == 3){
				result += '<a class="btn btn-primary btn-sm" target="_blank" onclick="viewPdfPrint(' + rowdata.id + ')">pdf预览打印</a>&nbsp;&nbsp;';
			}else if(rowdata.status == 1){
				result += '<a class="btn btn-primary btn-sm" target="_blank" onclick="showMeetingLogModal(' + rowdata.id + ')">录入会议纪要</a>&nbsp;&nbsp;';
			}
			result += '<a class="btn btn-primary btn-sm" target="_blank" onclick="vm.checkMemberStatus(' + rowdata.id + ')">签到人数</a>&nbsp;&nbsp;';
			return result;
		},
        handlePictureCardPreview(file) {
            const suffix = file.url.split(".")[file.url.split(".").length - 1];
            if (suffix.indexOf('doc') >= 0 || suffix.indexOf('xls') >= 0 || suffix.indexOf('ppt') >= 0) {
                window.open(officeViewUrl+file.url);
            } else if (suffix.indexOf('pdf') >= 0) {
                window.open(file.url);
            } else {
                this.dialogImageUrl = file.url;
                this.dialogVisible = true;
            }
        },
		//上传图片
		//移除图片
		handleRemove(file, fileList) {
			console.log("handleRemove", file, fileList, vm.meetingLog.fileList);
			vm.meetingLog.fileList.remove(file);
		},
		handlePreview(file) {
			console.log("handlePreview", file);
		},
        handleProgress(){
            vm.progress = true
        },
		//图片上传成功
		handleSuccess(response, file, fileList){
		    vm.progress = false
			console.log("handleSuccess", response, file, fileList, vm.meetingLog.fileList);
			vm.meetingLog.fileList = fileList;
			console.log(vm.meetingLog.fileList)
		},
        handleBeforeUpload(file) {
            var isAllowedImg = file.type === 'image/jpeg' || file.type === 'image/png' || file.type === 'image/gif' ||
                file.type.indexOf('application/vnd') >= 0 || file.type.indexOf('application/ms') >= 0 ||
                file.type.indexOf('application/pdf') >= 0;
            var isLt2M = file.size / 1024 / 1024 < 3;

            if (!isAllowedImg) {
                this.$message.error('上传图片只能是word,ppt,excel,pdf,图片格式!');
            }
            if (!isLt2M) {
                this.$message.error('上传图片大小不能超过 3MB!');
            }
            return isAllowedImg && isLt2M;
        },
		//关闭查看大图
		closeDialog(){
			this.dialogVisible = false;
		},
		//保存会议记录
		saveMeetingLog: function (event) {
			console.log(vm.meetingLog);

			var url = "operation/meetinglog/save";
			if(vm.progress){
                vm.$confirm("您还有附件正在上传中，请稍后再点击“确定”提交");
            }else {
                $.ajax({
                    type: "POST",
                    url: baseURL + url,
                    contentType: "application/json",
                    data: JSON.stringify(vm.meetingLog),
                    success: function(r){
                        if(r.code === 0){
                            alert('操作成功', function(index){
                                // vm.reload();
                                $('#myModal').modal("hide");
                            });
                        }else{
                            alert(r.msg);
                        }
                    }
                });
            }
		},
		//关闭标签
		handleClose2(tag) {
			for (var i=0;i<vm.meeting.meetingMemberEntityList.length;i++){
				if (tag === vm.meeting.meetingMemberEntityList[i].uid) {
					vm.meeting.meetingMemberEntityList.splice(i, 1);
				}
			}
		},
		//关闭标签3
		handleClose3(tag) {
			for (var i=0;i<vm.meeting.copy2MeetingMemberEntityList.length;i++){
				if (tag === vm.meeting.copy2MeetingMemberEntityList[i].uid) {
					vm.meeting.copy2MeetingMemberEntityList.splice(i, 1);
				}
			}
		},
        //重新加载快递员grid
        reloadCourierGrid: function () {
            var page = $("#courierModalGrid").jqGrid('getGridParam','page');
            $("#courierModalGrid").jqGrid('setGridParam',{
                postData:{
                    'keyword': vm.courierQuery.keyword,
                    'zoneId': vm.courierQuery.zoneId,
                },
                page:page
            }).trigger("reloadGrid");
        },
        //重新加载快递员grid2
        reloadCourierGrid2: function () {
            var page = $("#courierModalGrid").jqGrid('getGridParam','page');
            $("#courierModalGrid2").jqGrid('setGridParam',{
                postData:{
                    'keyword': vm.courierQuery.keyword,
                    'zoneId': vm.courierQuery.zoneId,
                },
                page:page
            }).trigger("reloadGrid");
        },
        selectcCourier:function () {
            let rowIds = $('#courierModalGrid').jqGrid('getGridParam','selarrrow');

            for (let i = 0; i < rowIds.length; i++) {
                let rowData = $("#courierModalGrid").jqGrid('getRowData',rowIds[i]);
                let courier = {
                    memberRealname:null,
                    uid:null
                };
                courier.uid = Number(rowData.id);
                courier.memberRealname = rowData.realname;
                courier.mobile = rowData.mobile;
                vm.meeting.meetingMemberEntityList.push(courier);
            }

            console.log(vm.meeting.meetingMemberEntityList)
            vm.meeting.meetingMemberEntityList = unique(vm.meeting.meetingMemberEntityList);
            console.log(vm.meeting.meetingMemberEntityList)
            $("#courierModal").modal('hide');
        },
        selectcCourier2:function () {
            let rowIds = $('#courierModalGrid2').jqGrid('getGridParam','selarrrow');

            for (let i = 0; i < rowIds.length; i++) {
                let rowData = $("#courierModalGrid2").jqGrid('getRowData',rowIds[i]);
                let courier = {
                    memberRealname:null,
                    uid:null
                };
                courier.uid = rowData.id;
                courier.memberRealname = rowData.realname;
                courier.mobile = rowData.mobile;
                vm.meeting.copy2MeetingMemberEntityList.push(courier);
            }

            console.log(vm.meeting.copy2MeetingMemberEntityList)
            vm.meeting.copy2MeetingMemberEntityList = unique(vm.meeting.copy2MeetingMemberEntityList);
            console.log(vm.meeting.copy2MeetingMemberEntityList)
            $("#courierModal2").modal('hide');
        },
        //业委会成员分页
        getCourier:function () {
            $("#courierModal").modal('show');

            $("#courierModalGrid").jqGrid({
                url: baseURL + 'sys/user/list',
                datatype: "json",
                colModel: [
                    // { label: '用户ID', name: 'userId', index: "user_id", width: 45, key: true },
                    { label: '用户ID', name: 'id', index: 'id', width: 50, key: true },
                    { label: '小区名称', name: 'zoneName', index: 'zone_name', width: 80 },
                    { label: '真实姓名', name: 'realname', index: 'realname', width: 80 },
                    { label: '角色', name: 'roleNameList', index: 'role_name_list', width: 80 },
                    { label: '状态', name: 'status', index: 'status', width: 80, formatter:vm.status1Formatter }// 0禁用 1正常
                    /*{ label: '创建时间', name: 'createTime', index: 'create_time', width: 80, hidden:true },
                    { label: '更新时间', name: 'updateTime', index: 'update_time', width: 80, hidden:true },
                    { label: '小区id', name: 'zoneId', index: 'zone_id', width: 80, hidden:true },
                    { label: '角色id', name: 'roleId', index: 'role_id', width: 80, hidden:true },
                    { label: '角色code', name: 'roleCode', index: 'role_code', width: 80, hidden:true },
                    { label: '角色名称', name: 'roleName', index: 'role_name', width: 80, hidden:true },
                    { label: 'openid', name: 'openid', index: 'openid', width: 80, hidden:true },
                    { label: '上线uid', name: 'pid', index: 'pid', width: 80, hidden:true },
                    { label: '昵称', name: 'nickname', index: 'nickname', width: 80, hidden:true },
                    { label: '性别', name: 'gender', index: 'gender', width: 80, formatter:vm.genderFormatter },//1男 2女
                    { label: '市', name: 'city', index: 'city', width: 80, hidden:true },
                    { label: '省', name: 'province', index: 'province', width: 80, hidden:true },
                    { label: '国家', name: 'country', index: 'country', width: 80, hidden:true },
                    { label: '积分', name: 'integral', index: 'integral', width: 80, hidden:true },
                    { label: '头像', name: 'avatarUrl', index: 'avatar_url', width: 80, formatter: vm.avatar1UrlFormatter },
                    { label: '上次登录', name: 'lastLogin', index: 'last_login', width: 80, hidden:true },
                    { label: '用户名', name: 'username', index: 'username', width: 80 },
                    { label: '密码', name: 'password', index: 'password', width: 80, hidden:true },
                    { label: '手机', name: 'mobile', index: 'mobile', width: 80 },
                    { label: '部门ID', name: 'deptId', index: 'dept_id', width: 80, hidden:true },
                    { label: '部门名称', name: 'deptName', index: 'dept_name', width: 80, hidden:true },
                    { label: '盐', name: 'salt', index: 'salt', width: 80, hidden:true },
                    { label: '邮箱', name: 'email', index: 'email', width: 80, hidden:true },
                    { label: '审核状态', name: 'verifyStatus', index: 'verify_status', width: 80, formatter:vm.verifyStatus1Formatter },// 审核状态 0审核中 1审核通过 2撤销 3未通过
                    { label: '操作', name: 'ope', index: 'ope', width: 100, formatter: vm.btnFormatter }*/
                ],
                viewrecords: true,
                width: 640,
                height: 390,
                rowNum: 10,
                rowList : [10,30,50],
                rownumbers: true,
                rownumWidth: 25,
                autowidth:false,/*change*/
                multiselect: true,
                pager: "#courierModalPager",
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
                    $("#courierModalGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
                }
            });
        },
        //业委会成员分页2
        getCourier2:function () {
            $("#courierModal2").modal('show');

            $("#courierModalGrid2").jqGrid({
                url: baseURL + 'sys/user/list',
                datatype: "json",
                colModel: [
                    // { label: '用户ID', name: 'userId', index: "user_id", width: 45, key: true },
                    { label: '用户ID', name: 'id', index: 'id', width: 50, key: true },
                    { label: '小区名称', name: 'zoneName', index: 'zone_name', width: 80 },
                    { label: '真实姓名', name: 'realname', index: 'realname', width: 80 },
                    { label: '角色', name: 'roleNameList', index: 'role_name_list', width: 80 },
                    { label: '状态', name: 'status', index: 'status', width: 80, formatter:vm.status1Formatter }// 0禁用 1正常
                    /*{ label: '创建时间', name: 'createTime', index: 'create_time', width: 80, hidden:true },
                    { label: '更新时间', name: 'updateTime', index: 'update_time', width: 80, hidden:true },
                    { label: '小区id', name: 'zoneId', index: 'zone_id', width: 80, hidden:true },
                    { label: '角色id', name: 'roleId', index: 'role_id', width: 80, hidden:true },
                    { label: '角色code', name: 'roleCode', index: 'role_code', width: 80, hidden:true },
                    { label: '角色名称', name: 'roleName', index: 'role_name', width: 80, hidden:true },
                    { label: 'openid', name: 'openid', index: 'openid', width: 80, hidden:true },
                    { label: '上线uid', name: 'pid', index: 'pid', width: 80, hidden:true },
                    { label: '昵称', name: 'nickname', index: 'nickname', width: 80, hidden:true },
                    { label: '性别', name: 'gender', index: 'gender', width: 80, formatter:vm.genderFormatter },//1男 2女
                    { label: '市', name: 'city', index: 'city', width: 80, hidden:true },
                    { label: '省', name: 'province', index: 'province', width: 80, hidden:true },
                    { label: '国家', name: 'country', index: 'country', width: 80, hidden:true },
                    { label: '积分', name: 'integral', index: 'integral', width: 80, hidden:true },
                    { label: '头像', name: 'avatarUrl', index: 'avatar_url', width: 80, formatter: vm.avatar1UrlFormatter },
                    { label: '上次登录', name: 'lastLogin', index: 'last_login', width: 80, hidden:true },
                    { label: '用户名', name: 'username', index: 'username', width: 80 },
                    { label: '密码', name: 'password', index: 'password', width: 80, hidden:true },
                    { label: '手机', name: 'mobile', index: 'mobile', width: 80 },
                    { label: '部门ID', name: 'deptId', index: 'dept_id', width: 80, hidden:true },
                    { label: '部门名称', name: 'deptName', index: 'dept_name', width: 80, hidden:true },
                    { label: '盐', name: 'salt', index: 'salt', width: 80, hidden:true },
                    { label: '邮箱', name: 'email', index: 'email', width: 80, hidden:true },
                    { label: '审核状态', name: 'verifyStatus', index: 'verify_status', width: 80, formatter:vm.verifyStatus1Formatter },// 审核状态 0审核中 1审核通过 2撤销 3未通过
                    { label: '操作', name: 'ope', index: 'ope', width: 100, formatter: vm.btnFormatter }*/
                ],
                viewrecords: true,
                width: 640,
                height: 390,
                rowNum: 10,
                rowList : [10,30,50],
                rownumbers: true,
                rownumWidth: 25,
                autowidth:false,/*change*/
                multiselect: true,
                pager: "#courierModalPager2",
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
                    $("#courierModalGrid2").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
                }
            });
        },
        //快递员头像
        headIconUrlFormatter: function (cellvalue, options, rowdata) {
            return '<img src="' + rowdata.headIconUrl + '" id="img' + rowdata.id + '" style="width:30px;height:30px;" />';
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
        verifyStatus1Formatter: function (cellvalue, options, rowdata) {
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
                    return rowdata.gender;
                    break;
            }
        },
        // 0禁用 1正常
        status1Formatter: function (cellvalue, options, rowdata) {
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
        avatarUrl1Formatter: function (cellvalue, options, rowdata) {
            return '<img src="' + rowdata.avatarUrl + '" id="img' + rowdata.id + '" style="width:30px;height:30px;" />';
        }


	}
});

function onView(id) {
    if(id == null){
        vm.$message.warning('没有找到数据!');
        return;
    }
    vm.viewMode = true;
    vm.showList = false;
    vm.title = "浏览";
    vm.getInfo(id)
}

//打印pdf
function viewPdfPrint(id) {
    vm.$confirm('是否需要附件?', '提示', {
        confirmButtonText: '是',
        cancelButtonText: '不需要',
        distinguishCancelAndClose: true,
        type: 'info'
    }).then(() => {
        window.open(baseURL + `operation/meeting/viewPdf/${id}/yes`);
    }).catch((action) => {
        if (action === 'cancel') {
            window.open(baseURL + `operation/meeting/viewPdf/${id}/no` );
        }
    });
}

//录入会议纪要 modal弹出，保存，结束，有图片
function showMeetingLogModal(id){

	vm.meetingLog = {
		content: null,
			//资源上传
			fileList: [],
			//会议id
			mid:null
	};

	//会议id
	console.log(id);
	vm.meetingLog.mid = id;
	$('#myModal').modal("show");
}

//快递员数组去重
function unique(arr){
    var hash=[];
    for (var i = 0; i < arr.length; i++) {
        for (var j = i+1; j < arr.length; j++) {
            if(arr[i].uid===arr[j].uid){
                ++i;
            }
        }
        hash.push(arr[i]);
    }
    return hash;
}