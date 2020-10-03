package com.yeweihui.modules.sys.shiro;

import com.yeweihui.modules.enums.LoginTypeEnum;
import org.apache.shiro.authc.UsernamePasswordToken;

public class NoPasswordToken extends UsernamePasswordToken {

    private static final long serialVersionUID = -2564928913725078138L;

    private String loginType;

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    /**
     * 账号密码登录
     * @param username
     * @param password
     */
    public NoPasswordToken(String username, String password) {
        super(username,password);

        this.loginType = LoginTypeEnum.PASSWORD.getCode();
    }

    /**
     * 免密登录
     */
    public NoPasswordToken(String username) {
        super(username,"");

        this.loginType = LoginTypeEnum.NOPASSWORD.getCode();
    }
}
