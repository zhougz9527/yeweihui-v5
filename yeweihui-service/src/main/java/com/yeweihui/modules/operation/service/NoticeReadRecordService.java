package com.yeweihui.modules.operation.service;

import com.baomidou.mybatisplus.service.IService;
import com.yeweihui.modules.operation.entity.NoticeReadRecordEntity;

import java.util.List;

public interface NoticeReadRecordService extends IService<NoticeReadRecordEntity> {


    List<NoticeReadRecordEntity> getReadMembers(Long nid);


}
