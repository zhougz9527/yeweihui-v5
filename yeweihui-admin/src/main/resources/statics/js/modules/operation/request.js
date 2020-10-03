$(function () {
	function addCellAttr(rowId, val, rawObject, cm, rdata) {
		if(rawObject.recordStatus == 1 ){
			return "style='color:red'";
		}
	}
    $("#jqGrid").jqGrid({
		url: baseURL + 'operation/request/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true, cellattr: addCellAttr },

			{ label: '更新时间', name: 'updateTime', index: 'update_time', width: 80, hidden:true },
			{ label: '小区id', name: 'zoneId', index: 'zone_id', width: 80, hidden:true },
			{ label: '经办人id', name: 'uid', index: 'uid', width: 80, hidden:true },
			{ label: '已审批过人数', name: 'step', index: 'step', width: 80, hidden:true },
			{ label: '需要审批人数', name: 'stepTotal', index: 'step_total', width: 80, hidden:true },
			{ label: '备注', name: 'notice', index: 'notice', width: 80, hidden:true },
			{ label: '类型', name: 'type', index: 'type', width: 80, formatter:vm.typeFormatter, hidden:true},//类型 1用章

			{ label: '小区名称', name: 'zoneName', index: 'zone_name', width: 80, cellattr: addCellAttr },
			{ label: '经办人', name: 'uname', index: 'uname', width: 80, cellattr: addCellAttr },
			{ label: '使用日期', name: 'useDate', index: 'use_date', width: 80, cellattr: addCellAttr },
			{ label: '文件名', name: 'documentName', index: 'document_name', width: 80, cellattr: addCellAttr },
			{ label: '文件份数', name: 'num', index: 'num', width: 80, cellattr: addCellAttr },
			{ label: '文件类型', name: 'fileType', index: 'file_type', width: 80, formatter:vm.fileTypeFormatter, hidden:true  }, //类别 1合同 2公告 3发函 4其他
			{ label: '印章类型', name: 'seal', index: 'seal', width: 80, formatter:vm.sealFormatter },//印章 1业主大会 2业委会 3财务专用章 4其他
			{ label: '创建时间', name: 'createTime', index: 'create_time', width: 80, cellattr: addCellAttr },
			{ label: '状态', name: 'status', index: 'status', width: 80, formatter:vm.statusFormatter },//状态 0审核中 1审核通过 2撤销 3未通过
			{ label: '记录状态', name: 'recordStatus', index: 'record_status', width: 80, formatter:recordStatusFormatter},//状态 0已删除 1已隐藏 2正常
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
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		progress:false,
		viewMode: false,
		cascaderProps: { multiple: true },
		showList: true,
		title: null,
		request: {
			//抄送成员
			copyToMemberEntityList:[]
		},
		q:{
			keyword:null,
			zoneId:null
		},
		zonesOptions:[],
		//表决人类型
		requestMemberTypeOptions:[
			{
				value:1,
				label:"全体业委会成员"
			}, {
				value:2,
				label:"自定义"
			}
		],
		userList: [],
		requestMemberList: [],
		//状态 0审核中 1审核通过 2撤销 3未通过
		verifyStatusOptions:[
			{
				value:0,
				label:"审核中"
			},
			{
				value:1,
				label:"审核通过"
			},
			{
				value:2,
				label:"撤销"
			},
			{
				value:3,
				label:"未通过"
			}
		],
		//印章 1业主大会 2业委会 3财务专用章 4其他
		sealOptions:[
			{
				value:2,
				label:"业委会公章"
			},
			{
				value:1,
				label:"业主大会公章"
			},
			{
				value:3,
				label:"财务专用章"
			},
			{
				value:4,
				label:"其他"
			}
		],
		//类别 1合同 2公告 3发函 4其他
		fileTypeOptions:[
			{
				value:1,
				label:"合同"
			},
			{
				value:2,
				label:"公告"
			},
			{
				value:3,
				label:"发函"
			},
			{
				value:4,
				label:"其他"
			}
		],
		//类型 1用章
		typeOptions:[
			{
				value:1,
				label:"用章"
			}
		],
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
        //图片
		dialogImageUrl: '',
		dialogVisible: false,
        courierQuery:{
            keyword:null,
            zoneId:null
        }
	},
	computed: {
		requestMemberOptions() {
			const userList = this.userList;
			const roleNameSet = new Set();
			userList.forEach(user => {
				if (user && user.roleName) {
					roleNameSet.add(user.roleName);
				}
			});
			return Array.from(roleNameSet).map( r => {
				return {
					value: r,
					label: r,
					children: userList.filter( u => u.roleName === r).map( u => {
						return {
							value: u.id,
							label: u.realname
						}
					})

				}
			})
		}
	},
	methods: {
		resetRequestMemberList() {
			this.requestMemberList = [];
		},
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.request = {
				status:null,
				seal:null,
				zoneId:null,
				type:null,
				fileType:null,
				useDate:null,
				//资源上传
				fileList: [],
                copyToMemberEntityList:[]
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
			var url = vm.request.id == null ? "operation/request/save" : "operation/request/update";
			if(vm.request.fileList&&vm.request.fileList.length>0){
				for(let i = 0,len = vm.request.fileList.length;i<len;i++){
					if(vm.request.fileList[i].status !== "success"){
						return vm.$confirm("您还有附件正在上传中，请稍后再点击“确定”提交");
					}
				}
			}

			if(vm.progress){
				vm.$confirm("您还有附件正在上传中，请稍后再点击“确定”提交");
			}else {
				$.ajax({
					type: "POST",
					url: baseURL + url,
					contentType: "application/json",
					data: JSON.stringify(vm.request),
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
			}

		},
		del: function (event) {
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "operation/request/delete",
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
                    url: baseURL + "operation/request/hide",
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
                    url: baseURL + "operation/request/recovery",
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
			$.get(baseURL + "operation/request/info/"+id, function(r){
                vm.request = r.request;
				if (vm.request && vm.request.verifyMemberEntityList && vm.request.verifyMemberEntityList.length > 0) {
					if (vm.request.verifyType === 2) {
						vm.requestMemberList = vm.request.verifyMemberEntityList.map(v => {return [v.memberRoleName, v.uid]});
					}
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
		//0审核中 1审核通过 2撤销 3未通过
		statusFormatter: function (cellvalue, options, rowdata) {
			switch (rowdata.status) {
				case 0:
					return '<span class="label label-warning">审核中</span>';
					break;
				case 1:
					return '<span class="label label-success">审核通过</span>';
					break;
				case 2:
					return '<span class="label label-warning">撤销</span>';
					break;
				case 3:
					return '<span class=\"label label-danger\">未通过</span>';
					break;
				default:
					return rowdata.status;
					break;
			}
		},
		//1用章
		typeFormatter: function (cellvalue, options, rowdata) {
			switch (rowdata.type) {
				case 1:
					return '<span class="label label-success">用章</span>';
					break;
				default:
					return rowdata.type;
					break;
			}
		},
		//1合同 2公告 3发函 4其他
		fileTypeFormatter: function (cellvalue, options, rowdata) {
			switch (rowdata.fileType) {
				case 1:
					return '<span class="label label-success">合同</span>';
					break;
				case 2:
					return '<span class="label label-success">公告</span>';
					break;
				case 3:
					return '<span class="label label-success">发函</span>';
					break;
				case 4:
					return '<span class="label label-success">其他</span>';
					break;
				default:
					return rowdata.fileType;
					break;
			}
		},
		//印章 1业主大会 2业委会 3财务专用章 4其他
		sealFormatter: function (cellvalue, options, rowdata) {
			switch (rowdata.seal) {
				case 1:
					return '<span class="label label-success">业主大会</span>';
					break;
				case 2:
					return '<span class="label label-success">业委会</span>';
					break;
				case 3:
					return '<span class="label label-success">财务专用章</span>';
					break;
				case 4:
					return '<span class="label label-success">其他</span>';
					break;
				default:
					return rowdata.seal;
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
			console.log("handleRemove", file, fileList, vm.request.fileList);
			// vm.request.fileList.remove(file);
			const Index = vm.request.fileList.findIndex(item => item.uid == file.uid)
			vm.request.fileList.splice(Index,1)
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
			if(response.code == 0){
				vm.request.fileList = fileList
			}else {
				this.setCurrentImgFial(file.uid,fileList)
				vm.$confirm(`${file.name}，请删除后重新操作`)
			}
		},
		// 设置图片失败状态
		setCurrentImgFial(uid,fileList){
			fileList.forEach(item => {
				if(item.uid == uid){
					item.status = "fail"
					item.name = `${item.name} 上传失败`
				}
			});
			vm.request.fileList = fileList
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
		//按钮格式化
		btnFormatter: function (cellvalue, options, rowdata) {
			var result = "";
			result += '<a class="btn btn-primary btn-sm" target="_blank" onclick="onView(' + rowdata.id + ')">浏览</a>';
			if(rowdata.status == 1){
				result += '&nbsp;&nbsp;<a class="btn btn-primary btn-sm" target="_blank" onclick="viewPdfPrint(' + rowdata.id + ')">pdf预览打印</a>';
			}
			return result;
		},
        //关闭标签3
        handleClose3(tag) {
            for (var i=0;i<vm.request.copyToMemberEntityList.length;i++){
                if (tag === vm.request.copyToMemberEntityList[i].uid) {
                    vm.request.copyToMemberEntityList.splice(i, 1);
                }
            }
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
                courier.realname = rowData.realname;
                courier.mobile = rowData.mobile;
                vm.request.copyToMemberEntityList.push(courier);
            }

            console.log(vm.request.copyToMemberEntityList)
            vm.request.copyToMemberEntityList = unique(vm.request.copyToMemberEntityList);
            console.log(vm.request.copyToMemberEntityList)
            $("#courierModal2").modal('hide');
        },
        //业委会成员分页2
        getCourier2:function () {
            $("#courierModal2").modal('show');


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
        //关闭标签3
        handleClose3(tag) {
            for (var i=0;i<vm.request.copyToMemberEntityList.length;i++){
                if (tag === vm.request.copyToMemberEntityList[i].uid) {
                    vm.request.copyToMemberEntityList.splice(i, 1);
                }
            }
        }
	},
	mounted() {
		getUsers(this, {});
	},
	watch: {
		requestMemberList(val) {
			if (val && val.length > 0) {
				if (this.request.verifyType === 2) {
					this.request.verifyMemberEntityList = val.map(e => {return {uid: e[1]}});
				}
			} else {
				this.request.verifyMemberEntityList = [];
			}
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
		window.open(baseURL + `operation/request/viewPdf/${id}/yes`);
	}).catch((action) => {
		if (action === 'cancel') {
			window.open(baseURL + `operation/request/viewPdf/${id}/no` );
		}
	});
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