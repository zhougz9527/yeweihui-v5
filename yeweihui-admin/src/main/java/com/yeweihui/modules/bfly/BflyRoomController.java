package com.yeweihui.modules.bfly;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonArray;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.R;
import com.yeweihui.modules.bfly.entity.BflyRoom;
import com.yeweihui.modules.bfly.service.BflyRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.naming.spi.ResolveResult;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("bflyRoom")
public class BflyRoomController {

    @Autowired
    private BflyRoomService bflyRoomService;

    @RequestMapping("/list")
    public R list(@RequestBody Map<String, Object> params) {
        PageUtils page = bflyRoomService.queryPage(params);
        return R.ok().put("page", page);
    }

    @PostMapping("/update")
    public R update(@RequestBody BflyRoom bflyRoom) {
        return R.ok().put("bflyRoom", bflyRoomService.updateRoom(bflyRoom));
    }

    @PostMapping("/delete/{id}")
    public R delete(@PathVariable Long id) {
        bflyRoomService.delete(id);
        return R.ok();
    }

    @PostMapping("/info/{id}")
    public R info(@PathVariable Long id) {
        return R.ok().put("result", bflyRoomService.info(id));
    }

    /**
     * 搜索房屋
     * @return
     */
    @PostMapping("/queryRoom")
    public R queryRoom(@RequestParam(value = "userName", required = false) String userName,
            @RequestParam(value = "phoneNum", required = false) String phoneNum,
            @RequestParam(value = "zoneId", required = false) Long zoneId,
            @RequestParam(value = "userStatus", required = false) Integer userStatus,
            @RequestParam(value = "page", required = false) String page,
            @RequestParam(value = "size", required = false) String size) {
        return R.ok().put("page", bflyRoomService.queryRoom(userName, phoneNum, zoneId, userStatus, page, size));
    }

    /**
     *  查询小区总户数和房屋总面积
     * @param zoneId
     * @return
     */
    @PostMapping("/queryAllRoomAndArea")
    public R ueryAllRoomAndArea(@RequestParam(value = "zoneId", required = false) Long zoneId) {
        HashMap<String, Object> param = new HashMap<>();
        if (null != zoneId) {
            param.put("zoneId", zoneId);
        }
        //总户数
        List<Map<String, Long>> zoneRoomNumList = bflyRoomService.stateRoomNumByZoneId(param);
        Integer roomNum = zoneRoomNumList.size();
        // 总面积
        BigDecimal allArea = bflyRoomService.queryAllArea(zoneId);
        //封装数据
        HashMap<String, Object> result = new HashMap<>();
        result.put("roomNum", roomNum);
        result.put("allArea", allArea);
        return R.ok().put("result", result);
    }
}
