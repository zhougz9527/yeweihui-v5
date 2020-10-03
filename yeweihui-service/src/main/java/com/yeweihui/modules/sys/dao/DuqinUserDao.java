package com.yeweihui.modules.sys.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.yeweihui.modules.sys.entity.DuqinUserEntity;
import com.yeweihui.modules.vo.admin.DuqinUserVO;

import java.util.List;
import java.util.Map;

/**
 * 用户表（dq_user） 20180911
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2018-10-21 22:37:17
 */
public interface DuqinUserDao extends BaseMapper<DuqinUserEntity> {

    List<DuqinUserVO> selectPageVO(Page<DuqinUserVO> page, Map<String, Object> params);
}
