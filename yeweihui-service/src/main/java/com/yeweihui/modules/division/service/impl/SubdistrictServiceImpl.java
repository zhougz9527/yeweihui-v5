package com.yeweihui.modules.division.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.Query;
import com.yeweihui.modules.division.dao.SubdistrictDao;
import com.yeweihui.modules.division.entity.SubdistrictEntity;
import com.yeweihui.modules.division.service.SubdistrictService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("subDistrictService")
public class SubdistrictServiceImpl extends ServiceImpl<SubdistrictDao, SubdistrictEntity> implements SubdistrictService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String name = (String)params.get("name");

        Page<SubdistrictEntity> page = this.selectPage(
                new Query<SubdistrictEntity>(params).getPage(),
                new EntityWrapper<SubdistrictEntity>()
                        .like(StringUtils.isNotBlank(name),"name", name)
        );

        return new PageUtils(page);
    }

    @Override
    public List<SubdistrictEntity> findByDistrictId(Long districtId) {
        return this.selectList(new EntityWrapper<SubdistrictEntity>()
                        .eq(null != districtId, "district_Id", districtId));
    }


}
