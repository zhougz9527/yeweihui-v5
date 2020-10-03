$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'operation/votemember/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true },
			{ label: '创建时间', name: 'createTime', index: 'create_time', width: 80 }, 			
			{ label: '更新时间', name: 'updateTime', index: 'update_time', width: 80 }, 			
			{ label: '表决id', name: 'vid', index: 'vid', width: 80 }, 			
			{ label: '参与表决用户id', name: 'uid', index: 'uid', width: 80 }, 			
			{ label: '发起人id', name: 'referUid', index: 'refer_uid', width: 80 }, 			
			{ label: '类型', name: 'type', index: 'type', width: 80 , formatter:vm.typeFormatter},//类型 1表决 2抄送
			{ label: '状态', name: 'status', index: 'status', width: 80, formatter:vm.statusFormatter },//状态 1同意 2反对 3弃权 4超时
			{ label: '支持选项1', name: 'item1', index: 'item1', width: 80 }, 			
			{ label: '支持选项2', name: 'item2', index: 'item2', width: 80 }, 			
			{ label: '支持选项3', name: 'item3', index: 'item3', width: 80 }, 			
			{ label: '都不同意', name: 'item4', index: 'item4', width: 80 }, 			
			{ label: '备注', name: 'remark', index: 'remark', width: 80 }, 			
			{ label: '表决时间', name: 'voteTime', index: 'vote_time', width: 80 }			
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
		voteMember: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.voteMember = {};
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
			var url = vm.voteMember.id == null ? "operation/votemember/save" : "operation/votemember/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.voteMember),
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
				    url: baseURL + "operation/votemember/delete",
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
			$.get(baseURL + "operation/votemember/info/"+id, function(r){
                vm.voteMember = r.voteMember;
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                page:page
            }).trigger("reloadGrid");
		},
		//类型 1表决 2抄送
		typeFormatter: function (cellvalue, options, rowdata) {
			switch (rowdata.type) {
				case 1:
					return '<span class="label label-success">表决</span>';
					break;
				case 2:
					return '<span class="label label-warning">抄送</span>';
					break;
				default:
					return rowdata.type;
					break;
			}
		},
		//状态 1同意 2反对 3弃权 4超时
		statusFormatter: function (cellvalue, options, rowdata) {
			switch (rowdata.status) {
				case 1:
					return '<span class="label label-success">同意</span>';
					break;
				case 2:
					return '<span class="label label-danger">反对</span>';
					break;
				case 3:
					return '<span class="label label-success">弃权</span>';
					break;
				case 4:
					return '<span class="label label-warning">超时</span>';
					break;
				case 4:
					return '<span class="label label-success">多选选择确定</span>';
					break;
				default:
					return rowdata.status;
					break;
			}
		}

	}
});