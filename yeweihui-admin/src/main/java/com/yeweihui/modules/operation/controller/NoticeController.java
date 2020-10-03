package com.yeweihui.modules.operation.controller;

import com.itextpdf.text.DocumentException;
import com.yeweihui.common.utils.BeanUtil;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.R;
import com.yeweihui.common.validator.ValidatorUtils;
import com.yeweihui.modules.operation.entity.NoticeEntity;
import com.yeweihui.modules.operation.service.NoticeService;
import com.yeweihui.modules.sys.shiro.ShiroUtils;
import com.yeweihui.modules.user.entity.UserEntity;
import com.yeweihui.modules.vo.query.NoticeQueryParam;
import com.yeweihui.third.pdf.PdfUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("operation/notice")
public class NoticeController {
    @Autowired
    private NoticeService noticeService;
    @Autowired
    PdfUtils pdfUtils;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("operation:notice:list")
    public R list(NoticeQueryParam param){
        PageUtils page = noticeService.queryPage(BeanUtil.bean2map(param));

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */

    @RequestMapping("/info/{id}")
    @RequiresPermissions("operation:notice:info")
    public R info(@PathVariable("id") Long id){
        UserEntity userEntity = ShiroUtils.getUserEntity();
        NoticeEntity notice = noticeService.info(id, userEntity);

        return R.ok().put("notice", notice);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("operation:notice:save")
    public R save(@RequestBody NoticeEntity notice){
        Long userId = ShiroUtils.getUserId();
        notice.setUid(userId);
        noticeService.save(notice);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("operation:notice:update")
    public R update(@RequestBody NoticeEntity notice){
        ValidatorUtils.validateEntity(notice);
        noticeService.update(notice);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("operation:notice:delete")
    public R delete(@RequestBody Long[] ids){
        List<NoticeEntity> noticeEntities = new ArrayList<>();
        for (Long id: ids) {
            NoticeEntity noticeEntity = new NoticeEntity();
            noticeEntity.setId(id);
            noticeEntity.setRecordStatus(0);
            noticeEntities.add(noticeEntity);
        }
        noticeService.insertOrUpdateBatch(noticeEntities);

        return R.ok();
    }

    /**
     * 隐藏
     */
    @RequestMapping("/hide")
    @RequiresPermissions("operation:notice:hide")
    public R hide(@RequestBody Long[] ids){
        List<NoticeEntity> noticeEntities = new ArrayList<>();
        for (Long id: ids) {
            NoticeEntity noticeEntity = new NoticeEntity();
            noticeEntity.setId(id);
            noticeEntity.setRecordStatus(1);
            noticeEntities.add(noticeEntity);
        }
        noticeService.insertOrUpdateBatch(noticeEntities);

        return R.ok();
    }

    /**
     * 获取已阅读人数
     */
    @RequestMapping("/readMembers")
    public R readCount(@RequestParam Long id) {
        return R.ok().put("readMembers", noticeService.getReadMembers(id));
    }

    /**
     * 恢复
     */
    @RequestMapping("/recovery")
    @RequiresPermissions("operation:notice:recovery")
    public R recovery(@RequestBody Long[] ids){
        List<NoticeEntity> noticeEntities = new ArrayList<>();
        for (Long id: ids) {
            NoticeEntity noticeEntity = new NoticeEntity();
            noticeEntity.setId(id);
            noticeEntity.setRecordStatus(2);
            noticeEntities.add(noticeEntity);
        }
        noticeService.insertOrUpdateBatch(noticeEntities);

        return R.ok();
    }
}
