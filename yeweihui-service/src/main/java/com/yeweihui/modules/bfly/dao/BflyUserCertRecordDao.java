package com.yeweihui.modules.bfly.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yeweihui.modules.bfly.entity.BflyUserCertRecord;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;

import javax.validation.constraints.Null;
import java.util.List;

public interface BflyUserCertRecordDao extends BaseMapper<BflyUserCertRecord> {

    List<BflyUserCertRecord> selectUserCertList(
            @Param("ownerName") String ownerName,
            @Param("phone") String phone,
            @Param("zoneId") Long zoneId,
            @Param("current") Integer current,
            @Param("size") Integer size,
            @Param("submitMethod") Integer submitMethod);

    Long selectUserCertListCount(@Param("ownerName") String ownerName,
                                 @Param("phone") String phone,
                                 @Param("zoneId") Long zoneId,
                                 @Param("submitMethod") Integer submitMethod);
}
