package com.yeweihui.modules.operation.service.impl;

import com.yeweihui.modules.enums.bill.BillMemberStatusEnum;
import com.yeweihui.modules.vo.query.BillMemberQueryParam;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.Query;

import com.yeweihui.modules.operation.dao.BillMemberDao;
import com.yeweihui.modules.operation.entity.BillMemberEntity;
import com.yeweihui.modules.operation.service.BillMemberService;


@Service("billMemberService")
public class BillMemberServiceImpl extends ServiceImpl<BillMemberDao, BillMemberEntity> implements BillMemberService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<BillMemberEntity> page = this.selectPage(
                new Query<BillMemberEntity>(params).getPage(),
                new EntityWrapper<BillMemberEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<BillMemberEntity> simpleList(BillMemberQueryParam billMemberQueryParam) {
        return this.baseMapper.simpleList(billMemberQueryParam);
    }

    /**
     * 根据报销id删除之前需要审批的人员
     * @param billId
     */
    @Override
    public void deleteByBillId(Long billId) {
        this.baseMapper.deleteByBillId(billId);
    }

    @Override
    public BillMemberEntity getByBidUid(Long bid, Long uid, Integer type) {
        return this.baseMapper.getByBidUid(bid, uid, type);
    }

    @Override
    public int getCount(BillMemberQueryParam billMemberQueryParam) {
        return this.baseMapper.getCount(billMemberQueryParam);
    }

    @Override
    public List<BillMemberEntity> listByBillId(Long id) {
        List<BillMemberEntity> billMemberEntityList = this.baseMapper.listByBillId(id);
        for (BillMemberEntity billMemberEntity : billMemberEntityList) {
            billMemberEntity.setStatusCn(BillMemberStatusEnum.getName(billMemberEntity.getStatus()));
        }
        return billMemberEntityList;
    }

}
