package com.yeweihui.modules.division;

import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.R;
import com.yeweihui.common.validator.ValidatorUtils;
import com.yeweihui.modules.division.entity.ProvinceEntity;
import com.yeweihui.modules.division.service.ProvinceService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("division/province")
public class ProvinceController {

    @Autowired
    ProvinceService provinceService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("division:province:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = provinceService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 所有省份返回，供关联用
     */
    @RequestMapping("/all")
    @RequiresPermissions("division:province:list")
    public R all() {
        List<ProvinceEntity> all = provinceService.selectByMap(new HashMap<>());
        return R.ok().put("all", all);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("division:province:info")
    public R info(@PathVariable("id") Long id){
        ProvinceEntity province = provinceService.selectById(id);

        return R.ok().put("province", province);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("division:province:save")
    public R save(@RequestBody ProvinceEntity province){
        //校验类型
        ValidatorUtils.validateEntity(province);

        provinceService.insert(province);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("division:province:update")
    public R update(@RequestBody ProvinceEntity province){
        //校验类型
        ValidatorUtils.validateEntity(province);

        provinceService.updateById(province);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("division:province:delete")
    public R delete(@RequestBody Long[] ids){
        provinceService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}
