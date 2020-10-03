package com.yeweihui.modules.division;

import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.R;
import com.yeweihui.common.validator.ValidatorUtils;
import com.yeweihui.modules.division.entity.DistrictEntity;
import com.yeweihui.modules.division.service.DistrictService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("division/district")
public class DistrictController {

    @Autowired
    DistrictService districtService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("division:district:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = districtService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 所有区返回，供关联用
     */
    @RequestMapping("/all")
    @RequiresPermissions("division:district:list")
    public R all() {
        List<DistrictEntity> all = districtService.selectByMap(new HashMap<>());
        return R.ok().put("all", all);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("division:district:info")
    public R info(@PathVariable("id") Long id){
        DistrictEntity district = districtService.selectById(id);

        return R.ok().put("district", district);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("division:district:save")
    public R save(@RequestBody DistrictEntity district){
        //校验类型
        ValidatorUtils.validateEntity(district);

        districtService.insert(district);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("division:district:update")
    public R update(@RequestBody DistrictEntity district){
        //校验类型
        ValidatorUtils.validateEntity(district);

        districtService.updateById(district);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("division:district:delete")
    public R delete(@RequestBody Long[] ids){
        districtService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 根据城市id获取所有区
     */
    @RequestMapping("/districtsInCity")
    @RequiresPermissions("division:district:list")
    public R districtsByCity(@RequestParam("cityId") Long cityId) {
        List<DistrictEntity> districtEntityList = districtService.findByCityId(cityId);
        return R.ok().put("districts", districtEntityList);
    }

}
