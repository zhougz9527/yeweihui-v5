/*
 * @Author: xingjing
 * @Date: 2020-08-21 10:32:16
 * @LastEditors: xingjing
 * @LastEditTime: 2020-08-30 12:57:13
 * @Description: 9527
 */
var vm = new Vue({
  el: '#rrapp',
  data: {
    input: '',
    dt: '',
    wData: {},
    user: {},
    userList: [],
    currentPage: 1,
    pageSize: 5,
    total: 0,
    blance: {},
  },
  computed: {
    weaImgUrl() {
      if (this.wData && this.wData.wea_img) {
        return (
          'https://xuesax.com/tianqiapi/skin/pitaya/' +
          this.wData.wea_img +
          '.png'
        );
      } else {
        return null;
      }
    },
  },
  methods: {
    getBalance() {
      const self = this;
      $.ajax({
        type: 'GET',
        url: baseURL + 'accounts/reports/blance',
        contentType: 'application/json',
        success: function (r) {
          console.log(r);
          if (r.code === 0) {
            self.blance = r.data;
          }
          // if (r.code === 0) {
          //     self.userList = r.page.list;
          //     self.total = r.page.totalCount;
          // }
        },
      });
    },
    getWeather() {
      $.get(
        'https://www.tianqiapi.com/api?version=v6&appid=48136697&appsecret=sU3fDo0n',
        function (r) {
          vm.wData = r;
          // console.log(r);
        }
      );
    },
    hasClick() {
      if (this.blance.dredge === 0) {
        alert('该功能未开通，请联系管理员开通');
      } else if (this.blance.dredge === 1) {
        alert('当前小区无已封账的收支报表信息');
      } else {
        window.location.href = 'modules/operation/report.html';
      }
    },
    getUser() {
      $.getJSON('sys/user/info?_' + $.now(), function (r) {
        if (r.user && r.user.id) {
          $.get(baseURL + 'sys/user/infoMore/' + r.user.id, function (r2) {
            vm.user = r2.user;
          });
        } else {
          vm.$message.warning('获取用户失败');
        }
      });
    },
    getDate() {
      const d = new Date();
      var curr_date = d.getDate();
      var curr_month = d.getMonth() + 1;
      var curr_year = d.getFullYear();
      // String(curr_month).length < 2 ? (curr_month = "0" + curr_month): curr_month;
      // String(curr_date).length < 2 ? (curr_date = "0" + curr_date): curr_date;
      this.dt = curr_year + '年' + curr_month + '月' + curr_date + '日';
    },
    handleSizeChange(val) {
      vm.pageSize = val;
      vm.getUserList();
    },
    handleCurrentChange(val) {
      vm.currentPage = val;
      vm.getUserList();
    },
    getUserList() {
      const self = this;
      $.ajax({
        type: 'GET',
        url:
          baseURL +
          'sys/user/list?' +
          `page=${self.currentPage}&limit=${self.pageSize}&order=asc`,
        contentType: 'application/json',
        success: function (r) {
          if (r.code === 0) {
            self.userList = r.page.list;
            self.total = r.page.totalCount;
          }
        },
      });
    },
    // 获取上月登录次数和时长
    getTime() {
      const self = this;
      $.ajax({
        type: 'get',
        url: 'jmkj/myData',
        contentType: 'application/json',
        success: function (r) {
          if (r.code === 0) {
              console.log('r=========',r);
          }
        },
      });
    },
  },
  mounted() {
    this.getDate();
    this.getUser();
    this.getUserList();
    this.getWeather();
    this.getBalance();
    this.getTime()
  },
});

function linkTo(title, url) {
  parent.document.getElementById('ifcontent').src = url;
  const stateObject = {};
  const newUrl = parent.window.location.href + '#' + url;
  parent.history.pushState(stateObject, title, newUrl);

  // var a = document.createElement('a');
  // a.setAttribute('href', url);
  // // a.setAttribute('target', '_blank');
  // a.setAttribute('id', 'js_a');
  // //防止反复添加
  // if(document.getElementById('js_a')) {
  //     document.body.removeChild(document.getElementById('js_a'));
  // }
  // // document.body.appendChild(a);
  // a.click();
}
