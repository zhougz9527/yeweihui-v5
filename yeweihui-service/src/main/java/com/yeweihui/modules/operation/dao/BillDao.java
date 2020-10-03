package com.yeweihui.modules.operation.dao;

import com.baomidou.mybatisplus.plugins.Page;
import com.yeweihui.modules.operation.entity.BillEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yeweihui.modules.vo.api.vo.BillVO;
import com.yeweihui.modules.vo.query.BillQueryParam;

import java.util.List;
import java.util.Map;

/**
 * 报销表
 * 
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
public interface BillDao extends BaseMapper<BillEntity> {

    List<BillVO> selectPageVO(Page page, Map<String, Object> params);

    int getCount(BillQueryParam billQueryParam);

    List<BillEntity> selectPageEn(Page page, Map<String, Object> params);
}
