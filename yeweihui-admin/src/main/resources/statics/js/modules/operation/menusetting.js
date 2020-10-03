$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'user/zoneMenuMap/zoneGroups',
        datatype: "json",
        colModel: [
            {label: 'ID', name: 'id', index: "id", width: 45, key: true},
            {label: '分组', name: 'groupName', index: "group_name", width: 45},
            {label: '创建时间', name: 'createTime', index: "create_time", width: 80},
            {label: '更新时间', name: 'updateTime', index: "update_time", width: 80}
        ],
        viewrecords: true,
        height: 630,
        rowNum: 10,
        rowList: [10, 30, 50],
        rownumbers: true,
        rownumWidth: 25,
        autowidth: true,
        multiselect: true,
        jsonReader: {
            root: "zoneGroups",
        },
        gridComplete: function () {
            //隐藏grid底部滚动条
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
        }
    });

    //获取小区列表
    vm.getZonesList();
    vm.getUser();
    vm.getGroup();
});

//菜单树
var menu_ztree;
var menu_setting = {
    data: {
        simpleData: {
            enable: true,
            idKey: "id",
            // pIdKey: "id",
            rootPId: -1,
        },
        key: {
            url: "nourl"
        },
    },
    check: {
        enable: true,
        nocheckInherit: true,
    },
};


var vm = new Vue({
    el: '#rrapp',
    data: {
        zonesOptions: [],
        user: {},
        q: {
            zoneId: null,
            groupId: null
        },
        showList: true,
        title: null,
        groupName: null,
        role: {},
        groupList: [],
    },
    methods: {
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
        getUser: function () {
            $.ajax({
                type: "get",//方法类型
                dataType: "json",
                url: baseURL + "sys/user/info",
                contentType: "application/x-www-form-urlencoded; charset=utf-8",
                data: {},
                success: function (result) {
                    if (result.code == 0) {
                        vm.user = result.user;
                    }
                },
                error: function () {
                }
            });
        },
        getGroup: function () {
            $.ajax({
                type: "get",//方法类型
                dataType: "json",
                url: baseURL + 'user/zoneMenuMap/zoneGroups',
                contentType: "application/x-www-form-urlencoded; charset=utf-8",
                data: {},
                success: function (result) {
                    if (result.code == 0) {
                        vm.groupList = result.zoneGroups;
                    }
                },
                error: function () {
                }
            });
        },
        query: function () {
            vm.reload();
        },
        update: function () {
            var id = getSelectedRow();
            if (id == null) {
                return;
            }
            if (vm.user.zoneId === 0 && !vm.q.zoneId) {
                alert("请在下拉框中选择要修改的小区");
                return;
            }
            var zoneId = vm.user.zoneId > 0 ? vm.user.zoneId : vm.q.zoneId;

            vm.groupList.map(g => {
                if (g.id == id) {
                    vm.groupName = g.groupName;
                }
            });
            vm.showList = false;
            vm.title = "修改";
            vm.getMenuTree(id, zoneId);

        },
        getRole: function (groupId, zoneId) {
            $.get(baseURL + "user/zoneMenuMap/queryByZoneId?zoneId=" + zoneId + "&&groupId=" + groupId, function (r) {
                vm.role = r.zoneMenuMaps[0] ? r.zoneMenuMaps[0] : {
                    zoneId: zoneId,
                    groupId: groupId,
                    menuMapIds: []
                };
                //勾选角色所拥有的菜单
                var menuIds = vm.role.menuMapIds.length > 0 ? vm.role.menuMapIds.split(',') : [];
                for (var i = 0; i < menuIds.length; i++) {
                    var node = menu_ztree.getNodeByParam("id", Number(menuIds[i]));
                    if(node){
                        menu_ztree.checkNode(node, true, false);
                    }
                }
            });
        },
        saveOrUpdate: function () {
            //获取选择的菜单
            var nodes = menu_ztree.getCheckedNodes(true);
            var menuIdList = new Array();
            for (var i = 0; i < nodes.length; i++) {
                menuIdList.push(nodes[i].id);
            }
            vm.role.menuMapIds = menuIdList.join(',');
            $.ajax({
                type: "POST",
                url: baseURL + 'user/zoneMenuMap/updateByZoneMenuMap',
                contentType: "application/json",
                data: JSON.stringify(vm.role),
                success: function (r) {
                    if (r.code === 0) {
                        alert('操作成功', function () {
                            vm.reload();
                        });
                    } else {
                        alert(r.msg);
                    }
                }
            });
        },
        getMenuTree: function (groupId, zoneId) {
            //加载菜单树
            $.get(baseURL + "user/zoneMenuMap/menuMaps", function (r) {
                menu_ztree = $.fn.zTree.init($("#menuTree"), menu_setting, r.menuMaps);
                //展开所有节点
                menu_ztree.expandAll(true);
                vm.getRole(groupId, zoneId);
            });
        },

        reload: function () {
            vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                getData: {'zoneId': vm.q.zoneId},
            }).trigger("reloadGrid");
        }
    }
});