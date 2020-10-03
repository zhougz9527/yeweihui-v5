package com.yeweihui.modules.division.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.Query;
import com.yeweihui.modules.division.dao.ProvinceDao;
import com.yeweihui.modules.division.entity.ProvinceEntity;
import com.yeweihui.modules.division.service.ProvinceService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("provinceService")
public class ProvinceServiceImpl extends ServiceImpl<ProvinceDao, ProvinceEntity> implements ProvinceService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String name = (String)params.get("name");

        Page<ProvinceEntity> page = this.selectPage(
                new Query<ProvinceEntity>(params).getPage(),
                new EntityWrapper<ProvinceEntity>()
                        .like(StringUtils.isNotBlank(name),"name", name)
        );

        return new PageUtils(page);
    }

}
