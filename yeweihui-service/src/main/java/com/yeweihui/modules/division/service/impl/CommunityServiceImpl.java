package com.yeweihui.modules.division.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.Query;
import com.yeweihui.modules.division.dao.CommunityDao;
import com.yeweihui.modules.division.entity.CommunityEntity;
import com.yeweihui.modules.division.service.CommunityService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("communityService")
public class CommunityServiceImpl extends ServiceImpl<CommunityDao, CommunityEntity> implements CommunityService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String name = (String)params.get("name");

        Page<CommunityEntity> page = this.selectPage(
                new Query<CommunityEntity>(params).getPage(),
                new EntityWrapper<CommunityEntity>()
                        .like(StringUtils.isNotBlank(name),"name", name)
        );

        return new PageUtils(page);
    }

    @Override
    public List<CommunityEntity> findBySubdistrictId(Long subdistrictId) {
        return this.selectList(new EntityWrapper<CommunityEntity>()
                        .eq(null != subdistrictId, "subdistrict_id", subdistrictId));
    }

}
