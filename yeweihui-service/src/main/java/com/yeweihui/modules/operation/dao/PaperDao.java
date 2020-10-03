package com.yeweihui.modules.operation.dao;

import com.baomidou.mybatisplus.plugins.Page;
import com.yeweihui.modules.operation.entity.PaperEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yeweihui.modules.vo.api.vo.PaperVO;
import com.yeweihui.modules.vo.query.PaperQueryParam;

import java.util.List;
import java.util.Map;

/**
 * 来往函件表
 * 
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
public interface PaperDao extends BaseMapper<PaperEntity> {

    List<PaperVO> selectPageVO(Page page, Map<String, Object> params);

    int getCount(PaperQueryParam paperQueryParam);

    List<PaperEntity> selectPageEn(Page page, Map<String, Object> params);
}
