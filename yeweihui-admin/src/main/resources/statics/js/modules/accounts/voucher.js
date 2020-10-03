$(function () {

    //格式化凭证信息
    function FormatVoucherInfo(accountsVoucher) {
        accountsVoucher.accountsFinancialinforms.forEach(element => {

            element["moneyInfo"] = SplitMoney(element.money);
            var subjectIds = [];
            if (element.accountsSubject.parentId > 0) {
                subjectIds[0] = element.accountsSubject.parentId;
                subjectIds[1] = element.subjectId;
            } else {
                subjectIds[0] = element.subjectId;
            }

            element["subjectIds"] = subjectIds;

        });
    }
    //拆分金额
    function SplitMoney(money) {

        var splitResult = {};

        var numInfo = money.toString().split(".");
        var integerStr = numInfo[0];
        var decimalStr = "";
        if (numInfo.length > 1) {
            decimalStr = numInfo[1];
        }

        integerStr = integerStr.padStart(9, " ");
        decimalStr = decimalStr.padEnd(2, " ");

        var integerStrArray = integerStr.split("");
        var decimalStrArray = decimalStr.split("");

        isFindNumber = false;
        for (var i = integerStrArray.length; i > 0; i--) {
            var value = integerStrArray[integerStrArray.length - i].trim();
            if(!isFindNumber && value.length > 0 && value != "0"){
                isFindNumber = true;
            }
            splitResult["n_" + "0".padStart(i + 2, "0")] = (value == "0" || value == "")?isFindNumber?"0":"":value;
        }
        for (var i = decimalStr.length; i > 0; i--) {
            var value = decimalStr[decimalStr.length - i].trim();
            if(!isFindNumber && value.length > 0 && value != "0"){
                isFindNumber = true;
            }
            splitResult["n_" + "0".padStart(i, "0")] = (value == "0" || value == "")?isFindNumber?"0":"":value;
        }
        return splitResult;
    }
    //合并金额
    function MergeMoney(moneyInfo) {

        var moneyStr = "";
        for (var i = 11; i > 0; i--) {
            var numInfo = moneyInfo["n_" + "0".padEnd(i, "0")];
            if (i == 2) {
                moneyStr += ".";
            }
            if (numInfo == undefined || numInfo == null || numInfo.toString().trim().length == 0) {
                moneyStr += "0";
            }
            else {
                moneyStr += numInfo;
            }
        }
        return parseFloat(moneyStr).toFixed(2);
    }
    //根据ID获取科目ID
    function getSubjectObject(id, subjectArray) {
        if (subjectArray == null || subjectArray == undefined) {
            return null;
        }
        for (var i = 0; i < subjectArray.length; i++) {
            if (subjectArray[i].id == id) {
                return subjectArray[i];
            }
            if (subjectArray[i].children != null && subjectArray[i].children.length > 0) {
                var item = getSubjectObject(id, subjectArray[i].children);
                if (item != null) {
                    return item;
                }
            }
        }
        return null;
    }
    //获取url参数
    function getQueryString(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) return unescape(r[2]);
        return null;
    }
    //获取当天日期
    function CurentDate() {
        var now = new Date();

        var year = now.getFullYear();       //年
        var month = now.getMonth() + 1;     //月
        var day = now.getDate();            //日

        var clock = year + "-";

        if (month < 10)
            clock += "0";

        clock += month + "-";

        if (day < 10)
            clock += "0";

        clock += day;
        return (clock);
    }
    //日期字符串转对象：yyyy-MM-dd
    function stringToDate(dateStr) {
        var tempStrs = dateStr.split(" ");
        var dateStrs = tempStrs[0].split("-");
        var year = parseInt(dateStrs[0], 10);
        var month = parseInt(dateStrs[1], 10) - 1;
        var day = parseInt(dateStrs[2], 10);
        var date = new Date(year, month, day);
        return date;

    }
    //验证是否为当月日期
    function verifyCurrentMonth(targetDate) {
        var date = new Date();
        var firstDay = new Date(date.getFullYear(), date.getMonth(), 1);
        var lastDay = new Date(date.getFullYear(), date.getMonth() + 1, 0);

        return targetDate.getTime() >= firstDay.getTime() && targetDate.getTime() <= lastDay.getTime();
    }
    //验证是否为编辑模式
    function verifyEdit(voucher, accounts) {

        if (accounts != null) {
            if (accounts.status == 0 || accounts.status == 2) {
                if (voucher != null) {
                    if (
                        (!voucher.id || voucher.id == undefined) ||
                        verifyCurrentMonth(stringToDate(voucher.date))
                    ) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    //获取凭证信息
    function getVoucher(id) {
        return axios.get(
        		baseURL + 'accounts/voucher/info',
            {
                params: {
                    id: id
                }
            }
        );
    }
    //获取科目信息
    function getSubjects() {
        return axios.get(baseURL + 'accounts/subject/list');
    }
    //获账簿信息
    function getAccounts(id) {
        if (id != null && id != undefined && id.trim().length > 0) {
            return axios.post(baseURL + 'accounts/accounts/info',
            {
                params: {
                    id: id
                }
            });
        }
        else {
            return axios.get(baseURL + 'accounts/accounts/infoByUse');
        }
    }
    //获取凭证记字号数字
    function getTagNumber(accountsId) {
        return axios.get(baseURL + 'accounts/voucher/nextTagNumber',
           {
            params: {
            	accountsId: accountsId
            }
        });
    }
    //获取用户信息
    function getUser() {
        return axios.get(baseURL + 'sys/user/info');
    }

    startLoad();
    //加载起始方法
    function startLoad() {
        var voucherId = getQueryString("id");
        var requestMethod = [];
        if (voucherId != null) {
            requestMethod[0] = getVoucher(voucherId);
            requestMethod[1] = getSubjects();
            requestMethod[2] = null;
            requestMethod[3] = null;
            requestMethod[4] = null;
        }
        else {
            requestMethod[0] = null;
            requestMethod[1] = getSubjects();
            requestMethod[2] = getAccounts(null);
            requestMethod[3] = getUser();
            requestMethod[4] = null;
        }

        axios.all(requestMethod)
            .then(axios.spread(function (voucher, subjects, accounts, user, tagNumber) {
                if (voucher == null) {
                	axios.all([getTagNumber(accounts.data.accounts.id)])
                    .then(axios.spread(function (tagNumber) {
                    	var accountsVoucher = {
                                "accountsId": accounts.data.accounts.id,
                                "date": CurentDate(),
                                "tagNumber": tagNumber.data.nexttagNumber,
                                "makeUsername": user.data.user.realname,
                                "accountsFinancialinforms": [
                                    {
                                        "digest": "",
                                        "subjectId": 0,
                                        "accountsSubject": {
                                            "rdtype": 0,
                                            "parentId": 0
                                        },
                                        "auxiliary": "",
                                        "money": 0.00,
                                        "accessory": {
                                            "fileInfos": []
                                        }
                                    }
                                ]
                            }
                            loadVue(accounts.data.accounts, accountsVoucher, subjects.data.subjects);
                    }));
                }
                else {
                    axios.all([getAccounts(null)])
                        .then(axios.spread(function (accounts) {
                            loadVue(accounts.data.accounts, voucher.data.accountsVoucher, subjects.data.subjects);
                        }));
                }
            }));
    }



    var Main = null;
    function loadVue(accounts, accountsVoucher, subjects) {

        accountsVoucher["isEdit"] = verifyEdit(accountsVoucher,accounts);
        FormatVoucherInfo(accountsVoucher);

        Main = {
            data() {
                return {
                    "accountsVoucher": accountsVoucher,
                    "accounts": accounts,
                    "subjects": subjects,
                    "monetaryUnits": [
                        { name: "亿", field: "n_00000000000" },
                        { name: "千", field: "n_0000000000" },
                        { name: "百", field: "n_000000000" },
                        { name: "十", field: "n_00000000" },
                        { name: "万", field: "n_0000000" },
                        { name: "千", field: "n_000000" },
                        { name: "百", field: "n_00000" },
                        { name: "十", field: "n_0000" },
                        { name: "元", field: "n_000" },
                        { name: "角", field: "n_00" },
                        { name: "分", field: "n_0" }
                    ],
                    "dialogAccessoryVisible": false,
                    "accessoryRowIndex": 0,
                    "fileList": [],

                    pickerOptions: {
                        disabledDate: (time) => {
                            return !verifyCurrentMonth(time);
                        }
                    }
                }
            },

            methods: {
                getAccessoryCountAll() {
                    var accessoryCount = 0;
                    this._data.accountsVoucher.accountsFinancialinforms.forEach(element => {
                        accessoryCount += element.accessory.fileInfos.length;
                    });
                    return accessoryCount;
                },
                accessoryManage(value, index) {
                    this._data.fileList = this._data.accountsVoucher.accountsFinancialinforms[index].accessory.fileInfos;
                    this._data.accessoryRowIndex = index;
                    this._data.dialogAccessoryVisible = true
                }
                ,
                moneyInput(value, filedName, index, object1) {
                    value = value.toString().trim().replace("-", "");
                    if (value.length > 1) {
                        value = value.split("")[0];
                    }
                    this._data.accountsVoucher.accountsFinancialinforms[index].moneyInfo[filedName] = value;
                    this._data.accountsVoucher.accountsFinancialinforms[index].money = MergeMoney(this._data.accountsVoucher.accountsFinancialinforms[index].moneyInfo);
                    this._data.accountsVoucher.accountsFinancialinforms[index].moneyInfo = SplitMoney(this._data.accountsVoucher.accountsFinancialinforms[index].money);
                },
                moneyFocus(event) {
                    event.target.select();
                },
                subjectChange(value, index) {

                    this._data.accountsVoucher.accountsFinancialinforms[index].subjectId = value[value.length - 1];
                    this._data.accountsVoucher.accountsFinancialinforms[index].subjectIds = value;
                    var subjectObj = getSubjectObject(this._data.accountsVoucher.accountsFinancialinforms[index].subjectId, this._data.subjects);
                    this._data.accountsVoucher.accountsFinancialinforms[index].accountsSubject = subjectObj;
                    //this.$set(this.accountsFinancialinforms, index, this.accountsFinancialinforms[index]);
                },
                getSubjectLabels(ids) {

                    var labelsStr = "";
                    var subjects = this._data.subjects;
                    if (ids != null && ids != undefined && ids.length > 0) {
                        ids.forEach(element => {
                            var item = getSubjectObject(element, subjects);
                            if (item != null) {
                                if (labelsStr.length > 0) {
                                    labelsStr += " / ";
                                }
                                labelsStr += item.name;
                                subjects = item.children;
                            }
                        });
                    }
                    return labelsStr;
                }
                ,
                handleRemove(file, fileList) {
                    console.log(file, fileList);
                    this._data.accountsVoucher.accountsFinancialinforms[this._data.accessoryRowIndex].accessory.fileInfos = fileList;
                },
                handleSuccess(response, file, fileList) {
                    console.log(response, file, fileList);
                    if (response.code == 0) {
                    	var resultArray = [];
                    	if(this._data.accountsVoucher.accountsFinancialinforms[this._data.accessoryRowIndex].accessory.fileInfos.length > 0){
                    		resultArray = this._data.accountsVoucher.accountsFinancialinforms[this._data.accessoryRowIndex].accessory.fileInfos.slice(0);
                    	}
                        resultArray[resultArray.length] = {name:file.name,url:file.response.url}
                        this._data.accountsVoucher.accountsFinancialinforms[this._data.accessoryRowIndex].accessory.fileInfos = resultArray;
                    } else {
                        this.$confirm(`${file.name}，请删除后重新操作`)
                    }
                },
                handlePreview(file) {
                    console.log(file);
                },
                handleExceed(files, fileList) {
                    console.log(file, fileList);
                    /* this.$message.warning(`当前限制选择 3 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`); */
                },
                beforeRemove(file, fileList) {
                    return this.$confirm(`确定移除 ${file.name}？`);
                },
                handleBeforeUpload(file) {
                    var isLt2M = file.size / 1024 / 1024 < 10;
                    if (!isLt2M) {
                        this.$message.error('上传文件大小不能超过 10MB!');
                    }
                    return isLt2M;
                },
                rowClassName({ row, rowIndex }) {
                    //把每一行的索引放进row
                    row.index = rowIndex;
                },
                addRow(event) {
                    var afLength = this._data.accountsVoucher.accountsFinancialinforms.length;
                    if (afLength >= 6) {

                        this.$message.warning('一条凭证对多只能录入 6 条财务信息!');
                        return;
                    }
                    var addRowObject = {
                        "digest": "",
                        "subjectId": 0,
                        "accountsSubject": {
                            "rdtype": 0,
                            "parentId": 0
                        },
                        "auxiliary": "",
                        "money": 0.00,
                        "moneyInfo": SplitMoney(0.00),
                        "accessory": {
                            "fileInfos": []
                        }
                    }
                    //this._data.accountsVoucher.accountsFinancialinforms[afLength] = addRowObject;
                    this.$set(this._data.accountsVoucher.accountsFinancialinforms, afLength, addRowObject);
                },
                removeRow() {
                    var selectRows = this.$refs.AFTable.selection;
                    for (var i = selectRows.length - 1; i >= 0; i--) {
                        this._data.accountsVoucher.accountsFinancialinforms.splice(selectRows[i].index, 1);
                    }
                },
                submit() {

                    if (this._data.accountsVoucher.date == null || this._data.accountsVoucher.date == undefined || this._data.accountsVoucher.date == "") {
                        this.$message.warning('凭证日期不能为空!');
                        return;
                    }
                    if (this._data.accountsVoucher.accountsFinancialinforms == null || this._data.accountsVoucher.accountsFinancialinforms.length == 0) {
                        this.$message.warning('请录入至少一条财务收支信息!');
                        return;
                    }
                    for (var i = 0; i < this._data.accountsVoucher.accountsFinancialinforms.length; i++) {
                        if (this._data.accountsVoucher.accountsFinancialinforms[i].subjectId <= 0) {
                            this.$message.warning('第 ' + (this._data.accountsVoucher.accountsFinancialinforms[i].index + 1) + ' 条财务收支信息,请选择会计科目!');
                            return;
                        }
                    }

                    var thisObje = this;
                    if(this._data.accountsVoucher.id){
                    	axios.post(baseURL + 'accounts/voucher/update', this._data.accountsVoucher).then(function (response) {
                    		if(response.data.code == "0"){
                    			thisObje.$message({
                    		          message: '修改成功!',
                    		          type: 'success'
                    		        });
        					}
                    		else{
                    			thisObje.$message.error(response.data.msg);
                    		}
        				}).catch(function (e) {
        					thisObje.$message.error(e.message);
        				});
                    }
                    else{
                    	axios.post(baseURL + 'accounts/voucher/add', this._data.accountsVoucher).then(function (response) {
                    		if(response.data.code == "0"){
                    			thisObje.$message({
                  		          message: '添加成功!',
                  		          type: 'success'
                  		        });
                    			location.reload();
        					}
                    		else{
                    			thisObje.$message.error(response.data.msg);
                    		}
        				}).catch(function (e) {
        					thisObje.$message.error(e.message);
        				});
                    }
                    
                },
                goBack() {
                    history.go(-1);
                }
            }
        }
        var Ctor = Vue.extend(Main);
        new Ctor().$mount('#app');
    }







});