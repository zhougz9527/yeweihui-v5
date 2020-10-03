package com.yeweihui.modules.vo.api.vo;

import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;

public class LoginVO {
    private String token;
    private Long expire;
    private WxMaJscode2SessionResult session;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getExpire() {
        return expire;
    }

    public void setExpire(Long expire) {
        this.expire = expire;
    }

    public WxMaJscode2SessionResult getSession() {
        return session;
    }

    public void setSession(WxMaJscode2SessionResult session) {
        this.session = session;
    }
}
