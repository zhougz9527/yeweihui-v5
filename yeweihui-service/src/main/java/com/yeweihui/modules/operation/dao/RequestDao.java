package com.yeweihui.modules.operation.dao;

import com.baomidou.mybatisplus.plugins.Page;
import com.yeweihui.modules.operation.entity.RequestEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yeweihui.modules.vo.api.vo.RequestVO;
import com.yeweihui.modules.vo.query.RequestQueryParam;

import java.util.List;
import java.util.Map;

/**
 * 用章申请表
 * 
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
public interface RequestDao extends BaseMapper<RequestEntity> {

    /**
     * 分页
     * @param page
     * @param params
     * @return
     */
    List<RequestVO> selectPageVO(Page page, Map<String, Object> params);

    int getCount(RequestQueryParam requestQueryParam);

    List<RequestEntity> simpleList(RequestQueryParam requestQueryParam);

    List<RequestEntity> selectPageEn(Page page, Map<String, Object> params);
}
