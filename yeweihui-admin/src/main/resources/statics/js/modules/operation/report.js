/*
 * @Author: xingjing
 * @Date: 2020-08-22 12:27:43
 * @LastEditors: xingjing
 * @LastEditTime: 2020-09-23 10:36:13
 * @Description: 9527
 */
$(function () {

    //获取小区列表
    // vm.getZonesList();
    // vm.initData();
    vm.getMonthStatistical();  // 月度

    // $('#upload-file-form').change(function () {
    //     importExcel();
    // });
    // vm.pieWidth = document.getElementById("pieContent").offsetWidth; 


});

var vm = new Vue({
    el: '#rrapp',
    data: {
        showList: true,
        showedit: false,
        showListedit: true,
        value3: '',
        title: null,
        monthBlance: {},
        yearBlance: {},
        cellList: {},
        value2: "",
        name: "",
        bankname: "",
        cardnumber: "",
        tableline6: '',
        subjecttitle: '',
        fileInfosList: [],
        url: '',
        reportsdetails: [],
        isNull: 0,
        isNullyear: 0,
        q: {
            zoneId: null,
            groupId: null,
            voteTitle: null,
            creatDate: null,
            yearDate: null,
            yeardetails: null,
            monthdetails: null,
        },
        pickerOptions0: {
            disabledDate(time) {
                return time.getTime() > Date.now() - 8.64e6
            }
        }
    },
    created() {

    },
    mounted() {
        this.getYearStatistical();   // 年度
        this.getCellList(); // 查询小区信息
    },
    methods: {
        clickEnlarge(id) {
            window.open(id)
        },
        changeMonth() {
            vm.getMonthDetails();
            vm.getFile();
        },
        changeYear() {
            console.log(new Date().getFullYear())
            vm.getYearDetails();
        },
        chagnetime() {
            this.getMonthStatistical(this.q.creatDate)
        },
        yearchange() {
            this.getYearStatistical(this.q.yearDate);   // 年度
        },
        modify() {
            let that = this;
            //   修改用户信息 
            $.ajax({
                type: 'POST',
                url: baseURL + `accounts/account/saveOrUpdate`,
                contentType: 'application/json',
                data: JSON.stringify({
                    name: this.name,
                    bankname: this.bankname,
                    cardnumber: this.cardnumber,
                }),
                success: function (r) {
                    if (r.code === 0) {
                        that.goback();
                        that.getCellList();
                        that.$message.success('操作成功');
                    } else {
                        that.$message.warning(r.msg);
                    }
                }
            }
            );
        },
        getCellList() {
            //   查询小区信息
            let that = this;
            //  月度报表
            $.ajax({
                type: 'GET',
                url: baseURL + 'accounts/account/search',
                contentType: "application/json",
                success: function (r) {
                    if (r.code === 0) {
                        if (r.data != null) {
                            that.cellList = r.data;
                            that.bankname = r.data.bankname;
                            that.name = r.data.name;
                            that.cardnumber = r.data.cardnumber;
                        }
                    } else {
                        that.$message.warning(r.msg)
                    }
                }
            })
        },
        getMonthStatistical(time) {
            let that = this;
            //  月度报表
            $.ajax({
                type: 'GET',
                url: baseURL + `accounts/reports/monthStatistical?statisticalDate=${time ? time : ''}`,
                contentType: "application/json",
                success: function (r) {
                    if (r.code === 0) {
                        that.monthBlance = r.data;
                        vm.q.creatDate = (r.data.statisticalDate == null)?"":r.data.statisticalDate.substring(0, 7)
                        vm.isNull = r.data.isNull;
                        if (vm.isNull == 1) {
                        	vm.$message.warning('该月份财务未提交报表，请联系财务或等待财务提交');
                        }
                        let operationsCharts = r.data.operations_disbursements_charts;
                        let operations = r.data.operations_receipts_charts;
                        let deposit = r.data.deposit_receipts_charts;
                        let disbursements = r.data.deposit_disbursements_charts;
                        // 月度报表
                        let operatingExpenses = echarts.init(document.getElementById(`main1`));  // 经营支出
                        let myCharts = echarts.init(document.getElementById(`main2`));
                        let myChart3 = echarts.init(document.getElementById(`main3`));
                        let myChart4 = echarts.init(document.getElementById(`main4`));

                        // 绘制图表经营支出
                        const pieTitle1 = { name: '经营支出' };
                        const pieTitle2 = { name: '经营收入' };
                        const pieTitle3 = { name: '押金收入' };
                        const pieTitle4 = { name: '押金支出' };
                        operatingExpenses.setOption(getOptionMothed(pieTitle1, operationsCharts));
                        myCharts.setOption(getOptionMothed(pieTitle2, operations))
                        myChart3.setOption(getOptionMothed(pieTitle3, deposit))
                        myChart4.setOption(getOptionMothed(pieTitle4, disbursements))
                    } else {
                        that.$message.warning(r.msg)
                    }
                }
            })

        },
        getYearStatistical(time) {
            let that = this;
            // 年度报表
            $.ajax({
                type: 'GET',
                url: baseURL + `accounts/reports/yearStatistical?statisticalDate=${time ? time : ''}`,
                contentType: "application/json",
                success: function (r) {
                    if (r.code === 0) {
                        that.yearBlance = r.data;
                        vm.q.yearDate = (r.data.statisticalDate == null)?"":(r.data.statisticalDate).toString();
                        vm.isNullyear = r.data.isNull;
                        if (vm.isNullyear == 1) {
                        	vm.$message.warning('该年度未提交报表，请联系财务或等待财务提交');
                        }
                        let operationsCharts = r.data.operations_disbursements_charts;
                        let operations = r.data.operations_receipts_charts;
                        let deposit = r.data.deposit_receipts_charts;
                        let disbursements = r.data.deposit_disbursements_charts;

                        // 月度报表
                        let operatingExpenses = echarts.init(document.getElementById(`main5`));  // 经营支出
                        let myCharts = echarts.init(document.getElementById(`main6`));
                        let myChart3 = echarts.init(document.getElementById(`main7`));
                        let myChart4 = echarts.init(document.getElementById(`main8`));

                        // 绘制图表经营支出
                        const pieTitle1 = { name: '经营支出' };
                        const pieTitle2 = { name: '经营收入' };
                        const pieTitle3 = { name: '押金收入' };
                        const pieTitle4 = { name: '押金支出' };
                        operatingExpenses.setOption(getOptionMothed(pieTitle1, operationsCharts));
                        myCharts.setOption(getOptionMothed(pieTitle2, operations))
                        myChart3.setOption(getOptionMothed(pieTitle3, deposit))
                        myChart4.setOption(getOptionMothed(pieTitle4, disbursements))
                    } else {
                        that.$message.warning(r.msg)
                    }
                }
            })
        },
        getDetails() {
            //    详情
            layer.open({
                type: 1,
                title: false,
                content: $("#tableDetails"),
                closeBtn: 0, //不显示关闭按钮
                area: ['500px',], //宽高
                closeBtn: true,
                // yes: function (index) {
                //     layer.close(index)
                // }
            });
        },
        onCopy() {
            //   复制
            let panelContent = $('#panelContent');
            let txt = panelContent[0].innerText;
            let btn = $('#btn').val(txt)
            btn.select();
            document.execCommand("Copy")
            alert('复制成功');
        },
        goback() {
            this.showList = true;
            this.showedit = false;
            this.showListedit = true;
        },
        onLookup() {
            // 编辑
            this.showList = false;
            this.showListedit = false;
        },
        edit(index) {
            // 编辑
        	console.log(vm.isNull)
            if (index === 0) {
                if (vm.isNull == 1) {
                	
                    this.$message.warning('该月份财务未提交报表，请联系财务或等待财务提交');
                } else {
                    vm.title = '月度报表';
                    vm.q.monthdetails = vm.q.creatDate;
                    vm.getMonthDetails();
                    vm.getFile();
                    this.showedit = true;
                    this.showList = false;
                }

            } else {
                if (vm.isNullyear == 1) {
                    this.$message.warning('该年度未提交报表，请联系财务或等待财务提交');
                } else {
                    vm.title = '年度报表'
                    vm.q.yeardetails = vm.q.yearDate;
                    vm.getYearDetails()
                    this.showedit = true;
                    this.showList = false;
                }
            }

        },
        getMonthDownload() {
            let date = vm.q.creatDate;
            viewPdfPrintDowload(date)
        },
        getYearDownload() {
            let date = vm.q.yearDate ? vm.q.yearDate : new Date().getFullYear();
            viewPdfPrintYeadrDowload(date)
        },
        //初始化数据
        getMonthDetails() {
            let that = this;
            $.get(baseURL + `accounts/reports/monthdetail?statisticalDate=${vm.q.monthdetails}`, function (r) {
                if (r.code === 0) {
                    if(r.isNull == 1){
                        that.$message.warning('该月份未提交报表，请联系财务或等待财务提交');
                    } else {
                        $('#pre').html(r.data)
                    }
                    // that.tableline6 = r.data;
                } else {
                    that.$message.warning(r.msg);
                }
            }
            )
        },
        getYearDetails() {
            let that = this;
            $.get(baseURL + `accounts/reports/yeardetail?statisticalDate=${vm.q.yeardetails}`, function (r) {
                if (r.code === 0) {
                    if(r.isNull == 1){
                        that.$message.warning('该年度未提交报表，请联系财务或等待财务提交');
                    } else {
                        $('#pre').html(r.data)
                    }
                    // that.tableline6 = r.data;
                } else {
                    that.$message.warning(r.msg);
                }
            }
            )
        },
        getFile() {
            let that = this;
            $.get(baseURL + `accounts/reports/getFileInfoVO?statisticalDate=${this.q.monthdetails}`, function (r) {
                if (r.code === 0) {
                    if (Object.keys(r.data).length === 0) {
                        that.$message.warning('未查询到对账单！')
                        that.fileInfosList = [];
                    } else {
                        that.fileInfosList = r.data.fileInfoVO.fileInfos;
                    }
                } else {
                    that.$message.warning(r.msg)
                }
            })
        },
        getPrinting(index) {
            console.log(index)
            if (index === 1) {
                let date = vm.q.creatDate;
                viewPdfPrint(date)
            } else {
                let date = vm.q.yearDate;
                viewPdfPrintYear(date)
            }
        },
        detailsDownload(text) {
            if (text === "月度报表") {
                let date = vm.q.monthdetails;
                viewPdfPrintDowload(date)
            } else {
                let date = vm.q.yeardetails;
                viewPdfPrintYeadrDowload(date)
            }
            // 下载
        },
        detailsPrinting(text) {
            if (text === "月度报表") {
                let date = vm.q.monthdetails;
                viewPdfPrint(date);
            } else {
                let date = vm.q.yeardetails;
                viewPdfPrintYear(date);
            }
            // 打印
        }
    }
})



// 月度报表
function getOptionMothed(pie, data) {
    let arr = data.length === 0 ? [] : data;
    let newData = [];
    arr.forEach(item => {
    	if(item.name != null && item.money!=null){
        let obj = {
            name: item['name'],
            value: item['money'],
        }
        newData.push(obj)
    	}
    })
    var dataStatus = (newData.length>0);
    const option = {
        title: {
            text: pie.name,
            left: 'center'
        },
        tooltip: {
            trigger: 'item',
            formatter: dataStatus?"{b}<br/>{c}元，{d}%":"{b}"
        },
        series: [
            {
                type: 'pie',
                radius: '30%',
                data: dataStatus ? newData : [{"value":0, "name":"无"+pie.name}],
                label: {
                    formatter: dataStatus?"{b}\r\n{c}元，{d}%":"{b}",
                    position: dataStatus?"":"inner"
                }
            },
        ]
    };
    return option
}


function getinFo() {
    //  默认月份
    const nowDate = new Date();
    const year = nowDate.getFullYear();
    const month = nowDate.getMonth() + 1 < 10 ? "0" + (nowDate.getMonth() + 1) : nowDate.getMonth() + 1;
    const datestr = year + '-' + month;
    return datestr
}

function onView() { // type ： 1 ->浏览  2->上传线下结果
    vm.showList = false;
}

// 拼接option
function getVoteOptions(data) {
    let result = '[';
    for (let i = 0, len = data.length; i < len; i++) {
        if (i === len - 1) {
            result += `\"${data[i]}\"]`
        } else {
            result += `\"${data[i]}\",`
        }
    }
    return result
}

function getDetail(a, b, c) {
    $.get(baseURL + `accounts/reports/subjectDetail?statisticalDate=${c}&subjectId=${b}`, function (r) {
        if (r.code === 0) {
            vm.subjecttitle = a;
            vm.reportsdetails = r.data.reports;
            if (r.data.reports.length > 0) {
                setTimeout(function () {
                    layer.open({
                        type: 1,
                        title: false,
                        content: $("#tableDetails").html(),
                        closeBtn: 0, //不显示关闭按钮
                        area: ['500px', '200px'], //宽高
                        closeBtn: true,
                    });
                }, 1000)
            }
        } else {

        }

    })
    // console.log(a, b, c)
    // alert(a, b, c)
}

function viewPdfPrintDowload(date) {
    layer.open({
        type: 1,
        title: false,
        content: $("#tipcontent").html(),
        closeBtn: 0, //不显示关闭按钮
        area: ['300px', '150px'], //宽高
        btn: ['EXCEL', 'PDF'],
        btn1: function (index) {
            layer.close(index)
            window.open(baseURL + `accounts/reports/monthdetailexportExcel/${date}`)
        },
        btn2: function (index) {
            layer.close(index)
            window.open(baseURL + `accounts/reports/monthdetailviewPdf/${date}`)
        }
    });
}

function viewPdfPrintYeadrDowload(date) {
    layer.open({
        type: 1,
        title: false,
        content: $("#tipcontent").html(),
        closeBtn: 0, //不显示关闭按钮
        area: ['300px', '150px'], //宽高
        btn: ['EXCEL', 'PDF'],
        btn1: function (index) {
            layer.close(index)
            window.open(baseURL + `accounts/reports/yeardetailexportExcel/${date}`)
        },
        btn2: function (index) {
            layer.close(index)
            window.open(baseURL + `accounts/reports/yeardetailviewPdf/${date}`)
        }
    });
}

//打印pdf月度
function viewPdfPrint(id) {
    vm.$confirm('是否打印pdf?', '提示', {
        confirmButtonText: '是',
        cancelButtonText: '不需要',
        distinguishCancelAndClose: true,
        type: 'info'
    }).then(() => {
        window.open(baseURL + `accounts/reports/monthdetailviewPdf/${id}`);
    }).catch((action) => {
        console.log(action)
        if (action === 'cancel') {
            //             window.open(baseURL + `operation/announce/viewPdf/${id}/no`);
        }
    });
}


//打印pdf年度
function viewPdfPrintYear(id) {
    vm.$confirm('是否打印pdf', '提示', {
        confirmButtonText: '是',
        cancelButtonText: '不需要',
        distinguishCancelAndClose: true,
        type: 'info'
    }).then(() => {
        window.open(baseURL + `accounts/reports/yeardetailviewPdf/${id}`);
    }).catch((action) => {
        console.log(action)
        if (action === 'cancel') {
            //             window.open(baseURL + `operation/announce/viewPdf/${id}/no`);
        }
    });
}

