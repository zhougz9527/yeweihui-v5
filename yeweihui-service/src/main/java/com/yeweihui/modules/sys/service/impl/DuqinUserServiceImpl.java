package com.yeweihui.modules.sys.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.Query;
import com.yeweihui.modules.sys.dao.DuqinUserDao;
import com.yeweihui.modules.sys.entity.DuqinUserEntity;
import com.yeweihui.modules.sys.service.DuqinUserService;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("duqinUserService")
public class DuqinUserServiceImpl extends ServiceImpl<DuqinUserDao, DuqinUserEntity> implements DuqinUserService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<DuqinUserEntity> page = this.selectPage(
                new Query<DuqinUserEntity>(params).getPage(),
                new EntityWrapper<DuqinUserEntity>()
        );

        /*Page<DuqinUserEntity> page = new Query<DuqinUserEntity>(params).getPage();
        Wrapper wrapper = SqlHelper.fillWrapper(page, new EntityWrapper<DuqinUserEntity>());
        page.setRecords(this.baseMapper.selectPage(page, wrapper));*/

        /*分页查询vo示例*/
        /*params.put("id1",1);
        Page page = new Query(params).getPage();
        List<MenuVO> duqinUserVOList = this.baseMapper.selectPageVO2(page, params);
        page.setRecords(duqinUserVOList);*/

        return new PageUtils(page);
    }

}
