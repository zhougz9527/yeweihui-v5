$(function () {
  $('#jqGrid').jqGrid({
    url: baseURL + 'jmkj/OnlineNum',
    datatype: 'json',
    colModel: [
      { label: 'id', name: 'id', index: 'id', width: 50, key: true },
      { label: '用户名', name: 'createTime', index: 'create_time', width: 80 },
      {
        label: '管理级别',
        name: 'updateTime',
        index: 'update_time',
        width: 80,
      },
      { label: '管理区域', name: 'name', index: 'name', width: 80 },
      { label: '更新时间', name: 'address', index: 'address', width: 80 },
      { label: '更新用户', name: 'tel', index: 'tel', width: 80 },
      { label: '创建时间', name: 'status', index: 'status', width: 80 },
      {
        label: '创建用户',
        name: 'provinceId',
        index: 'province_id',
        width: 80,
      },
    ],
    viewrecords: true,
    height: 630,
    rowNum: 10,
    rowList: [10, 30, 50],
    rownumbers: true,
    rownumWidth: 25,
    autowidth: true,
    multiselect: true,
    pager: '#jqGridPager',
    jsonReader: {
      root: 'page.list',
      page: 'page.currPage',
      total: 'page.totalPage',
      records: 'page.totalCount',
    },
    prmNames: {
      page: 'page',
      rows: 'limit',
      order: 'order',
    },
    gridComplete: function () {
      //隐藏grid底部滚动条
      //$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
    },
  });

  //获取小区列表
  vm.getZonesList();
});
var setting = {
  data: {
    simpleData: {
      enable: true,
      idKey: 'deptId',
      pIdKey: 'parentId',
      rootPId: -1,
    },
    key: {
      url: 'nourl',
    },
  },
};
var ztree;

var vm = new Vue({
  el: '#rrapp',
  data: {
    userNum: '',
    userName: '',
    rank: '',
    options: [
      {
        name: '省级',
        id: 1,
      },
      {
        name: '市级',
        id: 2,
      },
      {
        name: '区级',
        id: 3,
      },
      {
        name: '街道级',
        id: 4,
      },
      {
        name: '社区级',
        id: 5,
      },
    ],
  },
  methods: {
    edit() {
      let dataStr = {
        size: 10,
        pages: 1,
        Telephone: '',
        name: '',
        level: '',
      };
      
      $.ajax({
        type: 'post', //方法类型
        dataType: 'json',
        url: baseURL + 'jmkj/IndustryDirector',
        contentType: 'application/json; charset=utf-8',
        data: JSON.stringify(dataStr),
        success: function (result) {
          if (result.code == 0) {
            console.log(result);
          }
        },
        error: function () {},
      });
    },
    // tabbar 切换
    handleTabs(e) {
      console.log(e);
      this.getList(e.index);
    },
    // tabbar 切换
    handleTabsTwo(e) {
      console.log(e);
      this.getListTwo(e.index);
    },
    // tabbar 切换
    handleTabsThree(e) {
      console.log(e);
      this.getListThree(e.index);
    },
    // 查询数据1
    getList(index) {
      console.log('getList', index);
      let url = baseURL;
      switch (Number(index)) {
        case 1:
          // 履职量
          url += 'jmkj/getPerformanceOfDutiesList';
          break;
        case 2:
          // 履职率
          url += 'jmkj/getPerformanceRateBeans';
          break;
        case 3:
          // 逾期量
          url += 'jmkj/OverdueQuantity';
          break;
        case 4:
          // 逾期率
          url += 'jmkj/OverdueRate';
          break;
        case 5:
          // 新建总量
          url += 'jmkj/operationNum';
          break;
      }
      $.ajax({
        type: 'get', //方法类型
        dataType: 'json',
        url: url,
        contentType: 'application/x-www-form-urlencoded; charset=utf-8',
        data: {
          zoneId: this.optionActive,
          timeStart: this.time[0].getTime(),
          timeEnd: this.time[1].getTime(),
        },
        success: function (result) {
          if (result.code == 0) {
            vm.$data.tabOneList = result.data;
            console.log(result, this.tabOneList);
          }
        },
        error: function () {},
      });
    },
    // 查询数据2
    getListTwo(index) {
      console.log('getListTwo', index);
      let url = baseURL;
      switch (Number(index)) {
        case 1:
          // 完成量
          url += 'jmkj/BrowseComplete';
          break;
        case 2:
          // 新建总量
          url += 'jmkj/NewBrowse';
          break;
      }
      $.ajax({
        type: 'get', //方法类型
        dataType: 'json',
        url: url,
        contentType: 'application/x-www-form-urlencoded; charset=utf-8',
        data: {
          zoneId: this.optionActive,
          timeStart: this.time[0].getTime(),
          timeEnd: this.time[1].getTime(),
        },
        success: function (result) {
          if (result.code == 0) {
            vm.$data.tabTwoList = result.data;
            console.log(result, this.tabTwoList);
          }
        },
        error: function () {},
      });
    },
    // 查询数据3
    getListThree(index) {
      console.log('getList', index);
      let url = baseURL;
      switch (Number(index)) {
        case 1:
          // 在线时长
          url += 'jmkj/OnlineDuration';
          break;
        case 2:
          // 登录次数
          url += 'jmkj/OnlineDuration';
          break;
      }
      $.ajax({
        type: 'get', //方法类型
        dataType: 'json',
        url: url,
        contentType: 'application/x-www-form-urlencoded; charset=utf-8',
        data: {
          zoneId: this.optionActive,
          timeStart: this.time[0].getTime(),
          timeEnd: this.time[1].getTime(),
        },
        success: function (result) {
          if (result.code == 0) {
            vm.$data.tabThreeList = result.data;
            console.log(result, this.tabThreeList);
          }
        },
        error: function () {},
      });
    },
    // 查看
    add() {
      console.log(this.optionActive, this.time);
      vm.getList(1);
      vm.getListTwo(1);
      vm.getListThree(1);
    },
    getDept: function () {
      //加载部门树
      $.get(baseURL + 'sys/dept/list', function (r) {
        console.log(r);
        ztree = $.fn.zTree.init($('#deptTree'), setting, r);
        var node = ztree.getNodeByParam('deptId', vm.user.deptId);
        if (node != null) {
          ztree.selectNode(node);

          vm.user.deptName = node.name;
        }
      });
    },
    update: function () {
      var id = getSelectedRow();
      if (id == null) {
        return;
      }

      vm.showList = false;
      vm.title = '修改';

      vm.getUser(id);
      //获取角色信息
      this.getRoleList();
    },
    del: function () {
      var userIds = getSelectedRows();
      if (userIds == null) {
        return;
      }

      confirm('确定要删除选中的记录？', function () {
        $.ajax({
          type: 'POST',
          url: baseURL + 'sys/user/delete',
          contentType: 'application/json',
          data: JSON.stringify(userIds),
          success: function (r) {
            if (r.code == 0) {
              alert('操作成功', function () {
                vm.reload();
              });
            } else {
              alert(r.msg);
            }
          },
        });
      });
    },
    saveOrUpdate: function () {
      var url = vm.user.id == null ? 'sys/user/save' : 'sys/user/update';
      console.log(vm.user);
      $.ajax({
        type: 'POST',
        url: baseURL + url,
        contentType: 'application/json',
        data: JSON.stringify(vm.user),
        success: function (r) {
          if (r.code === 0) {
            alert('操作成功', function () {
              vm.reload();
            });
          } else {
            alert(r.msg);
          }
        },
      });
    },
    getUser: function (userId) {
      $.get(baseURL + 'sys/user/info/' + userId, function (r) {
        vm.user = r.user;
        vm.user.password = null;

        vm.getDept();
      });
    },
    getRoleList: function () {
      $.get(baseURL + 'sys/role/select', function (r) {
        vm.roleList = r.list;
      });
    },
    deptTree: function () {
      layer.open({
        type: 1,
        offset: '50px',
        skin: 'layui-layer-molv',
        title: '选择部门',
        area: ['300px', '450px'],
        shade: 0,
        shadeClose: false,
        content: jQuery('#deptLayer'),
        btn: ['确定', '取消'],
        btn1: function (index) {
          var node = ztree.getSelectedNodes();
          //选择上级部门
          vm.user.deptId = node[0].deptId;
          vm.user.deptName = node[0].name;

          layer.close(index);
        },
      });
    },
    reload: function (event) {
      vm.showList = true;
      var page = $('#jqGrid').jqGrid('getGridParam', 'page');
      $('#jqGrid')
        .jqGrid('setGridParam', {
          page: page,
        })
        .trigger('reloadGrid');
    },
    //获取小区列表
    getZonesList: function () {
      //获取下拉框数据
      $.ajax({
        type: 'get', //方法类型
        dataType: 'json',
        url: baseURL + 'user/zones/simpleList',
        contentType: 'application/x-www-form-urlencoded; charset=utf-8',
        data: {},
        success: function (result) {
          if (result.code == 0) {
            vm.zonesOptions = result.zonesEntityList;
            console.log(vm.zonesOptions);
          }
        },
        error: function () {},
      });
    },
    //操作按钮格式化
    btnFormatter: function (cellvalue, options, rowdata) {
      var result = '';
      //审核状态 0审核中 1审核通过 2撤销 3未通过
      if (rowdata.verifyStatus == 0) {
        result +=
          '&nbsp;<a class="btn btn-primary btn-sm" onclick="verifyUser(' +
          rowdata.id +
          ', 1)">通过</a>';
        result +=
          '&nbsp;<a class="btn btn-primary btn-sm" onclick="verifyUser(' +
          rowdata.id +
          ', 3)">拒绝</a>';
      }

      return result;
    },
  },
});
