$(function () {
	function addCellAttr(rowId, val, rawObject, cm, rdata) {
		if(rawObject.recordStatus == 1 ){
			return "style='color:red'";
		}
	}
    $("#jqGrid").jqGrid({
			url: baseURL + 'operation/hisviewlog/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true, cellattr: addCellAttr },

			{ label: '小区id', name: 'zoneId', index: 'zone_id', width: 80, hidden:true },
			{ label: '经办人id', name: 'uid', index: 'uid', width: 80, hidden:true },

			{ label: '小区名称', name: 'zoneName', index: 'zone_name', width: 80, cellattr: addCellAttr},
			{ label: '查看类型', name: 'type', index: 'type', width: 80, cellattr: addCellAttr, formatter:bizTypeFormatter },
			{ label: '详情id', name: 'referId', index: 'refer_id', width: 80, cellattr: addCellAttr },
			{ label: '查看人', name: 'realname', index: 'realname', width: 80, cellattr: addCellAttr },
			{ label: '查看时间', name: 'viewTime', index: 'view_time', width: 80, cellattr: addCellAttr },
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
		viewUrl: '',
		log: { },
		q:{
			keyword:null,
			zoneId:null,
			type:null
		},
		zonesOptions:[],
		bizOptions: [
			{id: 1, name: '费用报销'},
			{id: 2, name: '业委会议'},
			{id: 3, name: '来往函件'},
			{id: 4, name: '用章申请'},
			{id: 6, name: '事项表决'},
			{id: 8, name: '通知公告'},
			{id: 9, name: '公示记录'},
		],
		// 所有可选用户
		userList: [],
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
		dialogShow: false,
	},
    computed: {
		typeCn() {
			let typeName = '';
			if (this.log && this.log.type) {
				switch (this.log.type) {
					case 1: typeName = '费用报销';break;
					case 2: typeName = '业委会议';break;
					case 3: typeName = '来往函件';break;
					case 4: typeName = '用章申请';break;
					case 6: typeName = '事务表决';break;
					case 8: typeName = '通知公告';break;
					case 9: typeName = '公示记录';break;
					default: typeName = ''
				}
			}
			return typeName;
		}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		del: function (event) {
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "operation/hisviewlog/delete",
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
                    url: baseURL + "operation/hisviewlog/hide",
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
                    url: baseURL + "operation/hisviewlog/recovery",
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
			$.get(baseURL + "operation/hisviewlog/info/"+id, function(r){
                vm.log = r.log;
                if (r.log && r.log.record && r.log.record.id) {
                	switch (r.log.type) {
						case 1: vm.viewUrl = '../../modules/operation/bill.html';break;
						case 2: vm.viewUrl = '../../modules/operation/meeting.html';break;
						case 3: vm.viewUrl = '../../modules/operation/paper.html';break;
						case 4: vm.viewUrl = '../../modules/operation/request.html';break;
						case 6: vm.viewUrl = '../../modules/operation/vote.html';break;
						case 8: vm.viewUrl = '../../modules/operation/notification.html';break;
						case 9: vm.viewUrl = '../../modules/operation/announce.html';break;
						default: vm.viewUrl = ''
					}
					if (vm.viewUrl != '') {
						vm.dialogShow = true;
					}
				} else {
                	this.$message.error("没有找到数据!");
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
						'zoneId': vm.q.zoneId,
						'type': vm.q.type
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
			result += '<a class="btn btn-primary btn-sm" target="_blank" onclick="vm.getInfo(' + rowdata.id + ')">浏览</a>';
			return result;
		}
	},
    mounted() {
		getUsers(this, {});
    },
});


function showDetail() {
	const frame = window.frames["hisview"];
	frame.window.onView(vm.log.record.id);
	const list1 = frame.document.getElementsByClassName("btn btn-warning");
	for (i = 0; i< list1.length; i++) {
		list1[i].style.display='none'
	}
	const list2 = frame.document.getElementsByClassName("btn btn-primary");
	for (i = 0; i< list2.length; i++) {
		list2[i].style.display='none'
	}
}