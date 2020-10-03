package com.yeweihui.controller.operation;

import com.yeweihui.annotation.GetUser;
import com.yeweihui.annotation.Login;
import com.yeweihui.annotation.LoginUser;
import com.yeweihui.common.exception.RRException;
import com.yeweihui.common.utils.BeanUtil;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.R;
import com.yeweihui.common.validator.ValidatorUtils;
import com.yeweihui.modules.common.service.MultimediaService;
import com.yeweihui.modules.enums.BizTypeEnum;
import com.yeweihui.modules.enums.announce.paper.AnnounceStatusEnum;
import com.yeweihui.modules.operation.entity.AnnounceEntity;
import com.yeweihui.modules.operation.service.AnnounceService;
import com.yeweihui.modules.operation.service.HisViewLogService;
import com.yeweihui.modules.user.entity.UserEntity;
import com.yeweihui.modules.vo.api.form.announce.AnnounceOpeForm;
import com.yeweihui.modules.vo.query.AnnounceQueryParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 公示记录
 *
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
@RestController
@RequestMapping("/api/operation/announce")
@Api(tags="公示记录")
public class ApiAnnounceController {

    @Autowired
    private AnnounceService announceService;
    @Autowired
    private HisViewLogService hisViewLogService;
    @Autowired
    private MultimediaService multimediaService;

//    @Login
    @PostMapping("/list")
    @ApiOperation("分页列表")
    @RequiresPermissions("operation:announce:list")
    public R list(
//            @ApiIgnore @LoginUser UserEntity userEntity,
            @ApiIgnore @GetUser UserEntity userEntity,
                  @RequestBody AnnounceQueryParam announceQueryParam) {
        if (null != userEntity) {
            announceQueryParam.setViewUid(userEntity.getId());
        }
        PageUtils page = announceService.queryPage(BeanUtil.bean2map(announceQueryParam));

        return R.ok().put("announce", page);
    }


    @PostMapping({"/info/{id}", "/info/{type}/{id}"})
    @ApiOperation("信息")
    @RequiresPermissions("operation:announce:info")
    public R info(@ApiIgnore @GetUser UserEntity userEntity,
                  @PathVariable("id") Long id,
                  @PathVariable(name = "type", required = false) String type){
        AnnounceEntity announceEntity = announceService.info(id, userEntity);

        if (userEntity != null && type != null && "history".equals(type)) {
            hisViewLogService.save(BizTypeEnum.Announcement, userEntity, id);
        }

        return R.ok().put("announce", announceEntity);
    }

    @Login
    @PostMapping("/save")
    @ApiOperation("保存")
    @RequiresPermissions("operation:announce:save")
    public R save(@ApiIgnore @LoginUser UserEntity userEntity,
                  @RequestBody AnnounceEntity announceEntity){
        announceEntity.setUid(userEntity.getId());
        announceService.save(announceEntity);

        return R.ok();
    }

    @Login
    @PostMapping("/update")
    @ApiOperation("修改")
    @RequiresPermissions("operation:announce:update")
    public R update(@RequestBody AnnounceEntity announceEntity){
        ValidatorUtils.validateEntity(announceEntity);
        announceService.update(announceEntity);

        return R.ok();
    }

    @Login
    @PostMapping("/delete")
    @ApiOperation("删除")
    @RequiresPermissions("operation:announce:delete")
    public R delete(@RequestBody Long[] ids){
        List<AnnounceEntity> announceEntityList = new ArrayList<>();
        for (Long id: ids) {
            AnnounceEntity announceEntity = new AnnounceEntity();
            announceEntity.setId(id);
            announceEntity.setRecordStatus(0);
            announceEntityList.add(announceEntity);
        }
        announceService.insertOrUpdateBatch(announceEntityList);

        return R.ok();
    }

    /****************************************************************
     *                            具体操作
     ****************************************************************/

    //撤销公示
    @Login
    @PostMapping("/revoke")
    @ApiOperation("撤销公示")
    public R revoke(@ApiIgnore @LoginUser UserEntity user,
                    @RequestBody AnnounceOpeForm announceOpeForm) {
        if (announceOpeForm != null && "revoke".equalsIgnoreCase(announceOpeForm.getOpeName())) {
            AnnounceEntity announceEntity = announceService.info(announceOpeForm.getAid(), null);
            if (announceEntity.getUid().equals(announceOpeForm.getUid())) {
                Date now = new Date();
                if (now.before(announceEntity.getEndTime())) {
                    announceEntity.setStatus(AnnounceStatusEnum.撤销.getCode());
                    announceService.update(announceEntity);
                } else {
                    throw new RRException("已过结束时间!");
                }
            } else {
                throw new RRException("非本人无法撤销!");
            }
            return R.ok();
        } else {
            return R.error("非法操作");
        }
    }

    @Login
    @PostMapping("/finish")
    @ApiOperation("完成公示")
    public R finish(@ApiIgnore @LoginUser UserEntity user,
                    @RequestBody AnnounceOpeForm announceOpeForm) {
        announceService.finish(announceOpeForm);
        return R.ok();
    }

    /**
     * 多文件上传公告(结束)附件(目前不使用)
     * @param files
     * @return
     * @throws Exception
     */
    @Login
    @ApiOperation("多文件上传公告(结束)附件")
    @PostMapping("uploadNoticeFile")
    public R uploadNoticeFile(@RequestParam("files") MultipartFile[] files) throws Exception {
        return R.ok(multimediaService.insertBatchByPublicity(files));
    }

}
