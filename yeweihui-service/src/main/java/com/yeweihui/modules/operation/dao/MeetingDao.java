package com.yeweihui.modules.operation.dao;

import com.baomidou.mybatisplus.plugins.Page;
import com.yeweihui.modules.operation.entity.MeetingEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yeweihui.modules.vo.api.vo.MeetingVO;
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
public interface MeetingDao extends BaseMapper<MeetingEntity> {

    List<MeetingVO> selectPageVO(Page page, Map<String, Object> params);

    List<MeetingEntity> selectPageEn(Page page, Map<String, Object> params);

    int getCount(MeetingQueryParam meetingQueryParam);

    List<MeetingEntity> simpleList(MeetingQueryParam meetingQueryParam);
}
