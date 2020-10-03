package com.yeweihui.modules.operation.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yeweihui.modules.operation.dao.NoticeReadRecordDao;
import com.yeweihui.modules.operation.entity.NoticeReadRecordEntity;
import com.yeweihui.modules.operation.service.NoticeReadRecordService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("noticeReadRecordService")
public class NoticeReadRecordServiceImpl extends ServiceImpl<NoticeReadRecordDao, NoticeReadRecordEntity> implements NoticeReadRecordService {


    @Override
    public List<NoticeReadRecordEntity> getReadMembers(Long nid) {
        return baseMapper.selectReadMembersByNoticeId(nid);
    }
}
