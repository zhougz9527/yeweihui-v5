package com.yeweihui.modules.operation.service.impl;

import com.yeweihui.modules.vo.query.MeetingMemberQueryParam;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.Query;

import com.yeweihui.modules.operation.dao.MeetingMemberDao;
import com.yeweihui.modules.operation.entity.MeetingMemberEntity;
import com.yeweihui.modules.operation.service.MeetingMemberService;


@Service("meetingMemberService")
public class MeetingMemberServiceImpl extends ServiceImpl<MeetingMemberDao, MeetingMemberEntity> implements MeetingMemberService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<MeetingMemberEntity> page = this.selectPage(
                new Query<MeetingMemberEntity>(params).getPage(),
                new EntityWrapper<MeetingMemberEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<MeetingMemberEntity> simpleList(MeetingMemberQueryParam meetingMemberQueryParam) {
        return this.baseMapper.simpleList(meetingMemberQueryParam);
    }


    @Override
    public MeetingMemberEntity getByMidUid(Long mid, Long uid, int meetingMemberType) {
        return this.baseMapper.getByMidUid(mid, uid, meetingMemberType);
    }

    @Override
    public int getCount(MeetingMemberQueryParam meetingMemberQueryParam) {
        return this.baseMapper.getCount(meetingMemberQueryParam);
    }

}
