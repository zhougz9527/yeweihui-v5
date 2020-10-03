package com.yeweihui.modules.division;

import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.R;
import com.yeweihui.common.validator.ValidatorUtils;
import com.yeweihui.modules.division.entity.SubdistrictEntity;
import com.yeweihui.modules.division.service.SubdistrictService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("division/subdistrict")
public class SubdistrictController {

    @Autowired
    SubdistrictService subdistrictService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("division:subdistrict:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = subdistrictService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 所有街道返回，供关联用
     */
    @RequestMapping("/all")
    @RequiresPermissions("division:district:list")
    public R all() {
        List<SubdistrictEntity> all = subdistrictService.selectByMap(new HashMap<>());
        return R.ok().put("all", all);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("division:subdistrict:info")
    public R info(@PathVariable("id") Long id){
        SubdistrictEntity subdistrict = subdistrictService.selectById(id);

        return R.ok().put("subdistrict", subdistrict);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("division:subdistrict:save")
    public R save(@RequestBody SubdistrictEntity subdistrict){
        //校验类型
        ValidatorUtils.validateEntity(subdistrict);

        subdistrictService.insert(subdistrict);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("division:subdistrict:update")
    public R update(@RequestBody SubdistrictEntity subdistrict){
        //校验类型
        ValidatorUtils.validateEntity(subdistrict);

        subdistrictService.updateById(subdistrict);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("division:subdistrict:delete")
    public R delete(@RequestBody Long[] ids){
        subdistrictService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 根据区id获取所有街道
     */
    @RequestMapping("subdistrictsInDistrict")
    @RequiresPermissions("division:subdistrict:list")
    public R subdistrictsByDistrict(@Param("districtId") Long districtId) {
        List<SubdistrictEntity> subdistrictList = subdistrictService.findByDistrictId(districtId);
        return R.ok().put("subdistricts", subdistrictList);
    }
}
