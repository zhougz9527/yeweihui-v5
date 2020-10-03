package com.yeweihui.modules.operation.dao;

import com.yeweihui.modules.operation.entity.MeetingLogEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yeweihui.modules.vo.query.MeetingLogQueryParam;

import java.util.List;

/**
 * 会议记录表
 * 
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
public interface MeetingLogDao extends BaseMapper<MeetingLogEntity> {

    List<MeetingLogEntity> simpleList(MeetingLogQueryParam meetingLogQueryParam);
}
