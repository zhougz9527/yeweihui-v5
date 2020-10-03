package com.yeweihui.modules.operation.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.yeweihui.modules.operation.entity.NoticeEntity;
import com.yeweihui.modules.vo.query.NoticeQueryParam;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

public interface NoticeDao extends BaseMapper<NoticeEntity> {

    List<NoticeEntity> selectPageEn(Page page, Map<String, Object> params);

    @Update("UPDATE notice SET read_count = read_count + 1 WHERE id = #{id}")
    void updateReadCount(NoticeEntity noticeEntity);

    int getCount(NoticeQueryParam noticeQueryParam);
}
