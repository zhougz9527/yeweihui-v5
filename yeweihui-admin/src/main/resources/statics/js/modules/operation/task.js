$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'operation/task/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true },
			{ label: '创建时间', name: 'createTime', index: 'create_time', width: 80, hidden:true },
			{ label: '更新时间', name: 'updateTime', index: 'update_time', width: 80, hidden:true },
			{ label: '小区id', name: 'zoneId', index: 'zone_id', width: 80 }, 			
			{ label: '小区', name: 'zoneName', index: 'zone_name', width: 80 },
			{ label: '用户id', name: 'uid', index: 'uid', width: 80 },
			{ label: '标题', name: 'title', index: 'title', width: 80 }, 			
			{ label: '内容', name: 'content', index: 'content', width: 80 }, 			
			{ label: '总任务数', name: 'total', index: 'total', width: 80 }, 			
			{ label: '完成任务数', name: 'checked', index: 'checked', width: 80 }, 			
			{ label: '状态', name: 'status', index: 'status', width: 80, formatter:vm.statusFormatter },//状态 1完成 2超时
            { label: '记录状态', name: 'recordStatus', index: 'record_status', width: 80, formatter:recordStatusFormatter},//状态 0已删除 1已隐藏 2正常
			{ label: '截止日期', name: 'endDate', index: 'end_date', width: 80 }
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
		showList: true,
		title: null,
		task: {},
		zonesOptions:[],
		//状态 1完成 2超时
		statusOptions:[
			{
				value:1,
				label:"完成"
			}, {
				value:2,
				label:"超时"
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
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.task = {
				zoneId:null,
				status:null,
				endDate:null
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
			var url = vm.task.id == null ? "operation/task/save" : "operation/task/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.task),
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
				    url: baseURL + "operation/task/delete",
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
                    url: baseURL + "operation/task/hide",
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
                    url: baseURL + "operation/task/recovery",
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
			$.get(baseURL + "operation/task/info/"+id, function(r){
                vm.task = r.task;
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                page:page
            }).trigger("reloadGrid");
		},
		//状态 1完成 2超时
		statusFormatter: function (cellvalue, options, rowdata) {
			switch (rowdata.status) {
				case 0:
					return '<span class="label label-danger">未完成</span>';
					break;
				case 1:
					return '<span class="label label-success">完成</span>';
					break;
				case 2:
					return '<span class="label label-warning">超时</span>';
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
		}


	}
});