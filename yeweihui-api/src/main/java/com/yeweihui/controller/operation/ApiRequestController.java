package com.yeweihui.controller.operation;

import com.yeweihui.annotation.GetUser;
import com.yeweihui.annotation.Login;
import com.yeweihui.annotation.LoginUser;
import com.yeweihui.common.utils.BeanUtil;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.R;
import com.yeweihui.common.validator.ValidatorUtils;
import com.yeweihui.modules.enums.BizTypeEnum;
import com.yeweihui.modules.operation.entity.RequestEntity;
import com.yeweihui.modules.operation.service.HisViewLogService;
import com.yeweihui.modules.operation.service.RequestService;
import com.yeweihui.modules.user.entity.UserEntity;
import com.yeweihui.modules.vo.api.form.request.RequestCancelForm;
import com.yeweihui.modules.vo.api.form.request.RequestVerifyForm;
import com.yeweihui.modules.vo.query.RequestQueryParam;
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
 * 用章申请表
 *
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
@RestController
@RequestMapping("/api/operation/request")
@Api(tags="用章申请")
public class ApiRequestController {
    @Autowired
    private RequestService requestService;
    @Autowired
    private HisViewLogService hisViewLogService;

    @Login
    @PostMapping("/list")
    @ApiOperation("分页列表")
    @RequiresPermissions("operation:request:list")
    public R list(@ApiIgnore @LoginUser UserEntity userEntity,
                  @RequestBody RequestQueryParam requestQueryParam){
        requestQueryParam.setViewUid(userEntity.getId());
        PageUtils page = requestService.queryPage(BeanUtil.bean2map(requestQueryParam));

        return R.ok().put("page", page);
    }

    @PostMapping({"/info/{id}", "/info/{type}/{id}"})
    @ApiOperation("信息")
    public R info(@ApiIgnore @GetUser UserEntity userEntity,
                  @PathVariable("id") Long id,
                  @PathVariable(name = "type", required = false) String type){
        RequestEntity request = requestService.info(id, userEntity);

        if (userEntity != null && type != null && "history".equals(type)) {
            hisViewLogService.save(BizTypeEnum.REQUEST, userEntity, id);
        }

        return R.ok().put("request", request);
    }

    @Login
    @PostMapping("/save")
    @ApiOperation("保存")
    @RequiresPermissions("operation:request:save")
    public R save(@ApiIgnore @LoginUser UserEntity userEntity,
                  @RequestBody RequestEntity request){
        request.setUid(userEntity.getId());
        requestService.save(request);

        return R.ok();
    }

    @Login
    @PostMapping("/update")
    @ApiOperation("修改")
    @RequiresPermissions("operation:request:update")
    public R update(@RequestBody RequestEntity request){
        ValidatorUtils.validateEntity(request);
        requestService.update(request);//全部更新

        return R.ok();
    }

    @Login
    @PostMapping("/delete")
    @ApiOperation("删除")
    @RequiresPermissions("operation:request:delete")
    public R delete(@RequestBody Long[] ids){
        List<RequestEntity> requestEntities = new ArrayList<>();
        for (Long id: ids) {
            RequestEntity requestEntity = new RequestEntity();
            requestEntity.setId(id);
            requestEntity.setRecordStatus(0);
            requestEntities.add(requestEntity);
        }
        requestService.insertOrUpdateBatch(requestEntities);

        return R.ok();
    }

    /****************************************************************
     *                            具体操作
     ****************************************************************/

    @Login
    @PostMapping("/verify")
    @ApiOperation("审核通过拒绝用章")
    public R verify(@ApiIgnore @LoginUser UserEntity user,
                    @RequestBody RequestVerifyForm requestVerifyForm){
        requestVerifyForm.setUid(user.getId());
        requestService.verify(requestVerifyForm);

        return R.ok();
    }

    @Login
    @PostMapping("/requestCancel")
    @ApiOperation("审核撤销")
    public R requestCancel(@ApiIgnore @LoginUser UserEntity user,
                    @RequestBody RequestCancelForm requestCancelForm){
        requestCancelForm.setUid(user.getId());
        requestService.requestCancel(requestCancelForm);

        return R.ok();
    }

}
