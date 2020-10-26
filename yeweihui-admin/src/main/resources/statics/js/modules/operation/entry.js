
var vm = new Vue({
    el: "#rrapp",
    data: {
    	currentAccounts:null,
    	isAddAccounts:false,
    	isLockScreen:false,
    	isLoadVoucherData:true,
    	isSubmitAduit:false,
    	isCanSealingAccount:false,
    	isSubmitAduitResult:false,
    	loginUser:null,
    	pageInfo:{currPage:1,pageSize:5,totalPage:1,totalCount:0},
    	subjects:[],

    	
        admin: false,
        zonesOptions: [],
        showList: true,
        isActive: 1,
        value2: '',
        name1: '',
        name2: '',
        name3: '',
        accounts: '',
        printingArray: [],
        report: {
            operationsReceipts: 0,
            operationsDisbursements: 0,
            depositBlance: 0,
            operationsBlance: 0,   //已录入经营结余
            depositReceipts: 0,    //已录入押金收入
            depositDisbursements: 0,	 	//已录入押金支出
        },
        money1: {
            one: '',
            two: '',
            three: '',
            four: '',
            five: '',
            six: '',
            seven: '',
            eight: '',
            nine: '',
            ten: '',
            ten1: '',
        },
        value1: '',
        input: '',
        operatingBalance: '', // 经营结余
        depositBalance: '', // 押金结余
        indexs: '',
        bookId: "",
        multipleSelection: [],
        subjectId: null,
        q: {
            zoneId: null,
            groupId: null,
            voteTitle: undefined,
            creatDate: null,
            yearDate: null,
        },
        dialogAccessoryVisible: false,
        accessoryRowIndex: 0,
        fileList: [],
        tableData6: [],
        //日期
        pickerOptions0: {
            disabledDate(time) {
                return time.getTime() > Date.now() - 8.64e6
            }
        },
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
        optionalStartDate:{
            disabledDate: (targetDate) => {
            	return !vm.verifyMonth("verifyStartMonth",targetDate);
            }
        },
        optionalEndDate:{
            disabledDate: (targetDate) => {
            	return !vm.verifyMonth("verifyEndMonth",targetDate);
            }
        }
    },
    mounted() {
        //this.getinfoByUse();
    },
    created() {
        this.getinfoByUse();
        if(this.currentAccounts == null){
        	if(Role_Accounts_Add){
        		this.isAddAccounts = true;
        		var currentDate = new Date();
        		this.currentAccounts = {startDate:currentDate.getFullYear()+"-"+(currentDate.getMonth()+1).toString().padStart(2,"0")};
        		
        	}
        	else{
        		if(Role_Accounts_Audit){
            		this.$alert('您所在的小区未开启记账账簿！', '提示');
            	}
            	else{
            		this.$alert('当前用户无相关操作权限！', '提示');
            	}
        		this.currentAccounts = {startDate:"空"};
        	}
        	this.isLockScreen = true;
    		this.isLoadVoucherData = false;
        }
        else{
        	if(Role_Accounts_Audit && this.currentAccounts.status != 1){
            	this.$alert('当前账簿正在录入中，不能进行审核！', '提示');
            }

            this.getStatementOfAccounts();
            this.statistics();
            this.getSubject();
            if(Role_Voucher_List){
            	this.getData();
            }else{
            	this.isLoadVoucherData = false;
            }
        }
    },
    methods: {
        subjectChange(value) {
        	 vm.subjectId = value[value.length - 1];
        },
        getSubject: function () {
            //   全部科目
            let that = this;
            $.get(baseURL + `accounts/subject/list`, function (r) {
                if (r.code === 0) {
                	that.subjects = r.subjects;
                } else {
                    that.$message.warning(r.msg);
                }
            })
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
        },
        changPage(val) {
        	this.pageInfo.currPage = val;
            this.getData();
        },
        handleSizeChange(val){
        	this.pageInfo.pageSize = val;
        	this.getData();
        },
        handleSelectionChange(val) {
            flerId(val)
            this.multipleSelection = val;
        },
        arraySpanMethod({ row, column, rowIndex, columnIndex }) {
            if (rowIndex % 2 === 0) {
                if (columnIndex === 0) {
                    return [1, 2];
                } else if (columnIndex === 1) {
                    return [0, 0];
                }
            }
        },
        objectSpanMethod({ row, column, rowIndex, columnIndex }) {
            if (columnIndex === 0) {
                if (rowIndex % 2 === 0) {
                    return {
                        rowspan: 2,
                        colspan: 1
                    };
                } else {
                    return {
                        rowspan: 0,
                        colspan: 0
                    };
                }
            }
        },
        statistics() {
            let that = this;
            $.get(baseURL + `accounts/reports/recorded?statisticalDate=${that.q.creatDate ? that.q.creatDate : getinFo()}`, function (r) {
                if (r.code === 0) {
                	if(r.data != null){
                		r.data.operationsReceipts = (r.data.operationsReceipts == null)?0:parseFloat(r.data.operationsReceipts);
                		r.data.operationsDisbursements = (r.data.operationsDisbursements == null)?0:parseFloat(r.data.operationsDisbursements);
                		r.data.operationsBlance = (r.data.operationsReceipts - r.data.operationsDisbursements).toFixed(2);   //已录入经营结余
                		r.data.operationsBlance = (r.data.operationsBlance == 0.00)?0:r.data.operationsBlance;
                		
                		r.data.depositReceipts = (r.data.depositReceipts == null)?0:parseFloat(r.data.depositReceipts);    //已录入押金收入
                		r.data.depositDisbursements = (r.data.depositDisbursements == null)?0:parseFloat(r.data.depositDisbursements);	 	//已录入押金支出
                		r.data.depositBlance = (r.data.depositReceipts - r.data.depositDisbursements).toFixed(2);
                		r.data.depositBlance = (r.data.depositBlance == 0.00)?0:r.data.depositBlance;
                		
                		that.report = r.data
                	}
                } else {
                    that.$message.warning(r.msg)
                }
            })
        },
        getData() {
            let that = this;
            that.isLoadVoucherData = true;
            that.printingArray = [];
            $.ajax({
                type: 'POST',
                url: baseURL + `accounts/voucher/list`,
                data: JSON.stringify({
                    accountsId:that.currentAccounts.id,
                    tagNumber: that.q.voteTitle,
                    subjectId: (that.subjectId)?[that.subjectId]:null,
                    date: that.q.creatDate ? that.q.creatDate : '',
                    page: that.pageInfo.currPage,
                    limit: that.pageInfo.pageSize
                }),
                contentType: "application/json",
                success: function (r) {
                    if (r.code === 0) {
                    	that.pageInfo = r.page;
                        that.tableData6 = [];
                        r.page.list.forEach(element => {
                        	FormatVoucherInfo(element);
                        	element.accountsFinancialinforms.forEach(af => {
                        		af["voucher"] = element;
                        		that.tableData6[that.tableData6.length] = af;
                            });
                        });
                        
                        
                    } else {
                        that.$message.warning(r.msg);
                    }
                    that.isLoadVoucherData = false;
                },
                error : function(e){
                	that.isLoadVoucherData = false;
                }
            })
        },
        chagnetime() {

            console.log(this.q.creatDate)
            this.getMonthStatistical(this.q.creatDate)
        },
        // accounts/accounts/infoByUse 
        getinfoByUse() {
            $.ajaxSettings.async = false;
            let that = this;
            $.get(baseURL + `accounts/accounts/infoByUse`, function (r) {
                if (r.code === 0) {
                    console.log(r.accounts);
                    that.currentAccounts = r.accounts;
                } else {

                    that.$message.warning(r.msg)
                }
            })
            $.ajaxSettings.async = true;
        },
        goback() {
            console.log('5555')
            // this.getOpen();
            vm.showList = true;
        },
        getOpen: function () {
            layer.open({
                type: 1,
                title: false,
                closeBtn: 0, //不显示关闭按钮
                skin: 'layui-layer-rim', //加上边框
                area: ['540px', '360px'], //宽高
                content: jQuery('#fitsthint'),
                shadeClose: true, //开启遮罩关闭
            });
        },
        changbg(index) {
            console.log(vm.operatingBalance)
            this.isActive = index;
            if (index === 1) {
                this.getData()
            }
            if (index === 2) {
                this.showList = false;
                let that = this;
                $.get(baseURL + 'accounts/accounts/infoByUse', function (r) {
                    if (r.code === 0) {
                        vm.bookId = r.accounts.id;
                        $.get(baseURL + `accounts/voucher/nextTagNumber?accountsId=${r.accounts.id}`, function (r) {
                            Console.log(r)
                        })
                    } else {
                        that.$message.warning(r.msg);
                    }
                })

            }
            if (index === 4) {
                if (vm.printingArray.length === 0) {
                    this.$alert('请先勾选要删除的凭证！')
                } else if (vm.printingArray.length > 1) {
                    this.$alert('一次只能删除一条凭证信息！')
                } else {
                    let that = this;
                    this.$confirm('删除后不可恢复，是否确认删除？', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                      }).then(() => {
                    	  $.get(baseURL + `accounts/voucher/delete?id=${vm.printingArray.toString()}`, function (r) {
                              if (r.code === 0) {
                                  that.getData()
                                  that.$message.success('删除成功');
                              } else {
                                  that.$message.warning(r.msg);
                              }
                          })
                      },() => {});
                }
            }
            if (index === 2) {
                window.location.href = "../../modules/operation/voucher.html"
            }
            if (index === 3) {
                if (vm.printingArray.length === 0) {
                    alert('先勾选要打印的凭证！')
                }else {
                    window.location.href ="../../modules/operation/voucher.html?id="+`${vm.printingArray[0].toString()}`+"";
                }
            }
        },
        getStatementOfAccounts(){
        	let that = this;
        	$.get(baseURL + "accounts/accounts/getStatementOfAccounts?id="+that.currentAccounts.id, function (r) {
                if (r.code === 0) {
                    that.fileList=r.statementOfAccounts.fileInfos
                }else
                {
                    that.$message.warning(r.msg)
                }
            })
        }
        ,
        submitAduit() {
            let that = this;

            if(that.currentAccounts.endDate == null || that.currentAccounts.endDate == undefined){
            	that.$message({
                    type: 'warning',
                    message: "请选择账簿封账日期！"
                  });
            	return;
            }

            this.$confirm('是否将当前账簿进行提交审核?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
              }).then(() => {
            	 that.isSubmitAduit = false;
            	 that.isLockScreen=true;
            	 $.get(baseURL + `accounts/accounts/submitAudit?endDate=${that.currentAccounts.endDate}`, function (r) {
                      if (r.code === 0) {
                          that.$message.success('提交审核成功');
                          setTimeout(function(){
                        	  window.location.reload();
                		  }, 1500 )
                      } else {
                          that.$message.warning(r.msg);
                          that.isLockScreen=false;
                      }
                 })
              },() => {});
        }
        ,
        submitAduitResult(result) {
        	
            let that = this;
            this.$confirm('是否确认提交审核结果？', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
              }).then(() => {
            	 that.isSubmitAduitResult = false;
            	 that.isLockScreen=true;
            	 $.get(baseURL + "accounts/accounts/submitAuditResult?result="+result, function (r) {
                      if (r.code === 0) {
                    	  var targerATag = $("a[href='#modules/operation/expendituredetails.html']", window.parent.document);
                    	  
                    	  that.$message.success('提交审核结果成功'+((targerATag.length > 0)?"，即将进入收支详情页":""));
                    	  if(targerATag.length > 0)
                    	  {
                    		  setTimeout(function(){
                    			  targerATag[0].click();
                    		  }, 1500 )
                    	  }
                    	  else
                    	  {
                    		  setTimeout(function(){
                    			  window.location.reload();
                    		  }, 1500 )
                    	  }
                      } else {
                          that.$message.warning(r.msg);
                          that.isLockScreen=false;
                      }
                 })
              },() => {});
        },
        submitStatement()
        {
            $.ajax({
                type: 'POST',
                url: baseURL + `accounts/accounts/updateStatementOfAccounts`,
                data: JSON.stringify({
                    id: this.currentAccounts.id,
                    fileInfos: this.fileList,
                }),
                contentType: "application/json",
                success: function (r) {
                    layer.close(layer.index)
                    if (r.code === 0) {
                        vm.dialogAccessoryVisible = false;
                        vm.$message.success('操作成功');
                    } else {
                        vm.$message.error(r.msg);
                    }
                }
            })
        },
        submitAccounts() {
            let that = this;
            
            if(that.currentAccounts != null){
            	if(that.currentAccounts.startDate == null || that.currentAccounts.startDate.length != 7){that.$message.warning("记账起始月份不能为空"); return;}
            	if(that.currentAccounts.lastOperatingSurplus == null){that.$message.warning("起始月上月经营结余不能为空"); return;}
            	if(that.currentAccounts.lastPledgeSurplus == null){that.$message.warning("起始月上月押金结余不能为空"); return;}
            }else{
            	that.$message.warning("请录入正确的账簿信息");
            	 return;
            }
            
            $.ajax({
                type: 'POST',
                url: baseURL + "accounts/accounts/add",
                data: JSON.stringify(that.currentAccounts),
                contentType: "application/json",
                success: function (r) {
                    if (r.code === 0) {
                        that.$message.success('启用成功');
                        location.reload();
                    } else {
                        that.$message.warning(r.msg);
                    }
                }
            })
        },
        accessoryManage(value, index) {
            /*this._data.fileList = this._data.accountsVoucher.accountsFinancialinforms[index].accessory.fileInfos;
            this._data.accessoryRowIndex = index;*/
            this._data.dialogAccessoryVisible = true
        },
        handleRemove(file, fileList) {
            this.fileList = fileList;
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
            			this.fileList = copyFileList;
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
        	window.open(file.url);
            console.log(file);
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
        },//时间格式化
        voucherDateFormat:function(row,column){
            var t=new Date(row.voucher.date);//row 表示一行数据
            return t.getFullYear()+"-"+(t.getMonth()+1)+"-"+t.getDate();
        },//凭证合并行
        mergeVoucherRow:function(info, row, column, rowIndex, columnIndex){
           
        	if(
        		info.columnIndex == 0 ||
        		info.columnIndex == 1 ||
        		info.columnIndex == 2 ||
        		info.columnIndex == 9 ||
        		info.columnIndex == 10
        	){
        	
        	if(info.rowIndex==0 || this.tableData6[info.rowIndex-1].voucher.id != this.tableData6[info.rowIndex].voucher.id){

        		var rowSpan = info.row.voucher.accountsFinancialinforms.length;
        		rowSpan = (rowSpan<=0)?1:rowSpan;
        		return {
                    rowspan: rowSpan,
                    colspan: 1
                  };
        	}
        	else{
        		return {
                    rowspan: 0,
                    colspan: 0
                  };
        	}
        	}
        },
        rowClassName({ row, rowIndex }) {
            // 把每一行的索引放进row
            row.index = rowIndex;
        },
        canEdit(){
        	if(
        		(this.currentAccounts && (this.currentAccounts.status == 0 || this.currentAccounts.status == 2))
        	){
        		return true;
        	}
        	return false;
        },
        verifyMonth(mode,targetDate){
        	
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

        	switch(mode){
        		
        		//验证是否有可选的封账月份,目标时间默认定为记账起始时间
    			case "canSelectEndMonth": 
        		//验证指定封账月份是否可行
        		case "verifyEndMonth":
        			var minMonthInfo = vm.currentAccounts.startDate.split("-");
        			minYear = parseInt(minMonthInfo[0], 10);
        			minMonth = parseInt(minMonthInfo[1], 10);
        			
        			maxYear = minYear;
        			maxMonth = ((minYear != currentYear)?12:currentMonth-1);
        			if(maxMonth == 0){
        				maxYear--;
        				maxMonth = 12;
        			}
        			break;
        		//验证指定起始月份是否可行
        		case "verifyStartMonth": 
        			minYear = 2000;
        			minMonth = 1;
        			
        			maxYear = currentYear;
        			maxMonth = currentMonth;
        			break;
        	}
        	
        	
        	minMonthDate = new Date(minYear, minMonth-1, 1);
        	if(targetDate==null && mode == "canSelectEndMonth"){
				targetDate = minMonthDate;
			}
        	maxMonthDate = new Date(maxYear, maxMonth-1, 1);
        	
            return (targetDate.getTime() >= minMonthDate.getTime() && targetDate.getTime() <= maxMonthDate.getTime());
        },
        SubmitAduitButtonClick(){
        	if(this.fileList.length == 0)
        	{
        		vm.$confirm('当前还未上传银行对账单。', '提示', {
                    confirmButtonText: '返回上传',
                    cancelButtonText: '不上传，继续封账',
                    distinguishCancelAndClose: true,
                    type: 'info'
                }).then(() => {
                	
                	if($("#accessoryManage").length > 0){
                		this.accessoryManage(null,null);
                	}
                	else
                	{
                		vm.$message.warning("当前用户没有银行对账单管理的相关权限！");
                	}

                },() => {
                	this.isSubmitAduit = true;
                	this.isCanSealingAccount = this.verifyMonth("canSelectEndMonth",null);
                }).catch((action) => {
                    
                });
        		
        	}else
        	{
        		this.isSubmitAduit = true;
            	this.isCanSealingAccount = this.verifyMonth("canSelectEndMonth",null);
        	}
        },
        viewPdfPrint() {
        	
        	var voucherIds = vm.printingArray;
            if (voucherIds.length == 0) {
            	vm.tableData6.forEach(element => {
            		if(voucherIds.length > 0 && voucherIds[voucherIds.length-1] == element.voucher.id){
            			return;
            		}
            		voucherIds[voucherIds.length] = element.voucher.id;
            	});
            }
            

            if(voucherIds.length == 0){
            	vm.$message.warning("当前无可打印凭证信息！");
            	 return;
            }
        	
            vm.$confirm('是否开始打印pdf?', '提示', {
                confirmButtonText: '是',
                cancelButtonText: '否',
                distinguishCancelAndClose: true,
                type: 'info'
            }).then(() => {
                console.log(vm.printingArray)
                window.open(baseURL + "accounts/voucher/viewPdf/"+voucherIds.toString());
            },() => {}).catch((action) => {
                console.log(action)
            });
        },
        viewPdfPrintPdf() {
        	var voucherIds = vm.printingArray;
            if (voucherIds.length == 0) {
            	vm.tableData6.forEach(element => {
            		if(voucherIds.length > 0 && voucherIds[voucherIds.length-1] == element.voucher.id){
            			return;
            		}
            		voucherIds[voucherIds.length] = element.voucher.id;
            	});
            }
            
            if(voucherIds.length == 0){
            	vm.$message.warning("当前无可导出凭证信息！");
            	 return;
            }
            vm.$confirm('是否开始导出Excel?', '提示', {
                confirmButtonText: '是',
                cancelButtonText: '否',
                distinguishCancelAndClose: true,
                type: 'info'
            }).then(() => {
                console.log(vm.printingArray)
                window.open(baseURL + "accounts/voucher/exportExcel/"+voucherIds.toString());
            },() => {}).catch((action) => {
                console.log(action)
                if (action === 'cancel') {
                    //             window.open(baseURL + `operation/announce/viewPdf/${id}/no`);
                }
            });
        }
    }
})

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
        arrId.push(arr[index].voucher.id)
    }
    vm.printingArray = arrId
}

//格式化凭证信息
function FormatVoucherInfo(accountsVoucher) {
    accountsVoucher.accountsFinancialinforms.forEach(element => {

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