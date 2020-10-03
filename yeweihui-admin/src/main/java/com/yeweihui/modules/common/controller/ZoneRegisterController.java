package com.yeweihui.modules.common.controller;

import java.util.Arrays;
import java.util.Map;

import com.yeweihui.common.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yeweihui.modules.common.entity.ZoneRegisterEntity;
import com.yeweihui.modules.common.service.ZoneRegisterService;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.R;



/**
 * 小区登记
 *
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-10-19 17:14:58
 */
@RestController
@RequestMapping("common/zoneregister")
public class ZoneRegisterController {
    @Autowired
    private ZoneRegisterService zoneRegisterService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("common:zoneregister:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = zoneRegisterService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("common:zoneregister:info")
    public R info(@PathVariable("id") Long id){
        ZoneRegisterEntity zoneRegister = zoneRegisterService.selectById(id);

        return R.ok().put("zoneRegister", zoneRegister);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("common:zoneregister:save")
    public R save(@RequestBody ZoneRegisterEntity zoneRegister){
        zoneRegisterService.insert(zoneRegister);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("common:zoneregister:update")
    public R update(@RequestBody ZoneRegisterEntity zoneRegister){
        ValidatorUtils.validateEntity(zoneRegister);
        zoneRegisterService.updateAllColumnById(zoneRegister);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("common:zoneregister:delete")
    public R delete(@RequestBody Long[] ids){
        zoneRegisterService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}
