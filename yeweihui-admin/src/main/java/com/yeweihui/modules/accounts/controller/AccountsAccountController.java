package com.yeweihui.modules.accounts.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.yeweihui.common.utils.R;
import com.yeweihui.common.validator.ValidatorUtils;
import com.yeweihui.modules.accounts.entity.AccountsAccountEntity;
import com.yeweihui.modules.accounts.service.AccountsAccountService;
import com.yeweihui.modules.sys.shiro.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zss86 on 2020/8/26.
 * 小区账号
 */
@RestController
@RequestMapping("accounts/account")
public class AccountsAccountController {

    @Autowired
    AccountsAccountService accountsAccountService;
    /***
    	 * @作者： zss
    	 * @生成时间：9:47 2020/8/26
    	 * @方法说明：//添加小区账户
         * @参数：[accountsAccount]
    	 * @返回值：com.yeweihui.common.utils.R
    	 */
    @PostMapping(value = "/saveOrUpdate")
    public R add(@RequestBody AccountsAccountEntity accountsAccount){
        Long zoneId= ShiroUtils.getUserEntity().getZoneId();
        AccountsAccountEntity  accountsAccountEntity=accountsAccountService.selectOne(new EntityWrapper<AccountsAccountEntity>().eq("zone_id",zoneId));
        if(accountsAccountEntity !=null)
        {
            accountsAccount.setId(accountsAccountEntity.getId());
        }
        accountsAccount.setZoneId(zoneId);
        return R.ok().put("data", accountsAccountService.insertOrUpdate(accountsAccount));
    }

    /***
    	 * @作者： zss
    	 * @生成时间：9:48 2020/8/26
    	 * @方法说明：//查询小区账户
         * @参数：[]
    	 * @返回值：com.yeweihui.common.utils.R
    	 */
    @RequestMapping("/search")
    public R info(){
        Long zoneId= ShiroUtils.getUserEntity().getZoneId();
        return R.ok().put("data", accountsAccountService.selectOne(new EntityWrapper<AccountsAccountEntity>().eq("zone_id",zoneId)));
    }

}
