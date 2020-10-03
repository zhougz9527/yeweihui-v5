//默认信息
var defaultInfos = {
	//默认请求地址
	postUrl: "http://" + window.location.host + ":8080/",
	//默认用户名
	//userName: "zh",
	//默认密码
	//password: "123123",
};
//通用请求参数数组
var generalFieldItems = [
	//{name:"提交网址",field:"URL",value:defaultInfos.postUrl,type:"text",must:true,describe:"请求的访问网址。",disabled:false,isParam:false,isPrivate:false},
	//{name:"用户名",field:"userName",value:defaultInfos.userName,type:"text",must:true,describe:"接口用户的用户名，用于进行安全安验证，此用户由集中授权系统管理员进行添加。",disabled:false,isParam:true,isPrivate:false},
	//{name:"密码",field:"password",value:defaultInfos.password,type:"text",must:true,describe:"接口用户的密码，用于进行安全安验证。",disabled:false,isParam:true,isPrivate:false},
	//{name:"是否MD5加密",field:"isMD5",value:"false",type:"text",must:false,describe:"用于处理密码校验方式，是：true、否：false或空或其他字符。",disabled:false,isParam:true,isPrivate:false}
];
//通用请求结果字段信息数组
var generalResultInfos = [
	//{field: "method",describe: "请求的方法名称。"}, 
	{field: "code",describe: "请求状态码，0(成功)，其他（错误）。"}, 
	{field: "msg",describe: "成功或失败的具体文字描述信息。"}, 
	//{field: "errorCode",describe: "发生错误的具体错误代码（错误代码=err_10000，不具体代表某个错误），请求状态为 error 时返回此字段。"}, 
	{field: "data",describe: "具体返回信息请查看具体功能，请求状态码为 0 时返回此字段。"}
];

//API相关数据
var apiInfos = [
{
	title: "1.1 账簿 - 新启账簿",
	describe: "添加账簿，新启。",
	name: "accountsAdd",
	vueEl: '#accountsAdd',
	requestResult: "",
	isShowPublicParam: false,
	method : "POST",
	methods : ["POST"],
	fieldItems: generalFieldItems.concat([
	    {name:"接口",field:"URL",value:"accounts/accounts/add",type:"text",must:true,describe:"",disabled:true,isParam:false,isPrivate:true},
		{name:"记账日期(月份)",field:"startDate",value:"2020-08",type:"text",must:true,describe:"格式：2020-08",disabled:false,isParam:true,isPrivate:true},
		{name:"上个账簿经营结余",field:"lastOperatingSurplus",value:123.0,type:"text",must:false,describe:"空值默认为0",disabled:false,isParam:true,isPrivate:true},
		{name:"上个账簿押金结余",field:"lastPledgeSurplus",value:456.0,type:"text",must:false,describe:"空值默认为0",disabled:false,isParam:true,isPrivate:true}
	]),
	resultInfos: generalResultInfos,
	resultDataInfos:[
	     //{field: "isOpen",describe: "授权中心是否开启。"}
	]
},
{
	title: "1.2 账簿 - 获取账簿",
	describe: "获取账簿信息，只返回第一条匹配的信息。",
	name: "accountsInfo",
	vueEl: '#accountsInfo',
	requestResult: "",
	isShowPublicParam: false,
	method : "POST",
	methods : ["POST"],
	fieldItems: generalFieldItems.concat([
	    {name:"接口",field:"URL",value:"accounts/accounts/info",type:"text",must:true,describe:"",disabled:true,isParam:false,isPrivate:true},
	    {name:"账簿ID",field:"id",value:0,type:"text",must:false,describe:"若指定账簿ID，可以不需要下方参数",disabled:false,isParam:true,isPrivate:true},
	    {name:"小区ID",field:"zoneId",value:0,type:"text",must:false,describe:"",disabled:false,isParam:true,isPrivate:true},
		{name:"账簿日期",field:"accountsDate",value:'2020-08',type:"text",must:false,describe:"格式：2020-08",disabled:false,isParam:true,isPrivate:true},
		{name:"账簿状态",field:"status",value:[0,1,2],type:"text",must:false,describe:"可选值:0(录入)、1(审核中)、2(审核失败)、3(审核成功)",disabled:false,isParam:true,isPrivate:true,dataType:"NumberArray"}
	]),
	resultInfos: generalResultInfos,
	resultDataInfos:[
	     //{field: "isOpen",describe: "授权中心是否开启。"}
	]
},
{
	title: "1.3 账簿 - 获取记账账簿",
	describe: "获取当前登录用户所属小区当前正在使用的账簿信息。",
	name: "accountsInfoByUse",
	vueEl: '#accountsInfoByUse',
	requestResult: "",
	isShowPublicParam: false,
	method : "GET",
	methods : ["GET"],
	fieldItems: generalFieldItems.concat([
	    {name:"接口",field:"URL",value:"accounts/accounts/infoByUse",type:"text",must:true,describe:"",disabled:true,isParam:false,isPrivate:true}
	]),
	resultInfos: generalResultInfos,
	resultDataInfos:[
	     //{field: "isOpen",describe: "授权中心是否开启。"}
	]
},
{
	title: "1.4 账簿 - 获取封账账簿",
	describe: "获取当前登录用户所属小区指定月份并且已封账的账簿信息。",
	name: "accountsInfoByMonth",
	vueEl: '#accountsInfoByMonth',
	requestResult: "",
	isShowPublicParam: false,
	method : "GET",
	methods : ["GET"],
	fieldItems: generalFieldItems.concat([
	    {name:"接口",field:"URL",value:"accounts/accounts/infoByMonth",type:"text",must:true,describe:"",disabled:true,isParam:false,isPrivate:true},
		{name:"账簿日期(月份)",field:"accountsDate",value:'2020-08',type:"text",must:true,describe:"格式：2020-08",disabled:false,isParam:true,isPrivate:true}
	]),
	resultInfos: generalResultInfos,
	resultDataInfos:[
	     //{field: "isOpen",describe: "授权中心是否开启。"}
	]
},
{
	title: "1.5 账簿 - 提交审核",
	describe: "将当前登录用户所属小区正在记账的账簿，进行提交审核，更改账簿状态为审核中。",
	name: "accountsSubmitAudit",
	vueEl: '#accountsSubmitAudit',
	requestResult: "",
	isShowPublicParam: false,
	method : "GET",
	methods : ["GET"],
	fieldItems: generalFieldItems.concat([
	    {name:"接口",field:"URL",value:"accounts/accounts/submitAudit",type:"text",must:true,describe:"",disabled:true,isParam:false,isPrivate:true},
		{name:"封账日期(月份)",field:"endDate",value:'2020-08',type:"text",must:true,describe:"格式：2020-08",disabled:false,isParam:true,isPrivate:true}
	]),
	resultInfos: generalResultInfos,
	resultDataInfos:[
	     //{field: "isOpen",describe: "授权中心是否开启。"}
	]
},
{
	title: "1.6 账簿 - 进行审核",
	describe: "将当前登录用户所属小区正在审核中的账簿，进行审核。",
	name: "accountsSubmitAuditResult",
	vueEl: '#accountsSubmitAuditResult',
	requestResult: "",
	isShowPublicParam: false,
	method : "GET",
	methods : ["GET"],
	fieldItems: generalFieldItems.concat([
	    {name:"接口",field:"URL",value:"accounts/accounts/submitAuditResult",type:"text",must:true,describe:"",disabled:true,isParam:false,isPrivate:true},
		{name:"审核结果",field:"result",value:true,type:"text",must:true,describe:"可选值：true(审核通过，即封账)、false(审核失败)",disabled:false,isParam:true,isPrivate:true}
	]),
	resultInfos: generalResultInfos,
	resultDataInfos:[
	     //{field: "isOpen",describe: "授权中心是否开启。"}
	]
}
,
{
	title: "1.7 账簿 - 获取对账单",
	describe: "获取账簿的对账单信息集合。",
	name: "accountsGetStatementOfAccounts",
	vueEl: '#accountsGetStatementOfAccounts',
	requestResult: "",
	isShowPublicParam: false,
	method : "GET",
	methods : ["GET"],
	fieldItems: generalFieldItems.concat([
	    {name:"接口",field:"URL",value:"accounts/accounts/getStatementOfAccounts",type:"text",must:true,describe:"",disabled:true,isParam:false,isPrivate:true},
		{name:"账簿ID",field:"id",value:1,type:"text",must:true,describe:"",disabled:false,isParam:true,isPrivate:true}
	]),
	resultInfos: generalResultInfos,
	resultDataInfos:[
	     //{field: "isOpen",describe: "授权中心是否开启。"}
	]
},
{
	title: "1.8 账簿 - 更新对账单",
	describe: "更新账簿的对账单信息集合。",
	name: "accountsUpdateStatementOfAccounts",
	vueEl: '#accountsUpdateStatementOfAccounts',
	requestResult: "",
	isShowPublicParam: false,
	method : "POST",
	methods : ["POST"],
	fieldItems: generalFieldItems.concat([
	    {name:"接口",field:"URL",value:"accounts/accounts/updateStatementOfAccounts",type:"text",must:true,describe:"",disabled:true,isParam:false,isPrivate:true},
		{name:"账簿ID",field:"id",value:1,type:"text",must:true,describe:"",disabled:false,isParam:true,isPrivate:true},
		{name:"对账单信息",field:"fileInfos",value:getJsonStr([{"name": "测试单1","url": "https://ywh-hdj.oss-cn-hangzhou.aliyuncs.com/file/20200211/62d87e5053c04e1a99e857df2420858b.jpg"},{"name": "测试单2","url": "https://ywh-hdj.oss-cn-hangzhou.aliyuncs.com/file/20200211/62d87e5053c04e1a99e857df2420858b.jpg"}]),type:"textarea",must:true,describe:"",disabled:false,isParam:true,isPrivate:true,dataType:"JsonArray"}
	]),
	resultInfos: generalResultInfos,
	resultDataInfos:[
	     //{field: "isOpen",describe: "授权中心是否开启。"}
	]
},
{
	title: "2.1 凭证 - 分页查询",
	describe: "分页查询凭证信息。",
	name: "accountsVoucherList",
	vueEl: '#accountsVoucherList',
	requestResult: "",
	isShowPublicParam: false,
	method : "POST",
	methods : ["POST"],
	fieldItems: generalFieldItems.concat([
	    {name:"接口",field:"URL",value:"accounts/voucher/list",type:"text",must:true,describe:"",disabled:true,isParam:false,isPrivate:true},
		{name:"账簿ID",field:"accountsId",value:1,type:"text",must:true,describe:"",disabled:false,isParam:true,isPrivate:true},
		{name:"记字号数字",field:"tagNumber",value:1,type:"text",must:false,describe:"",disabled:false,isParam:true,isPrivate:true},
		{name:"科目ID",field:"subjectIds",value:[1],type:"text",must:false,describe:"",disabled:false,isParam:true,isPrivate:true,dataType:"NumberArray"},
		{name:"凭证日期",field:"date",value:"2020-08-29",type:"text",must:false,describe:"",disabled:false,isParam:true,isPrivate:true},
		{name:"页码",field:"page",value:1,type:"text",must:true,describe:"",disabled:false,isParam:true,isPrivate:true},
		{name:"每页数量",field:"limit",value:5,type:"text",must:true,describe:"",disabled:false,isParam:true,isPrivate:true}
	]),
	resultInfos: generalResultInfos,
	resultDataInfos:[
	     //{field: "isOpen",describe: "授权中心是否开启。"}
	]
},
{
	title: "2.2 凭证 - 获取凭证",
	describe: "获取单个凭证信息。",
	name: "accountsVoucherInfo",
	vueEl: '#accountsVoucherInfo',
	requestResult: "",
	isShowPublicParam: false,
	method : "GET",
	methods : ["GET"],
	fieldItems: generalFieldItems.concat([
	    {name:"接口",field:"URL",value:"accounts/voucher/info",type:"text",must:true,describe:"",disabled:true,isParam:false,isPrivate:true},
		{name:"凭证ID",field:"id",value:1,type:"text",must:true,describe:"",disabled:false,isParam:true,isPrivate:true}
	]),
	resultInfos: generalResultInfos,
	resultDataInfos:[
	     //{field: "isOpen",describe: "授权中心是否开启。"}
	]
},
{
	title: "2.3 凭证 - 添加凭证",
	describe: "添加凭证及财务收支信息。",
	name: "accountsVoucherAdd",
	vueEl: '#accountsVoucherAdd',
	requestResult: "",
	isShowPublicParam: false,
	method : "POST",
	methods : ["POST"],
	fieldItems: generalFieldItems.concat([
	    {name:"接口",field:"URL",value:"accounts/voucher/add",type:"text",must:true,describe:"",disabled:true,isParam:false,isPrivate:true},
		{name:"账簿ID",field:"accountsId",value:1,type:"text",must:true,describe:"",disabled:false,isParam:true,isPrivate:true},
		{name:"凭证日期",field:"date",value:"2020-08-31",type:"text",must:true,describe:"",disabled:false,isParam:true,isPrivate:true},
		{name:"制单人",field:"makeUsername",value:"zxl",type:"text",must:true,describe:"",disabled:false,isParam:true,isPrivate:true},
		{name:"财务信息",field:"accountsFinancialinforms",value:getJsonStr([{"digest": "摘要信息","subjectId": 3,"auxiliary": "辅助账信息","money": 300, "accessory": { "fileInfos": [ {"name":"测试附件.pdf","url":"https://wx.zaijiawangluo.com/image/asset/2018/12/09/5a6d7cfba678398c1c7a72ccddbab680.jpg"}]}}]),type:"textarea",must:true,describe:"",disabled:false,isParam:true,isPrivate:true,dataType:"JsonArray"}
	]),
	resultInfos: generalResultInfos,
	resultDataInfos:[
	     //{field: "isOpen",describe: "授权中心是否开启。"}
	]
},
{
	title: "2.4 凭证 - 编辑凭证",
	describe: "编辑凭证及财务收支信息,务必保证在请求返回的结构体上对必要的字段信息进行编辑，其他的保持原数据即可，因为这是全量更新。    ！！！注意注意！！！。",
	name: "accountsVoucherUpdate",
	vueEl: '#accountsVoucherUpdate',
	requestResult: "",
	isShowPublicParam: false,
	method : "POST",
	methods : ["POST"],
	fieldItems: generalFieldItems.concat([
	    {name:"接口",field:"URL",value:"accounts/voucher/update",type:"text",must:true,describe:"",disabled:true,isParam:false,isPrivate:true},
	    {name:"凭证信息",field:"",value:"",type:"textarea",must:true,describe:"",disabled:false,isParam:true,isPrivate:true,dataType:"JsonBody"}
	]),
	resultInfos: generalResultInfos,
	resultDataInfos:[
	     //{field: "isOpen",describe: "授权中心是否开启。"}
	]
},
{
	title: "2.5 凭证 - 删除凭证",
	describe: "删除指定凭证。",
	name: "accountsVoucherDelete",
	vueEl: '#accountsVoucherDelete',
	requestResult: "",
	isShowPublicParam: false,
	method : "GET",
	methods : ["GET"],
	fieldItems: generalFieldItems.concat([
	    {name:"接口",field:"URL",value:"accounts/voucher/delete",type:"text",must:true,describe:"",disabled:true,isParam:false,isPrivate:true},
	    {name:"凭证ID",field:"id",value:1,type:"text",must:true,describe:"",disabled:false,isParam:true,isPrivate:true}
	]),
	resultInfos: generalResultInfos,
	resultDataInfos:[
	     //{field: "isOpen",describe: "授权中心是否开启。"}
	]
},
{
	title: "2.6 凭证 - 新记字号",
	describe: "获取指定账簿下一位凭证记字号数字。",
	name: "accountsVoucherNextTagNumber",
	vueEl: '#accountsVoucherNextTagNumber',
	requestResult: "",
	isShowPublicParam: false,
	method : "GET",
	methods : ["GET"],
	fieldItems: generalFieldItems.concat([
	    {name:"接口",field:"URL",value:"accounts/voucher/nextTagNumber",type:"text",must:true,describe:"",disabled:true,isParam:false,isPrivate:true},
	    {name:"账簿ID",field:"accountsId",value:1,type:"text",must:true,describe:"",disabled:false,isParam:true,isPrivate:true}
	]),
	resultInfos: generalResultInfos,
	resultDataInfos:[
	     //{field: "isOpen",describe: "授权中心是否开启。"}
	]
},
{
	title: "2.7 凭证 - 凭证打印",
	describe: "获取凭证打印文件PDF。",
	name: "accountsVoucherViewPdf",
	vueEl: '#accountsVoucherViewPdf',
	requestResult: "",
	isShowPublicParam: false,
	method : "GET",
	methods : ["GET"],
	isForm :true,
	fieldItems: generalFieldItems.concat([
	    {name:"接口",field:"URL",value:"accounts/voucher/viewPdf/1",type:"text",must:true,describe:"凭证ID信息，多个ID使用英文“,”隔开",disabled:false,isParam:false,isPrivate:true}
	]),
	resultInfos: generalResultInfos,
	resultDataInfos:[
	     //{field: "isOpen",describe: "授权中心是否开启。"}
	]
},
{
	title: "2.8 凭证 - 凭证导出",
	describe: "导出凭证信息为Excel。",
	name: "accountsVoucherExportExcel",
	vueEl: '#accountsVoucherExportExcel',
	requestResult: "",
	isShowPublicParam: false,
	method : "GET",
	methods : ["GET"],
	isForm :true,
	fieldItems: generalFieldItems.concat([
	    {name:"接口",field:"URL",value:"accounts/voucher/exportExcel/1",type:"text",must:true,describe:"凭证ID信息，多个ID使用英文“,”隔开",disabled:false,isParam:false,isPrivate:true}
	]),
	resultInfos: generalResultInfos,
	resultDataInfos:[
	     //{field: "isOpen",describe: "授权中心是否开启。"}
	]
},
{
	title: "3.1 财务收支 - 添加附件",
	describe: "给指定财务收支信息添加附件。",
	name: "accountsFinancialinformAddAccessory",
	vueEl: '#accountsFinancialinformAddAccessory',
	requestResult: "",
	isShowPublicParam: false,
	method : "POST",
	methods : ["POST"],
	fieldItems: generalFieldItems.concat([
	    {name:"接口",field:"URL",value:"accounts/financialinform/addAccessory",type:"text",must:true,describe:"",disabled:true,isParam:false,isPrivate:true},
	    {name:"财务信息ID",field:"id",value:1,type:"text",must:true,describe:"",disabled:false,isParam:true,isPrivate:true},
	    {name:"附件信息",field:"fileInfos",value:getJsonStr([{"name":"测试附件.paf","url":"https://wx.zaijiawangluo.com/image/asset/2018/12/09/5a6d7cfba678398c1c7a72ccddbab680.jpg"}]),type:"textarea",must:true,describe:"",disabled:false,isParam:true,isPrivate:true,dataType:"JsonArray"}
	]),
	resultInfos: generalResultInfos,
	resultDataInfos:[
	     //{field: "isOpen",describe: "授权中心是否开启。"}
	]
},
{
	title: "3.2 财务收支 - 删除附件",
	describe: "给指定财务收支信息添加附件,附件信息除了UID 字段，其他的可以省略掉。",
	name: "accountsFinancialinformDeleteAccessory",
	vueEl: '#accountsFinancialinformDeleteAccessory',
	requestResult: "",
	isShowPublicParam: false,
	method : "POST",
	methods : ["POST"],
	fieldItems: generalFieldItems.concat([
	    {name:"接口",field:"URL",value:"accounts/financialinform/deleteAccessory",type:"text",must:true,describe:"",disabled:true,isParam:false,isPrivate:true},
	    {name:"财务信息ID",field:"id",value:1,type:"text",must:true,describe:"",disabled:false,isParam:true,isPrivate:true},
	    {name:"附件信息",field:"fileInfos",value:getJsonStr([{"uid":1}]),type:"textarea",must:true,describe:"",disabled:false,isParam:true,isPrivate:true,dataType:"JsonArray"}
	]),
	resultInfos: generalResultInfos,
	resultDataInfos:[
	     //{field: "isOpen",describe: "授权中心是否开启。"}
	]
},
{
	title: "4.1 科目 - 获取全部科目",
	describe: "获取全部科目信息。",
	name: "accountsSubjectList",
	vueEl: '#accountsSubjectList',
	requestResult: "",
	isShowPublicParam: false,
	method : "GET",
	methods : ["GET"],
	fieldItems: generalFieldItems.concat([
	    {name:"接口",field:"URL",value:"accounts/subject/list",type:"text",must:true,describe:"",disabled:true,isParam:false,isPrivate:true}
	]),
	resultInfos: generalResultInfos,
	resultDataInfos:[
	     //{field: "isOpen",describe: "授权中心是否开启。"}
	]
},
{
	title: "4.2 科目 - 获取科目",
	describe: "获取指定科目的信息，包含下级科目信息集合。",
	name: "accountsSubjectInfo",
	vueEl: '#accountsSubjectInfo',
	requestResult: "",
	isShowPublicParam: false,
	method : "GET",
	methods : ["GET"],
	fieldItems: generalFieldItems.concat([
	    {name:"接口",field:"URL",value:"accounts/subject/info",type:"text",must:true,describe:"",disabled:true,isParam:false,isPrivate:true},
	    {name:"科目ID",field:"id",value:1,type:"text",must:true,describe:"",disabled:false,isParam:true,isPrivate:true}
	]),
	resultInfos: generalResultInfos,
	resultDataInfos:[
	     //{field: "isOpen",describe: "授权中心是否开启。"}
	]
},
{
	title: "4.3科目 - 获取下级科目",
	describe: "获取指定科目的下级科目信息集合。",
	name: "accountsSubjectListByParentId",
	vueEl: '#accountsSubjectListByParentId',
	requestResult: "",
	isShowPublicParam: false,
	method : "GET",
	methods : ["GET"],
	fieldItems: generalFieldItems.concat([
	    {name:"接口",field:"URL",value:"accounts/subject/listByParentId",type:"text",must:true,describe:"",disabled:true,isParam:false,isPrivate:true},
	    {name:"上级科目ID",field:"parentId",value:1,type:"text",must:true,describe:"",disabled:false,isParam:true,isPrivate:true}
	]),
	resultInfos: generalResultInfos,
	resultDataInfos:[
	     //{field: "isOpen",describe: "授权中心是否开启。"}
	]
},
    {
        title: "4.4 判断是否开通记账 - 首页收支报表月度结余",
        describe: "",
        name: "blance",
        vueEl: '#blance',
        requestResult: "",
        isShowPublicParam: false,
        method : "GET",
        methods : ["GET"],
        fieldItems: generalFieldItems.concat([
            {name:"接口",field:"URL",value:"/accounts/reports/blance",type:"text",must:true,describe:"",disabled:true,isParam:false,isPrivate:true}
        ]),
        resultInfos: generalResultInfos,
        resultDataInfos:[
            //{field: "isOpen",describe: "授权中心是否开启。"}
        ]
    },
    {
        title: "4.5 图表展示 - 月度报表首页",
        describe: "",
        name: "monthStatistical",
        vueEl: '#monthStatistical',
        requestResult: "",
        isShowPublicParam: false,
        method : "GET",
        methods : ["GET"],
        fieldItems: generalFieldItems.concat([
            {name:"接口",field:"URL",value:"/accounts/reports/monthStatistical",type:"text",must:true,describe:"",disabled:true,isParam:false,isPrivate:true},
            {name:"统计日期月",field:"statisticalDate",value:'2020-09',type:"text",must:true,describe:"",disabled:false,isParam:true,isPrivate:true}

        ]),
        resultInfos: generalResultInfos,
        resultDataInfos:[
            //{field: "isOpen",describe: "授权中心是否开启。"}
        ]
    },
    {
        title: "4.6 图表展示 - 年度报表首页",
        describe: "",
        name: "yearStatistical",
        vueEl: '#yearStatistical',
        requestResult: "",
        isShowPublicParam: false,
        method : "GET",
        methods : ["GET"],
        fieldItems: generalFieldItems.concat([
            {name:"接口",field:"URL",value:"/accounts/reports/yearStatistical",type:"text",must:true,describe:"",disabled:true,isParam:false,isPrivate:true},
            {name:"统计日期年",field:"statisticalDate",value:'2020',type:"text",must:true,describe:"",disabled:false,isParam:true,isPrivate:true}

        ]),
        resultInfos: generalResultInfos,
        resultDataInfos:[
            //{field: "isOpen",describe: "授权中心是否开启。"}
        ]
    },
    {
        title: "4.7 查询小区账户",
        describe: "",
        name: "search",
        vueEl: '#search',
        requestResult: "",
        isShowPublicParam: false,
        method : "GET",
        methods : ["GET"],
        fieldItems: generalFieldItems.concat([
            {name:"接口",field:"URL",value:"/accounts/account/search",type:"text",must:true,describe:"",disabled:true,isParam:false,isPrivate:true}
        ]),
        resultInfos: generalResultInfos,
        resultDataInfos:[
            //{field: "isOpen",describe: "授权中心是否开启。"}
        ]
    },
    {
        title: "4.8 修改小区账户",
        describe: "",
        name: "saveOrUpdate",
        vueEl: '#saveOrUpdate',
        requestResult: "",
        isShowPublicParam: false,
        method : "POST",
        methods : ["POST"],
        fieldItems: generalFieldItems.concat([
            {name:"接口",field:"URL",value:"/accounts/account/saveOrUpdate",type:"text",must:true,describe:"",disabled:true,isParam:false,isPrivate:true},
            {name:"开户名",field:"name",value:'sdfs',type:"text",must:true,describe:"",disabled:false,isParam:true,isPrivate:true},
            {name:"开户行",field:"bankname",value:'锦州',type:"text",must:true,describe:"",disabled:false,isParam:true,isPrivate:true},
            {name:"账号",field:"cardnumber",value:'1233333333',type:"text",must:true,describe:"",disabled:false,isParam:true,isPrivate:true}

        ]),
        resultInfos: generalResultInfos,
        resultDataInfos:[
            //{field: "isOpen",describe: "授权中心是否开启。"}
        ]
    },
    {
        title: "4.9 月报表详情页",
        describe: "",
        name: "monthdetail",
        vueEl: '#monthdetail',
        requestResult: "",
        isShowPublicParam: false,
        method : "GET",
        methods : ["GET"],
        fieldItems: generalFieldItems.concat([
            {name:"接口",field:"URL",value:"/accounts/reports/monthdetail",type:"text",must:true,describe:"",disabled:true,isParam:false,isPrivate:true},
            {name:"统计日期月",field:"statisticalDate",value:'2020-09',type:"text",must:true,describe:"",disabled:false,isParam:true,isPrivate:true}

        ]),
        resultInfos: generalResultInfos,
        resultDataInfos:[
            //{field: "isOpen",describe: "授权中心是否开启。"}
        ]
    },
    {
        title: "5.0 年度报表详情页",
        describe: "",
        name: "yeardetail",
        vueEl: '#yeardetail',
        requestResult: "",
        isShowPublicParam: false,
        method : "GET",
        methods : ["GET"],
        fieldItems: generalFieldItems.concat([
            {name:"接口",field:"URL",value:"/accounts/reports/yeardetail",type:"text",must:true,describe:"",disabled:true,isParam:false,isPrivate:true},
            {name:"统计日期年",field:"statisticalDate",value:'2020',type:"text",must:true,describe:"",disabled:false,isParam:true,isPrivate:true}

        ]),
        resultInfos: generalResultInfos,
        resultDataInfos:[
            //{field: "isOpen",describe: "授权中心是否开启。"}
        ]
    },
    {
        title: "5.1 查看科目详情页",
        describe: "",
        name: "subjectDetail",
        vueEl: '#subjectDetail',
        requestResult: "",
        isShowPublicParam: false,
        method : "GET",
        methods : ["GET"],
        fieldItems: generalFieldItems.concat([
            {name:"接口",field:"URL",value:"/accounts/reports/subjectDetail",type:"text",must:true,describe:"",disabled:true,isParam:false,isPrivate:true},
            {name:"统计日期年",field:"statisticalDate",value:'2020',type:"text",must:true,describe:"",disabled:false,isParam:true,isPrivate:true},
            {name:"科目id",field:"subjectId",value:'2',type:"text",must:true,describe:"",disabled:false,isParam:true,isPrivate:true}


        ]),
        resultInfos: generalResultInfos,
        resultDataInfos:[
            //{field: "isOpen",describe: "授权中心是否开启。"}
        ]
    },
    {
        title: "5.2 月度报表导出pdf",
        describe: "",
        name: "monthdetailviewPdf",
        vueEl: '#monthdetailviewPdf',
        requestResult: "",
        isShowPublicParam: false,
        method : "GET",
        methods : ["GET"],
		isForm:true,
        fieldItems: generalFieldItems.concat([
            {name:"接口",field:"URL",value:"/accounts/reports/monthdetailviewPdf/2020-09",type:"text",must:true,describe:"",disabled:false,isParam:false,isPrivate:true}
        ]),
        resultInfos: generalResultInfos,
        resultDataInfos:[
            //{field: "isOpen",describe: "授权中心是否开启。"}
        ]
    },
    {
        title: "5.3 年度报表导出pdf",
        describe: "",
        name: "yeardetailviewPdf",
        vueEl: '#yeardetailviewPdf',
        requestResult: "",
        isShowPublicParam: false,
        method : "GET",
        methods : ["GET"],
        isForm:true,
        fieldItems: generalFieldItems.concat([
            {name:"接口",field:"URL",value:"/accounts/reports/yeardetailviewPdf/2020",type:"text",must:true,describe:"",disabled:true,isParam:false,isPrivate:true}
        ]),
        resultInfos: generalResultInfos,
        resultDataInfos:[
            //{field: "isOpen",describe: "授权中心是否开启。"}
        ]
    },
    {
        title: "5.4 收支详情 - 分页查询",
        describe: "分页查询凭证信息。",
        name: "list",
        vueEl: '#list',
        requestResult: "",
        isShowPublicParam: false,
        method : "GET",
        methods : ["GET"],
        fieldItems: generalFieldItems.concat([
            {name:"接口",field:"URL",value:"/accounts/financialinform/list",type:"text",must:true,describe:"",disabled:true,isParam:false,isPrivate:true},
            {name:"记字号数字",field:"tagNumber",value:1,type:"text",must:false,describe:"",disabled:false,isParam:true,isPrivate:true},
            {name:"科目ID",field:"subjectId",value:1,type:"text",must:false,describe:"",disabled:false,isParam:true,isPrivate:true,dataType:"NumberArray"},
            {name:"开始日期",field:"startDate",value:"2020-08-29",type:"text",must:false,describe:"",disabled:false,isParam:true,isPrivate:true},
            {name:"结束日期",field:"endDate",value:"2020-08-29",type:"text",must:true,describe:"",disabled:false,isParam:true,isPrivate:true},
            {name:"辅助账",field:"auxiliary",value:1,type:"text",must:true,describe:"",disabled:false,isParam:true,isPrivate:true},
            {name:"摘要",field:"digest",value:1,type:"text",must:true,describe:"",disabled:false,isParam:true,isPrivate:true},
            {name:"页码",field:"page",value:1,type:"text",must:true,describe:"",disabled:false,isParam:true,isPrivate:true},
            {name:"每页数量",field:"limit",value:5,type:"text",must:true,describe:"",disabled:false,isParam:true,isPrivate:true}
        ]),
        resultInfos: generalResultInfos,
        resultDataInfos:[
            //{field: "isOpen",describe: "授权中心是否开启。"}
        ]
    },
    {
        title: "5.5 收支详情导出pdf",
        describe: "",
        name: "financialinformviewPdf",
        vueEl: '#financialinformviewPdf',
        requestResult: "",
        isShowPublicParam: false,
        method : "GET",
        methods : ["GET"],
        isForm:true,
        fieldItems: generalFieldItems.concat([
            {name:"接口",field:"URL",value:"/accounts/financialinform/financialinformviewPdf",type:"text",must:true,describe:"",disabled:false,isParam:false,isPrivate:true},
            {name:"记字号数字",field:"tagNumber",value:1,type:"text",must:false,describe:"",disabled:false,isParam:true,isPrivate:true},
            {name:"科目ID",field:"subjectId",value:[1],type:"text",must:false,describe:"",disabled:false,isParam:true,isPrivate:true,dataType:"NumberArray"},
            {name:"开始日期",field:"startDate",value:"2020-08-29",type:"text",must:false,describe:"",disabled:false,isParam:true,isPrivate:true},
            {name:"结束日期",field:"endDate",value:"2020-08-29",type:"text",must:true,describe:"",disabled:false,isParam:true,isPrivate:true},
            {name:"辅助账",field:"auxiliary",value:1,type:"text",must:true,describe:"",disabled:false,isParam:true,isPrivate:true},
            {name:"摘要",field:"digest",value:1,type:"text",must:true,describe:"",disabled:false,isParam:true,isPrivate:true},
            {name:"页码",field:"page",value:1,type:"text",must:true,describe:"",disabled:false,isParam:true,isPrivate:true},
            {name:"每页数量",field:"limit",value:5,type:"text",must:true,describe:"",disabled:false,isParam:true,isPrivate:true}
        ]),
        resultInfos: generalResultInfos,
        resultDataInfos:[
            //{field: "isOpen",describe: "授权中心是否开启。"}
        ]
    },
    {
        title: "5.6 收支录入统计",
        describe: "",
        name: "recorded",
        vueEl: '#recorded',
        requestResult: "",
        isShowPublicParam: false,
        method : "GET",
        methods : ["GET"],
        fieldItems: generalFieldItems.concat([
            {name:"接口",field:"URL",value:"/accounts/reports/recorded",type:"text",must:true,describe:"",disabled:true,isParam:false,isPrivate:true},
            {name:"统计日期",field:"statisticalDate",value:'2020-09',type:"text",must:true,describe:"",disabled:false,isParam:true,isPrivate:true}

        ]),
        resultInfos: generalResultInfos,
        resultDataInfos:[
            //{field: "isOpen",describe: "授权中心是否开启。"}
        ]
    },
    {
        title: "5.7 获取月度报表对账单",
        describe: "",
        name: "getFileInfoVO",
        vueEl: '#getFileInfoVO',
        requestResult: "",
        isShowPublicParam: false,
        method : "GET",
        methods : ["GET"],
        fieldItems: generalFieldItems.concat([
            {name:"接口",field:"URL",value:"/accounts/reports/getFileInfoVO",type:"text",must:true,describe:"",disabled:true,isParam:false,isPrivate:true},
            {name:"统计日期",field:"statisticalDate",value:'2020-09',type:"text",must:true,describe:"",disabled:false,isParam:true,isPrivate:true}

        ]),
        resultInfos: generalResultInfos,
        resultDataInfos:[
            //{field: "isOpen",describe: "授权中心是否开启。"}
        ]
    }
];

var panelBoxVue = new Vue({

	el: "#panelBox",
	data: {
		//API信息数组
		apiInfos: apiInfos
	},
	'/api':{
    	target: "https://www.v2ex.com/api",
    	changeOrigin:true,
    	pathRewrite:{
        	'^/api':''
    	}
	},
	methods: {
		getRequestPamams: function (apiInfo) {
			var requestParams = {};
			for (var i = 0; i < apiInfo.fieldItems.length; i++) {
				if (apiInfo.fieldItems[i].isParam) {
					var reuqestParamValue = apiInfo.fieldItems[i].value;
					if(apiInfo.fieldItems[i].dataType && typeof(reuqestParamValue) == 'string'){
						if(apiInfo.fieldItems[i].dataType == "NumberArray"){
		    				var rArray = reuqestParamValue.split(',');
		    				if(rArray.indexOf("")<0){
		    					reuqestParamValue = rArray.filter(function(v){return v && v.trim();}).map(Number);
		    				}
		    				if($.trim(reuqestParamValue)==""){
								reuqestParamValue = null;
							}
		    			}
		    			else if(apiInfo.fieldItems[i].dataType == "JsonArray"){
		    				try{
		    					reuqestParamValue = JSON.parse(reuqestParamValue);
							}catch(e){}
						}
		    			else if(apiInfo.fieldItems[i].dataType == "JsonBody"){
		    				try{
		    					requestParams = JSON.parse(reuqestParamValue);
		    				}catch(e){}
		    				break;
						}
					}
					requestParams[apiInfo.fieldItems[i].field] = reuqestParamValue;
				}
			}
			return requestParams;
		},
		submitRequest: function (apiInfo) {
			apiInfo.requestResult = "正在提交请求，请等待...";
			var callVueObj = apiInfo;
			if(apiInfo.method == "GET"){
				if(apiInfo.isForm && apiInfo.isForm == true)
				{
					var formMehotdByGet = $("#FormMehotdByGet");
					if(!formMehotdByGet || formMehotdByGet.attr("id") != "FormMehotdByGet"){
						formMehotdByGet = $('<form id="FormMehotdByGet" action="" method="get" target="_blank"></form>');
						$("body").append(formMehotdByGet);
					}
					formMehotdByGet.attr("action","../../"+apiInfo.fieldItems[0].value);
					formMehotdByGet.html("");
					var getParams = this.getRequestPamams(apiInfo);
					$.each(getParams,function(name,value) {
						if(typeof(value) == "object"){
							value = JSON.stringify(value);
						}
						var inputItem = $('<intpu type="hidden" name="'+name+'" value="'+value+'"></input>');
						formMehotdByGet.append(inputItem);
					});
					formMehotdByGet.submit();
				}
				else{
					axios.get("../../"+apiInfo.fieldItems[0].value,{ params : this.getRequestPamams(apiInfo)}).then(function (response) {
						callVueObj.requestResult = response.data;
					}).catch(function (error) {
						callVueObj.requestResult = "GET请求失败:" + error.message;
					});
				}
			}
			else if(apiInfo.method == "POST"){
				axios.post("../../"+apiInfo.fieldItems[0].value, this.getRequestPamams(apiInfo)).then(function (response) {
					callVueObj.requestResult = response.data;
				}).catch(function (error) {
					callVueObj.requestResult = "POST请求失败:" + error.message;
				});
			}
			else{
				callVueObj.requestResult = "无效的提交方式";
			}
		},
		showPublicParams: function (apiInfo) {
			apiInfo.isShowPublicParam = !apiInfo.isShowPublicParam;
		},
	    inputValue : function (e,fieldItem) {
	    	
	    }
	}
});

function getJsonStr(json){
	if(typeof(json) == "object"){
		json = JSON.stringify(json);
	}
	json = formatJson(json).replace(/(^[\r\n]+)|([\r\n]{2,})/g,"\r\n");
	return json;
}

function formatJson(json, options) {
    var reg = null,
    formatted = '',
    pad = 0,
    PADDING = '    '; // one can also use '\t' or a different number of spaces
  // optional settings
  options = options || {};
  // remove newline where '{' or '[' follows ':'
  options.newlineAfterColonIfBeforeBraceOrBracket = (options.newlineAfterColonIfBeforeBraceOrBracket === true) ? true : false;
  // use a space after a colon
  options.spaceAfterColon = (options.spaceAfterColon === false) ? false : true;

  // begin formatting...

  // make sure we start with the JSON as a string
  if (typeof json !== 'string') {
      json = JSON.stringify(json);
  }
  // parse and stringify in order to remove extra whitespace
  json = JSON.parse(json);
  json = JSON.stringify(json);

  // add newline before and after curly braces
  reg = /([\{\}])/g;
  json = json.replace(reg, '\r\n$1\r\n');

  // add newline before and after square brackets
  reg = /([\[\]])/g;
  json = json.replace(reg, '\r\n$1\r\n');

  // add newline after comma
  reg = /(\,)/g;
  json = json.replace(reg, '$1\r\n');

  // remove multiple newlines
  reg = /(\r\n\r\n)/g;
  json = json.replace(reg, '\r\n');

  // remove newlines before commas
  reg = /\r\n\,/g;
  json = json.replace(reg, ',');

  // optional formatting...
  if (!options.newlineAfterColonIfBeforeBraceOrBracket) {
      reg = /\:\r\n\{/g;
      json = json.replace(reg, ':{');
      reg = /\:\r\n\[/g;
      json = json.replace(reg, ':[');
  }
  if (options.spaceAfterColon) {
      reg = /\:/g;
      json = json.replace(reg, ': ');
  }

  $.each(json.split('\r\n'), function(index, node) {
      var i = 0,
        indent = 0,
        padding = '';

      if (node.match(/\{$/) || node.match(/\[$/)) {
          indent = 1;
      } else if (node.match(/\}/) || node.match(/\]/)) {
          if (pad !== 0) {
              pad -= 1;
          }
      } else {
          indent = 0;
      }

      for (i = 0; i < pad; i++) {
          padding += PADDING;
      }
      if(node != ""){
    	  if(formatted != ""){formatted += '\r\n'}
    	  formatted += padding + node;
      }
      pad += indent;
  });

  return formatted;
}

