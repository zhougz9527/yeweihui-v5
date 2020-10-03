package com.yeweihui.modules.operation.controller;

import com.itextpdf.text.DocumentException;
import com.yeweihui.common.utils.BeanUtil;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.R;
import com.yeweihui.common.validator.ValidatorUtils;
import com.yeweihui.modules.operation.entity.AnnounceEntity;
import com.yeweihui.modules.operation.service.AnnounceService;
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

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 公示记录表
 *
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
@RestController
@RequestMapping("operation/announce")
public class AnnounceController {
    @Autowired
    private AnnounceService announceService;
    @Autowired
    private PdfUtils pdfUtils;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("operation:announce:list")
    public R list(PaperQueryParam paperQueryParam){
        PageUtils page = announceService.queryPage(BeanUtil.bean2map(paperQueryParam));

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("operation:announce:info")
    public R info(@PathVariable("id") Long id) {
        AnnounceEntity announce = announceService.info(id,null);

        return R.ok().put("announce", announce);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("operation:announce:save")
    public R save(@RequestBody AnnounceEntity announceEntity){
        Long userId = ShiroUtils.getUserId();
        announceEntity.setUid(userId);
        announceService.save(announceEntity);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("operation:announce:update")
    public R update(@RequestBody AnnounceEntity announceEntity){
        ValidatorUtils.validateEntity(announceEntity);
        announceService.update(announceEntity);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
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

    /**
     * 隐藏
     */
    @RequestMapping("/hide")
    @RequiresPermissions("operation:announce:hide")
    public R hide(@RequestBody Long[] ids){
        List<AnnounceEntity> announceEntityList = new ArrayList<>();
        for (Long id: ids) {
            AnnounceEntity announceEntity = new AnnounceEntity();
            announceEntity.setId(id);
            announceEntity.setRecordStatus(1);
            announceEntityList.add(announceEntity);
        }
        announceService.insertOrUpdateBatch(announceEntityList);

        return R.ok();
    }

    /**
     * 恢复
     */
    @RequestMapping("/recovery")
    @RequiresPermissions("operation:announce:recovery")
    public R recovery(@RequestBody Long[] ids){
        List<AnnounceEntity> announceEntityList = new ArrayList<>();
        for (Long id: ids) {
            AnnounceEntity announceEntity = new AnnounceEntity();
            announceEntity.setId(id);
            announceEntity.setRecordStatus(2);
            announceEntityList.add(announceEntity);
        }
        announceService.insertOrUpdateBatch(announceEntityList);

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
        AnnounceEntity announceEntity = announceService.info(id, null);
        boolean needAttach = true;
        if (StringUtils.isNotBlank(needAttachStr) && "no".equals(needAttachStr)) {
            needAttach = false;
        }
        Map data = BeanUtil.bean2map(announceEntity);
        data.put("needAttach", needAttach);
        // 补充打印人信息
        UserEntity userEntity = ShiroUtils.getUserEntity();
        data.put("printer", userEntity.getRealname());
        data.put("printerDate", new Date());
        String content = pdfUtils.freeMarkerRender(data,"pdf-ftl/announce.ftl");
        //创建pdf
        pdfUtils.createPdf(content, "yeweihui-admin/target/", "announce.pdf");
        // 读取pdf并预览
        pdfUtils.readPdf(response, "yeweihui-admin/target/announce.pdf");
    }


}
