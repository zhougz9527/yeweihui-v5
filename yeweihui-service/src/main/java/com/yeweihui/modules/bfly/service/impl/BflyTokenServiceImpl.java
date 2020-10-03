package com.yeweihui.modules.bfly.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yeweihui.modules.bfly.dao.BflyTokenDao;
import com.yeweihui.modules.bfly.entity.BflyToken;
import com.yeweihui.modules.bfly.service.BflyTokenService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.UUID;

@Service
public class BflyTokenServiceImpl extends ServiceImpl<BflyTokenDao, BflyToken> implements BflyTokenService {

    /**
     * 一周后过期
     */
    private final static int EXPIRE = 3600 * 24 * 7;


    @Override
    public BflyToken createToken(long userId) {
        //当前时间
        long currentTimestamp = System.currentTimeMillis();
        //过期时间
        Timestamp expireTimestamp = new Timestamp(currentTimestamp + EXPIRE * 1000);
        //生成token
        String token = generateToken();
        // 保存或者更新用户token
        BflyToken bflyToken = new BflyToken();
        bflyToken.setUserId(userId);
        bflyToken.setToken(token);
        bflyToken.setExpireTime(expireTimestamp);
        this.insertOrUpdate(bflyToken);
        return bflyToken;
    }

    @Override
    public BflyToken queryByToken(String token) {
        return this.selectOne(new EntityWrapper<BflyToken>().eq("token", token));
    }

    @Override
    public void expireToken(long userId) {
        BflyToken bflyToken = new BflyToken();
        bflyToken.setExpireTime(new Timestamp(System.currentTimeMillis()));
        bflyToken.setUserId(userId);
        this.insertOrUpdate(bflyToken);
    }

    private String generateToken(){
        return UUID.randomUUID().toString().replace("-", "");
    }

}
