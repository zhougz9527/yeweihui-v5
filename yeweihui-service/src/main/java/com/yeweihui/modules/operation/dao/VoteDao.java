package com.yeweihui.modules.operation.dao;

import com.baomidou.mybatisplus.plugins.Page;
import com.yeweihui.modules.operation.entity.VoteEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yeweihui.modules.vo.api.vo.VoteVO;
import com.yeweihui.modules.vo.query.VoteQueryParam;

import java.util.List;
import java.util.Map;

/**
 * 事务表决表
 * 
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
public interface VoteDao extends BaseMapper<VoteEntity> {

    /**
     * 分页
     * @param page
     * @param params
     * @return
     */
    List<VoteVO> selectPageVO(Page page, Map<String, Object> params);

    int getCount(VoteQueryParam voteQueryParam);

    List<VoteEntity> simpleList(VoteQueryParam voteQueryParam);

    List<VoteEntity> selectPageEn(Page page, Map<String, Object> params);
}
