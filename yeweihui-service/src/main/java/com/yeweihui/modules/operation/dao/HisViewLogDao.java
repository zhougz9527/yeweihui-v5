package com.yeweihui.modules.operation.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.yeweihui.modules.operation.entity.HisViewLogEntity;

import java.util.List;
import java.util.Map;

/**
 * 历史记录查看日志
 * 
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
public interface HisViewLogDao extends BaseMapper<HisViewLogEntity> {

    List<HisViewLogEntity> selectPageEn(Page page, Map<String, Object> params);

}
