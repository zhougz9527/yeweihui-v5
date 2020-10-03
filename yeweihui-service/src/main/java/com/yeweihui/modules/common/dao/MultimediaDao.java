package com.yeweihui.modules.common.dao;

import com.yeweihui.modules.common.entity.MultimediaEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * 文件
 * 
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-15 15:09:44
 */
public interface MultimediaDao extends BaseMapper<MultimediaEntity> {

    Long insertAndGetId(MultimediaEntity multimediaEntity);

    void updateRelatedId(Map<String, Object> paramsMap);

    List<MultimediaEntity> findList(Map<String, Object> paramsMap);
	
}
