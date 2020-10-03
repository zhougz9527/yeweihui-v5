package com.yeweihui.modules.operation.controller;

import com.itextpdf.text.DocumentException;
import com.yeweihui.common.exception.RRException;
import com.yeweihui.common.utils.BeanUtil;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.R;
import com.yeweihui.common.validator.ValidatorUtils;
import com.yeweihui.modules.operation.entity.BillEntity;
import com.yeweihui.modules.operation.service.BillService;
import com.yeweihui.modules.sys.shiro.ShiroUtils;
import com.yeweihui.modules.user.entity.UserEntity;
import com.yeweihui.modules.vo.query.BillQueryParam;
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 报销表
 *
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
@RestController
@RequestMapping("operation/bill")
public class BillController {
    @Autowired
    private BillService billService;
    @Autowired
    PdfUtils pdfUtils;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("operation:bill:list")
    public R list(BillQueryParam billQueryParam){
        PageUtils page = billService.queryPage(BeanUtil.bean2map(billQueryParam));

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */

    @RequestMapping("/info/{id}")
    @RequiresPermissions("operation:bill:info")
    public R info(@PathVariable("id") Long id){
        BillEntity bill = billService.info(id, null);

        return R.ok().put("bill", bill);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("operation:bill:save")
    public R save(@RequestBody BillEntity bill){
        Long userId = ShiroUtils.getUserId();
        bill.setUid(userId);
        billService.save(bill);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("operation:bill:update")
    public R update(@RequestBody BillEntity bill){
        ValidatorUtils.validateEntity(bill);
        billService.update(bill);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
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

    /**
     * 隐藏
     */
    @RequestMapping("/hide")
    @RequiresPermissions("operation:bill:hide")
    public R hide(@RequestBody Long[] ids){
        List<BillEntity> billEntities = new ArrayList<>();
        for (Long id: ids) {
            BillEntity billEntity = new BillEntity();
            billEntity.setId(id);
            billEntity.setRecordStatus(1);
            billEntities.add(billEntity);
        }
        billService.insertOrUpdateBatch(billEntities);

        return R.ok();
    }

    /**
     * 恢复
     */
    @RequestMapping("/recovery")
    @RequiresPermissions("operation:bill:recovery")
    public R recovery(@RequestBody Long[] ids){
        List<BillEntity> billEntities = new ArrayList<>();
        for (Long id: ids) {
            BillEntity billEntity = new BillEntity();
            billEntity.setId(id);
            billEntity.setRecordStatus(2);
            billEntities.add(billEntity);
        }
        billService.insertOrUpdateBatch(billEntities);

        return R.ok();
    }
    /**
     * 报销pdf
     * @param response
     * @param id
     * @throws IOException
     * @throws DocumentException
     */
    @RequestMapping(value = "viewPdf/{id}/{needAttach}")
    public void viewPdf(HttpServletResponse response, @PathVariable("id") Long id, @PathVariable("needAttach") String needAttachStr) throws IOException, DocumentException {
        //需要填充的数据
        BillEntity billEntity = billService.info(id, null);
        boolean needAttach = true;
        if (StringUtils.isNotBlank(needAttachStr) && "no".equals(needAttachStr)) {
            needAttach = false;
        }
        Map data = BeanUtil.bean2map(billEntity);
        data.put("needAttach", needAttach);
        // 补充打印人信息
        UserEntity userEntity = ShiroUtils.getUserEntity();
        data.put("printer", userEntity.getRealname());
        String content = pdfUtils.freeMarkerRender(data,"pdf-ftl/bill.ftl");
        //创建pdf
        pdfUtils.createPdf(content, "yeweihui-admin/target/", "bill.pdf");
        // 读取pdf并预览
        pdfUtils.readPdf(response, "yeweihui-admin/target/bill.pdf");
    }

}
