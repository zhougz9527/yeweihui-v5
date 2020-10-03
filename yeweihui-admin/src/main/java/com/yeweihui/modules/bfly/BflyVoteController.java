package com.yeweihui.modules.bfly;

import com.alibaba.fastjson.JSONArray;
import com.yeweihui.common.exception.RRException;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.R;
import com.yeweihui.modules.bfly.dao.BflySubVoteDao;
import com.yeweihui.modules.bfly.entity.BflySubVote;
import com.yeweihui.modules.bfly.entity.BflyUserVoteInfo;
import com.yeweihui.modules.bfly.entity.BflyVote;
import com.yeweihui.modules.bfly.entity.BflyVoteItem;
import com.yeweihui.modules.bfly.service.BflyUserSubVoteService;
import com.yeweihui.modules.bfly.service.BflyUserVoteService;
import com.yeweihui.modules.bfly.service.BflyVoteService;
import com.yeweihui.modules.bfly.service.WxMessageService;
import com.yeweihui.modules.common.service.ExcelService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

import static com.yeweihui.modules.enums.bfly.MagicEnum.*;

@RestController
@RequestMapping("bflyVote")
public class BflyVoteController {

    @Autowired
    private BflyVoteService bflyVoteService;
    @Resource
    private WxMessageService wxMessageService;
    @Resource
    private ExcelService excelService;
    @Resource
    private BflyUserVoteService bflyUserVoteService;
    @Resource
    private BflyUserSubVoteService bflyUserSubVoteService;
    @Resource
    private BflySubVoteDao bflySubVoteDao;


    /**
     * 发起表决
     * @param bflyVote
     * @return
     */
    @PostMapping("/save")
    public R save(@RequestBody BflyVote bflyVote) {
        String checkResult = this.checkSaveParm(bflyVote);
        if (null != checkResult) {
            return R.error(checkResult);
        }
        wxMessageService.getPublicOpenIds();
        bflyVoteService.saveVote(bflyVote);
        return R.ok();
    }

    //校验发起表决时的相关参数
    private String checkSaveParm(BflyVote bflyVote) {
        if (null == bflyVote.getTitle() || bflyVote.getTitle() == "") {
            return  "请输入会议主题";
        }
        if (null == bflyVote.getMeetingStartTime() || null == bflyVote.getMeetingEndTime() || null == bflyVote.getVoteStartTime() || null == bflyVote.getVoteEndTime()) {
            return "请输入会议相关的时间";
        }
        for (BflySubVote bflySubVote : bflyVote.getBflySubVotes()) {
            if (null == bflySubVote.getTitle() || bflySubVote.getTitle() == "") {
                return "请输入表决事项名称";
            }
            for (BflyVoteItem bflyVoteItem : bflySubVote.getBflyVoteItems()) {
                if (null == bflyVoteItem.getMatter() || bflyVoteItem.getMatter() == ""
                        || null == bflyVoteItem.getContent() || bflyVoteItem.getContent() == "") {
                    return "请补全表决选项和内容";
                }
            }
            String optionsStr = bflySubVote.getOptions();
            JSONArray optionsList = JSONArray.parseArray(optionsStr);
            int size = optionsList.size();
            for (int i = 0; i < size; i++) {
                if (optionsList.get(i).equals("")) {
                    return "请补全意见选项";
                }
            }
            for (int i = 0; i < size; i++) {
                for (int j = i + 1; j < size; j++) {
                    if (optionsList.get(i).equals(optionsList.get(j))) {
                        return "意见选项不允许相同";
                    }
                }
            }
        }
        return null;
    }

    /**
     * 表决详情
     * @param id
     * @return
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable Long id) {
        BflyVote bflyVote = bflyVoteService.info(id);
        return R.ok().put("result", bflyVote);
    }

    /**
     * 表决当前详情
     * @param id
     * @return
     */
    @GetMapping("/curInfo/{id}")
    public R curInfo(@PathVariable Long id) {
        BflyVote bflyVote = bflyVoteService.curInfo(id);
        return R.ok().put("result", bflyVote);

    }

    /**
     * 更新表决
     * @param bflyVote
     * @return
     */
    @PostMapping("/updateVote")
    public R updateVote(@RequestBody BflyVote bflyVote) {
        return R.ok().put("vote", bflyVoteService.updateVote(bflyVote));
    }

    /**
     * 更新线下结果
     * @param bflyVote
     * @return
     */
    @PostMapping("/deleteVoteOfflineUrl")
    public R deleteVoteOfflineUrl(@RequestBody BflyVote bflyVote) {
        bflyVoteService.updateBflyVoteOfflineVoteResultUrl(bflyVote.getId(), bflyVote.getOfflineVoteResultUrl());
        return R.ok();
    }

    /**
     * 用户投票列表
     * @param zoneId
     * @param nickname
     * @param title
     * @param userName
     * @param page
     * @param size
     * @return
     */
    @PostMapping("/userVoteList")
    public R userVoteList(@RequestParam(value = "zoneId", required = false) Long zoneId,
                          @RequestParam(value = "nickname", required = false) String nickname,
                          @RequestParam(value = "title", required = false) String title,
                          @RequestParam(value = "userName", required = false) String userName,
                          @RequestParam(value = "status", required = false) Integer[] status,
                          @RequestParam(value = "page", required = false) Integer page,
                          @RequestParam(value = "size", required = false) Integer size
                          ) {
        if (null == status || status.length == 0) {
            status = new Integer[]{1,2};
        }
        return R.ok().put("page", bflyVoteService.userVoteList(zoneId,nickname,title,userName,status,page,size));
    }

    /**
     * 用户参会列表
     * @param zoneId
     * @param title
     * @param userName
     * @param page
     * @param size
     * @return
     */
    @PostMapping("/userMeetingList")
    public R userMeetingList(@RequestParam(value = "zoneId", required = false) Long zoneId,
                          @RequestParam(value = "nickname", required = false) String nickname,
                          @RequestParam(value = "title", required = false) String title,
                          @RequestParam(value = "userName", required = false) String userName,
                          @RequestParam(value = "status", required = false) Integer status,
                          @RequestParam(value = "page", required = false) Integer page,
                          @RequestParam(value = "size", required = false) Integer size
    ) {
        Integer[] statusArray = null;
        if (null != status) {
            if (status.equals(用户表决未参会.getCode())) {
                statusArray = new Integer[]{(Integer) 用户表决未参会.getCode()};
            } else if (status.equals(用户表决已参会.getCode())) {
                statusArray = new Integer[]{(Integer) 用户表决已参会.getCode(), (Integer) 用户表决已投票.getCode()};
            }
        }
        if (null == statusArray){
            statusArray = new Integer[]{(Integer) 用户表决未参会.getCode(), (Integer) 用户表决已参会.getCode(), (Integer) 用户表决已投票.getCode()};
        }
        PageUtils<BflyUserVoteInfo> meetingPage = bflyVoteService.userVoteList(zoneId, nickname, title, userName, statusArray, page, size);
        List<BflyUserVoteInfo> list = meetingPage.getList();
        list.forEach(bflyUserVoteInfo -> {
            if (bflyUserVoteInfo.getStatus().equals(用户表决已投票.getCode())) {
                bflyUserVoteInfo.setStatus((Integer) 用户表决已参会.getCode());
            }
        });
        return R.ok().put("page", meetingPage);
    }

    
    /** 
    * @Description: 查询投票列表 
    * @Param:
    * @return:  
    * @Author: tenaciousVine
    * @Date: 2020/3/16 
    */
    @PostMapping("/selectVoteList")
    public R selectVoteList(
            @RequestParam(value = "zoneId", required = false) Long zoneId,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size
                            ) {
        return R.ok().put("page", bflyVoteService.selectVoteList(zoneId,title,startDate,endDate,page,size));
    }

    /**
     * @Description: 查询实时投票列表
     * @Param:
     * @return:
     * @Author: tenaciousVine
     * @Date: 2020/3/16
     */
    @PostMapping("/selectCurVoteList")
    public R selectCurVoteList(
            @RequestParam(value = "zoneId", required = false) Long zoneId,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size
    ) {
        return R.ok().put("page", bflyVoteService.selectCurVoteList(zoneId, title, startDate, endDate, page, size));
    }

    
    /** 
    * @Description: 查询用户表决投票详情
    * @Param:  
    * @return:  
    * @Author: tenaciousVine
    * @Date: 2020/3/18 
    */
    @PostMapping("/selectUserVoteDetail")
    public R selectUserVoteDetail(
            @RequestParam(value = "bflyUserId") Long bflyUserId,
            @RequestParam(value = "bflyVoteId") Long bflyVoteId
    ) throws Exception {
        return R.ok().put("bflyInfo",bflyVoteService. searchUserVoteDetail(bflyUserId,bflyVoteId));
    }

    /**
     * 获取统计表决的结果
     * @param id
     * @return
     */
    @GetMapping("/statVoteResult/{id}")
    public R statVoteResult(@PathVariable Long id) {
        bflyVoteService.voteResult(id);
        return R.ok();
//        BflyVote bflyVote = bflyVoteService.selectStateByBflyVoteId(id);
//        return R.ok().put("result", bflyVote);
    }

    /**
     * 导出参会数据excel
     * @param id
     */
    @GetMapping("/exportMeeting/{id}")
    public void exportMeeting(HttpServletResponse response, @PathVariable Long id) throws Exception {
        List<Map<String, Object>> bflyUserList = bflyUserVoteService.meetingUserInfoByVoteId(id);
        double housingArea = 0.00;
        List<List<String>> lists = new ArrayList<>();
        lists.add(Arrays.asList(new String[]{"小区名称", "苑", "楼号", "单元", "房号", "姓名", "面积", "确认参会时间"}));
        for (int i = 0; i < bflyUserList.size(); i++) {
            List<String> list = new ArrayList<>();
            list.add(String.valueOf(bflyUserList.get(i).get("name")));
            list.add(String.valueOf(bflyUserList.get(i).get("court")));
            list.add(String.valueOf(bflyUserList.get(i).get("building")));
            list.add(String.valueOf(bflyUserList.get(i).get("unit_name")));
            list.add(String.valueOf(bflyUserList.get(i).get("floor_name")));
            list.add(String.valueOf(bflyUserList.get(i).get("user_name")));
            list.add(String.valueOf(bflyUserList.get(i).get("housing_area")));
            list.add(String.valueOf(bflyUserList.get(i).get("meeting_submit_time")));
            lists.add(list);
            housingArea += ((BigDecimal) bflyUserList.get(i).get("housing_area")).doubleValue();
        }
        lists.add(Arrays.asList(new String[]{"合计"}));
        lists.add(Arrays.asList(new String[]{"确认参会总户数", String.valueOf(bflyUserList.size())}));
        lists.add(Arrays.asList(new String[]{"确认参会总面积", String.valueOf(housingArea)}));
        excelService.write(response, "sheet1", "meeting-data", lists);
    }

    /**
     * 导出表决数据excel
     * @param id
     */
    @GetMapping("/exportVote/{id}")
    public void exportVote(HttpServletResponse response, @PathVariable Long id) throws Exception {
        BflyVote bflyVote = bflyVoteService.info(id);

        List<Map<String, Object>> bflyUserList = bflyUserVoteService.voteUserInfoByVoteId(id);
        List<List<String>> lists = new ArrayList<>();
        String[] arr = {"小区名称", "表决主题", "苑", "楼号", "单元", "房号", "姓名", "面积", "确认参会时间", "投票时间"};
        List<String> header = new ArrayList<String>(Arrays.asList(arr));
        List<String> subVoteTitleList = new ArrayList<>();
        for (int i = 0; i < bflyVote.getBflySubVotes().size(); i++) {
            subVoteTitleList.add(bflyVote.getBflySubVotes().get(i).getTitle());
            header.add("子表决" + (i+1) + "标题");
            header.add("子表决" + (i+1) + "选项");
        }
        lists.add(header);
        for (int i = 0; i < bflyUserList.size(); i++) {
            List<String> list = new ArrayList<>();
            Long userId = (Long) bflyUserList.get(i).get("bfly_user_id");
            list.add(String.valueOf(bflyUserList.get(i).get("name")));
            list.add(bflyVote.getTitle());
            list.add(String.valueOf(bflyUserList.get(i).get("court")));
            list.add(String.valueOf(bflyUserList.get(i).get("building")));
            list.add(String.valueOf(bflyUserList.get(i).get("unit_name")));
            list.add(String.valueOf(bflyUserList.get(i).get("floor_name")));
            list.add(String.valueOf(bflyUserList.get(i).get("user_name")));
            list.add(String.valueOf(bflyUserList.get(i).get("housing_area")));
            list.add(String.valueOf(bflyUserList.get(i).get("meeting_submit_time")));
            list.add(String.valueOf(bflyUserList.get(i).get("vote_submit_time")));
            for (int i1 = 0; i1 < subVoteTitleList.size(); i1++) {
                Map<String, Object> params = new HashMap<>();
                params.put("bflySubVoteTitle", subVoteTitleList.get(i1));
                params.put("bflyVoteId", id);
                params.put("bflyUserId", userId);
                Map<String, Object> option = bflyUserSubVoteService.selectUserSubVoteResultByTitle(params);
                if (option != null) {
                    list.add(String.valueOf(subVoteTitleList.get(i1)));
                    list.add(String.valueOf(option.get("result_option")));
                }
            }
            lists.add(list);
        }
        excelService.write(response, "sheet1", "vote-data", lists);

    }

}
