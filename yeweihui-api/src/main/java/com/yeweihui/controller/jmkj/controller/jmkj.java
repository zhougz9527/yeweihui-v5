package com.yeweihui.controller.jmkj.controller;

import com.yeweihui.annotation.LoginUser;
import com.yeweihui.common.utils.R;
import com.yeweihui.modules.jmkj.service.impl.JmkjServiceImpl;
import com.yeweihui.modules.user.entity.UserEntity;
import com.yeweihui.modules.user.service.TokenService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *
 * 基沐科技新加功能
 *
 * */

@RestController
@RequestMapping("api/jmkj")
public class jmkj {

    @Autowired
    JmkjServiceImpl jmkjServiceImpl;

    @Autowired
    private TokenService tokenService;

    /**
     * 获取该用户上下级行业主管列表
     * */
    @GetMapping(value = "/administratorList")
    @ApiOperation(value = "获取该用户上下级行业主管列表")
    public R updateTime(@LoginUser UserEntity user){

        return R.ok().put("data",jmkjServiceImpl.administratorList(user.getId()));
    }

    @GetMapping(value = "/getToken")
    public R getToken(){


        return R.ok().put("data",tokenService.createToken(50).getToken());
    }

    /**
     * 添加当月登录时间
     * */
    @GetMapping(value = "/updateTime")
    @ApiOperation(value = "更新当月登录时间",notes = "time:增加的时间数 单位秒")
    public R updateTime(@LoginUser UserEntity user,@RequestParam(value = "time") Long time){

        return R.ok().put("data",jmkjServiceImpl.updateTime(user.getId(),time));
    }

    /**
     * 添加当月登录次数
     * */
    @GetMapping(value = "/updateNum")
    @ApiOperation(value = "更新当月登录次数")
    public R updateNum(@LoginUser UserEntity user){

        return R.ok().put("data",jmkjServiceImpl.updateNum(user.getId()));
    }

    /**
     * 履职数据 操作性任务查询 履职量
     * */
    @GetMapping(value = "/getPerformanceOfDutiesList")
    @ApiOperation(value = "履职数据 操作性任务查询 履职量")
    public R getPerformanceOfDutiesList(
            @LoginUser UserEntity user,
            @RequestParam(value = "zoneId") Long zoneId,
            @RequestParam(value = "timeStart",required=false) Long timeStart,
            @RequestParam(value = "timeEnd",required=false) Long timeEnd
    ){

//        return R.ok().put("data",jmkjServiceImpl.getPerformanceOfDutiesList(ShiroUtils.getUserId().longValue(),zoneId,timeStart,timeEnd));
        return R.ok().put("data",jmkjServiceImpl.getPerformanceOfDutiesList(user.getId(),zoneId,timeStart,timeEnd));
    }

    /**
     * 履职数据 操作性任务查询 履职率
     * */
    @GetMapping(value = "/getPerformanceRateBeans")
    @ApiOperation(value = "履职数据 操作性任务查询 履职率")
    public R getPerformanceRateBeans(
            @LoginUser UserEntity user,
            @RequestParam(value = "zoneId") Long zoneId,
            @RequestParam(value = "timeStart",required=false) Long timeStart,
            @RequestParam(value = "timeEnd",required=false) Long timeEnd
    ){

        //return R.ok().put("data",jmkjServiceImpl.getPerformanceRateBeans(ShiroUtils.getUserId().longValue(),zoneId,timeStart,timeEnd));
        return R.ok().put("data",jmkjServiceImpl.getPerformanceRateBeans(user.getId(),zoneId,timeStart,timeEnd));
    }

    /**
     * 履职数据 操作性任务查询 逾期量
     * */
    @GetMapping(value = "/OverdueQuantity")
    @ApiOperation(value = "履职数据 操作性任务查询 逾期量")
    public R OverdueQuantity(
            @LoginUser UserEntity user,
            @RequestParam(value = "zoneId") Long zoneId,
            @RequestParam(value = "timeStart",required=false) Long timeStart,
            @RequestParam(value = "timeEnd",required=false) Long timeEnd
    ){

        //return R.ok().put("data",jmkjServiceImpl.OverdueQuantity(ShiroUtils.getUserId().longValue(),zoneId,timeStart,timeEnd));
        return R.ok().put("data",jmkjServiceImpl.OverdueQuantity(user.getId(),zoneId,timeStart,timeEnd));
    }

    /**
     * 履职数据 操作性任务查询 逾期率
     * */
    @GetMapping(value = "/OverdueRate")
    @ApiOperation(value = "履职数据 操作性任务查询 逾期率")
    public R OverdueRate(
            @LoginUser UserEntity user,
            @RequestParam(value = "zoneId") Long zoneId,
            @RequestParam(value = "timeStart",required=false) Long timeStart,
            @RequestParam(value = "timeEnd",required=false) Long timeEnd
    ){

        //return R.ok().put("data",jmkjServiceImpl.OverdueRate(ShiroUtils.getUserId().longValue(),zoneId,timeStart,timeEnd));
        return R.ok().put("data",jmkjServiceImpl.OverdueRate(user.getId(),zoneId,timeStart,timeEnd));
    }

    /**
     * 操作性任务新建总量
     * */
    @GetMapping(value = "/operationNum")
    @ApiOperation(value = "履职数据 操作性任务查询 新建总量")
    public R operationNum(
            @LoginUser UserEntity user,
            @RequestParam(value = "zoneId") Long zoneId,
            @RequestParam(value = "timeStart",required=false) Long timeStart,
            @RequestParam(value = "timeEnd",required=false) Long timeEnd
    ){

        //return R.ok().put("data",jmkjServiceImpl.operationNum(ShiroUtils.getUserId().longValue(),zoneId,timeStart,timeEnd));
        return R.ok().put("data",jmkjServiceImpl.operationNum(user.getId(),zoneId,timeStart,timeEnd));
    }

    /**
     * 浏览任务完成总量
     * */
    @GetMapping(value = "/BrowseComplete")
    @ApiOperation(value = "履职数据 浏览任务 完成总量")
    public R BrowseComplete(
            @LoginUser UserEntity user,
            @RequestParam(value = "zoneId") Long zoneId,
            @RequestParam(value = "timeStart",required=false) Long timeStart,
            @RequestParam(value = "timeEnd",required=false) Long timeEnd
    ){

        //return R.ok().put("data",jmkjServiceImpl.BrowseComplete(ShiroUtils.getUserId().longValue(),zoneId,timeStart,timeEnd));
        return R.ok().put("data",jmkjServiceImpl.BrowseComplete(user.getId(),zoneId,timeStart,timeEnd));
    }

    /**
     * 浏览任务新建总量
     * */
    @GetMapping(value = "/NewBrowse")
    @ApiOperation(value = "履职数据 浏览任务 新建总量")
    public R NewBrowse(
            @LoginUser UserEntity user,
            @RequestParam(value = "zoneId") Long zoneId,
            @RequestParam(value = "timeStart",required=false) Long timeStart,
            @RequestParam(value = "timeEnd",required=false) Long timeEnd
    ){

        //return R.ok().put("data",jmkjServiceImpl.NewBrowse(ShiroUtils.getUserId().longValue(),zoneId,timeStart,timeEnd));
        return R.ok().put("data",jmkjServiceImpl.NewBrowse(user.getId(),zoneId,timeStart,timeEnd));
    }

    /**
     * 履职数据 在线时长
     * */
    @GetMapping(value = "/OnlineDuration")
    @ApiOperation(value = "履职数据 在线时长")
    public R OnlineDuration(
            @LoginUser UserEntity user,
            @RequestParam(value = "zoneId") Long zoneId,
            @RequestParam(value = "timeStart",required=false) Long timeStart,
            @RequestParam(value = "timeEnd",required=false) Long timeEnd
    ){

        //return R.ok().put("data",jmkjServiceImpl.OnlineDuration(ShiroUtils.getUserId().long Value(),zoneId,timeStart,timeEnd));
        return R.ok().put("data",jmkjServiceImpl.OnlineDuration(user.getId(),zoneId,timeStart,timeEnd));
    }

    @GetMapping(value = "/OnlineNum")
    @ApiOperation(value = "履职数据 登录次数")
    public R OnlineNum(
            @LoginUser UserEntity user,
            @RequestParam(value = "zoneId") Long zoneId,
            @RequestParam(value = "timeStart",required=false) Long timeStart,
            @RequestParam(value = "timeEnd",required=false) Long timeEnd
    ){

        // return R.ok().put("data",jmkjServiceImpl.OnlineNum(ShiroUtils.getUserId().longValue(),zoneId,timeStart,timeEnd));
        return R.ok().put("data",jmkjServiceImpl.OnlineNum(user.getId(),zoneId,timeStart,timeEnd));
    }


}
