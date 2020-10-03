package com.yeweihui.modules.common.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.yeweihui.common.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.yeweihui.modules.common.entity.MultimediaEntity;
import com.yeweihui.modules.common.service.MultimediaService;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.R;
import org.springframework.web.multipart.MultipartFile;


/**
 * 文件
 *
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-15 15:09:44
 */
@RestController
@RequestMapping("common/file")
public class MultiMediaController {
    @Autowired
    private MultimediaService multimediaService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("common:file:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = multimediaService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("common:file:info")
    public R info(@PathVariable("id") Long id){
        MultimediaEntity file = multimediaService.selectById(id);

        return R.ok().put("file", file);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("common:file:save")
    public R save(@RequestBody MultimediaEntity file){
        multimediaService.insert(file);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("common:file:update")
    public R update(@RequestBody MultimediaEntity file){
        ValidatorUtils.validateEntity(file);
        multimediaService.updateAllColumnById(file);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("common:file:delete")
    public R delete(@RequestBody Long[] ids){
        multimediaService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

    
    /** 
    * @Description: 上传文件 
    * @Param:  
    * @return:  
    * @Author: tenaciousVine
    * @Date: 2020/3/18 
    */
    @PostMapping("updateMultimedia")
    public R updateMultimedia(@RequestParam("files") MultipartFile[] files) throws Exception {
        return R.ok(multimediaService.insertBatchByPublicity(files));
    }

}
