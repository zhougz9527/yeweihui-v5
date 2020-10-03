package com.yeweihui.modules.user.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yeweihui.modules.user.dao.ZoneGroupDao;
import com.yeweihui.modules.user.entity.ZoneGroupEntity;
import com.yeweihui.modules.user.service.ZoneGroupService;
import org.springframework.stereotype.Service;

@Service("zoneGroupService")
public class ZoneGroupServiceImpl extends ServiceImpl<ZoneGroupDao, ZoneGroupEntity> implements ZoneGroupService {
}
