package com.yeweihui.modules.sys.controller;

import com.yeweihui.common.utils.R;
import com.yeweihui.modules.common.service.MainPageService;
import com.yeweihui.modules.vo.api.vo.MainPage1VO;
import com.yeweihui.modules.vo.query.UserIdQueryParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("sys/index")
public class SysIndexController extends AbstractController {

    @Autowired
    private MainPageService mainPageService;

    @GetMapping("/mainPage1")
    @ApiOperation("首页头部信息1（小区信息，待我用章审批数量，待我事项表决数量，网上发函数量，费用报销数量）")
    public R mainPage1() {
        Long uid = getUserId();
        UserIdQueryParam param = new UserIdQueryParam();
        param.setUid(uid);
        MainPage1VO mainPage1VO = mainPageService.mainPage1(param);
        return R.ok().put("mainPage1", mainPage1VO);
    }
}
