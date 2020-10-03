package com.yeweihui.modules.division.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.Query;
import com.yeweihui.modules.division.dao.CityDao;
import com.yeweihui.modules.division.entity.CityEntity;
import com.yeweihui.modules.division.service.CityService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("cityService")
public class CityServiceImpl extends ServiceImpl<CityDao, CityEntity> implements CityService {

    @Autowired CityDao cityDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String name = (String)params.get("name");

        Page<CityEntity> page = this.selectPage(
                new Query<CityEntity>(params).getPage(),
                new EntityWrapper<CityEntity>()
                        .like(StringUtils.isNotBlank(name),"name", name)
        );

        return new PageUtils(page);
    }

    @Override
    public List<CityEntity> findByProvinceId(Long provinceId) {
        return this.selectList(new EntityWrapper<CityEntity>()
                        .eq(null != provinceId, "province_id", provinceId));
    }

}
