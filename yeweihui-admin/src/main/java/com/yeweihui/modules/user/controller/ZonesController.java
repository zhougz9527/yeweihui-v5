package com.yeweihui.modules.user.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.yeweihui.common.validator.ValidatorUtils;
import com.yeweihui.modules.user.service.ZoneMenuMapService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yeweihui.modules.user.entity.ZonesEntity;
import com.yeweihui.modules.user.service.ZonesService;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.R;



/**
 * 小区
 *
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-03 19:51:04
 */
@RestController
@RequestMapping("user/zones")
public class ZonesController {
    @Autowired
    private ZonesService zonesService;
    @Autowired
    private ZoneMenuMapService zoneMenuMapService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("user:zones:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = zonesService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/simpleList")
//    @RequiresPermissions("user:zones:list")
    public R simpleList(@RequestParam Map<String, Object> params){
        List<ZonesEntity> zonesEntityList = zonesService.simpleList2(params);

        return R.ok().put("zonesEntityList", zonesEntityList);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("user:zones:info")
    public R info(@PathVariable("id") Long id){
        ZonesEntity zones = zonesService.info(id);

        return R.ok().put("zones", zones);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("user:zones:save")
    public R save(@RequestBody ZonesEntity zones){
        zonesService.save(zones);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("user:zones:update")
    public R update(@RequestBody ZonesEntity zones){
        ValidatorUtils.validateEntity(zones);
        if (1 == zones.getStatus() && null != zones.getId() && zones.getId() > 0) {
            zoneMenuMapService.insertByZoneId(zones.getId());
        }
        zonesService.update(zones);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("user:zones:delete")
    public R delete(@RequestBody Long[] ids){
        zonesService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}
