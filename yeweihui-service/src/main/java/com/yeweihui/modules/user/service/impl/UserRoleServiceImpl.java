package com.yeweihui.modules.user.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.Query;

import com.yeweihui.modules.user.dao.UserRoleDao;
import com.yeweihui.modules.user.entity.UserRoleEntity;
import com.yeweihui.modules.user.service.UserRoleService;


@Service("userRoleService")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleDao, UserRoleEntity> implements UserRoleService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<UserRoleEntity> page = this.selectPage(
                new Query<UserRoleEntity>(params).getPage(),
                new EntityWrapper<UserRoleEntity>()
        );

        return new PageUtils(page);
    }

}
