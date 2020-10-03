package com.yeweihui.modules.operation.service.impl;

import com.yeweihui.modules.vo.query.VoteMemberQueryParam;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.Query;

import com.yeweihui.modules.operation.dao.VoteMemberDao;
import com.yeweihui.modules.operation.entity.VoteMemberEntity;
import com.yeweihui.modules.operation.service.VoteMemberService;


@Service("voteMemberService")
public class VoteMemberServiceImpl extends ServiceImpl<VoteMemberDao, VoteMemberEntity> implements VoteMemberService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<VoteMemberEntity> page = this.selectPage(
                new Query<VoteMemberEntity>(params).getPage(),
                new EntityWrapper<VoteMemberEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<VoteMemberEntity> simpleList(VoteMemberQueryParam voteMemberQueryParam) {
        return this.baseMapper.simpleList(voteMemberQueryParam);
    }

    @Override
    public VoteMemberEntity getByVidUid(Long vid, Long uid, int voteMemberType) {
        return this.baseMapper.getByVidUid(vid, uid, voteMemberType);
    }

    @Override
    public int getCount(VoteMemberQueryParam voteMemberQueryParam) {
        return this.baseMapper.getCount(voteMemberQueryParam);
    }

}
