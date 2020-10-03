/**
 * Created by cutie on 2018/10/28.
 */
//七牛云上传token
var qiniuToken;
var qiniuDomain;

function initQiniuUpload(uploadBtnId, uploadInputFileId, showUrlInputId, uploadImgId) {//singerHeadUploadBtn singerHeadUploadInputFile vm.singer.headIconUrl
    $('#'+ uploadBtnId).click(function(){
        $('#'+ uploadInputFileId).click();
        //获取token和domain
        $.get(baseURL + "sys/oss/getTokenDomain", function (r) {
            console.log("qiniu tokenDomain : " + r.tokenDomain.token);
            console.log("qiniu tokenDomain : " + r.tokenDomain.domain);
            qiniuToken = r.tokenDomain.token;
            qiniuDomain = r.tokenDomain.domain + "/";
        });
    });

// 上传歌曲封面
    $("#" + uploadInputFileId).change(function(){
        //检查文件是否选择
        if(!$("#" + uploadInputFileId).val()){
            return false;
        }
        uploadQiniu();
    });

//直接上传
    function uploadQiniu() {
        var QiNiu_upload = function (file, token, key) {
            loding = parent.layer.msg('上传中,请稍等', { icon: 16 ,shade:0.5,time:0 });
            var formData = new FormData();
            if(key !== null && key !== undefined){
                formData.append("key", key);
            }
            formData.append("token", token);
            formData.append("file", file);
            var xhr = new XMLHttpRequest();

            xhr.open("POST", "http://up-z1.qiniu.com");
            xhr.onload = function () {
                if (xhr.status == 200) {
                    var res = JSON.parse(xhr.responseText);
                    var uploadUrl = qiniuDomain + res.key;
                    //歌手头像url绑定

                    // console.log($("#" + vmObj).val() + "  ;" + vm.singer.headIconUrl);
                    // if(vm.singer.headIconUrl != undefined){
                    //     vm.singer.headIconUrl = uploadUrl;
                    // }
                    $("#" + showUrlInputId).val(uploadUrl);
                    $("#" + uploadImgId).attr('src',uploadUrl);
                    parent.layer.close(loding);
                } else {
                    parent.layer.close(loding);
                    parent.layer.msg("未上传成功！", {icon: 1});
                }
            };
            xhr.send(formData);
        };

        if ($("#" + uploadInputFileId)[0].files.length > 0 && qiniuToken != "") {
            var file = $("#" + uploadInputFileId)[0].files[0];
            // 在key后面加时间戳
            var keyBefore = $("#" + uploadInputFileId)[0].files[0].name;
            var keyArr = keyBefore.split('.');
            keyArr[0] = keyArr[0]+'_'+new Date().getTime();
            var key = keyArr.join(".");
            //上传图片
            QiNiu_upload(file, qiniuToken, key);
        } else {
            console.log("form input error");
        }
    }
}

/*aliyun*/
// var element1 = "", value1 = "";
function initAliyunUpload(uploadBtnId, uploadInputFileId, functionName) {//singerHeadUploadBtn singerHeadUploadInputFile vm.singer.headIconUrl   showUrlInputId, uploadImgId,
    $('#'+ uploadBtnId).click(function(){
        $('#'+ uploadInputFileId).click();
    });

// 上传歌曲封面
    $("#" + uploadInputFileId).change(function(){
        //检查文件是否选择
        if(!$("#" + uploadInputFileId).val()){
            return false;
        }
        uploadAliyun();
    });

//直接上传
    function uploadAliyun() {
        var Aliyun_upload = function (file, key) {
            loding = parent.layer.msg('上传中,请稍等', { icon: 16 ,shade:0.5,time:0 });
            client.multipartUpload(key, file).then(function (result) {
                console.log(result);
                var returnUrl = result.url;
                if(returnUrl == null || returnUrl == ""){
                    returnUrl = result.res.requestUrls[0];
                    returnUrl = returnUrl.replace(returnUrl.substr(returnUrl.indexOf("?uploadId")), "");
                }
                // $("#" + showUrlInputId).val(returnUrl);
                // $("#" + uploadImgId).attr('src',returnUrl);
                //歌手头像上传使用
                debugger;
                // if (null != vm.singer){
                //     vm.singer.headIconUrl = returnUrl;
                // }
                eval(functionName+"(\""+ returnUrl +"\")");
                parent.layer.close(loding);
            }).catch(function (err) {
                console.log(err);
                parent.layer.close(loding);
                parent.layer.msg("未上传成功！", {icon: 1});
            });
        };

        if ($("#" + uploadInputFileId)[0].files.length > 0 && qiniuToken != "") {
            var file = $("#" + uploadInputFileId)[0].files[0];
            // 在key后面加时间戳
            var keyBefore = $("#" + uploadInputFileId)[0].files[0].name;
            var keyArr = keyBefore.split('.');
            keyArr[0] = keyArr[0]+'_'+new Date().getTime();
            var key = keyArr.join(".");
            //上传图片
            Aliyun_upload(file, key);
        } else {
            console.log("form input error");
        }
    }
}

/*ali oss client*/
var client = new OSS.Wrapper({
    region : 'oss-cn-beijing',
    accessKeyId : 'LTAIgNbt3Lgz8NmF',
    accessKeySecret : 'OYaGQb6lftWORYbniSrYVjOVWB91fY',
    bucket : 'yeweihui'
});

function aliyunUpload(){
    //调用插入文件上传的方法
    var file = files[0];
    var fileName = file.name;
    var suffix = fileName.substr(fileName.indexOf("."));
    var storeAs = "demo/" + timestamp() + suffix;
    console.log(file.name + ' => ' + storeAs);
    client.multipartUpload(storeAs, file).then(function (result) {
        console.log(result);
        var returnUrl = result.url;
        if(returnUrl == null || returnUrl == ""){
            returnUrl = result.res.requestUrls[0];
            returnUrl = returnUrl.replace(returnUrl.substr(returnUrl.indexOf("?uploadId")), "");
        }
    }).catch(function (err) {
        console.log(err);
        alert(err);
    });
}

/**
 * 获取时间戳
 * @returns
 */
function timestamp(){
    var time = new Date();
    var y = time.getFullYear();
    var m = time.getMonth()+1;
    var d = time.getDate();
    var h = time.getHours();
    var mm = time.getMinutes();
    var s = time.getSeconds();
    var ms = time.getMilliseconds();
    console.log(y);
    return ""+y+add0(m)+add0(d)+add0(h)+add0(mm)+add0(s)+ms;
}
function add0(m){
    return m<10?'0'+m : m;
}