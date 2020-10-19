//生成菜单
var menuItem = Vue.extend({
  name: 'menu-item',
  props: { item: {} },
  template: [
    '<li>',
    '	<a v-if="item.type === 0" href="javascript:;">',
    '		<i v-if="item.icon != null" :class="item.icon"></i>',
    '		<span>{{item.name}}</span>',
    '		<i class="fa fa-angle-left pull-right"></i>',
    '	</a>',
    '	<ul v-if="item.type === 0" class="treeview-menu">',
    '		<menu-item :item="item" v-for="item in item.list"></menu-item>',
    '	</ul>',

    '	<a v-if="item.type === 1 && item.parentId === 0" :href="\'#\'+item.url">',
    '		<i v-if="item.icon != null" :class="item.icon"></i>',
    '		<span>{{item.name}}</span>',
    '	</a>',

    '	<a v-if="item.type === 1 && item.parentId != 0" :href="\'#\'+item.url"><i v-if="item.icon != null" :class="item.icon"></i><i v-else class="fa fa-circle-o"></i> {{item.name}}</a>',
    '</li>',
  ].join(''),
});

//iframe自适应
$(window)
  .on('resize', function () {
    var $content = $('.content');
    $content.height($(this).height() - 155);
    $content.find('iframe').each(function () {
      $(this).height($content.height());
    });
  })
  .resize();

//注册菜单组件
Vue.component('menuItem', menuItem);

var vm = new Vue({
  el: '#rrapp',
  data: {
    timer: null,
    user: {},
    menuList: {},
    main: 'main.html',
    password: '',
    newPassword: '',
    navTitle: '控制台',
    myInfoVisible: false,
    showVersion: false,
    pageInfo: {},
  },
  methods: {
    // 更新登录时长,每1分钟更新一次
    updateLoginNum() {
      $.ajax({
        type: 'get',
        url: 'jmkj/updateTime',
        data: {
          time: 60,
        },
        dataType: 'json',
        success: function (result) {
          console.log('更新时长', result);
          if (result.code == 0) {
          } else {
          }
        },
      });
    },
    getMenuList: function (event) {
      $.getJSON('sys/menu/nav?_' + $.now(), function (r) {
        // todo 隱藏某些菜單.
        // vm.menuList = r.menuList;
        let data3 = {
          menuId: 0,
          parentId: 0,
          name: '行业主管',
          orderNum: 0,
          type: 1,
          url: 'modules/director/director1.html',
        };
        vm.menuList = r.menuList.map((it) => {
          if (it.name === '用户管理') {
            let list = it.list;
            list.push(data3);
            return { ...it, list: list };
          }
        });
        let data = {
          icon: 'fa fa-cog',
          menuId: 0,
          parentId: 0,
          name: '履职数据',
          orderNum: 0,
          type: 1,
          url: 'modules/data/data.html',
        };
        // let data1 = {
        //   menuId: 0,
        //   parentId: 0,
        //   name: '行业主管',
        //   orderNum: 0,
        //   type: 1,
        //   url: 'modules/director/director.html',
        // };
        // let data2 = {
        //   menuId: 0,
        //   parentId: 0,
        //   name: '版本信息',
        //   orderNum: 0,
        //   type: 1,
        //   url: 'modules/version/version.html',
        // };
        vm.menuList.push(data);
        // vm.menuList.push(data1);
        // vm.menuList.push(data2);
        // vm.menuList.push(data3);
        console.log(vm.menuList);
      });
    },
    getUser: function () {
      $.getJSON('sys/user/info?_' + $.now(), function (r) {
        vm.user = r.user;
      });
    },
    getTaskInfo() {
      $.getJSON('sys/index/mainPage1?_' + $.now(), function (r) {
        vm.pageInfo = r.mainPage1;
      });
    },
    showMyInfo() {
      this.myInfoVisible = true;
    },
    setShowVersion() {
      this.showVersion = true;
    },
    updatePassword: function () {
      layer.open({
        type: 1,
        skin: 'layui-layer-molv',
        title: '修改密码',
        area: ['550px', '270px'],
        shadeClose: false,
        content: jQuery('#passwordLayer'),
        btn: ['修改', '取消'],
        btn1: function (index) {
          var data =
            'password=' + vm.password + '&newPassword=' + vm.newPassword;
          $.ajax({
            type: 'POST',
            url: 'sys/user/password',
            data: data,
            dataType: 'json',
            success: function (result) {
              if (result.code == 0) {
                layer.close(index);
                layer.alert('修改成功', function (index) {
                  location.reload();
                });
              } else {
                layer.alert(result.msg);
              }
            },
          });
        },
      });
    },
    donate: function () {
      layer.open({
        type: 2,
        title: false,
        area: ['806px', '467px'],
        closeBtn: 1,
        shadeClose: false,
        content: ['http://cdn.renren.io/donate.jpg', 'no'],
      });
    },
  },
  created: function () {
    this.getMenuList();
    this.getUser();
  },
  mounted() {
    let that = this;
    let timer = setInterval(() => {
      that.updateLoginNum();
      console.log('更新登录时间');
    }, 60000);
    this.getTaskInfo();
  },
  updated: function () {
    //路由
    var router = new Router();
    routerList(router, vm.menuList);
    router.start();
  },
});

function routerList(router, menuList) {
  for (var key in menuList) {
    var menu = menuList[key];
    if (menu.type == 0) {
      routerList(router, menu.list);
    } else if (menu.type == 1) {
      router.add('#' + menu.url, function () {
        var url = window.location.hash;

        //替换iframe的url
        vm.main = url.replace('#', '');

        //导航菜单展开
        $('.treeview-menu li').removeClass('active');
        $("a[href='" + url + "']")
          .parents('li')
          .addClass('active');

        vm.navTitle = $("a[href='" + url + "']").text();
      });
    }
  }
}
