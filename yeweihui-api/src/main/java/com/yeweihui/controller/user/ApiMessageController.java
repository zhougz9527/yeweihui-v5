package com.yeweihui.controller.user;

import com.yeweihui.common.utils.BeanUtil;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.R;
import com.yeweihui.modules.user.entity.MessageEntity;
import com.yeweihui.modules.user.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/message")
@Api(tags="推送消息")
public class ApiMessageController {

    @Autowired
    MessageService messageService;

    @PostMapping("/list")
    @ApiOperation("分页列表")
    public R list(@RequestBody MessageEntity messageEntity) {
        PageUtils page = messageService.queryPage(BeanUtil.bean2map(messageEntity));
        return R.ok().put("pages", page);
    }

    @PostMapping("/simpleList")
    @ApiOperation("普通列表")
    public R simpleList(@RequestBody MessageEntity messageEntity){
        List<MessageEntity> messageEntityList = messageService.simpleList(messageEntity);

        return R.ok().put("messageEntityList", messageEntityList);
    }


    @PostMapping("/info/{id}")
    @ApiOperation("信息")
    public R info(@PathVariable("id") Long id){
        MessageEntity messageEntity = messageService.selectById(id);

        return R.ok().put("message", messageEntity);
    }

    @PostMapping("/delete")
    @ApiOperation("删除")
    public R delete(@RequestBody Long[] ids){
        messageService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }


}
