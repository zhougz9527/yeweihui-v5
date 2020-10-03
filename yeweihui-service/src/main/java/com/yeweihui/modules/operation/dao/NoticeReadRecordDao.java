package com.yeweihui.modules.operation.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.yeweihui.modules.operation.entity.NoticeEntity;
import com.yeweihui.modules.operation.entity.NoticeReadRecordEntity;
import com.yeweihui.modules.vo.query.NoticeQueryParam;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

public interface NoticeReadRecordDao extends BaseMapper<NoticeReadRecordEntity> {


    List<NoticeReadRecordEntity> selectReadMembersByNoticeId(Long nid);
}
