package com.yeweihui.modules.bfly;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.yeweihui.common.utils.R;
import com.yeweihui.modules.bfly.entity.BflyRoomTemp;
import com.yeweihui.modules.bfly.service.BflyRoomTempService;
import com.yeweihui.modules.sys.controller.AbstractController;
import com.yeweihui.modules.user.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("bflyRoomTemp")
public class BflyRoomTempController extends AbstractController {

    @Autowired
    private BflyRoomTempService bflyRoomTempService;

    @RequestMapping("/list")
    public R list(Long zoneId) {
        UserEntity user = getUser();
        return R.ok().put("list", bflyRoomTempService.selectList(new EntityWrapper<BflyRoomTemp>()
                .eq("operation_uid", user.getId()).eq("zone_id", zoneId)));
    }

    /**
     * 确定导入（根据tempID）
     * @param tempIds
     * @return
     */
    @PostMapping("/saveAll")
    public R saveAll(Long zoneId ,Long[] tempIds) {
        List<Long> tempIdsList = new LinkedList<Long>();
        for (int i = 0; i < tempIds.length; i++) {
            tempIdsList.add(tempIds[i]);
        }
        UserEntity user = getUser();
        bflyRoomTempService.saveAll(zoneId ,user.getId(), tempIdsList);
        return R.ok();
    }


    @PostMapping("/giveUp")
    public R delete(Long zoneId) {
        UserEntity user = getUser();
        bflyRoomTempService.delete(new EntityWrapper<BflyRoomTemp>()
                .eq("operation_uid", user.getId()).eq("zone_id", zoneId));
        return R.ok();
    }

    /**
     * 导入excel
     * @param file
     * @param zoneId
     * @return
     */
    @PostMapping("/importExcel")
    public R importExcel(@RequestParam("file") MultipartFile file, Long zoneId) {
        UserEntity user = getUser();
        if (!file.isEmpty()) {
            try {
                List<BflyRoomTemp> bflyRoomTemps = bflyRoomTempService.importExcel(user.getId(), zoneId, file.getInputStream());
                return R.ok().put("result", bflyRoomTemps);
            } catch (Exception e) {
                e.printStackTrace();
                return R.error(e.getMessage());
            }
        } else {
            return R.error("上传文件不能为空");
        }
    }
}
