function initJqGrid() {
    $("#jqGrid").jqGrid({
		url: baseURL + 'division/manager/list',
        datatype: "json",
        colModel: [
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true },
			{ label: '用户名', name: 'userId', index: 'user_id', width: 80, formatter: vm.userFormatter },
			{ label: '管理级别', name: 'level', index: 'level', width: 80, formatter: vm.levelFormatter },
			{ label: '管理区域', name: 'divisionId', index: 'division_id', width: 80, formatter: vm.divisionFormatter },
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
};

import common from './common.js';

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
        userList: [],
		levelList: [
			{'en': 'province', 'cn': '省级'},
			{'en': 'city', 'cn': '市级'},
			{'en': 'district', 'cn': '区级'},
			{'en': 'subdistrict', 'cn': '街道级'},
			{'en': 'community', 'cn': '社区级'}],
		divisionList: [],
		provinceList: [],
		cityList: [],
		districtList: [],
		subdistrcitList: [],
		communityList: [],
		formData: {
            userId: null,
			level: null,
            divisionId: null
		},
		q:{
			userId: null,
            userName: null,
            level: null,
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
		dialogVisible: false
	},
	methods: {
		query: function () {
			vm.reload();
		},
		onReset() {
            this.q = {
                userId: null,
                userName: null,
                level: null,
            }
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.formData = {
                userId: null,
                level: null,
                divisionId: null
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
			var url = vm.formData.id == null ? "division/manager/save" : "division/manager/update";
			const form = vm.formData;
			if (form && form.userId && form.level && form.divisionId) {
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
			} else {
				this.$message.warning("请填写完整信息！");
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
				    url: baseURL + "division/manager/delete",
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
			$.get(baseURL + "division/manager/info/"+id, function(r){
                vm.formData = r.divisionManager;
                vm.changeDivisionList(r.divisionManager.level);
            });
		},
		async getDivisionList() {
			const self = this;
			const prov = $.ajax({
				type: "GET",
				url: baseURL + "division/province/all",
				success: function (r) {
					if (r.code === 0) {
						self.provinceList = r.all
					} else {
						alert(r.msg);
					}
				}
			});
			const city = $.ajax({
				type: "GET",
				url: baseURL + "division/city/all",
				success: function (r) {
					if (r.code === 0) {
						self.cityList = r.all
					} else {
						alert(r.msg);
					}
				}
			});
			const district = $.ajax({
				type: "GET",
				url: baseURL + "division/district/all",
				success: function (r) {
					if (r.code === 0) {
						self.districtList = r.all
					} else {
						alert(r.msg);
					}
				}
			});
			const subdistrict = $.ajax({
				type: "GET",
				url: baseURL + "division/subdistrict/all",
				success: function (r) {
					if (r.code === 0) {
						self.subdistrictList = r.all
					} else {
						alert(r.msg);
					}
				}
			});
			const community = $.ajax({
				type: "GET",
				url: baseURL + "division/community/all",
				success: function (r) {
					if (r.code === 0) {
						self.communityList = r.all
					} else {
						alert(r.msg);
					}
				}
			});
			await $.when(prov, city, district, subdistrict, community).done(() => {
				console.log("加载完成基础数据!");
				return;
			})
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
					postData:{
						'userId': vm.q.userId,
						'userName': vm.q.userName,
						'level': vm.q.level
					},
					page:page
            }).trigger("reloadGrid");
		},
        changeDivisionList(level) {
			if (level) {
                switch (level) {
                    case 'province': return this.divisionList = this.provinceList;
                    case 'city': return this.divisionList = this.cityList;
                    case 'district': return this.divisionList = this.districtList;
                    case 'subdistrict': return this.divisionList = this.subdistrictList;
                    case 'community': return this.divisionList = this.communityList;
                    default: throw Error("未找到对应的管理级别!");
                }
			}
		},
        userFormatter(cell, options, row) {
            if (row && row.userId) {
                const list = this.userList.filter(e => e.id === row.userId);
                if (list && list.length > 0) {
                    return list[0].realname;
                }
            }
            return row.userId || row;
        },
        levelFormatter(cell, options, row) {
			if (row && row.level) {
				const list = this.levelList.filter(e => e.en === row.level);
				if (list && list.length > 0) {
					return list[0].cn;
				}
			}
			return row.level;
		},
		divisionFormatter(cell, options, row) {
			if (row && row.level && row.divisionId) {
				switch (row.level) {
					case 'province': return this.provinceList.filter(e => e.id === row.divisionId)[0].name;
					case 'city': return this.cityList.filter(e => e.id === row.divisionId)[0].name;
					case 'district': return this.districtList.filter(e => e.id === row.divisionId)[0].name;
					case 'subdistrict': return this.subdistrictList.filter(e => e.id === row.divisionId)[0].name;
					case 'community': return this.communityList.filter(e => e.id === row.divisionId)[0].name;
					default: return "找不到对应的区域，请重新配置";
				}
			}
			return "找不到对应的区域，请重新配置";
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
		common.getUserListByRoleName(this, '行业主管');
		this.getDivisionList().then(() => {
			initJqGrid();
		});
	}
});