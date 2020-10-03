package com.yeweihui.modules.operation.service;

import com.baomidou.mybatisplus.service.IService;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.modules.operation.entity.MeetingLogEntity;
import com.yeweihui.modules.vo.query.MeetingLogQueryParam;

import java.util.List;
import java.util.Map;

/**
 * 会议记录表
 *
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
public interface MeetingLogService extends IService<MeetingLogEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<MeetingLogEntity> simpleList(MeetingLogQueryParam meetingMemberQueryParam);

    void save(MeetingLogEntity meetingLog);

    MeetingLogEntity info(Long id);
}

