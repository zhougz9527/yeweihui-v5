//jqGrid的配置信息
$.jgrid.defaults.width = 1000;
$.jgrid.defaults.responsive = true;
$.jgrid.defaults.styleUI = 'Bootstrap';

var baseURL = "../../";
var url = window.location.href;
// console.log(url);
// var baseURL = url.substring(0, url.lastIndexOf('ineyes-admin') + 13).replace("miaogui/", "");
// var baseURL = url.substring(0, url.lastIndexOf('yeweihui-admin') + 15);
// var baseURL = url + "";
// var baseURL = url +"yeweihui-admin";
console.log(baseURL);

const officeViewUrl="https://view.officeapps.live.com/op/view.aspx?src=";

//工具集合Tools
window.T = {};

// 获取请求参数
// 使用示例
// location.href = http://localhost:8080/index.html?id=123
// T.p('id') --> 123;
var url = function(name) {
	var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if(r!=null)return  unescape(r[2]); return null;
};
T.p = url;

//全局配置
$.ajaxSetup({
	dataType: "json",
	cache: false
});

//重写alert
window.alert = function(msg, callback){
	parent.layer.alert(msg, function(index){
		parent.layer.close(index);
		if(typeof(callback) === "function"){
			callback("ok");
		}
	});
};

//重写confirm式样框
window.confirm = function(msg, callback){
	parent.layer.confirm(msg, {btn: ['确定','取消']},
	function(){//确定事件
		if(typeof(callback) === "function"){
			callback("ok");
		}
	});
};

//选择一条记录
function getSelectedRow() {
    var grid = $("#jqGrid");
    var rowKey = grid.getGridParam("selrow");
    if(!rowKey){
    	alert("请选择一条记录");
    	return ;
    }
    
    var selectedIDs = grid.getGridParam("selarrrow");
    if(selectedIDs.length > 1){
    	alert("只能选择一条记录");
    	return ;
    }
    
    return selectedIDs[0];
}

//选择多条记录
function getSelectedRows() {
    var grid = $("#jqGrid");
    var rowKey = grid.getGridParam("selrow");
    if(!rowKey){
    	alert("请选择一条记录");
    	return ;
    }
    
    return grid.getGridParam("selarrrow");
}

//判断是否为空
function isBlank(value) {
    return !value || !/\S/.test(value)
}


function recordStatusFormatter(cellvalue, options, rowdata) {
    switch (rowdata.recordStatus) {
        case 0:
            return '<span class="label label-danger">已删除</span>';
        case 1:
            return '<span class="label label-warning">已隐藏</span>';
        case 2:
            return '<span class="label label-success">正常</span>';
        default:
            return rowdata.status;
    }
}

function bizTypeFormatter(cellvalue, options, rowdata) {
    switch (rowdata.type) {
        case 1:
            return '费用报销';
        case 2:
            return '会议';
        case 3:
            return '来往函件';
        case 4:
            return '用章申请';
        case 5:
            return '任务';
        case 6:
            return '事务表决';
        case 7:
            return '会议日志';
        case 8:
            return '通知公告';
        case 9:
            return '公示记录';
        default:
            return rowdata.type;
    }
}


function getUsers(vue, postBody) {
    const self = vue;
    console.log(baseURL + 'sys/user/listByUser');
    $.ajax({
        type: "POST",
        url: baseURL + 'sys/user/listByUser',
        contentType: "application/json",
        data: JSON.stringify(postBody),
        success: function(r){
            if(r.code === 0){
                self.userList = r.list;
            }else{
                alert(r.msg);
            }
        }
    });
}