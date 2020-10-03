package com.yeweihui.modules.operation.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.modules.operation.dao.AnnounceMemberDao;
import com.yeweihui.modules.operation.entity.AnnounceMemberEntity;
import com.yeweihui.modules.operation.service.AnnounceMemberService;
import com.yeweihui.modules.vo.query.AnnounceMemberQueryParam;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("announceMemberService")
public class AnnounceMemberServiceImpl extends ServiceImpl<AnnounceMemberDao, AnnounceMemberEntity> implements AnnounceMemberService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        return null;
    }

    @Override
    public List<AnnounceMemberEntity> simpleList(AnnounceMemberQueryParam announceMemberQueryParam) {
        return baseMapper.simpleList(announceMemberQueryParam);
    }
}
