package com.yeweihui.modules.operation.dao;

import com.yeweihui.modules.operation.entity.MeetingMemberEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yeweihui.modules.vo.query.MeetingMemberQueryParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 参会人员表
 * 
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
public interface MeetingMemberDao extends BaseMapper<MeetingMemberEntity> {

    List<MeetingMemberEntity> simpleList(MeetingMemberQueryParam meetingMemberQueryParam);

    MeetingMemberEntity getByMidUid(@Param("mid") Long mid, @Param("uid") Long uid, @Param("meetingMemberType") int meetingMemberType);

    int getCount(MeetingMemberQueryParam meetingMemberQueryParam);
}
