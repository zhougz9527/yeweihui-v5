/*
 * @Author: xingjing
 * @Date: 2020-08-24 13:37:11
 * @LastEditors: xingjing
 * @LastEditTime: 2020-09-21 20:37:29
 * @Description: 9527
 */
let arr = [];
let pages = 1;
$(function () {
    function addCellAttr(rowId, val, rawObject, cm, rdata) {
        if (rawObject.recordStatus == 1) {
            return "style='color:red'";
        }
    }
    $("#jqGrid").jqGrid({
        url: baseURL + 'accounts/financialinform/list',
        datatype: "json",
        postData: {
            accountsId: "12",		//账簿ID
            tagNumber: "",		//记字号数字
            projectId: null,		//科目ID
            date: "",			//凭证日期
            page: 1,			//页码
            limit: 5			//每页数量
        },
        colModel: [
            { label: 'id', name: 'id', index: 'id', width: 50, key: true, cellattr: addCellAttr },

            { label: '日期', name: 'date', index: 'date_id', width: 50, cellattr: addCellAttr },
            { label: '凭证字号', name: 'tagNumber', index: 'zone_id', width: 80, cellattr: addCellAttr },
            { label: '摘要', name: 'digest', index: 'uid', width: 80, cellattr: addCellAttr },
            { label: '科目', name: 'accountsSubject.name', index: 'num', width: 80, cellattr: addCellAttr },
            { label: '小区id', name: 'id', index: 'zone_id', width: 80, hidden: true },
            { label: '辅助账', name: 'auxiliary', index: 'auxiliary_id', width: 80, cellattr: addCellAttr },
            { label: '借方', name: 'debit', index: 'no_num', width: 80, cellattr: addCellAttr },
            { label: '货方', name: 'credit', index: 'quit_num', width: 80, cellattr: addCellAttr },
            { label: '附件张数', name: 'accessory.fileInfos.length', index: 'vote_item', width: 80, cellattr: addCellAttr },

            { label: '制单人', name: 'makeUsername', index: 'zone_name', width: 80, cellattr: addCellAttr },
            { label: '审核人', name: 'auditor', index: 'realname', width: 80, cellattr: addCellAttr },
            { label: '单据', name: 'title', index: 'title', width: 80, formatter: vm.btnFormatter },
        ],
        onSelectRow: function (id) {
            if (arr.indexOf(id) == -1) {
                arr.push(id)
            } else {
                arr.splice(arr.indexOf(id), 1)
            }
        },
        onPaging: function (page) {
            if (page === "next") {
                pages += 1
            } else {
                pages--
            }
            console.log(pages)
        },
        viewrecords: true,
        height: 630,
        rowNum: 10,
        rowList: [10, 30, 50],
        rownumbers: true,
        rownumWidth: 25,
        autowidth: true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader: {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames: {
            page: "page",
            rows: "limit",
            order: "order"
        },
        gridComplete: function () {
            //隐藏grid底部滚动条
            // $("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
        }
    });

    //获取小区列表
    // vm.getZonesList();
})

var vm = new Vue({
    el: '#rrapp',
    data: {
        admin: false,
        zonesOptions: [],
        ab: [],
        options: [],
        subsidiary: '',
        subjectId: '',
        q: {
            id: null,
            groupId: null,
            voteTitle: '',
            creatDate: null,
            select: null,
            value1: [],
            number: '',
        },
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
    created: function () {
        this.getSubject()
    },
    methods: {
        query: function () {
            // vm.reload();
            console.log(vm.q.zoneId)
            vm.reload();
        },
        changesope() {
            console.log(vm.q.value1)
        },
        reload() {
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                postData: {
                    startDate: vm.q.value1[0],
                    endDate: vm.q.value1[1],
                    digest: vm.q.voteTitle,
                    auxiliary: vm.subsidiary,
                    subjectId: vm.subjectId,
                    tagNumber: vm.q.number
                },
                page: page
            }).trigger("reloadGrid");
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
        exportPdf() {
            viewPdfPrint();
        },
        exportExcel() {
            viewExcelPrint();
        },
        changSelect(e) {
            let a = e.length;
            vm.subjectId = e[a - 1];
        },
        //按钮格式化
        btnFormatter: function (cellvalue, options, rowdata) {
            const a = rowdata.accessory.fileInfos;
            var result = "";
            a.map((value, index) => {
                result += '<a href="' + value.url + '" target="_blank" style="font-size:12px;color:blue" onclick="onView()">' + value.name + '</a>';
            })
            return result;
        },
    },
})

function onView(index) {
    console.log(index)
    // window.open(baseURL + vm.ab[index])
    // window.open(baseURL + url)
}

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

// pdf 
function viewPdfPrint(id) {
    vm.$confirm('是否导出pdf?', '提示', {
        confirmButtonText: '是',
        cancelButtonText: '不需要',
        distinguishCancelAndClose: true,
        type: 'info'
    }).then(() => {
        window.open(baseURL + `accounts/financialinform/financialinformviewPdf?startDate=${vm.q.value1.length === 0 ? '' : vm.q.value1[0]}&endDate=${vm.q.value1.length === 0 ? '' : vm.q.value1[1]}&subjectId=${vm.subjectId}&auxiliary=${vm.subsidiary}&digest=${vm.q.voteTitle}&tagNumber=${vm.q.number}&limit=10&page=${pages}`);
    }).catch((action) => {
        console.log(action)
        if (action === 'cancel') {
            //             window.open(baseURL + `operation/announce/viewPdf/${id}/no`);
        }
    });
}

// pdf 
function viewExcelPrint(id) {
    vm.$confirm('是否导出Excel?', '提示', {
        confirmButtonText: '是',
        cancelButtonText: '不需要',
        distinguishCancelAndClose: true,
        type: 'info'
    }).then(() => {
        window.open(baseURL + `accounts/financialinform/exportExcel?startDate=${vm.q.value1.length === 0 ? '' : vm.q.value1[0]}&endDate=${vm.q.value1.length === 0 ? '' : vm.q.value1[1]}&subjectId=${vm.subjectId}&auxiliary=${vm.subsidiary}&digest=${vm.q.voteTitle}&tagNumber=${vm.q.number}&limit=10&page=${pages}`);
    }).catch((action) => {
        console.log(action)
        if (action === 'cancel') {
            //             window.open(baseURL + `operation/announce/viewPdf/${id}/no`);
        }
    });
}
