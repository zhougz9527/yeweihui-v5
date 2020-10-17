$(function () {
  $('#jqGrid').jqGrid({
    url: baseURL + 'sys/user/list',
    datatype: 'json',
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
  // 初始化时间
  vm.setTime()
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
    time: '',
    optionActive: 0,
    tabOneList: {
      listData: [],
      avatarUrl: '',
      num: 1,
      ranking: 2,
      realname: '静静',
    },
    pickerOptions: {
      shortcuts: [
        {
          text: '上月',
          onClick(picker) {
            // 当前时间
            const nwo = new Date();
            // 当前月份
            const nwoMonth = nwo.getMonth();
            // 当前年份
            const nwoYear = nwo.getFullYear();
            const y1 = nwo.getFullYear();
            if (nwoMonth == 0) {
              nwoMonth = 12;
              nwoYear = nwoYear - 1;
            }
            // 上月初
            const firstTime = new Date(nwoYear, nwoMonth - 1);
            const lastTime = new Date(y1, nwoMonth);
            const end = new Date();
            end.setTime(lastTime.getTime() - 3600 * 1000 * 24);
            picker.$emit('pick', [firstTime, end]);
          },
        },
        {
          text: '一月',
          onClick(picker) {
            const end = new Date();
            let first = end.getFullYear();
            let endTime = new Date(first, 1);
            let start = new Date(first, 0);
            end.setTime(endTime.getTime() - 3600 * 1000 * 24);
            picker.$emit('pick', [start, end]);
          },
        },
        {
          text: '二月',
          onClick(picker) {
            const end = new Date();
            let first = end.getFullYear();
            let endTime = new Date(first, 2);
            let start = new Date(first, 1);
            end.setTime(endTime.getTime() - 3600 * 1000 * 24);
            picker.$emit('pick', [start, end]);
          },
        },
        {
          text: '三月',
          onClick(picker) {
            const end = new Date();
            let first = end.getFullYear();
            let endTime = new Date(first, 3);
            let start = new Date(first, 2);
            end.setTime(endTime.getTime() - 3600 * 1000 * 24);
            picker.$emit('pick', [start, end]);
          },
        },
        {
          text: '四月',
          onClick(picker) {
            const end = new Date();
            let first = end.getFullYear();
            let endTime = new Date(first, 4);
            let start = new Date(first, 3);
            end.setTime(endTime.getTime() - 3600 * 1000 * 24);
            picker.$emit('pick', [start, end]);
          },
        },
        {
          text: '五月',
          onClick(picker) {
            const end = new Date();
            let first = end.getFullYear();
            let endTime = new Date(first, 5);
            let start = new Date(first, 4);
            end.setTime(endTime.getTime() - 3600 * 1000 * 24);
            picker.$emit('pick', [start, end]);
          },
        },
        {
          text: '六月',
          onClick(picker) {
            const end = new Date();
            let first = end.getFullYear();
            let endTime = new Date(first, 6);
            let start = new Date(first, 5);
            end.setTime(endTime.getTime() - 3600 * 1000 * 24);
            picker.$emit('pick', [start, end]);
          },
        },
        {
          text: '七月',
          onClick(picker) {
            const end = new Date();
            let first = end.getFullYear();
            let endTime = new Date(first, 7);
            let start = new Date(first, 6);
            end.setTime(endTime.getTime() - 3600 * 1000 * 24);
            picker.$emit('pick', [start, end]);
          },
        },
        {
          text: '八月',
          onClick(picker) {
            const end = new Date();
            let first = end.getFullYear();
            let endTime = new Date(first, 8);
            let start = new Date(first, 7);
            end.setTime(endTime.getTime() - 3600 * 1000 * 24);
            picker.$emit('pick', [start, end]);
          },
        },
        {
          text: '九月',
          onClick(picker) {
            const end = new Date();
            let first = end.getFullYear();
            let endTime = new Date(first, 9);
            let start = new Date(first, 8);
            end.setTime(endTime.getTime() - 3600 * 1000 * 24);
            picker.$emit('pick', [start, end]);
          },
        },
        {
          text: '十月',
          onClick(picker) {
            const end = new Date();
            let first = end.getFullYear();
            let endTime = new Date(first, 10);
            let start = new Date(first, 9);
            end.setTime(endTime.getTime() - 3600 * 1000 * 24);
            picker.$emit('pick', [start, end]);
          },
        },
        {
          text: '十一月',
          onClick(picker) {
            const end = new Date();
            let first = end.getFullYear();
            let endTime = new Date(first, 11);
            let start = new Date(first, 10);
            end.setTime(endTime.getTime() - 3600 * 1000 * 24);
            picker.$emit('pick', [start, end]);
          },
        },
        {
          text: '十二月',
          onClick(picker) {
            const end = new Date();
            let first = end.getFullYear();
            let endTime = new Date(first + 1, 0);
            let start = new Date(first, 11);
            end.setTime(endTime.getTime() - 3600 * 1000 * 24);
            picker.$emit('pick', [start, end]);
          },
        },
        {
          text: '上年',
          onClick(picker) {
            const time = new Date();
            let year = time.getFullYear();
            let start = new Date(year - 1, 0);
            let end = new Date(year - 1, 11);
            picker.$emit('pick', [start, end]);
          },
        },
      ],
    },
    options: [
      {
        name: '本小区',
        id: 1,
      },
      {
        name: '本社区',
        id: 2,
      },
      {
        name: '本街道',
        id: 3,
      },
      {
        name: '本区',
        id: 4,
      },
      {
        name: '本市',
        id: 5,
      },
      {
        name: '本省',
        id: 6,
      },
      {
        name: '本平台',
        id: 7,
      },
    ],
    tabberListOne: [
      {
        name: '履职量',
        id: 0,
      },
      {
        name: '履职率',
        id: 1,
      },
      {
        name: '逾期量',
        id: 2,
      },
      {
        name: '逾期率',
        id: 3,
      },
      {
        name: '新建总量',
        id: 4,
      },
    ],
    tabberListTwo: [
      {
        name: '完成量',
        id: 0,
      },
      {
        name: '新建总量',
        id: 1,
      },
    ],
    tabberListThree: [
      {
        name: '在线时长',
        id: 0,
      },
      {
        name: '登录次数',
        id: 1,
      },
    ],
    q: {
      keyword: null,
      zoneId: null,
    },
    showList: true,
    title: null,
    roleList: {},
    user: {
      status: 1,
      deptId: null,
      deptName: null,
      roleIdList: [],
    },
    //性别
    genderList: [
      {
        id: 1,
        gender: '男',
      },
      {
        id: 2,
        gender: '女',
      },
      {
        id: 0,
        gender: '未知',
      },
    ],
    zonesOptions: [],
  },
  methods: {
    // tabbar 切换
    handleTabs(e) {
      console.log(e);
      this.getList();
    },
    // 查询数据
    getList() {
      $.ajax({
        type: 'get', //方法类型
        dataType: 'json',
        url: baseURL + 'jmkj/getPerformanceOfDutiesList',
        contentType: 'application/x-www-form-urlencoded; charset=utf-8',
        data: {
          zoneId: this.optionActive,
          timeStart: this.time[0].getTime(),
          timeEnd: this.time[1].getTime(),
        },
        success: function (result) {
          if (result.code == 0) {
			  vm.$data.tabOneList = result.data
            console.log(result, this.tabOneList);
          }
        },
        error: function () {},
      });
    },
    // 查看
    add() {
      console.log(this.optionActive, this.time);
    },
    // 初始化时间
    setTime() {
      // 当前时间
      const nwo = new Date();
      // 当前月份
      const nwoMonth = nwo.getMonth();
      // 当前年份
      const nwoYear = nwo.getFullYear();
      const y1 = nwo.getFullYear();
      if (nwoMonth == 0) {
        nwoMonth = 12;
        nwoYear = nwoYear - 1;
      }
      // 上月初
      const firstTime = new Date(nwoYear, nwoMonth - 1);
      const lastTime = new Date(y1, nwoMonth);
      const end = new Date();
      end.setTime(lastTime.getTime() - 3600 * 1000 * 24);
      this.time = [firstTime, end];
      console.log('time', firstTime, end);
    },
    query: function () {
      vm.reload();
    },
    // add: function () {
    //   vm.showList = false;
    //   vm.title = '新增';
    //   vm.roleList = {};
    //   vm.user = {
    //     deptName: null,
    //     deptId: null,
    //     status: 1,
    //     roleIdList: [],
    //     zoneId: null,
    //     gender: null,
    //   };

    //   //获取角色信息
    //   this.getRoleList();

    //   vm.getDept();
    // },
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
    reload: function () {
      vm.showList = true;
      var page = $('#jqGrid').jqGrid('getGridParam', 'page');
      $('#jqGrid')
        .jqGrid('setGridParam', {
          postData: {
            keyword: vm.q.keyword,
            zoneId: vm.q.zoneId,
          },
          page: page,
        })
        .trigger('reloadGrid');
    },
    // 1男 2女
    genderFormatter: function (cellvalue, options, rowdata) {
      switch (rowdata.gender) {
        case 1:
          return '<span class="label label-danger">男</span>';
          break;
        case 2:
          return '<span class="label label-success">女</span>';
          break;
        case 0:
          return '<span class="label label-warning">未知</span>';
          break;
        default:
          return rowdata.gender;
          break;
      }
    },
    // 审核状态 0审核中 1审核通过 2撤销 3未通过
    verifyStatusFormatter: function (cellvalue, options, rowdata) {
      switch (rowdata.verifyStatus) {
        case 0:
          return '<span class="label label-warning">审核中</span>';
          break;
        case 1:
          return '<span class="label label-success">审核通过</span>';
          break;
        case 2:
          return '<span class="label label-warning">撤销</span>';
          break;
        case 3:
          return '<span class="label label-danger">未通过</span>';
          break;
        default:
          return rowdata.verifyStatus;
          break;
      }
    },
    // 0禁用 1正常
    statusFormatter: function (cellvalue, options, rowdata) {
      switch (rowdata.status) {
        case 0:
          return '<span class="label label-danger">禁用</span>';
          break;
        case 1:
          return '<span class="label label-success">正常</span>';
          break;
        default:
          return rowdata.status;
          break;
      }
    },
    // 头像formatter
    avatarUrlFormatter: function (cellvalue, options, rowdata) {
      return (
        '<img src="' +
        rowdata.avatarUrl +
        '" id="img' +
        rowdata.id +
        '" style="width:30px;height:30px;" />'
      );
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

//审核  审核状态 0审核中 1审核通过 2撤销 3未通过
function verifyUser(id, verifyStatus) {
  $.ajax({
    type: 'post', //方法类型
    dataType: 'json',
    url: baseURL + '/user/user/verifyUser',
    // contentType: "application/json; charset=utf-8",
    contentType: 'application/x-www-form-urlencoded; charset=utf-8',
    // data: JSON.stringify({parentId:0}),
    data: {
      userId: id,
      verifyStatus: verifyStatus,
    },
    success: function (result) {
      if (result.code == 0) {
        alert('操作成功', function (index) {
          vm.reload();
        });
      } else {
        alert(result.msg);
      }
    },
    error: function () {},
  });
  vm.reload();
}
