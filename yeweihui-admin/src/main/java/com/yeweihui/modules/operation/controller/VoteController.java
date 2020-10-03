package com.yeweihui.modules.operation.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.DocumentException;
import com.yeweihui.common.exception.RRException;
import com.yeweihui.common.utils.BeanUtil;
import com.yeweihui.common.validator.ValidatorUtils;
import com.yeweihui.modules.enums.vote.VoteItemTypeEnum;
import com.yeweihui.modules.operation.entity.VoteEntity;
import com.yeweihui.modules.operation.entity.VoteMemberEntity;
import com.yeweihui.modules.operation.service.VoteMemberService;
import com.yeweihui.modules.sys.shiro.ShiroUtils;
import com.yeweihui.modules.vo.query.VoteMemberQueryParam;
import com.yeweihui.modules.vo.query.VoteQueryParam;
import com.yeweihui.third.pdf.PdfUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yeweihui.modules.operation.service.VoteService;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.R;

import javax.servlet.http.HttpServletResponse;


/**
 * 事务表决表
 *
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
@RestController
@RequestMapping("operation/vote")
public class VoteController {
    @Autowired
    private VoteService voteService;
    @Autowired
    private VoteMemberService voteMemberService;
    @Autowired
    private PdfUtils pdfUtils;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("operation:vote:list")
    public R list(VoteQueryParam voteQueryParam){
        PageUtils page = voteService.queryPage(BeanUtil.bean2map(voteQueryParam));

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("operation:vote:info")
    public R info(@PathVariable("id") Long id){
        VoteEntity vote = voteService.info(id, null);

        return R.ok().put("vote", vote);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("operation:vote:save")
    public R save(@RequestBody VoteEntity vote){
        Long userId = ShiroUtils.getUserId();
        vote.setUid(userId);
        voteService.save(vote);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("operation:vote:update")
    public R update(@RequestBody VoteEntity vote){
        ValidatorUtils.validateEntity(vote);
        voteService.update(vote);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("operation:vote:delete")
    public R delete(@RequestBody Long[] ids){
        List<VoteEntity> voteEntities = new ArrayList<>();
        for (Long id: ids) {
            VoteEntity voteEntity = new VoteEntity();
            voteEntity.setId(id);
            voteEntity.setRecordStatus(0);
            voteEntities.add(voteEntity);
        }
        voteService.insertOrUpdateBatch(voteEntities);

        return R.ok();
    }

    /**
     * 隐藏
     */
    @RequestMapping("/hide")
    @RequiresPermissions("operation:vote:hide")
    public R hide(@RequestBody Long[] ids){
        List<VoteEntity> voteEntities = new ArrayList<>();
        for (Long id: ids) {
            VoteEntity voteEntity = new VoteEntity();
            voteEntity.setId(id);
            voteEntity.setRecordStatus(1);
            voteEntities.add(voteEntity);
        }
        voteService.insertOrUpdateBatch(voteEntities);

        return R.ok();
    }

    /**
     * 恢复
     */
    @RequestMapping("/recovery")
    @RequiresPermissions("operation:vote:recovery")
    public R recovery(@RequestBody Long[] ids){
        List<VoteEntity> voteEntities = new ArrayList<>();
        for (Long id: ids) {
            VoteEntity voteEntity = new VoteEntity();
            voteEntity.setId(id);
            voteEntity.setRecordStatus(2);
            voteEntities.add(voteEntity);
        }
        voteService.insertOrUpdateBatch(voteEntities);

        return R.ok();
    }


    /**
     * 投票pdf
     * @param response
     * @param id
     * @throws IOException
     * @throws DocumentException
     */
    @RequestMapping(value = "viewPdf/{id}/{needAttach}")
    public void viewPdf(HttpServletResponse response, @PathVariable("id") Long id, @PathVariable("needAttach") String needAttachStr) throws IOException, DocumentException {
        //需要填充的数据
        VoteEntity voteEntity = voteService.info(id, null);
        boolean needAttach = true;
        if (StringUtils.isNotBlank(needAttachStr) && "no".equals(needAttachStr)) {
            needAttach = false;
        }
        Map data = BeanUtil.bean2map(voteEntity);
        data.put("needAttach", needAttach);
        if (voteEntity.getItemType().equals(VoteItemTypeEnum.多选.getCode())){
            String content = pdfUtils.freeMarkerRender(data,"pdf-ftl/vote_mul.ftl");
            //创建pdf
            pdfUtils.createPdf(content, "yeweihui-admin/target/", "vote_mul.pdf");
            // 读取pdf并预览
            pdfUtils.readPdf(response, "yeweihui-admin/target/vote_mul.pdf");
        }else if(voteEntity.getItemType().equals(VoteItemTypeEnum.单项.getCode())){
            String content = pdfUtils.freeMarkerRender(data,"pdf-ftl/vote_one.ftl");
            //创建pdf
            pdfUtils.createPdf(content, "yeweihui-admin/target/", "vote_one.pdf");
            // 读取pdf并预览
            pdfUtils.readPdf(response, "yeweihui-admin/target/vote_one.pdf");
        }
    }
}
