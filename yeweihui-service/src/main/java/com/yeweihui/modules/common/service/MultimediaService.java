package com.yeweihui.modules.common.service;

import com.baomidou.mybatisplus.service.IService;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.modules.common.entity.MultimediaEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件
 *
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-15 15:09:44
 */
public interface MultimediaService extends IService<MultimediaEntity> {

    PageUtils queryPage(Map<String, Object> params);

    HashMap<String, Object> insertBatchByPublicity(MultipartFile[] files) throws Exception;
}

