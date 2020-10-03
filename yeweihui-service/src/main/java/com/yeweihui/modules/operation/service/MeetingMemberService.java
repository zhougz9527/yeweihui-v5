package com.yeweihui.modules.operation.service;

import com.baomidou.mybatisplus.service.IService;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.modules.operation.entity.MeetingMemberEntity;
import com.yeweihui.modules.vo.query.MeetingMemberQueryParam;

import java.util.List;
import java.util.Map;

/**
 * 参会人员表
 *
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
public interface MeetingMemberService extends IService<MeetingMemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<MeetingMemberEntity> simpleList(MeetingMemberQueryParam meetingMemberQueryParam);

    MeetingMemberEntity getByMidUid(Long mid, Long uid, int meetingMemberType);

    int getCount(MeetingMemberQueryParam meetingMemberQueryParam);
}

