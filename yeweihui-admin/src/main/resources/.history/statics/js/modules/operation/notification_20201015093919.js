$(function () {
	function addCellAttr(rowId, val, rawObject, cm, rdata) {
		if(rawObject.recordStatus == 1 ){
			return "style='color:red'";
		}
	}
    $("#jqGrid").jqGrid({
			url: baseURL + 'operation/notice/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true, cellattr: addCellAttr },

			{ label: '更新时间', name: 'updateTime', index: 'update_time', width: 80, hidden:true },
			{ label: '小区id', name: 'zoneId', index: 'zone_id', width: 80, hidden:true },
			{ label: '经办人id', name: 'uid', index: 'uid', width: 80, hidden:true },
			{ label: '小区名称', name: 'zoneName', index: 'zone_name', width: 80, cellattr: addCellAttr, hidden:true },

			{ label: '通知小区', name: 'zoneNameListStr', index: 'zone_name_list_str', width: 80, cellattr: addCellAttr },
			{ label: '标题', name: 'title', index: 'title', width: 80, cellattr: addCellAttr },
			{ label: '发起人', name: 'realname', index: 'realname', width: 80, cellattr: addCellAttr },
			// { label: '通知人员', name: 'userIdList', index: 'user_id_list', width: 80, cellattr: addCellAttr },
			{ label: '阅读次数', name: 'readCount', index: 'read_count', width: 80, cellattr: addCellAttr },
			{ label: '创建时间', name: 'createTime', index: 'create_time', width: 80, cellattr: addCellAttr },
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
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		viewMode: false,
		editor: null,
		showList: true,
		title: null,
        content: '',
		notice: { },
		q:{
			keyword:null,
			zoneId:null
		},
		zonesOptions:[],
		// 所有可选用户
		userList: [],
		// 需要通知的userId
		notifyUidList: [],
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
		cascaderProps: { multiple: true },
		dialogShow: false,
		readerList: [],
	},
    computed: {
		readCnt() {
			if (this.readerList && this.readerList.length > 0) {
				return this.readerList.filter(r => r.status === 2).length;
			}
			return 0;
		},
		unreadCnt() {
			if (this.readerList && this.readerList.length > 0) {
				return this.readerList.filter(r => r.status !== 2).length;
			}
			return 0;
		},
		userListFiltered() {
        	if (this.notice.zoneIdSet && this.notice.zoneIdSet.length > 0) {
        		return this.userList.filter(u => this.notice.zoneIdSet.indexOf(u.zoneId) >= 0 )
			}
        	return this.userList;
		},
		notifyMemberOptions() {
			const userList = this.userListFiltered;
			const roleNameSet = new Set();
			userList.forEach(user => {
				if (user && user.roleName) {
					roleNameSet.add(user.roleName);
				}
			});
			roleNameSet.delete('行业主管');
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
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.notice = {
				title:null,
				noticeMemberEntityList: [],
				//资源上传
				content: null
			};
			vm.editor.txt.clear();
			vm.notifyUidList = [];
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
		upload() {
			$.ajax({
				type: "POST",
				url: baseURL + "sys/oss/upload",
			})
		},
		saveOrUpdate: function (event) {
			var url = vm.notice.id == null ? "operation/notice/save" : "operation/notice/update";
			vm.notice.noticeMemberEntityList = this.notifyUidList.map(u => {
				return { uid: u[1] }
			});
			vm.notice.content = this.editor.txt.html();
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.notice),
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
				    url: baseURL + "operation/notice/delete",
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
        hide: function () {
            var ids = getSelectedRows();
            if(ids == null){
                return ;
            }

            confirm('确定要隐藏选中的记录？', function(){
                $.ajax({
                    type: "POST",
                    url: baseURL + "operation/notice/hide",
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
        recovery: function () {
            var ids = getSelectedRows();
            if(ids == null){
                return ;
            }

            confirm('确定要恢复选中的记录？', function(){
                $.ajax({
                    type: "POST",
                    url: baseURL + "operation/notice/recovery",
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
			$.get(baseURL + "operation/notice/info/"+id, function(r){
                vm.notice = r.notice;
                vm.editor.txt.html(vm.notice.content);
				if (vm.notice && vm.notice.noticeMemberEntityList && vm.notice.noticeMemberEntityList.length > 0) {
					const zoneSet = new Set();
					vm.notice.noticeMemberEntityList.forEach(user => {
						if (user && user.memberZoneId) {
							zoneSet.add(user.memberZoneId);
						}
					});
					vm.notice.zoneIdSet = Array.from(zoneSet);
					vm.notifyUidList = vm.notice.noticeMemberEntityList.map(n => {return [n.memberRoleName, n.uid]});
				}
            });
		},
		getReadMembers: function(id){
			$.ajax({
				type: "get",//方法类型
				dataType: "json",
				url: baseURL + "operation/notice/readMembers",
				contentType: "application/x-www-form-urlencoded; charset=utf-8",
				data: {id},
				success: function (r) {
					vm.dialogShow = true;
					vm.readerList = r.readMembers;
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
		statusFormatter: function (row) {
			if (row && row.status && row.status === 2) {
				return '已读'
			} else {
				return '未读'
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
			result += '<a class="btn btn-primary btn-sm" target="_blank" onclick="onView(' + rowdata.id + ')">浏览</a>';
			result += '&nbsp;&nbsp;<a class="btn btn-primary btn-sm" target="_blank" onclick="vm.getReadMembers(' + rowdata.id + ')">阅读情况</a>';
			return result;
		},
		initEditor() {
			const E = window.wangEditor;
			this.editor = new E('#editor');
			this.editor.customConfig.uploadImgServer = baseURL + "sys/oss/upload";
			this.editor.customConfig.uploadImgMaxSize = 3 * 1024 * 1024;
			this.editor.customConfig.uploadImgMaxLength = 9;
			this.editor.customConfig.uploadFileName = 'file';
			this.editor.customConfig.zIndex = 1;
			this.editor.customConfig.uploadImgHooks = {
				error: function (xhr, editor) {
					alert('图片上传失败！');
				},
				timeout: function (xhr, editor) {
					alert('图片上传超时！');
				},
				customInsert: function (insertImg, result, editor) {
					console.log(result);
					if (result.code === 0) {
						insertImg(result.url);
					}
				}
			};
			this.editor.create();
		}
	},
    mounted() {
		getUsers(this, {});
		this.initEditor();
    },
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