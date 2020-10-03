package com.yeweihui.modules.operation.service;

import com.baomidou.mybatisplus.service.IService;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.modules.operation.entity.NoticeEntity;
import com.yeweihui.modules.operation.entity.NoticeReadRecordEntity;
import com.yeweihui.modules.user.entity.UserEntity;
import com.yeweihui.modules.vo.query.NoticeQueryParam;

import java.util.List;
import java.util.Map;

public interface NoticeService extends IService<NoticeEntity> {
    
    PageUtils queryPage(Map<String, Object> params);

    NoticeEntity info(Long id, UserEntity userEntity);

    void save(NoticeEntity notice);

    void update(NoticeEntity notice);

    List<NoticeReadRecordEntity> getReadMembers(Long id);

    int getCount(NoticeQueryParam noticeQueryParam);
}
