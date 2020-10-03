package com.yeweihui.modules.operation.controller;

import java.io.IOException;
import java.util.*;

import com.itextpdf.text.DocumentException;
import com.yeweihui.common.utils.BeanUtil;
import com.yeweihui.common.validator.ValidatorUtils;
import com.yeweihui.modules.operation.entity.PaperEntity;
import com.yeweihui.modules.sys.shiro.ShiroUtils;
import com.yeweihui.modules.user.entity.UserEntity;
import com.yeweihui.modules.vo.query.PaperQueryParam;
import com.yeweihui.third.pdf.PdfUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yeweihui.modules.operation.service.PaperService;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.R;

import javax.servlet.http.HttpServletResponse;


/**
 * 来往函件表
 *
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
@RestController
@RequestMapping("operation/paper")
public class PaperController {
    @Autowired
    private PaperService paperService;
    @Autowired
    private PdfUtils pdfUtils;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("operation:paper:list")
    public R list(PaperQueryParam paperQueryParam){
        PageUtils page = paperService.queryPage(BeanUtil.bean2map(paperQueryParam));

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("operation:paper:info")
    public R info(@PathVariable("id") Long id){
        PaperEntity paper = paperService.info(id, null);

        return R.ok().put("paper", paper);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("operation:paper:save")
    public R save(@RequestBody PaperEntity paper){
        Long userId = ShiroUtils.getUserId();
        paper.setUid(userId);
        paperService.save(paper);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("operation:paper:update")
    public R update(@RequestBody PaperEntity paper){
        ValidatorUtils.validateEntity(paper);
        paperService.update(paper);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
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

    /**
     * 隐藏
     */
    @RequestMapping("/hide")
    @RequiresPermissions("operation:paper:hide")
    public R hide(@RequestBody Long[] ids){
        List<PaperEntity> paperEntities = new ArrayList<>();
        for (Long id: ids) {
            PaperEntity paperEntity = new PaperEntity();
            paperEntity.setId(id);
            paperEntity.setRecordStatus(1);
            paperEntities.add(paperEntity);
        }
        paperService.insertOrUpdateBatch(paperEntities);

        return R.ok();
    }

    /**
     * 恢复
     */
    @RequestMapping("/recovery")
    @RequiresPermissions("operation:paper:recovery")
    public R recovery(@RequestBody Long[] ids){
        List<PaperEntity> paperEntities = new ArrayList<>();
        for (Long id: ids) {
            PaperEntity paperEntity = new PaperEntity();
            paperEntity.setId(id);
            paperEntity.setRecordStatus(2);
            paperEntities.add(paperEntity);
        }
        paperService.insertOrUpdateBatch(paperEntities);

        return R.ok();
    }


    /**
     * 发函pdf
     * @param response
     * @param id
     * @throws IOException
     * @throws DocumentException
     */
    @RequestMapping(value = "viewPdf/{id}/{needAttach}")
    public void viewPdf(HttpServletResponse response, @PathVariable("id") Long id, @PathVariable("needAttach") String needAttachStr) throws IOException, DocumentException {
        //需要填充的数据
        PaperEntity paperEntity = paperService.info(id, null);
        boolean needAttach = true;
        if (StringUtils.isNotBlank(needAttachStr) && "no".equals(needAttachStr)) {
            needAttach = false;
        }
        Map data = BeanUtil.bean2map(paperEntity);
        data.put("needAttach", needAttach);
        // 补充打印人信息
        UserEntity userEntity = ShiroUtils.getUserEntity();
        data.put("printer", userEntity.getRealname());
        data.put("printerDate", new Date());
        String content = pdfUtils.freeMarkerRender(data,"pdf-ftl/paper.ftl");
        //创建pdf
        pdfUtils.createPdf(content, "yeweihui-admin/target/", "paper.pdf");
        // 读取pdf并预览
        pdfUtils.readPdf(response, "yeweihui-admin/target/paper.pdf");
    }


}
