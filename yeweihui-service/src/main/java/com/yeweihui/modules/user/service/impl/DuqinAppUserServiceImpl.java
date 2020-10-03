package com.yeweihui.modules.user.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.Query;

import com.yeweihui.modules.user.dao.DuqinAppUserDao;
import com.yeweihui.modules.user.entity.DuqinAppUserEntity;
import com.yeweihui.modules.user.service.DuqinAppUserService;


@Service("duqinAppUserService")
public class DuqinAppUserServiceImpl extends ServiceImpl<DuqinAppUserDao, DuqinAppUserEntity> implements DuqinAppUserService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<DuqinAppUserEntity> page = this.selectPage(
                new Query<DuqinAppUserEntity>(params).getPage(),
                new EntityWrapper<DuqinAppUserEntity>()
        );

        return new PageUtils(page);
    }

}
