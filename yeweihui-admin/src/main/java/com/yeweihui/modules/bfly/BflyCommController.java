package com.yeweihui.modules.bfly;

import com.yeweihui.common.utils.R;
import com.yeweihui.modules.oss.service.SysOssService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
@RequestMapping("bflyComm")
public class BflyCommController {

    @Resource
    SysOssService sysOssService;

    @Value("${aliyun.oss.comm.bucket}")
    String commBucket;


    @PostMapping("/upload")
    @ApiOperation("上传文件")
    public R upload(@RequestParam("file") MultipartFile file) {
        String url = sysOssService.upload(file, commBucket);
        return R.ok().put("url", url);
    }


}
