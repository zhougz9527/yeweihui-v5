package com.yeweihui.modules.division.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.Query;
import com.yeweihui.modules.division.dao.DistrictDao;
import com.yeweihui.modules.division.entity.DistrictEntity;
import com.yeweihui.modules.division.service.DistrictService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("districtService")
public class DistrictServiceImpl extends ServiceImpl<DistrictDao, DistrictEntity> implements DistrictService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String name = (String)params.get("name");

        Page<DistrictEntity> page = this.selectPage(
                new Query<DistrictEntity>(params).getPage(),
                new EntityWrapper<DistrictEntity>()
                        .like(StringUtils.isNotBlank(name),"name", name)
        );

        return new PageUtils(page);
    }

    @Override
    public List<DistrictEntity> findByCityId(Long cityId) {
        return this.selectList(new EntityWrapper<DistrictEntity>()
                        .eq(null != cityId, "city_id", cityId));
    }

}
