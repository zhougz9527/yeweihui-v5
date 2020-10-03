package com.yeweihui.controller.common;

import com.yeweihui.annotation.Login;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.R;
import com.yeweihui.common.validator.ValidatorUtils;
import com.yeweihui.modules.common.entity.ZoneRegisterEntity;
import com.yeweihui.modules.common.service.ZoneRegisterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 小区登记
 *
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-10-19 17:14:58
 */
@RestController
@RequestMapping("/api/common/zoneregister")
@Api(tags="小区登记")
public class ApiZoneRegisterController {
    @Autowired
    private ZoneRegisterService zoneRegisterService;

    @PostMapping("/list")
    @ApiOperation("分页列表")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = zoneRegisterService.queryPage(params);

        return R.ok().put("page", page);
    }

    @PostMapping("/info/{id}")
    @ApiOperation("信息")
    public R info(@PathVariable("id") Long id){
        ZoneRegisterEntity zoneRegister = zoneRegisterService.selectById(id);

        return R.ok().put("zoneRegister", zoneRegister);
    }

    @PostMapping("/save")
    @ApiOperation("保存")
    public R save(@RequestBody ZoneRegisterEntity zoneRegister){
        zoneRegisterService.save(zoneRegister);
        return R.ok();
    }

    @PostMapping("/update")
    @ApiOperation("修改")
    public R update(@RequestBody ZoneRegisterEntity zoneRegister){
        ValidatorUtils.validateEntity(zoneRegister);
        zoneRegisterService.updateAllColumnById(zoneRegister);//全部更新
        
        return R.ok();
    }

    @PostMapping("/delete")
    @ApiOperation("删除")
    public R delete(@RequestBody Long[] ids){
        zoneRegisterService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}
