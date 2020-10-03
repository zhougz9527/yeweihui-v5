package com.yeweihui.controller.operation;

import com.yeweihui.annotation.GetUser;
import com.yeweihui.annotation.Login;
import com.yeweihui.annotation.LoginUser;
import com.yeweihui.common.utils.BeanUtil;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.R;
import com.yeweihui.common.validator.ValidatorUtils;
import com.yeweihui.modules.enums.BizTypeEnum;
import com.yeweihui.modules.operation.entity.PaperEntity;
import com.yeweihui.modules.operation.service.HisViewLogService;
import com.yeweihui.modules.operation.service.PaperService;
import com.yeweihui.modules.user.entity.UserEntity;
import com.yeweihui.modules.vo.api.form.paper.PaperSignForm;
import com.yeweihui.modules.vo.query.PaperQueryParam;
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
 * 来往函件表
 *
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
@RestController
@RequestMapping("/api/operation/paper")
@Api(tags="来往函件")
public class ApiPaperController {
    @Autowired
    private PaperService paperService;
    @Autowired
    private HisViewLogService hisViewLogService;

    @Login
    @PostMapping("/list")
    @ApiOperation("分页列表")
    @RequiresPermissions("operation:paper:list")
    public R list(@ApiIgnore @LoginUser UserEntity userEntity,
                  @RequestBody PaperQueryParam paperQueryParam){
        paperQueryParam.setViewUid(userEntity.getId());
        PageUtils page = paperService.queryPage(BeanUtil.bean2map(paperQueryParam));

        return R.ok().put("page", page);
    }


    @PostMapping({"/info/{id}", "/info/{type}/{id}"})
    @ApiOperation("信息")
    @RequiresPermissions("operation:paper:info")
    public R info(@ApiIgnore @GetUser UserEntity userEntity,
                  @PathVariable("id") Long id,
                  @PathVariable(name = "type", required = false) String type){
        PaperEntity paper = paperService.info(id, userEntity);

        if (userEntity != null && type != null && "history".equals(type)) {
            hisViewLogService.save(BizTypeEnum.PAPER, userEntity, id);
        }

        return R.ok().put("paper", paper);
    }

    @Login
    @PostMapping("/save")
    @ApiOperation("保存")
    @RequiresPermissions("operation:paper:save")
    public R save(@ApiIgnore @LoginUser UserEntity userEntity,
                  @RequestBody PaperEntity paper){
        paper.setUid(userEntity.getId());
        paperService.save(paper);

        return R.ok();
    }

    @Login
    @PostMapping("/update")
    @ApiOperation("修改")
    @RequiresPermissions("operation:paper:update")
    public R update(@RequestBody PaperEntity paper){
        ValidatorUtils.validateEntity(paper);
        paperService.update(paper);

        return R.ok();
    }

    @Login
    @PostMapping("/delete")
    @ApiOperation("删除")
    @RequiresPermissions("operation:paper:delete")
    public R delete(@RequestBody Long[] ids){
        List<PaperEntity> paperEntities = new ArrayList<>();
        for (Long id: ids) {
            PaperEntity paperEntity = new PaperEntity();
            paperEntity.setId(id);
            paperEntity.setRecordStatus(0);
            paperEntities.add(paperEntity);
        }
        paperService.insertOrUpdateBatch(paperEntities);

        return R.ok();
    }

    /****************************************************************
     *                            具体操作
     ****************************************************************/

    //签收发函
    @Login
    @PostMapping("/paperSign")
    @ApiOperation("签收发函")
    public R paperSign(@ApiIgnore @LoginUser UserEntity user,
                    @RequestBody PaperSignForm paperSignForm){
        paperSignForm.setUid(user.getId());
        paperService.paperSign(paperSignForm);

        return R.ok();
    }

}
