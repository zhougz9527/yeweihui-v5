package com.yeweihui.modules.sys.shiro;

import com.yeweihui.modules.enums.LoginTypeEnum;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;

public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {

        NoPasswordToken noPasswordToken = (NoPasswordToken) token;
        //如果是免密，就不需要核对密码了
        if (noPasswordToken.getLoginType().equals(LoginTypeEnum.NOPASSWORD.getCode())) {
            return true;
        }
        return super.doCredentialsMatch(token, info);
    }
}
