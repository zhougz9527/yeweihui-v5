package com.yeweihui.modules.operation.controller;


import com.yeweihui.common.utils.BeanUtil;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.R;
import com.yeweihui.modules.operation.entity.HisViewLogEntity;
import com.yeweihui.modules.operation.service.HisViewLogService;
import com.yeweihui.modules.vo.query.HisViewLogQueryParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 历史记录查询日志
 *
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
@RestController
@RequestMapping("operation/hisviewlog")
public class HisViewLogController {

    @Autowired
    private HisViewLogService hisViewLogService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("operation:hisviewlog:list")
    public R list(HisViewLogQueryParam hisViewLogQueryParam){
        PageUtils page = hisViewLogService.queryPage(BeanUtil.bean2map(hisViewLogQueryParam));

        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("operation:hisviewlog:info")
    public R info(@PathVariable("id") Long id){
        HisViewLogEntity hisViewLogEntity = hisViewLogService.info(id, null);

        return R.ok().put("log", hisViewLogEntity);
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("operation:hisviewlog:delete")
    public R delete(@RequestBody Long[] ids){
        List<HisViewLogEntity> list = new ArrayList<>();
        for (Long id: ids) {
            HisViewLogEntity hisViewLogEntity = new HisViewLogEntity();
            hisViewLogEntity.setId(id);
            hisViewLogEntity.setRecordStatus(0);
            list.add(hisViewLogEntity);
        }
        hisViewLogService.insertOrUpdateBatch(list);

        return R.ok();
    }

    /**
     * 隐藏
     */
    @RequestMapping("/hide")
    @RequiresPermissions("operation:hisviewlog:hide")
    public R hide(@RequestBody Long[] ids){
        List<HisViewLogEntity> list = new ArrayList<>();
        for (Long id: ids) {
            HisViewLogEntity hisViewLogEntity = new HisViewLogEntity();
            hisViewLogEntity.setId(id);
            hisViewLogEntity.setRecordStatus(1);
            list.add(hisViewLogEntity);
        }
        hisViewLogService.insertOrUpdateBatch(list);

        return R.ok();
    }

    /**
     * 恢复
     */
    @RequestMapping("/recovery")
    @RequiresPermissions("operation:hisviewlog:recovery")
    public R recovery(@RequestBody Long[] ids){
        List<HisViewLogEntity> list = new ArrayList<>();
        for (Long id: ids) {
            HisViewLogEntity hisViewLogEntity = new HisViewLogEntity();
            hisViewLogEntity.setId(id);
            hisViewLogEntity.setRecordStatus(2);
            list.add(hisViewLogEntity);
        }
        hisViewLogService.insertOrUpdateBatch(list);

        return R.ok();
    }

}
