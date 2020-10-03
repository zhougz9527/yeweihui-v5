package com.yeweihui.modules.bfly.service;

import com.baomidou.mybatisplus.service.IService;
import com.yeweihui.modules.bfly.entity.BflyToken;

public interface BflyTokenService extends IService<BflyToken> {

    // 生成token
    BflyToken createToken(long userId);

    // 查询token
    BflyToken queryByToken(String token);

    // 设置过期token
    void expireToken(long userId);


}
