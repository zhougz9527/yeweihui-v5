package com.yeweihui.modules.jmkj.controller;

import com.yeweihui.common.utils.R;
import com.yeweihui.modules.jmkj.service.impl.JmkjServiceImpl;
import com.yeweihui.modules.sys.shiro.ShiroUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
     * 添加当月登录时间
     * */
    @GetMapping(value = "/updateTime")
    @ApiOperation(value = "更新当月登录时间",notes = "time:增加的时间数 单位秒")
    public R updateTime(@RequestParam(value = "time") Long time){

       return R.ok().put("data",jmkjServiceImpl.updateTime(ShiroUtils.getUserId(),time));
    }

    /**
     * 添加当月登录次数
     * */
    @GetMapping(value = "/updateNum")
    @ApiOperation(value = "更新当月登录次数")
    public R updateNum(){

        return R.ok().put("data",jmkjServiceImpl.updateNum(ShiroUtils.getUserId()));
    }

    /**
     * 我的数据
     * */
    @GetMapping(value = "/myData")

    @ApiOperation(value = "获取上月在线时长和上月登录次数")
    public R getMyData(){

        return R.ok().put("data",jmkjServiceImpl.getMyData(ShiroUtils.getUserId()));
    }

    /**
     * 操作性任务查询
     * */
    @GetMapping(value = "/operationTask")
    @ApiOperation(value = "操作性任务查询",notes = "type:1 履职量 2履职率")
    public R operationTask(
            @RequestParam(value = "type") int type,
            @RequestParam(value = "zoneId") Long zoneId,
            @RequestParam(value = "timeStart",required=false) Long timeStart,
            @RequestParam(value = "timeEnd",required=false) Long timeEnd
    ){

        return R.ok().put("data",jmkjServiceImpl.operationTask(type,ShiroUtils.getUserId().longValue(),zoneId,timeStart,timeEnd));
    }


}
