package com.yeweihui.modules.bfly;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.yeweihui.common.utils.BeanUtil;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.R;
import com.yeweihui.modules.bfly.entity.BflyRoom;
import com.yeweihui.modules.bfly.entity.BflyUser;
import com.yeweihui.modules.bfly.entity.BflyVote;
import com.yeweihui.modules.bfly.service.BflyRoomService;
import com.yeweihui.modules.bfly.service.BflyUserCertRecordService;
import com.yeweihui.modules.bfly.service.BflyUserService;
import com.yeweihui.modules.bfly.service.BflyVoteService;
import com.yeweihui.modules.sys.shiro.ShiroUtils;
import com.yeweihui.modules.user.entity.UserEntity;
import com.yeweihui.modules.user.service.ZonesService;
import com.yeweihui.third.pdf.PdfUtils;
import io.swagger.models.auth.In;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.yeweihui.modules.enums.bfly.MagicEnum.*;

@RestController
@RequestMapping("bflyUser")
public class BflyUserController {

    @Autowired
    private BflyUserService bflyUserService;
    @Autowired
    BflyUserCertRecordService bflyUserCertRecordService;
    @Resource
    BflyVoteService bflyVoteService;
    @Resource
    BflyRoomService bflyRoomService;
    @Resource
    private PdfUtils pdfUtils;

    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        UserEntity userEntity = ShiroUtils.getUserEntity();
        Long roleId = userEntity.getRoleId();
        if (roleId != 0) {
            params.put("zoneId", userEntity.getZoneId());
        }
        PageUtils pages = bflyUserService.queryPage(params);
        return R.ok().put("page", pages);
    }

    /**
     * 业主信息
     * @param id
     * @return
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable Long id) {
        BflyUser bflyUser = bflyUserService.selectOne(new EntityWrapper<BflyUser>().eq("id", id));
        return R.ok().put("result", bflyUser);
    }


    /**
     * 更新业主的信息接口
     * @param bflyUser
     * @return
     */
    @PostMapping("/update")
    public R update(@RequestBody BflyUser bflyUser) {
        return R.ok().put("bflyUser", bflyUserService.updateUser(bflyUser));
    }

    @PostMapping("/delete/{id}")
    public R delete(@PathVariable Long id) {
        String result = bflyUserService.deleteUser(id);
        return R.ok(result);
    }

    
    /** 
    * @Description: 查询业主列表（已认证和认证后撤销认证的）
    * @Param:
    * @return:  
    * @Author: tenaciousVine
    * @Date: 2020/3/16 
    */
    @PostMapping("/selectOwnerList")
    public R selectOwnerList(
            @RequestParam(value = "ownerName", required = false) String ownerName,
            @RequestParam(value = "phoneNum", required = false) String phoneNum,
            @RequestParam(value = "zoneId", required = false) Long zoneId,
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size
    ) {
        Integer[] statusArray = null;
        if (null != status) {
            statusArray = new Integer[]{status};
        } else {
            statusArray = new Integer[]{(Integer) 无效.getCode(), (Integer) 有效.getCode()};
        }
        HashMap hashMap = bflyUserService.queryOwnerInfo(zoneId, ownerName, phoneNum, statusArray, page, size);
        return R.ok().put("page", hashMap);
    }

    
    /** 
    * @Description: 查询业主审核列表 
    * @Param:  
    * @return:  
    * @Author: tenaciousVine
    * @Date: 2020/3/16 
    */
    @PostMapping("/selectOwnerCertList")
    public R selectOwnerCertList(
            @RequestParam(value = "ownerName", required = false) String ownerName,
            @RequestParam(value = "phoneNum", required = false) String phoneNum,
            @RequestParam(value = "zoneId", required = false) Long zoneId,
            @RequestParam(value = "submitMethod", required = false, defaultValue = "0") Integer submitMethod,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size
    ) {
        HashMap hashMap = bflyUserCertRecordService.selectUserCertList(zoneId, ownerName, phoneNum, page, size, submitMethod);
        return R.ok().put("page", hashMap);
    }


    /**
    * @Description: 审核用户
    * @Param: userCertId  bfly_user_cert_record表id
    * @return:
    * @Author: tenaciousVine
    * @Date: 2020/3/16
    */
    @PostMapping("/certOwner")
    public R certOwner(@RequestParam(value = "userCertId") Long userCertId,
                       @RequestParam(value = "cert") Boolean cert) {
        bflyUserCertRecordService.certOwner(userCertId, cert);
        return R.ok();
    }

    /**
     * 导出用户参会的详情PDF
     * @param response
     * @param voteId
     * @param userId
     * @param needAttachStr
     * @throws Exception
     */
    @GetMapping("/meetingVoteDetailExport")
    public void meetingVoteDetailExport(HttpServletResponse response,
                         @RequestParam(value = "voteId") Long voteId,
                         @RequestParam(value = "userId") Long userId,
                         @RequestParam(value = "needAttach", required = false) String needAttachStr) throws Exception {

        BflyVote bflyVote = bflyVoteService.searchUserVoteDetail(userId, voteId);
        boolean needAttach = true;
        if (StringUtils.isNotBlank(needAttachStr) && "no".equals(needAttachStr)) {
            needAttach = false;
        }
        Map data = BeanUtil.bean2map(bflyVote);
        data.put("needAttach", needAttach);
        // 补充打印人信息
        UserEntity userEntity = ShiroUtils.getUserEntity();
        data.put("printer", userEntity.getRealname());
        data.put("printerDate", new Date());
        String content = pdfUtils.freeMarkerRender(data,"pdf-ftl/meetingVoteDetail.ftl");
        //创建pdf
        pdfUtils.createPdf(content, "yeweihui-admin/target/", "meetingVoteDetail.pdf");
        // 读取pdf并预览
        pdfUtils.readPdf(response, "yeweihui-admin/target/meetingVoteDetail.pdf");
    }

    /**
     * 导出用户表决投票详情的PDF
     * @param response
     * @param voteId
     * @param userId
     * @param needAttachStr
     */
    @GetMapping("/voteDetailExport")
    public void voteDetail(HttpServletResponse response,
                           @RequestParam(value = "voteId") Long voteId,
                           @RequestParam(value = "userId") Long userId,
                           @RequestParam(value = "needAttach", required = false) String needAttachStr) throws Exception {

        BflyVote bflyVote = bflyVoteService.searchUserVoteDetail(userId, voteId);
        boolean needAttach = true;
        if (StringUtils.isNotBlank(needAttachStr) && "no".equals(needAttachStr)) {
            needAttach = false;
        }
        Map data = BeanUtil.bean2map(bflyVote);
        data.put("needAttach", needAttach);
        // 补充打印人信息
        UserEntity userEntity = ShiroUtils.getUserEntity();
        data.put("printer", userEntity.getRealname());
        data.put("printerDate", new Date());
        String content = pdfUtils.freeMarkerRender(data,"pdf-ftl/voteDetail.ftl");
        //创建pdf
        pdfUtils.createPdf(content, "yeweihui-admin/target/", "voteDetail.pdf");
        // 读取pdf并预览
        pdfUtils.readPdf(response, "yeweihui-admin/target/voteDetail.pdf");
    }

    /**
     * 导出表决投票详情的PDF
     * @param response
     * @param id
     * @param needAttachStr
     * @throws Exception
     */
    @GetMapping("/voteManageDetailExport")
    public void voteManageDetailExport(HttpServletResponse response,
                                       @RequestParam(value = "id") Long id,
                                       @RequestParam(value = "needAttach", required = false) String needAttachStr) throws Exception{
        BflyVote bflyVote = bflyVoteService.info(id);
        boolean needAttach = true;
        if (StringUtils.isNotBlank(needAttachStr) && "no".equals(needAttachStr)) {
            needAttach = false;
        }
        Map data = BeanUtil.bean2map(bflyVote);
        data.put("needAttach", needAttach);
        // 补充打印人信息
        UserEntity userEntity = ShiroUtils.getUserEntity();
        data.put("printer", userEntity.getRealname());
        data.put("printerDate", new Date());
        String content = pdfUtils.freeMarkerRender(data,"pdf-ftl/voteManageDetail.ftl");
        //创建pdf
        pdfUtils.createPdf(content, "yeweihui-admin/target/", "voteManageDetail.pdf");
        // 读取pdf并预览
        pdfUtils.readPdf(response, "yeweihui-admin/target/voteManageDetail.pdf");

    }

    @PostMapping("queryUserNumAndAllRoomArea")
    public R queryUserNumAndAllRoomArea(@RequestParam(value = "zoneId", required = false) Long zoneId) {
        Integer[] statusArray = {(Integer) 审核通过.getCode()};
        Map<String, Object> result = bflyUserService.queryUserNumAndAllAreaByZoneIdAndUserStatus(zoneId, statusArray);
        return R.ok().put("result", result);
    }
}
