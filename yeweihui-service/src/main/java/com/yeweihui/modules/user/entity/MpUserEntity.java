package com.yeweihui.modules.user.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName("mp_user")
public class MpUserEntity {

    @TableId
    private String unionId;

    private Long uid;

    private String openidMp;

    private String openidPublic;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getOpenidMp() {
        return openidMp;
    }

    public void setOpenidMp(String openidMp) {
        this.openidMp = openidMp;
    }

    public String getOpenidPublic() {
        return openidPublic;
    }

    public void setOpenidPublic(String openidPublic) {
        this.openidPublic = openidPublic;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }
}
