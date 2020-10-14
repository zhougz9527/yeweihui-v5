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
     * 履职数据 操作性任务查询 履职量
     * */
    @GetMapping(value = "/getPerformanceOfDutiesList")
    @ApiOperation(value = "履职数据 操作性任务查询 履职量")
    public R getPerformanceOfDutiesList(
            @RequestParam(value = "zoneId") Long zoneId,
            @RequestParam(value = "timeStart",required=false) Long timeStart,
            @RequestParam(value = "timeEnd",required=false) Long timeEnd
    ){

        return R.ok().put("data",jmkjServiceImpl.getPerformanceOfDutiesList(ShiroUtils.getUserId().longValue(),zoneId,timeStart,timeEnd));
    }

    /**
     * 履职数据 操作性任务查询 履职率
     * */
    @GetMapping(value = "/getPerformanceRateBeans")
    @ApiOperation(value = "履职数据 操作性任务查询 履职率")
    public R getPerformanceRateBeans(
            @RequestParam(value = "zoneId") Long zoneId,
            @RequestParam(value = "timeStart",required=false) Long timeStart,
            @RequestParam(value = "timeEnd",required=false) Long timeEnd
    ){

        return R.ok().put("data",jmkjServiceImpl.getPerformanceRateBeans(ShiroUtils.getUserId().longValue(),zoneId,timeStart,timeEnd));
    }

    /**
     * 履职数据 操作性任务查询 逾期量
     * */
    @GetMapping(value = "/OverdueQuantity")
    @ApiOperation(value = "履职数据 操作性任务查询 逾期量")
    public R OverdueQuantity(
            @RequestParam(value = "zoneId") Long zoneId,
            @RequestParam(value = "timeStart",required=false) Long timeStart,
            @RequestParam(value = "timeEnd",required=false) Long timeEnd
    ){

        return R.ok().put("data",jmkjServiceImpl.OverdueQuantity(ShiroUtils.getUserId().longValue(),zoneId,timeStart,timeEnd));
    }

    /**
     * 履职数据 操作性任务查询 逾期率
     * */
    @GetMapping(value = "/OverdueRate")
    @ApiOperation(value = "履职数据 操作性任务查询 逾期率")
    public R OverdueRate(
            @RequestParam(value = "zoneId") Long zoneId,
            @RequestParam(value = "timeStart",required=false) Long timeStart,
            @RequestParam(value = "timeEnd",required=false) Long timeEnd
    ){

        return R.ok().put("data",jmkjServiceImpl.OverdueRate(ShiroUtils.getUserId().longValue(),zoneId,timeStart,timeEnd));
    }

    /**
     * 操作性任务新建总量
     * */
    @GetMapping(value = "/operationNum")
    @ApiOperation(value = "履职数据 操作性任务查询 新建总量")
    public R operationNum(
            @RequestParam(value = "zoneId") Long zoneId,
            @RequestParam(value = "timeStart",required=false) Long timeStart,
            @RequestParam(value = "timeEnd",required=false) Long timeEnd
    ){

        return R.ok().put("data",jmkjServiceImpl.operationNum(ShiroUtils.getUserId().longValue(),zoneId,timeStart,timeEnd));
    }

    /**
     * 浏览任务完成总量
     * */
    @GetMapping(value = "/BrowseComplete")
    @ApiOperation(value = "履职数据 浏览任务 完成总量")
    public R BrowseComplete(
            @RequestParam(value = "zoneId") Long zoneId,
            @RequestParam(value = "timeStart",required=false) Long timeStart,
            @RequestParam(value = "timeEnd",required=false) Long timeEnd
    ){

        return R.ok().put("data",jmkjServiceImpl.BrowseComplete(ShiroUtils.getUserId().longValue(),zoneId,timeStart,timeEnd));
    }

    /**
     * 浏览任务新建总量
     * */
    @GetMapping(value = "/NewBrowse")
    @ApiOperation(value = "履职数据 浏览任务 新建总量")
    public R NewBrowse(
            @RequestParam(value = "zoneId") Long zoneId,
            @RequestParam(value = "timeStart",required=false) Long timeStart,
            @RequestParam(value = "timeEnd",required=false) Long timeEnd
    ){

        return R.ok().put("data",jmkjServiceImpl.NewBrowse(ShiroUtils.getUserId().longValue(),zoneId,timeStart,timeEnd));
    }

    /**
     * 履职数据 在线时长
     * */
    @GetMapping(value = "/OnlineDuration")
    @ApiOperation(value = "履职数据 在线时长")
    public R OnlineDuration(
            @RequestParam(value = "zoneId") Long zoneId,
            @RequestParam(value = "timeStart",required=false) Long timeStart,
            @RequestParam(value = "timeEnd",required=false) Long timeEnd
    ){

        return R.ok().put("data",jmkjServiceImpl.OnlineDuration(ShiroUtils.getUserId().longValue(),zoneId,timeStart,timeEnd));
    }

    @GetMapping(value = "/OnlineNum")
    @ApiOperation(value = "履职数据 登录次数")
    public R OnlineNum(
            @RequestParam(value = "zoneId") Long zoneId,
            @RequestParam(value = "timeStart",required=false) Long timeStart,
            @RequestParam(value = "timeEnd",required=false) Long timeEnd
    ){

        return R.ok().put("data",jmkjServiceImpl.OnlineNum(ShiroUtils.getUserId().longValue(),zoneId,timeStart,timeEnd));
    }
}
