package com.yeweihui.controller.jmkj.controller;

import com.yeweihui.common.utils.R;
import com.yeweihui.modules.jmkj.service.impl.JmkjServiceImpl;
import com.yeweihui.modules.sys.shiro.ShiroUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *
 * 基沐科技新加功能
 *
 * */

@RestController
@RequestMapping("jmkj")
public class jmkj {

    @Autowired
    JmkjServiceImpl jmkjServiceImpl;


    /**
     * 获取该用户上下级行业主管列表
     * */
    @GetMapping(value = "/administratorList")
    @ApiOperation(value = "获取该用户上下级行业主管列表")
    public R updateTime(){

        return R.ok().put("data",jmkjServiceImpl.administratorList(ShiroUtils.getUserId()));
    }

}
