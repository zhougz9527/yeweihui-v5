package com.yeweihui.modules.operation.controller;

import java.util.Arrays;
import java.util.Map;

import com.yeweihui.common.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yeweihui.modules.operation.entity.BillMemberEntity;
import com.yeweihui.modules.operation.service.BillMemberService;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.R;



/**
 * 报销审批表
 *
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
@RestController
@RequestMapping("operation/billmember")
public class BillMemberController {
    @Autowired
    private BillMemberService billMemberService;

    /**
     * 列表
     */
    @RequestMapping("/list")
//    @RequiresPermissions("operation:billmember:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = billMemberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
//    @RequiresPermissions("operation:billmember:info")
    public R info(@PathVariable("id") Long id){
        BillMemberEntity billMember = billMemberService.selectById(id);

        return R.ok().put("billMember", billMember);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions("operation:billmember:save")
    public R save(@RequestBody BillMemberEntity billMember){
        billMemberService.insert(billMember);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("operation:billmember:update")
    public R update(@RequestBody BillMemberEntity billMember){
        ValidatorUtils.validateEntity(billMember);
        billMemberService.updateAllColumnById(billMember);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//    @RequiresPermissions("operation:billmember:delete")
    public R delete(@RequestBody Long[] ids){
        billMemberService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}
