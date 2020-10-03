$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'sys/yeweihuiuser/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true },
			{ label: '账号', name: 'name', index: 'name', width: 80 }, 			
			{ label: '密码', name: 'password', index: 'password', width: 80 }, 			
			{ label: '用户类型（游客 GUEST、普通用户 NORMAL_USER、业委会达人 DQ_EXPERT、业委会歌手 DQ_SINGER、业委会号 DQ_OFFICIAL_ACCOUNT、管理员 ADMIN） ', name: 'type', index: 'type', width: 80 }, 			
			{ label: '申请成为 type中的一个角色,默认NONE,不申请', name: 'applyFor', index: 'apply_for', width: 80 }, 			
			{ label: '升级申请时间', name: 'applyForTime', index: 'apply_for_time', width: 80 }, 			
			{ label: '手机', name: 'phone', index: 'phone', width: 80 }, 			
			{ label: '昵称', name: 'nickname', index: 'nickname', width: 80 }, 			
			{ label: '用户头像url', name: 'headIconUrl', index: 'head_icon_url', width: 80 }, 			
			{ label: '注册时间', name: 'registerTime', index: 'register_time', width: 80 }, 			
			{ label: '最后一次登陆时间', name: 'lastLoginTime', index: 'last_login_time', width: 80 }, 			
			{ label: '累计浏览次数', name: 'viewTimes', index: 'view_times', width: 80 }, 			
			{ label: '累计评论次数', name: 'commentTimes', index: 'comment_times', width: 80 }, 			
			{ label: '累计发微读数', name: 'weReadTimes', index: 'we_read_times', width: 80 }, 			
			{ label: '累计小视频数', name: 'tinyVideoCount', index: 'tiny_video_count', width: 80 }, 			
			{ label: '累计视频数', name: 'videoCount', index: 'video_count', width: 80 }, 			
			{ label: '累计歌曲数', name: 'songCount', index: 'song_count', width: 80 }, 			
			{ label: '累计文章数', name: 'articleCount', index: 'article_count', width: 80 }, 			
			{ label: '累计图集数', name: 'picGroupCount', index: 'pic_group_count', width: 80 }, 			
			{ label: '手机唯一识别码', name: 'phoneUniqueCode', index: 'phone_unique_code', width: 80 }, 			
			{ label: 'token', name: 'token', index: 'token', width: 80 }, 			
			{ label: '创建时间', name: 'createTime', index: 'create_time', width: 80 }, 			
			{ label: '修改时间', name: 'modifyTime', index: 'modify_time', width: 80 }, 			
			{ label: '用户生日', name: 'birthday', index: 'birthday', width: 80 }, 			
			{ label: '是否冻结', name: 'isFreeze', index: 'is_freeze', width: 80 }, 			
			{ label: '冻结原因', name: 'freezeReason', index: 'freeze_reason', width: 80 }, 			
			{ label: '是否禁言', name: 'isMute', index: 'is_mute', width: 80 }, 			
			{ label: '禁言原因', name: 'muteReason', index: 'mute_reason', width: 80 }, 			
			{ label: '用户介绍', name: 'description', index: 'description', width: 80 }, 			
			{ label: 'app转发次数', name: 'appShareCount', index: 'app_share_count', width: 80 }, 			
			{ label: '微信转发次数', name: 'wechatShareCount', index: 'wechat_share_count', width: 80 }, 			
			{ label: '朋友圈转发次数', name: 'momentsShareCount', index: 'moments_share_count', width: 80 }, 			
			{ label: '微博转发次数', name: 'microBlogShareCount', index: 'micro_blog_share_count', width: 80 }, 			
			{ label: 'qq转发次数', name: 'qqShareCount', index: 'qq_share_count', width: 80 }, 			
			{ label: 'app今日阅读次数', name: 'todayReadCount', index: 'today_read_count', width: 80 }, 			
			{ label: 'app累计阅读次数', name: 'totalReadCount', index: 'total_read_count', width: 80 }, 			
			{ label: 'app平均日阅读次数', name: 'avgDayReadCount', index: 'avg_day_read_count', width: 80 }, 			
			{ label: '今日停留（分钟）', name: 'todayStayMinutes', index: 'today_stay_minutes', width: 80 }, 			
			{ label: '累计停留（分钟）', name: 'totalStayMinutes', index: 'total_stay_minutes', width: 80 }, 			
			{ label: '使用app天数(使用一次一天累加一次)', name: 'totalAppUseDays', index: 'total_app_use_days', width: 80 }, 			
			{ label: '地区', name: 'area', index: 'area', width: 80 }, 			
			{ label: 'MALE,FEMALE', name: 'gender', index: 'gender', width: 80 }			
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
		yeweihuiUser: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.yeweihuiUser = {};
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
			var url = vm.yeweihuiUser.id == null ? "sys/yeweihuiuser/save" : "sys/yeweihuiuser/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.yeweihuiUser),
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
				    url: baseURL + "sys/yeweihuiuser/delete",
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
			$.get(baseURL + "sys/yeweihuiuser/info/"+id, function(r){
                vm.yeweihuiUser = r.yeweihuiUser;
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                page:page
            }).trigger("reloadGrid");
		}
	}
});