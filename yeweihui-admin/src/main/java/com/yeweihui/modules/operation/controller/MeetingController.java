package com.yeweihui.modules.operation.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.DocumentException;
import com.yeweihui.common.exception.RRException;
import com.yeweihui.common.utils.BeanUtil;
import com.yeweihui.common.validator.ValidatorUtils;
import com.yeweihui.modules.operation.entity.MeetingEntity;
import com.yeweihui.modules.sys.shiro.ShiroUtils;
import com.yeweihui.modules.vo.query.MeetingQueryParam;
import com.yeweihui.third.pdf.PdfUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yeweihui.modules.operation.service.MeetingService;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.R;

import javax.servlet.http.HttpServletResponse;


/**
 * 会议表
 *
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
@RestController
@RequestMapping("operation/meeting")
public class MeetingController {
    @Autowired
    private MeetingService meetingService;
    @Autowired
    PdfUtils pdfUtils;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("operation:meeting:list")
    public R list(MeetingQueryParam meetingQueryParam){
        PageUtils page = meetingService.queryPage(BeanUtil.bean2map(meetingQueryParam));

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("operation:meeting:info")
    public R info(@PathVariable("id") Long id){
        MeetingEntity meeting = meetingService.info(id, null);

        return R.ok().put("meeting", meeting);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("operation:meeting:save")
    public R save(@RequestBody MeetingEntity meeting){
        Long userId = ShiroUtils.getUserId();
        meeting.setUid(userId);
        meetingService.save(meeting);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("operation:meeting:update")
    public R update(@RequestBody MeetingEntity meeting){
        ValidatorUtils.validateEntity(meeting);
        meetingService.update(meeting);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("operation:meeting:delete")
    public R delete(@RequestBody Long[] ids){
        List<MeetingEntity> meetingEntities = new ArrayList<>();
        for (Long id: ids) {
            MeetingEntity meetingEntity = new MeetingEntity();
            meetingEntity.setId(id);
            meetingEntity.setRecordStatus(0);
            meetingEntities.add(meetingEntity);
        }
        meetingService.insertOrUpdateBatch(meetingEntities);

        return R.ok();
    }

    /**
     * 隐藏
     */
    @RequestMapping("/hide")
    @RequiresPermissions("operation:meeting:hide")
    public R hide(@RequestBody Long[] ids){
        List<MeetingEntity> meetingEntities = new ArrayList<>();
        for (Long id: ids) {
            MeetingEntity meetingEntity = new MeetingEntity();
            meetingEntity.setId(id);
            meetingEntity.setRecordStatus(1);
            meetingEntities.add(meetingEntity);
        }
        meetingService.insertOrUpdateBatch(meetingEntities);

        return R.ok();
    }

    /**
     * 恢复
     */
    @RequestMapping("/recovery")
    @RequiresPermissions("operation:meeting:recovery")
    public R recovery(@RequestBody Long[] ids){
        List<MeetingEntity> meetingEntities = new ArrayList<>();
        for (Long id: ids) {
            MeetingEntity meetingEntity = new MeetingEntity();
            meetingEntity.setId(id);
            meetingEntity.setRecordStatus(2);
            meetingEntities.add(meetingEntity);
        }
        meetingService.insertOrUpdateBatch(meetingEntities);

        return R.ok();
    }


    /**
     * 用章pdf
     * @param response
     * @param id
     * @throws IOException
     * @throws DocumentException
     */
    @RequestMapping(value = "viewPdf/{id}/{needAttach}")
    public void viewPdf(HttpServletResponse response, @PathVariable("id") Long id, @PathVariable("needAttach") String needAttachStr) throws IOException, DocumentException {
        //需要填充的数据
        MeetingEntity meetingEntity = meetingService.info(id, null);
        boolean needAttach = true;
        if (StringUtils.isNotBlank(needAttachStr) && "no".equals(needAttachStr)) {
            needAttach = false;
        }
        Map data = BeanUtil.bean2map(meetingEntity);
        data.put("needAttach", needAttach);
        String content = pdfUtils.freeMarkerRender(data,"pdf-ftl/meeting.ftl");
        //创建pdf
        pdfUtils.createPdf(content, "yeweihui-admin/target/", "request.pdf");
        // 读取pdf并预览
        pdfUtils.readPdf(response, "yeweihui-admin/target/request.pdf");
    }

}
