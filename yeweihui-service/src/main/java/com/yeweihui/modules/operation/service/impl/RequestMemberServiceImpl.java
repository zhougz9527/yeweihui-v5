package com.yeweihui.modules.operation.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.yeweihui.modules.enums.request.RequestMemberVerifyStatusEnum;
import com.yeweihui.modules.enums.request.RequestOpeTypeEnum;
import com.yeweihui.modules.operation.entity.RequestEntity;
import com.yeweihui.modules.operation.service.RequestService;
import com.yeweihui.modules.vo.query.RequestMemberQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.Query;

import com.yeweihui.modules.operation.dao.RequestMemberDao;
import com.yeweihui.modules.operation.entity.RequestMemberEntity;
import com.yeweihui.modules.operation.service.RequestMemberService;


@Service("requestMemberService")
public class RequestMemberServiceImpl extends ServiceImpl<RequestMemberDao, RequestMemberEntity> implements RequestMemberService {


    @Autowired
    RequestService requestService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<RequestMemberEntity> page = this.selectPage(
                new Query<RequestMemberEntity>(params).getPage(),
                new EntityWrapper<RequestMemberEntity>()
        );
        List<RequestMemberEntity> requestMemberEntityList = page.getRecords();
        this.packageExtraInfo(requestMemberEntityList);
        page.setRecords(requestMemberEntityList);
        return new PageUtils(page);
    }

    /**
     * 根据用章id获取审批情况列表
     * @param requestId
     * @param requestMemberStatus
     * @param requestMemberOpeType
     * @return
     */
    @Override
    public List<RequestMemberEntity> listByRequestId(Long requestId, Integer requestMemberStatus, Integer requestMemberOpeType) {
        List<RequestMemberEntity> requestMemberEntityList = this.baseMapper.listByRequestId(requestId, requestMemberStatus, requestMemberOpeType);
        this.packageExtraInfo(requestMemberEntityList);
        return requestMemberEntityList;
    }

    /**
     * 根据用章id和审批用户id获取
     * @param requestId
     * @param uid
     * @return
     */
    @Override
    public RequestMemberEntity getByRidUid(Long requestId, Long uid) {
        return this.baseMapper.getByRidUid(requestId, uid);
    }

    /**
     * 获取用章参与数量
     * @param requestMemberQueryParam
     * @return
     */
    @Override
    public int getCount(RequestMemberQueryParam requestMemberQueryParam) {
        return this.baseMapper.getCount(requestMemberQueryParam);
    }

    /**
     * 包装
     * @param requestMemberEntityList
     */
    private void packageExtraInfo(List<RequestMemberEntity> requestMemberEntityList){
        for (RequestMemberEntity requestMemberEntity : requestMemberEntityList) {
            //未审核且时间超过使用时间24小时，为超时通过审核
            RequestEntity requestEntity = requestService.selectById(requestMemberEntity.getRid());
            if (requestMemberEntity.getStatus().equals(RequestMemberVerifyStatusEnum.未审核.getCode())
                    && DateUtil.offset(requestEntity.getUseDate(), DateField.HOUR, 24).compareTo(DateUtil.date()) < 0
            && requestMemberEntity.getType().equals(RequestOpeTypeEnum.审批.getCode())){
                requestMemberEntity.setStatusCn(RequestMemberVerifyStatusEnum.超时同意.getName());
            }else{
                requestMemberEntity.setStatusCn(RequestMemberVerifyStatusEnum.getName(requestMemberEntity.getStatus()));
            }
        }
    }

}
