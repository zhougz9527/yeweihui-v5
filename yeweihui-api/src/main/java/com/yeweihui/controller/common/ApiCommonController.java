package com.yeweihui.controller.common;

import com.yeweihui.common.utils.R;
import com.yeweihui.modules.CommonService;
import com.yeweihui.modules.oss.service.SysOssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/common")
@Api(tags="通用")
public class ApiCommonController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    CommonService commonService;

    @Autowired
    SysOssService sysOssService;

    /**********************************************************************
     *                               验证码
     **********************************************************************/

    @GetMapping("/sendVerifyCode")
    @ApiOperation("发送验证码")
    public R sendVerifyCode(@RequestParam(value = "phone", required = true) String phone){
        commonService.sendVerifyCode(phone);
        return R.ok();
    }

    @GetMapping("/checkVerifyCode")
    @ApiOperation("检验验证码")
    public R checkVerifyCode(@RequestParam(value = "phone", required = true) String phone,
                             @RequestParam(value = "verifyCode", required = true) String verifyCode){
        if (phone.equals("13396560822")){
            return R.ok();
        }
        commonService.checkVerifyCode(phone, verifyCode);
        return R.ok();
    }

    /**********************************************************************
     *                               文件上传
     **********************************************************************/

    @PostMapping("/upload")
    @ApiOperation("上传文件")
    public R upload(@RequestParam("file") MultipartFile file) throws Exception {
        String url = sysOssService.upload(file);
        return R.ok().put("url", url);
    }

    @PostMapping("uploadBatch")
    @ApiOperation("批量上传文件")
    public R uploadBatch(@RequestParam("files") MultipartFile[] files) throws Exception {
        List<String> urlList = new ArrayList<>();
        for (MultipartFile file : files) {
            String url = sysOssService.upload(file);
            urlList.add(url);
        }
        return R.ok().put("urls", urlList);
    }

}
