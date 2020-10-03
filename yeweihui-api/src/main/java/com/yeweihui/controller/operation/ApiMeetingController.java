package com.yeweihui.controller.operation;

import com.alibaba.fastjson.JSONObject;
import com.yeweihui.annotation.GetUser;
import com.yeweihui.annotation.Login;
import com.yeweihui.annotation.LoginUser;
import com.yeweihui.common.utils.BeanUtil;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.R;
import com.yeweihui.common.validator.ValidatorUtils;
import com.yeweihui.modules.bfly.service.BflyVoteService;
import com.yeweihui.modules.bfly.service.WxMessageService;
import com.yeweihui.modules.enums.BizTypeEnum;
import com.yeweihui.modules.operation.entity.MeetingEntity;
import com.yeweihui.modules.operation.entity.MeetingLogEntity;
import com.yeweihui.modules.operation.service.HisViewLogService;
import com.yeweihui.modules.operation.service.MeetingLogService;
import com.yeweihui.modules.operation.service.MeetingService;
import com.yeweihui.modules.user.entity.UserEntity;
import com.yeweihui.modules.vo.api.form.meeting.*;
import com.yeweihui.modules.vo.query.MeetingQueryParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * 会议表
 *
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
@RestController
@RequestMapping("/api/operation/meeting")
@Api(tags="会议表")
public class ApiMeetingController {
    @Autowired
    private MeetingService meetingService;
    @Autowired
    private MeetingLogService meetingLogService;
    @Autowired
    private HisViewLogService hisViewLogService;
    @Autowired
    private BflyVoteService bflyVoteService;

    @Login
    @PostMapping("/list")
    @ApiOperation("分页列表")
    @RequiresPermissions("operation:meeting:list")
    public R list(@ApiIgnore @LoginUser UserEntity userEntity,
                  @RequestBody MeetingQueryParam meetingQueryParam){
        meetingQueryParam.setViewUid(userEntity.getId());
        PageUtils page = meetingService.queryPage(BeanUtil.bean2map(meetingQueryParam));

        return R.ok().put("page", page);
    }

    @PostMapping({"/info/{id}", "/info/{type}/{id}"})
    @ApiOperation("信息1")
    public R info(@ApiIgnore @GetUser UserEntity userEntity,
                  @PathVariable("id") Long id,
                  @PathVariable(name = "type", required = false) String type){
        MeetingEntity meeting = meetingService.info(id, userEntity);

        if (userEntity != null && type != null && "history".equals(type)) {
            hisViewLogService.save(BizTypeEnum.MEETING, userEntity, id);
        }
        return R.ok().put("meeting", meeting);
    }

    @PostMapping("/infoDetail/{id}")
    @ApiOperation("信息2")
    public R infoDetail(@PathVariable("id") Long id){
        MeetingEntity meeting = meetingService.infoDetail(id);

        return R.ok().put("meeting", meeting);
    }

    @Login
    @PostMapping("/save")
    @ApiOperation("保存")
    @RequiresPermissions("operation:meeting:save")
    public R save(@ApiIgnore @LoginUser UserEntity userEntity,
                  @RequestBody MeetingEntity meeting){
        meeting.setUid(userEntity.getId());
        meetingService.save(meeting);

        return R.ok();
    }

    @Login
    @PostMapping("/update")
    @ApiOperation("修改")
    @RequiresPermissions("operation:meeting:update")
    public R update(@RequestBody MeetingEntity meeting){
        ValidatorUtils.validateEntity(meeting);
        meetingService.update(meeting);

        return R.ok();
    }

    @Login
    @PostMapping("/delete")
    @ApiOperation("删除")
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

    /****************************************************************
     *                            具体操作
     ****************************************************************/

    //会议签到，会议撤销；上传会议纪要，会议结束签字；

    //会议签到
    @Login
    @PostMapping("/meetingSignIn")
    @ApiOperation("会议签到")
    public R meetingSignIn(@ApiIgnore @LoginUser UserEntity user,
                           @RequestBody MeetingSignInForm meetingSignInForm){
        meetingSignInForm.setUid(user.getId());
        meetingService.meetingSignIn(meetingSignInForm);
        return R.ok();
    }

    //会议撤销
    @Login
    @PostMapping("/meetingCancel")
    @ApiOperation("会议撤销")
    public R meetingCancel(@ApiIgnore @LoginUser UserEntity user,
                           @RequestBody MeetingCancelForm meetingCancelForm){
        meetingCancelForm.setUid(user.getId());
        meetingService.meetingCancel(meetingCancelForm);
        return R.ok();
    }

    //上传会议纪要
    @Login
    @PostMapping("/meetingLogUpload")
    @ApiOperation("上传会议纪要")
    public R meetingLogUpload(@ApiIgnore @LoginUser UserEntity user,
                              @RequestBody MeetingLogEntity meetingLogEntity){
        meetingLogEntity.setUid(user.getId());
        meetingLogService.save(meetingLogEntity);
        return R.ok();
    }

    //会议结束签字
    @Login
    @PostMapping("/meetingEndSign")
    @ApiOperation("会议结束签字")
    public R meetingEndSign(@ApiIgnore @LoginUser UserEntity user,
                              @RequestBody MeetingEndSignForm meetingEndSignForm){
        meetingEndSignForm.setUid(user.getId());
        meetingService.meetingEndSign(meetingEndSignForm);
        return R.ok();
    }

    //会议签字
    @Login
    @PostMapping("/meetingSign")
    @ApiOperation("会议签字")
    public R meetingSign(@ApiIgnore @LoginUser UserEntity user,
                         @RequestBody MeetingSignForm meetingSignForm){
        meetingSignForm.setUid(user.getId());
        meetingService.meetingSign(meetingSignForm);
        return R.ok();
    }
}
