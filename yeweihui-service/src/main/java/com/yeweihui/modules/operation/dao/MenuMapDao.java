package com.yeweihui.modules.operation.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.yeweihui.modules.operation.entity.MenuMapEntity;
import com.yeweihui.modules.operation.entity.VoteEntity;
import com.yeweihui.modules.vo.api.vo.VoteVO;
import com.yeweihui.modules.vo.query.VoteQueryParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 菜单权限映射到小程序菜单 的表
 * 
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
public interface MenuMapDao extends BaseMapper<MenuMapEntity> {

//    List<MenuMapEntity> findMenuMapByUserId(Long userId);

    List<MenuMapEntity> selectMenuMapIdIn(@Param("menuMapIdList")List<String> menuMapIdList);

}
