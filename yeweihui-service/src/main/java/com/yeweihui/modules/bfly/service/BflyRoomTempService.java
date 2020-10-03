package com.yeweihui.modules.bfly.service;

import com.baomidou.mybatisplus.service.IService;
import com.yeweihui.modules.bfly.entity.BflyRoomTemp;

import java.io.InputStream;
import java.util.List;

public interface BflyRoomTempService extends IService<BflyRoomTemp> {

    void saveAll( Long zoneId, Long userId, List<Long> tempIds);


    List<BflyRoomTemp> importExcel(Long userId, Long zoneId, InputStream inputStream) throws Exception;
}
