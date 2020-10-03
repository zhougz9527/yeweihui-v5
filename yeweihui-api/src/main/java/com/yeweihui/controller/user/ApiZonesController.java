package com.yeweihui.controller.user;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.yeweihui.annotation.Login;
import com.yeweihui.annotation.LoginUser;
import com.yeweihui.common.utils.BeanUtil;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.R;
import com.yeweihui.common.validator.ValidatorUtils;
import com.yeweihui.modules.operation.entity.BillEntity;
import com.yeweihui.modules.user.entity.UserEntity;
import com.yeweihui.modules.user.entity.ZonesEntity;
import com.yeweihui.modules.user.service.ZonesService;
import com.yeweihui.modules.vo.query.ZonesQueryParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 小区
 *
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-03 19:51:04
 */
@RestController
@RequestMapping("/api/zones")
@Api(tags="小区")
public class ApiZonesController {
    @Autowired
    private ZonesService zonesService;

    @PostMapping("/list")
    @ApiOperation("分页列表")
    public R list(@RequestBody ZonesQueryParam zonesQueryParam){
        PageUtils page = zonesService.queryPage(BeanUtil.bean2map(zonesQueryParam));

        return R.ok().put("page", page);
    }

    @PostMapping("/simpleList")
    @ApiOperation("普通列表")
    public R simpleList(@RequestBody ZonesQueryParam zonesQueryParam){
        List<ZonesEntity> zonesEntityList = zonesService.simpleList(zonesQueryParam);

        return R.ok().put("zonesEntityList", zonesEntityList);
    }

    @PostMapping("/info/{id}")
    @ApiOperation("信息")
    public R info(@PathVariable("id") Long id){
        ZonesEntity zones = zonesService.info(id);

        return R.ok().put("zones", zones);
    }

    @PostMapping("/save")
    @ApiOperation("保存")
    public R save(@RequestBody ZonesEntity zones){
        zonesService.save(zones);

        return R.ok();
    }

    @PostMapping("/update")
    @ApiOperation("修改")
    public R update(@RequestBody ZonesEntity zones){
        ValidatorUtils.validateEntity(zones);
        zonesService.update(zones);


        return R.ok();
    }

    @PostMapping("/delete")
    @ApiOperation("删除")
    public R delete(@RequestBody Long[] ids){
        zonesService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

    @Login
    @PostMapping("/managerZoneList")
    @ApiOperation("如果用户为行业主管，根据用户token返回其掌管的小区列表")
    public R getManagerZoneList(@ApiIgnore @LoginUser UserEntity userEntity) {
        List<ZonesEntity> zonesEntities =  zonesService.getManagerZoneLists(userEntity);
        return R.ok().put("zonesList", zonesEntities);
    }

    //根据用户token，判断是不是管理员，是的话根据用户中的zoneId获取zone中的refund_enable_time，生成倒计时时间
    @Login
    @PostMapping("/getRefundEnableLeftDays")
    @ApiOperation("根据用户token，判断是不是管理员，是的话根据用户中的zoneId获取zone中的refund_enable_time，生成倒计时时间")
    public R getRefundEnableLeftDays(@ApiIgnore @LoginUser UserEntity userEntity){
        long refundEnableLeftDays = zonesService.getRefundEnableLeftDays(userEntity);
        return R.ok().put("refundEnableLeftDays", refundEnableLeftDays);
    }

    @Login
    @PostMapping("/getEnableUseLeftDays")
    @ApiOperation("根据用户token，判断是不是管理员，是的话根据用户中的zoneId获取zone中的 enableUseTime ，生成使用的倒计时时间")
    public R getEnableUseLeftDays(@ApiIgnore @LoginUser UserEntity userEntity){
        long enableUseLeftDays = zonesService.getEnableUseLeftDays(userEntity);
        return R.ok().put("enableUseLeftDays", enableUseLeftDays);
    }

}
