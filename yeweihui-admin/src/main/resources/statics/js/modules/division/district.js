$(function () {
    $("#jqGrid").jqGrid({
			url: baseURL + 'division/district/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true },
			{ label: '行政区名称', name: 'name', index: 'name', width: 80 },
			{ label: '所属城市', name: 'dominantCity', index: 'dominant_city', width: 80, formatter: vm.cityFormatter },
			{ label: '更新时间', name: 'updateTime', index: 'update_time', width: 80},
            { label: '更新用户', name: 'updateUser', index: 'update_user', width: 80},
            { label: '创建时间', name: 'createTime', index: 'create_time', width: 80},
            { label: '创建用户', name: 'createUser', index: 'create_user', width: 80},
        ],
		viewrecords: true,
        height: 500,
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

import common from './common.js';

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		cityList: [],
		formData: {
            cityId: null
		},
		q:{
			district: null,
            dominantCity: null
		},
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
		dialogVisible: false,
	},
	methods: {
		query: function () {
			vm.reload();
		},
		onReset() {
            this.q = {
                district: null,
                dominantCity: null
            }
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.formData = {
				cityId: null
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
			var url = vm.formData.id == null ? "division/district/save" : "division/district/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.formData),
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
				    url: baseURL + "division/district/delete",
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
			$.get(baseURL + "division/district/info/"+id, function(r){
                vm.formData = r.district;
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
					postData:{
						'name': vm.q.district,
						'dominantcity': vm.q.dominantCity
					},
					page:page
            }).trigger("reloadGrid");
		},
		cityFormatter(cell, options, row) {
			if (row && row.cityId) {
				const list = this.cityList.filter(e => e.id === row.cityId);
				if (list && list.length > 0) {
					return list[0].name;
				}
			}
			return '未找到对应的城市，请重新配置';
		},
		//按钮格式化
		btnFormatter: function (cellvalue, options, rowdata) {
			var result = "";
			if(rowdata.status == 1){
				result += '<a class="btn btn-primary btn-sm" target="_blank" onclick="viewPdfPrint(' + rowdata.id + ')">pdf预览打印</a>';
			}
			return result;
		},
	},
	mounted() {
		common.getCityList(this);
    }
});