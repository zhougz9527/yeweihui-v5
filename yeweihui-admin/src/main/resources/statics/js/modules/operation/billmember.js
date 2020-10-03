$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'operation/billmember/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true },
			{ label: '创建时间', name: 'createTime', index: 'create_time', width: 80 }, 			
			{ label: '更新时间', name: 'updateTime', index: 'update_time', width: 80 }, 			
			{ label: '报销id', name: 'bid', index: 'bid', width: 80 }, 			
			{ label: '用户id', name: 'uid', index: 'uid', width: 80 }, 			
			{ label: '参与用户id', name: 'referUid', index: 'refer_uid', width: 80 }, 			
			{ label: '状态', name: 'status', index: 'status', width: 80, formatter:vm.statusFormatter }//状态 0未审批 1已通过 2未通过
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
		billMember: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.billMember = {};
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
			var url = vm.billMember.id == null ? "operation/billmember/save" : "operation/billmember/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.billMember),
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
				    url: baseURL + "operation/billmember/delete",
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
			$.get(baseURL + "operation/billmember/info/"+id, function(r){
                vm.billMember = r.billMember;
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                page:page
            }).trigger("reloadGrid");
		},
		//状态 0未审批 1已通过 2未通过
		statusFormatter: function (cellvalue, options, rowdata) {
			switch (rowdata.status) {
				case 0:
					return '<span class="label label-warning">未审批</span>';
					break;
				case 1:
					return '<span class="label label-success">已通过</span>';
					break;
				case 2:
					return '<span class="label label-danger">未通过</span>';
					break;
				default:
					return rowdata.status;
					break;
			}
		}


	}
});