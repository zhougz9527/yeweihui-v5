package com.yeweihui.controller.operation;

import com.yeweihui.annotation.GetUser;
import com.yeweihui.annotation.Login;
import com.yeweihui.annotation.LoginUser;
import com.yeweihui.common.utils.BeanUtil;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.R;
import com.yeweihui.common.validator.ValidatorUtils;
import com.yeweihui.modules.enums.BizTypeEnum;
import com.yeweihui.modules.operation.entity.NoticeEntity;
import com.yeweihui.modules.operation.service.HisViewLogService;
import com.yeweihui.modules.operation.service.NoticeService;
import com.yeweihui.modules.user.entity.UserEntity;
import com.yeweihui.modules.vo.query.NoticeQueryParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.List;


/**
 * 报销表
 *
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
@RestController
@RequestMapping("/api/operation/notice")
@Api(tags="通知公告")
public class ApiNoticeController {
    @Autowired
    private NoticeService noticeService;
    @Autowired
    private HisViewLogService hisViewLogService;

//    @Login
    @PostMapping("/list")
    @ApiOperation("分页列表")
    @RequiresPermissions("operation:notice:list")
    public R list(
//            @ApiIgnore @LoginUser UserEntity userEntity,
            @ApiIgnore @GetUser UserEntity userEntity,
                  @RequestBody NoticeQueryParam notice ) {
        if (null != userEntity) {
            notice.setViewUid(userEntity.getId());
        }
        PageUtils page = noticeService.queryPage(BeanUtil.bean2map(notice));
        return R.ok().put("page", page);
    }


    @PostMapping({"/info/{id}", "/info/{type}/{id}"})
    @ApiOperation("信息")
    public R info(@ApiIgnore @GetUser UserEntity userEntity,
                  @PathVariable("id") Long id,
                  @PathVariable(name = "type", required = false) String type){
        NoticeEntity notice = noticeService.info(id, userEntity);

        if (userEntity != null && type != null && "history".equals(type)) {
            hisViewLogService.save(BizTypeEnum.NOTICE, userEntity, id);
        }

        return R.ok().put("notice", notice);
    }

    @Login
    @PostMapping("/save")
    @ApiOperation("保存")
    @RequiresPermissions("operation:notice:save")
    public R save(@ApiIgnore @LoginUser UserEntity userEntity,
                  @RequestBody NoticeEntity notice){
        notice.setUid(userEntity.getId());
        noticeService.save(notice);

        return R.ok();
    }

    @Login
    @PostMapping("/update")
    @ApiOperation("修改")
    @RequiresPermissions("operation:notice:update")
    public R update(@RequestBody NoticeEntity notice){
        ValidatorUtils.validateEntity(notice);
        noticeService.update(notice);

        return R.ok();
    }

    @Login
    @PostMapping("/delete")
    @ApiOperation("删除")
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
     * 获取已阅读人数
     */
    @ApiOperation("已阅读成员")
    @GetMapping("/readMembers")
    public R readCount(@RequestParam Long id) {
        return R.ok().put("readMembers", noticeService.getReadMembers(id));
    }


}
