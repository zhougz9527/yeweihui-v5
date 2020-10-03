package com.yeweihui.modules.division;

import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.R;
import com.yeweihui.common.validator.ValidatorUtils;
import com.yeweihui.modules.division.entity.CityEntity;
import com.yeweihui.modules.division.service.CityService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("division/city")
public class CityController {

    @Autowired
    CityService cityService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("division:city:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = cityService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 所有城市返回，供关联用
     */
    @RequestMapping("/all")
    @RequiresPermissions("division:city:list")
    public R all() {
        List<CityEntity> all = cityService.selectByMap(new HashMap<>());
        return R.ok().put("all", all);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("division:city:info")
    public R info(@PathVariable("id") Long id){
        CityEntity city = cityService.selectById(id);

        return R.ok().put("city", city);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("division:city:save")
    public R save(@RequestBody CityEntity city){
        //校验类型
        ValidatorUtils.validateEntity(city);

        cityService.insert(city);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("division:city:update")
    public R update(@RequestBody CityEntity city){
        //校验类型
        ValidatorUtils.validateEntity(city);

        cityService.updateById(city);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("division:city:delete")
    public R delete(@RequestBody Long[] ids){
        cityService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 根据省id获取所有城市
     */
    @RequestMapping("/citiesInProvince")
    @RequiresPermissions("division:city:list")
    public R citiesByProvince(@RequestParam("provinceId") Long provinceId) {
        List<CityEntity> cityEntityList = cityService.findByProvinceId(provinceId);
        return R.ok().put("cities", cityEntityList);
    }

}
