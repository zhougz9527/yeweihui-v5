package com.yeweihui.modules.test;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.yeweihui.common.utils.R;
import com.yeweihui.modules.bfly.entity.BflyUser;
import com.yeweihui.modules.bfly.entity.BflyVote;
import com.yeweihui.modules.bfly.service.BflyUserService;
import com.yeweihui.modules.bfly.service.impl.BflyUserCertRecordServiceImpl;
import com.yeweihui.modules.user.dao.UserDao;
import com.yeweihui.modules.user.entity.UserEntity;
import com.yeweihui.modules.vo.api.vo.SubjectVO;
import com.yeweihui.modules.vo.bfly.form.room.CheckRoomForm;
import com.yeweihui.modules.vo.bfly.form.user.SubmitCertFrom;
import com.yeweihui.third.sms.SendSmsUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import com.yeweihui.modules.bfly.service.BflyVoteService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.yeweihui.modules.enums.bfly.MagicEnum.审核通过;

@RestController
@RequestMapping("/mytest")
@Api(tags = "测试接口")
@ApiIgnore
public class AdminTestController {
    @Resource
    private UserDao userDao;
    @Autowired
    BflyUserCertRecordServiceImpl bflyUserCertRecordService;
    @Resource
    SendSmsUtils sendSmsUtils;

    @Resource
    BflyUserService bflyUserService;
    @Resource
    BflyVoteService bflyVoteService;


    @RequestMapping("/testUserDao")
    public String testUserDao() {
        UserEntity manage = userDao.getManageByZoneId((long) 5);
        System.out.println(manage);
        return manage.toString();
    }

    @RequestMapping("/test")
    public String test() {
        HashMap<String, Object> test = bflyUserCertRecordService.selectUserCertList(1l, "通知你", "15602626126", 1, 10, 1);
        return test.toString();
    }

    @RequestMapping("test1")
    public String test1(@RequestParam(value = "ownerName", required = false) String ownerName,
                        @RequestParam(value = "phoneNum", required = false) String phoneNum,
                        @RequestParam(value = "zoneId", required = false) Long zoneId,
                        @RequestParam(value = "page", required = false) Integer page,
                        @RequestParam(value = "size", required = false) Integer size,
                        @RequestParam(value = "submitMethod", required = false) Integer submitMethod) {
        HashMap<String, Object> test = bflyUserCertRecordService.selectUserCertList(zoneId, ownerName, phoneNum, page, size, 1);
        return test.toString();
    }

    @RequestMapping("/test2")
    public String test2(SubmitCertFrom submitCertFrom) throws Exception {
        String s = bflyUserService.submitCert(submitCertFrom);
        return s;
    }

    @RequestMapping("/test3")
    public R test3(@RequestBody CheckRoomForm checkRoomForm) {
        return R.ok().put("result", bflyUserService.checkRoomStatus(checkRoomForm));
    }

    @RequestMapping("/test4")
    public R historyVote(@PathVariable("id") Long id) {
        List<BflyVote> list = bflyVoteService.historyVote(id);
        return R.ok().put("result", list);
    }


    @RequestMapping("/test5")
    public String submitCert(@RequestBody SubmitCertFrom submitCertFrom) throws Exception {
        return bflyUserService.submitCert(submitCertFrom);
    }

    @RequestMapping("/test6")
    public String pushMessageToPerson(Long uid, Long voteId) {
        bflyVoteService.pushMessageToPerson(uid, voteId);
        return "成功";
    }

    @RequestMapping("/test7")
    public R test7(String phone) {
        List<BflyUser> phone_num = bflyUserService.selectList(new EntityWrapper<BflyUser>()
                .eq("phone_num", phone)
                .eq("status", (Integer) 审核通过.getCode()));
        if (null != phone_num && phone_num.size() != 0) {
            return R.error("该手机号已经被认证");
        }
        return R.ok("发短信");
    }

    @RequestMapping("/test8")
    public R test8(Long voteId) {
        bflyVoteService.voteResult(voteId);
        return R.ok();
    }

    @RequestMapping("test9")
    public String test9() {
        bflyVoteService.crontabUpdateVote();
        return "success";
    }
    public static void main(String[] args) {
        List<SubjectVO> list=new ArrayList<SubjectVO>();
        SubjectVO subjectVO1=new SubjectVO();
        subjectVO1.setScount(2);
        subjectVO1.setMoney("100");
        SubjectVO subjectVO2=new SubjectVO();
        subjectVO2.setMoney("100");
        SubjectVO subjectVO3=new SubjectVO();
        subjectVO3.setMoney("100");
        SubjectVO subjectVO4=new SubjectVO();
        subjectVO4.setScount(2);
        subjectVO4.setMoney("100");
        SubjectVO subjectVO5=new SubjectVO();
        subjectVO5.setMoney("100");
        SubjectVO subjectVO6=new SubjectVO();
        subjectVO6.setMoney("10110");
        list.add(subjectVO1);
        list.add(subjectVO2);
        list.add(subjectVO3);
        list.add(subjectVO4);
        list.add(subjectVO5);
        list.add(subjectVO6);
        int ind=1;
        int ss=0;
        StringBuffer html = new StringBuffer();
        for ( int i=0; i<list.size();i++)
        {
            if (i < ss) {
                html.append("<td >'"+list.get(i).getMoney()+"'</td>");
            }
            else
            {
                if(!list.get(i).getScount().equals("")) {
                    int scount=list.get(i).getScount();
                    ss = i+scount;
                    html.append("<td rowspan='"+scount+"'>"+ind+"</td>");
                    html.append("<td >'"+list.get(i).getMoney()+"'</td>");
                    ind=ind+1;

                }
                else {
                    html.append("<td >"+ind+"</td>");
                    html.append("<td >'" + list.get(i).getMoney() + "'</td>");
                    ind=ind+1;
                }
            }

        }
        System.out.println(html.toString());
    }

}




