package com.yeweihui.controller.test;


import com.yeweihui.annotation.Login;
import com.yeweihui.annotation.LoginUser;
import com.yeweihui.common.utils.R;
import com.yeweihui.modules.common.dao.MultimediaDao;
import com.yeweihui.modules.common.service.MultimediaService;
import com.yeweihui.modules.user.entity.UserEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试接口
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-23 15:47
 * @modified cutie 20181124 user --> appuser
 */
@RestController
@RequestMapping("/api")
@Api(tags = "测试接口")
@ApiIgnore
public class ApiTestController {

    @Resource
    MultimediaDao multimediaDao;
    @Resource
    MultimediaService multimediaService;

    @Login
    @GetMapping("userInfo")
    @ApiOperation(value = "获取用户信息", response = UserEntity.class, authorizations = {@Authorization(value = "token")})
    public R userInfo(@ApiIgnore @LoginUser UserEntity user) {
        return R.ok().put("user", user);
    }

    @Login
    @GetMapping("userId")
//    @ApiOperation(value = "获取用户ID", authorizations = {@Authorization(value = "token")}) 如果swagger没配置securityContexts则需要一个一个设置
    @ApiOperation(value = "获取用户ID")
    public R userInfo(@ApiIgnore @RequestAttribute("userId") Integer userId) {
        return R.ok().put("userId", userId);
    }

    @GetMapping("notToken")
    @ApiOperation("忽略Token验证测试")
    public R notToken() {
        return R.ok().put("msg", "无需token也能访问。。。");
    }

    @PostMapping("updateMultimedia")
    public R updateMultimedia(@RequestParam("files") MultipartFile[] files) throws Exception {
        return R.ok(multimediaService.insertBatchByPublicity(files));
    }

}
