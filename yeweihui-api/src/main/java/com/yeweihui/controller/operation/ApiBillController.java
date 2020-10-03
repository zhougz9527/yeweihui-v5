package com.yeweihui.controller.operation;

import com.yeweihui.annotation.GetUser;
import com.yeweihui.annotation.Login;
import com.yeweihui.annotation.LoginUser;
import com.yeweihui.common.utils.BeanUtil;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.R;
import com.yeweihui.common.validator.ValidatorUtils;
import com.yeweihui.modules.enums.BizTypeEnum;
import com.yeweihui.modules.operation.entity.BillEntity;
import com.yeweihui.modules.operation.service.BillService;
import com.yeweihui.modules.operation.service.HisViewLogService;
import com.yeweihui.modules.user.entity.UserEntity;
import com.yeweihui.modules.vo.api.form.BillVerifyForm;
import com.yeweihui.modules.vo.query.BillQueryParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * 报销表
 *
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
@RestController
@RequestMapping("/api/operation/bill")
@Api(tags="报销")
public class ApiBillController {
    @Autowired
    private BillService billService;
    @Autowired
    private HisViewLogService hisViewLogService;

    @Login
    @PostMapping("/list")
    @ApiOperation("分页列表")
    @RequiresPermissions("operation:bill:list")
    public R list(@ApiIgnore @LoginUser UserEntity userEntity,
                  @RequestBody BillQueryParam billQueryParam){
        billQueryParam.setViewUid(userEntity.getId());
        PageUtils page = billService.queryPage(BeanUtil.bean2map(billQueryParam));

        return R.ok().put("page", page);
    }

    @PostMapping({"/info/{id}", "/info/{type}/{id}"})
    @ApiOperation("信息")
    @RequiresPermissions("operation:bill:info")
    public R info(@ApiIgnore @GetUser UserEntity userEntity,
                  @PathVariable("id") Long id,
                  @PathVariable(name = "type", required = false) String type){
        BillEntity bill = billService.info(id, userEntity);

        if (userEntity != null && type != null && "history".equals(type)) {
            hisViewLogService.save(BizTypeEnum.BILL, userEntity, id);
        }

        return R.ok().put("bill", bill);
    }

    @Login
    @PostMapping("/save")
    @ApiOperation("保存")
    @RequiresPermissions("operation:bill:save")
    public R save(@ApiIgnore @LoginUser UserEntity userEntity,
                  @RequestBody BillEntity bill){
        bill.setUid(userEntity.getId());
        billService.save(bill);

        return R.ok();
    }

    @Login
    @PostMapping("/update")
    @ApiOperation("修改")
    @RequiresPermissions("operation:bill:update")
    public R update(@RequestBody BillEntity bill){
        ValidatorUtils.validateEntity(bill);
        billService.update(bill);

        return R.ok();
    }

    @Login
    @PostMapping("/delete")
    @ApiOperation("删除")
    @RequiresPermissions("operation:bill:delete")
    public R delete(@RequestBody Long[] ids){
        List<BillEntity> billEntities = new ArrayList<>();
        for (Long id: ids) {
            BillEntity billEntity = new BillEntity();
            billEntity.setId(id);
            billEntity.setRecordStatus(0);
            billEntities.add(billEntity);
        }
        billService.insertOrUpdateBatch(billEntities);

        return R.ok();
    }

    /****************************************************************
     *                            具体操作
     ****************************************************************/

    //通过拒绝报销审批
    @Login
    @PostMapping("/verify")
    @ApiOperation("通过拒绝报销审批")
    public R verify(@ApiIgnore @LoginUser UserEntity user,
                    @RequestBody BillVerifyForm billVerifyForm){
        billVerifyForm.setUid(user.getId());
        billService.verify(billVerifyForm);

        return R.ok();
    }

}
