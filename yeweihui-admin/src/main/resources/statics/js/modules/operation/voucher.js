$(function () {

    // 格式化凭证信息
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
    // 拆分金额
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
    // 合并金额
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
    // 根据ID获取科目ID
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
    // 获取url参数
    function getQueryString(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) return unescape(r[2]);
        return null;
    }
    // 获取当天日期
    function CurentDate() {
        var now = new Date();

        var year = now.getFullYear();       // 年
        var month = now.getMonth() + 1;     // 月
        var day = now.getDate();            // 日

        var clock = year + "-";

        if (month < 10)
            clock += "0";

        clock += month + "-";

        if (day < 10)
            clock += "0";

        clock += day;
        return (clock);
    }
    // 日期字符串转对象：yyyy-MM-dd
    function stringToDate(dateStr) {
        var tempStrs = dateStr.split(" ");
        var dateStrs = tempStrs[0].split("-");
        var year = parseInt(dateStrs[0], 10);
        var month = parseInt(dateStrs[1], 10) - 1;
        var day = parseInt(dateStrs[2], 10);
        var date = new Date(year, month, day);
        return date;

    }
    // 验证是否为当月日期
    function verifyCurrentMonth(targetDate) {
        var date = new Date();
        var firstDay = new Date(date.getFullYear(), date.getMonth(), 1);
        var lastDay = new Date(date.getFullYear(), date.getMonth() + 1, 0);
        return targetDate.getTime() >= firstDay.getTime() && targetDate.getTime() <= lastDay.getTime();
    }
    // 验证是否为编辑模式
    function verifyEdit(voucher, accounts) {

        if (accounts != null) {
            if (accounts.status == 0 || accounts.status == 2) {
                if (voucher != null) {
                    if (
                        (!voucher.id || voucher.id == undefined) || true //verifyCurrentMonth(stringToDate(voucher.date))
                    ) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    // 获取凭证信息
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
    // 获取科目信息
    function getSubjects() {
        return axios.get(baseURL + 'accounts/subject/list');
    }
    // 获账簿信息
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
    // 获取凭证记字号数字
    function getTagNumber(accountsId) {
        return axios.get(baseURL + 'accounts/voucher/nextTagNumber',
           {
            params: {
            	accountsId: accountsId
            }
        });
    }
    // 获取用户信息
    function getUser() {
        return axios.get(baseURL + 'sys/user/info');
    }

    //用于处理金额输入时自动切换到下一位
    var moneyInputSwitch = true;
    var moneyInputValue = null;
    var moneyInputEle = null;
    var moneyInputNextEle = null;
    
    
  // 以下为空的结构数据，用于页面最初的加载，之后会进行真实数据的请求
    // 凭证数据
    var voucherData = {
        "accountsId": 0,
        "date": "",
        "tagNumber": "",
        "makeUsername": "",
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
    // 格式化凭证数据
    FormatVoucherInfo(voucherData);
    // 账簿数据
    var accountsData = {
        "id": 0,
        "status": 0
      }
    // 科目数据
    var subjecstData = [
        {
          "id": 0,
          "name": "",
        }
    ]



    var Main = {
        data() {
            return {
                "accountsVoucher": voucherData,
                "accounts": accountsData,
                "subjects": subjecstData,
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
                "isEdit":false,
                "isLoading":true,
                "isSubmit":false,
                "pickerOptions": {
                    disabledDate: (time) => {
                        return !this.AddVoucherSelectDate(time);
                    }
                }
            }
        },

        methods: {
        	// 添加凭证可选日期
            AddVoucherSelectDate(targetDate){
            	
            	var targetDateStr = "";
            	
            	var minMonthDate = null;
            	var minYear = 0;
            	var minMonth = 0;
            	
            	var maxMonthDate = null;
            	var maxYear = 0;
            	var maxMonth = 0;

            	var currentDate = new Date();
            	var currentYear = currentDate.getFullYear();
            	var currentMonth = currentDate.getMonth()+1;
            	var currentDay = currentDate.getMonth();

            	var minMonthInfo = this._data.accounts.startDate.split("-");
            	minYear = parseInt(minMonthInfo[0], 10);
            	minMonth = parseInt(minMonthInfo[1], 10);

            	minMonthDate = new Date(minYear, minMonth-1, 1);
            	
            	if(currentMonth == 12)
            	{
            		maxYear = currentYear+1;
            		maxMonth = 0;
            	}
            	else
            	{
            		maxYear = currentYear;
            		maxMonth = currentMonth;
            	}
            	maxMonthDate = new Date(currentYear, maxMonth, 1);
            	
                return (targetDate.getTime() >= minMonthDate.getTime() && targetDate.getTime() < maxMonthDate.getTime());
            },
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
            moneyInput(value, filedName, index) {
                value = value.toString().trim().replace("-", "");
                if (value.length > 1) {
                	var values = value.split("");
                    value = values[values.length-1];
                    //$(this._data.moneyInputEle).select();
                }
                this._data.accountsVoucher.accountsFinancialinforms[index].moneyInfo[filedName] = value;
                this._data.accountsVoucher.accountsFinancialinforms[index].money = MergeMoney(this._data.accountsVoucher.accountsFinancialinforms[index].moneyInfo);
                this._data.accountsVoucher.accountsFinancialinforms[index].moneyInfo = SplitMoney(this._data.accountsVoucher.accountsFinancialinforms[index].money);
                
            },
            moneyFocus(event) {

            	event.target.select();

            	//获取下一位输入节点
            	/*var parentEle = event.srcElement.offsetParent.offsetParent;
            	var parentEleClassNameInfo = parentEle.className.substring(0,parentEle.className.lastIndexOf("_"));
            	var nextEle = parentEle.nextSibling;
            	var nextEleClassNameInfo = nextEle.className.substring(0,nextEle.className.lastIndexOf("_"));
            	if(parentEleClassNameInfo == nextEleClassNameInfo){
            		var fildResult = $(nextEle).find("input.el-input__inner");
            		if(fildResult && fildResult.length > 0){
            			this._data.moneyInputNextEle = fildResult[0];
            		}
            		else{
            			this._data.moneyInputNextEle = null;
            		}
            	}else{
            		this._data.moneyInputNextEle = null;
            	}*/
            	
            },
            subjectChange(value, index) {

                this._data.accountsVoucher.accountsFinancialinforms[index].subjectId = value[value.length - 1];
                this._data.accountsVoucher.accountsFinancialinforms[index].subjectIds = value;
                var subjectObj = getSubjectObject(this._data.accountsVoucher.accountsFinancialinforms[index].subjectId, this._data.subjects);
                this._data.accountsVoucher.accountsFinancialinforms[index].accountsSubject = subjectObj;
                // this.$set(this.accountsFinancialinforms, index,
				// this.accountsFinancialinforms[index]);
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
                this._data.accountsVoucher.accountsFinancialinforms[this._data.accessoryRowIndex].accessory.fileInfos = fileList;
            },
            handleSuccess(response, file, fileList) {
                var copyFileList = fileList.slice(0);
                var lastFileIndex = (copyFileList.length-1);
                if(response.code == 0){
                	for(var i=0;i<copyFileList.length;i++)
                	{
                		if(file.uid == copyFileList[i].uid)
                		{
                			copyFileList[i] = {name:copyFileList[i].name,url:copyFileList[i].response.url,uid:copyFileList[i].uid};
                			this._data.accountsVoucher.accountsFinancialinforms[this._data.accessoryRowIndex].accessory.fileInfos = copyFileList;
                			break;
                		}
                	}
                } else {
                	console.log("上传失败：");
                	console.log(file);
                    this.$aler(`${file.name}，请删除后重新操作`)
                }
             },
            handlePreview(file) {
            	window.open(file.response.url);
            },
            handleExceed(files, fileList) {
                console.log(file, fileList);
                /*
				 * this.$message.warning(`当前限制选择 3 个文件，本次选择了 ${files.length}
				 * 个文件，共选择了 ${files.length + fileList.length} 个文件`);
				 */
            },
            beforeRemove(file, fileList) {
                return true;
            },
            handleBeforeUpload(file) {
                var isLt2M = file.size / 1024 / 1024 < 10;
                if (!isLt2M) {
                    this.$message.error('上传文件大小不能超过 10MB!');
                }
                return isLt2M;
            },
            rowClassName({ row, rowIndex }) {
                // 把每一行的索引放进row
                row.index = rowIndex;
            },
            addRow(event) {
                var afLength = this._data.accountsVoucher.accountsFinancialinforms.length;
                if (afLength >= 6) {

                    this.$message.warning('一条凭证只能录入 6 条财务信息!');
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
                // this._data.accountsVoucher.accountsFinancialinforms[afLength]
				// = addRowObject;
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
                thisObje.isSubmit = true;
                if(this._data.accountsVoucher.id){
                	axios.post(baseURL + 'accounts/voucher/update', this._data.accountsVoucher).then(function (response) {
                		if(response.data.code == "0"){
                			thisObje.$message({
                		          message: '修改成功!',
                		          type: 'success'
                		        });
                			window.location.reload();
    					}
                		else{
                			thisObje.$message.error(response.data.msg);
                		}
    				}).catch(function (e) {
    					thisObje.$message.error(e.message);
    				}).finally(function(){
    					thisObje.isSubmit = false;
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
    				}).finally(function(){
    					thisObje.isSubmit = false;
    				});
                }

            },
            goBack() {
                history.go(-1);
            },
            indexs:function(){
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
            requestMethod[2] = getAccounts();
            requestMethod[3] = getUser();
            requestMethod[4] = null;
        }

        var thisData = this._data;

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

                            accountsVoucher["isEdit"] = verifyEdit(accountsVoucher,accounts.data.accounts);
                            FormatVoucherInfo(accountsVoucher);

                            thisData.accountsVoucher = accountsVoucher;
                            thisData.accounts = accounts.data.accounts;
                            thisData.subjects = subjects.data.subjects;
                            thisData.isLoading = false;
                    }));
                }
                else {
                    axios.all([getAccounts(null)])
                        .then(axios.spread(function (accounts) {

                            voucher.data.accountsVoucher["isEdit"] = verifyEdit(voucher.data.accountsVoucher,accounts.data.accounts);
                            FormatVoucherInfo(voucher.data.accountsVoucher);

                            thisData.accountsVoucher = voucher.data.accountsVoucher;
                            thisData.accounts = accounts.data.accounts;
                            thisData.subjects = subjects.data.subjects;
                            thisData.isLoading = false;
                        }));
                }
            }));
            }
        },
        mounted(){
            // 自动加载indexs方法
            this.indexs();
        }
    }
    var Ctor = Vue.extend(Main);
    new Ctor().$mount('#app');
    
    
});