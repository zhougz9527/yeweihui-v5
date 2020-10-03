package com.yeweihui.modules.operation.service;

import com.baomidou.mybatisplus.service.IService;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.modules.operation.entity.MeetingEntity;
import com.yeweihui.modules.user.entity.UserEntity;
import com.yeweihui.modules.vo.api.form.meeting.MeetingCancelForm;
import com.yeweihui.modules.vo.api.form.meeting.MeetingEndSignForm;
import com.yeweihui.modules.vo.api.form.meeting.MeetingSignForm;
import com.yeweihui.modules.vo.api.form.meeting.MeetingSignInForm;
import com.yeweihui.modules.vo.query.MeetingQueryParam;

import java.util.List;
import java.util.Map;

/**
 * 会议表
 *
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
public interface MeetingService extends IService<MeetingEntity> {

    PageUtils queryPage(Map<String, Object> params);

    MeetingEntity info(Long id, UserEntity userEntity);

    void save(MeetingEntity meeting);

    void update(MeetingEntity meeting);

    MeetingEntity infoDetail(Long id);

    /**
     * 会议签到
     * @param meetingSignInForm
     */
    void meetingSignIn(MeetingSignInForm meetingSignInForm);

    /**
     * 会议撤销
     * @param meetingCancelForm
     */
    void meetingCancel(MeetingCancelForm meetingCancelForm);

    /**
     * 会议结束签字
     * @param meetingEndSignForm
     */
    void meetingEndSign(MeetingEndSignForm meetingEndSignForm);

    /**
     * 参加的会议数量
     * @param meetingQueryParam
     * @return
     */
    int getCount(MeetingQueryParam meetingQueryParam);

    /**
     * 会议时间到，会议状态待签到变为进行中
     */
    void change2Running();

    void expireMeeting();

    List<MeetingEntity> simpleList(MeetingQueryParam meetingQueryParam);

    /**
     * 会议签字
     * @param meetingSignForm
     */
    void meetingSign(MeetingSignForm meetingSignForm);
}

