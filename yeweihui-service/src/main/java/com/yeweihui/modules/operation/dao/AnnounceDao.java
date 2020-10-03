package com.yeweihui.modules.operation.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.yeweihui.modules.operation.entity.AnnounceEntity;
import com.yeweihui.modules.vo.query.AnnounceQueryParam;

import java.util.List;
import java.util.Map;

/**
 * 公示记录
 * 
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
public interface AnnounceDao extends BaseMapper<AnnounceEntity> {

    List<AnnounceEntity> selectPageEn(Page page, Map<String, Object> params);

    List<AnnounceEntity> simpleList(AnnounceQueryParam announceQueryParam);
}
