package com.yeweihui.modules.operation.service.impl;

import com.yeweihui.modules.vo.query.PaperMemberQueryParam;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.Query;

import com.yeweihui.modules.operation.dao.PaperMemberDao;
import com.yeweihui.modules.operation.entity.PaperMemberEntity;
import com.yeweihui.modules.operation.service.PaperMemberService;


@Service("paperMemberService")
public class PaperMemberServiceImpl extends ServiceImpl<PaperMemberDao, PaperMemberEntity> implements PaperMemberService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<PaperMemberEntity> page = this.selectPage(
                new Query<PaperMemberEntity>(params).getPage(),
                new EntityWrapper<PaperMemberEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PaperMemberEntity getByPidUid(Long pid, Long uid) {
        return this.baseMapper.getByPidUid(pid, uid);
    }

    @Override
    public int getCount(PaperMemberQueryParam paperMemberQueryParam) {
        return this.baseMapper.getCount(paperMemberQueryParam);
    }

    @Override
    public List<PaperMemberEntity> getListByPid(Long id) {
        return this.baseMapper.getListByPid(id);
    }

}
