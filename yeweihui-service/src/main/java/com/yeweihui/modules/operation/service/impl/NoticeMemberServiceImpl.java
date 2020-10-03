package com.yeweihui.modules.operation.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yeweihui.modules.operation.dao.NoticeMemberDao;
import com.yeweihui.modules.operation.entity.NoticeMemberEntity;
import com.yeweihui.modules.operation.service.NoticeMemberService;
import com.yeweihui.modules.vo.query.NoticeMemberQueryParam;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("noticeMemberService")
public class NoticeMemberServiceImpl extends ServiceImpl<NoticeMemberDao, NoticeMemberEntity> implements NoticeMemberService {


    @Override
    public List<NoticeMemberEntity> simpleList(NoticeMemberQueryParam noticeMemberQueryParam) {
        return this.baseMapper.simpleList(noticeMemberQueryParam);
    }
}
