$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'operation/meetingmember/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true },
			{ label: '创建时间', name: 'createTime', index: 'create_time', width: 80 }, 			
			{ label: '更新时间', name: 'updateTime', index: 'update_time', width: 80 }, 			
			{ label: '会议id', name: 'mid', index: 'mid', width: 80 }, 			
			{ label: '参加会议的人', name: 'uid', index: 'uid', width: 80 }, 			
			{ label: '会议发起人', name: 'referUid', index: 'refer_uid', width: 80 }, 			
			{ label: '类型', name: 'type', index: 'type', width: 80, formatter:vm.typeFormatter },// 类型 1参会 2抄送
			{ label: '签到时间', name: 'signTime', index: 'sign_time', width: 80 }, 			
			{ label: '签字时间', name: 'signNameTime', index: 'sign_name_time', width: 80 }, 			
			{ label: '状态', name: 'status', index: 'status', width: 80, formatter:vm.statusFormatter }, //状态 1已签到 2已签字
			{ label: '签字照片', name: 'signImg', index: 'sign_img', width: 80 }			
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
		meetingMember: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.meetingMember = {};
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
			var url = vm.meetingMember.id == null ? "operation/meetingmember/save" : "operation/meetingmember/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.meetingMember),
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
				    url: baseURL + "operation/meetingmember/delete",
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
			$.get(baseURL + "operation/meetingmember/info/"+id, function(r){
                vm.meetingMember = r.meetingMember;
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                page:page
            }).trigger("reloadGrid");
		},
		// 类型 1参会 2抄送
		typeFormatter: function (cellvalue, options, rowdata) {
			switch (rowdata.type) {
				case 1:
					return '<span class="label label-success">参会</span>';
					break;
				case 2:
					return '<span class="label label-warning">抄送</span>';
					break;
				default:
					return rowdata.type;
					break;
			}
		},
		//状态 0未签到 1已签到 2已签字
		statusFormatter: function (cellvalue, options, rowdata) {
			switch (rowdata.status) {
				case 0:
					return '<span class="label label-warning">未签到</span>';
					break;
				case 1:
					return '<span class="label label-info">已签到</span>';
					break;
				case 2:
					return '<span class="label label-success">已签字</span>';
					break;
				default:
					return rowdata.status;
					break;
			}
		}

	}
});