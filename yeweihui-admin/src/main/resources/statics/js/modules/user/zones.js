$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'user/zones/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true },
			{ label: '创建时间', name: 'createTime', index: 'create_time', width: 80, hidden:true },
			{ label: '更新时间', name: 'updateTime', index: 'update_time', width: 80, hidden:true },
			{ label: '小区名称', name: 'name', index: 'name', width: 80 }, 			
			{ label: '地址', name: 'address', index: 'address', width: 80 }, 			
			{ label: '前台电话', name: 'tel', index: 'tel', width: 80 }, 			
			{ label: '状态', name: 'status', index: 'status', width: 80, formatter:vm.statusFormatter },//0未审批 1通过 2未通过
			{ label: '省id', name: 'provinceId', index: 'province_id', width: 80, hidden:true },
			{ label: '省', name: 'provinceName', index: 'province_name', width: 80, hidden:true },
			{ label: '市id', name: 'cityId', index: 'city_id', width: 80, hidden:true },
			{ label: '市', name: 'cityName', index: 'city_name', width: 80, hidden:true },
			{ label: '区id', name: 'districtId', index: 'district_id', width: 80, hidden:true },
			{ label: '区', name: 'districtName', index: 'district_name', width: 80, hidden:true },
			{ label: '邀请码', name: 'inviteCode', index: 'invite_code', width: 80, hidden:true },
			{ label: '注册时间', name: 'registerTime', index: 'register_time', width: 80 },
			{ label: '可全额退款时间', name: 'refundEnableTime', index: 'refund_enable_time', width: 80 },
			{ label: '使用截止时间', name: 'enableUseTime', index: 'enable_use_time', width: 80 },
			{ label: '邀请码', name: 'inviteCode', index: 'invite_code', width: 80 }
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

import common from '../division/common.js';

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		zones: {},
		statusList:[
			{
				id:0,
				status:"禁用"
			}, {
				id:1,
				status:"启用"
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
		q:{
			keyword:null,
			zoneId:null
		},
		zonesOptions:[],
		communityList:[],
		subdistrictList:[],
		districtList:[],
		cityList:[],
		provinceList:[],
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.zones = {
				status:null,
                registerTime:null,
                refundEnableTime:null,
                enableUseTime:null,
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
			var url = vm.zones.id == null ? "user/zones/save" : "user/zones/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.zones),
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
				    url: baseURL + "user/zones/delete",
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
			$.get(baseURL + "user/zones/info/"+id, function(r){
                vm.zones = r.zones;
            });
		},
		reload: function (event) {
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
		//小区审批状态 0未审批 1通过 2未通过
		statusFormatter: function (cellvalue, options, rowdata) {
			switch (rowdata.status) {
				case 0:
					return '<span class="label label-danger">禁用</span>';
				case 1:
					return '<span class=\"label label-success\">正常</span>';
				default:
					return rowdata.status;
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
		loadDistrict() {
			common.getProvinceList(this);
			common.getCityList(this, this.zones.provinceId);
			common.getDistrictList(this, this.zones.cityId);
			common.getSubdistrictList(this, this.zones.districtId);
			common.getCommunityList(this, this.zones.subdistrictId);
		}
	},
	mounted() {
		this.loadDistrict();
	}
});