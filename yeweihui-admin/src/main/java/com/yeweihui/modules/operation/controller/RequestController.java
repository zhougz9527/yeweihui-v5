package com.yeweihui.modules.operation.controller;

import com.itextpdf.text.DocumentException;
import com.yeweihui.common.exception.RRException;
import com.yeweihui.common.utils.BeanUtil;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.R;
import com.yeweihui.common.validator.ValidatorUtils;
import com.yeweihui.modules.operation.entity.RequestEntity;
import com.yeweihui.modules.operation.service.RequestService;
import com.yeweihui.modules.sys.shiro.ShiroUtils;
import com.yeweihui.modules.vo.query.RequestQueryParam;
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
 * 用章申请表
 *
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
@RestController
@RequestMapping("operation/request")
public class RequestController {
    @Autowired
    private RequestService requestService;
    @Autowired
    PdfUtils pdfUtils;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("operation:request:list")
    public R list(RequestQueryParam requestQueryParam){
        PageUtils page = requestService.queryPage(BeanUtil.bean2map(requestQueryParam));

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("operation:request:info")
    public R info(@PathVariable("id") Long id){
        RequestEntity request = requestService.info(id, null);

        return R.ok().put("request", request);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("operation:request:save")
    public R save(@RequestBody RequestEntity request){
        Long userId = ShiroUtils.getUserId();
        request.setUid(userId);
        requestService.save(request);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("operation:request:update")
    public R update(@RequestBody RequestEntity request){
        ValidatorUtils.validateEntity(request);
        requestService.update(request);//全部更新

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
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

    /**
     * 隐藏
     */
    @RequestMapping("/hide")
    @RequiresPermissions("operation:request:hide")
    public R hide(@RequestBody Long[] ids){
        List<RequestEntity> requestEntities = new ArrayList<>();
        for (Long id: ids) {
            RequestEntity requestEntity = new RequestEntity();
            requestEntity.setId(id);
            requestEntity.setRecordStatus(1);
            requestEntities.add(requestEntity);
        }
        requestService.insertOrUpdateBatch(requestEntities);

        return R.ok();
    }

    /**
     * 恢复
     */
    @RequestMapping("/recovery")
    @RequiresPermissions("operation:request:recovery")
    public R recovery(@RequestBody Long[] ids){
        List<RequestEntity> requestEntities = new ArrayList<>();
        for (Long id: ids) {
            RequestEntity requestEntity = new RequestEntity();
            requestEntity.setId(id);
            requestEntity.setRecordStatus(2);
            requestEntities.add(requestEntity);
        }
        requestService.insertOrUpdateBatch(requestEntities);

        return R.ok();
    }


    /**
     * 用章pdf
     * @param response
     * @param id
     * @throws IOException
     * @throws DocumentException
     */
    @RequestMapping(value = "viewPdf/{id}/{needAttach}")
    public void viewPdf(HttpServletResponse response, @PathVariable("id") Long id, @PathVariable("needAttach") String needAttachStr) throws IOException, DocumentException {
        //需要填充的数据
        RequestEntity requestEntity = requestService.info(id, null);
        boolean needAttach = true;
        if (StringUtils.isNotBlank(needAttachStr) && "no".equals(needAttachStr)) {
            needAttach = false;
        }
        Map data = BeanUtil.bean2map(requestEntity);
        data.put("needAttach", needAttach);
        String content = pdfUtils.freeMarkerRender(data,"pdf-ftl/request.ftl");
        //创建pdf
        pdfUtils.createPdf(content, "yeweihui-admin/target/", "request.pdf");
        // 读取pdf并预览
        pdfUtils.readPdf(response, "yeweihui-admin/target/request.pdf");
    }

}
