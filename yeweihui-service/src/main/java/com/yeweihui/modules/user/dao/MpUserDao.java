package com.yeweihui.modules.user.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yeweihui.modules.user.entity.MpUserEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

public interface MpUserDao extends BaseMapper<MpUserEntity> {

    @Insert("INSERT IGNORE INTO mp_user(union_id, uid, openid_mp, openid_public) values (#{mp.unionId}, #{mp.uid}, #{mp.openidMp}, #{mp.openidPublic})")
    void InsertIgnoreDuplicate(@Param("mp") MpUserEntity mp);
}
