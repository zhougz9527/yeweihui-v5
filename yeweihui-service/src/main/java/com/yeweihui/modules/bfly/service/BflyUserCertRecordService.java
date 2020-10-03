package com.yeweihui.modules.bfly.service;

import com.baomidou.mybatisplus.service.IService;
import com.yeweihui.modules.bfly.entity.BflyUserCertRecord;

import java.util.HashMap;
import java.util.List;

public interface BflyUserCertRecordService extends IService<BflyUserCertRecord> {

    //后台查询用户提交的审核业主请求
    HashMap<String,Object> selectUserCertList(Long zoneId, String ownerName, String phoen, Integer page, Integer size,Integer submitMethod);

    //后台审核用户提交的审核请求
    void certOwner(Long userCertId,Boolean cert);
}
