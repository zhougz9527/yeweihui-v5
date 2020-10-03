/*
 * @Author: xingjing
 * @Date: 2020-08-27 08:51:22
 * @LastEditors: xingjing
 * @LastEditTime: 2020-09-15 21:51:06
 * @Description: 9527
 */
$(function () {

})

var vm = new Vue({
    el: '#rrapp',
    data: {
        showList: true,
        active: 1,
        tableData6: [],
        totalPages: 1,
        options: [],
        off: 0,
        printingArray: [],
        accounts: {
            infoByUseId: '',
            start_date: '',
        },
        report: {
            operationsReceipts: null,
            operationsDisbursements: null,
            depositBlance: null,
            operationsBlance: null,   //已录入经营结余
            depositReceipts: null,    //已录入押金收入
            depositDisbursements: null,	 	//已录入押金支出
        },
        q: {
            zoneId: null,
            groupId: null,
            voteTitle: '',
            creatDate: null,
            select: null

        },
        subjectId: null,
        //日期
        pickerOptions: {
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
    },
    created() {
        this.getSubject()
        this.statistics()
        this.infoByUse()
    },
    mounted() {
        this.getData();
    },
    methods: {
        changSelect(e) {
            let a = e.length;
            vm.subjectId = e[a - 1];
        },
        getSubject: function () {
            //   全部科目
            let that = this;
            $.get(baseURL + `accounts/subject/list`, function (r) {
                if (r.code === 0) {
                    options(r.subjects)
                } else {
                    that.$message.warning(r.msg);
                }
            })
        },
        getUrl(url) {
            window.open(url)
        },
        changPage(val) {
            vm.off = 1;
            this.getData(val);
        },
        handleSelectionChange(val) {
            console.log(val)
            flerId(val)
            this.multipleSelection = val;
        },
        changbg(index) {
            console.log(this.active)
            this.active = index
            if (index === 1) {
                this.getData()
            } else if (index === 2) {
                viewPdfPrintYeadrDowload(this)
            } else if (index === 3) {
                if (vm.printingArray.length === 0) {
                    alert('先勾选要打印的凭证！')
                } else {
                    viewPdfPrint()
                }
            } else if (index === 4) {
                if (vm.printingArray.length === 0) {
                    alert('先勾选要导出的凭证！')
                } else {
                    viewPdfPrintPdf()
                }
            }
        },
        reload() {
            this.getData()
        },
        getData(val) {
            let that = this;
            $.ajax({
                type: 'POST',
                url: baseURL + `accounts/voucher/list`,
                data: JSON.stringify({
                    accountsId: "3",
                    tagNumber: that.q.voteTitle,
                    subjectIds: that.subjectId,
                    date: that.q.creatDate ? that.q.creatDate : '',
                    page: this.off === 0 ? that.totalPages : val,
                    limit: 5
                }),
                contentType: "application/json",
                success: function (r) {
                    if (r.code === 0) {
                        that.tableData6 = r.page.list;
                        that.totalPages = r.page.totalPage;
                    } else {
                        that.$message.warning(r.msg);
                    }
                }
            })
        },
        statistics() {
            let that = this;
            $.get(baseURL + `accounts/reports/recorded?statisticalDate=${that.q.creatDate ? that.q.creatDate : getinFo()}`, function (r) {
                if (r.code === 0) {
                    that.report = r.data
                } else {
                    that.$message.warning(r.msg)
                }
            })
        },
        infoByUse() {
            // 获取记账 id  
            let that = this;
            $.get(baseURL + 'accounts/accounts/infoByUse', function (r) {
                if (r.code === 0) {
                    that.accounts.infoByUseId = r.accounts.id;
                    that.accounts.start_date = r.accounts.startDate;
                } else {
                    that.$message.warning(r.msg)
                }
            })
        }
    }
})

function options(data) {
    let key = "label";
    let value = "value";
    for (let index in data) {
        let newdata = data[index].children;
        data[index][key] = data[index]['name'];
        data[index][value] = data[index]['id'];
        delete data[index]['name'];
        delete data[index]['id'];
        for (let j in newdata) {
            newdata[j][key] = newdata[j]['name'];
            newdata[j][value] = newdata[j]['id'];
            delete newdata[j]['id'];
        }
    }
    vm.options = data;
    console.log(data)
}

function viewPdfPrintYeadrDowload(name) {
    let that = name;
    layer.open({
        type: 1,
        title: false,
        content: $("#tipcontent").html(),
        closeBtn: 0, //不显示关闭按钮
        area: ['300px', '150px'], //宽高
        btn: ['审核通过', '不通过'],
        btn1: function (index) {
            layer.close(index)
            $.get(baseURL + 'accounts/accounts/submitAuditResult?result=true', function (r) {
                if (r.code === 0) {
                    that.$message.success('操作成功')
                } else {
                    that.$message.warning(r.msg)
                }
            })
        },
        btn2: function (index) {
            layer.close(index)
            $.get(baseURL + 'accounts/accounts/submitAuditResult?result=false', function (r) {
                if (r.code === 0) {
                    that.$message.success('操作失败')
                } else {
                    that.$message.warning(r.msg)
                }
            })
        }
    });
}

function getinFo() {
    //  默认月份
    const nowDate = new Date();
    const year = nowDate.getFullYear();
    const month = nowDate.getMonth() + 1 < 10 ? "0" + (nowDate.getMonth() + 1) : nowDate.getMonth() + 1;
    const datestr = year + '-' + month;
    return datestr
}

function flerId(arr) {
    let arrId = [];
    for (index in arr) {
        arrId.push(arr[index].id)
    }
    vm.printingArray = arrId
}

function viewPdfPrint(arr) {
    vm.$confirm('是否需要打印pdf?', '提示', {
        confirmButtonText: '是',
        cancelButtonText: '不需要',
        distinguishCancelAndClose: true,
        type: 'info'
    }).then(() => {
        console.log(vm.printingArray)
        window.open(baseURL + `accounts/voucher/viewPdf/${vm.printingArray.toString()}`);
    }).catch((action) => {
        console.log(action)
        if (action === 'cancel') {
            //             window.open(baseURL + `operation/announce/viewPdf/${id}/no`);
        }
    });
}

function viewPdfPrintPdf(arr) {
    vm.$confirm('是否需要导出pdf?', '提示', {
        confirmButtonText: '是',
        cancelButtonText: '不需要',
        distinguishCancelAndClose: true,
        type: 'info'
    }).then(() => {
        console.log(vm.printingArray)
        window.open(baseURL + `accounts/voucher/viewPdf/${vm.printingArray.toString()}`);
    }).catch((action) => {
        console.log(action)
        if (action === 'cancel') {
            //             window.open(baseURL + `operation/announce/viewPdf/${id}/no`);
        }
    });
}

