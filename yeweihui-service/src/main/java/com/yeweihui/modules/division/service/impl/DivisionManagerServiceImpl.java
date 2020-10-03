package com.yeweihui.modules.division.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.Query;
import com.yeweihui.modules.division.dao.DivisionManagerDao;
import com.yeweihui.modules.division.entity.CityEntity;
import com.yeweihui.modules.division.entity.DivisionManagerEntity;
import com.yeweihui.modules.division.service.DivisionManagerService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("divisionManagerService")
public class DivisionManagerServiceImpl extends ServiceImpl<DivisionManagerDao, DivisionManagerEntity>
        implements DivisionManagerService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String level = (String)params.get("level");

        Page<DivisionManagerEntity> page = this.selectPage(
                new Query<DivisionManagerEntity>(params).getPage(),
                new EntityWrapper<DivisionManagerEntity>()
                        .like(StringUtils.isNotBlank(level),"level", level)
        );

        return new PageUtils(page);

    }
}
