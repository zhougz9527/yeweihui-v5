package com.yeweihui.modules.common.service.impl;

import com.yeweihui.modules.enums.FileTypeEnum;
import com.yeweihui.modules.oss.service.SysOssService;
import org.springframework.stereotype.Service;

import java.util.*;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.Query;

import com.yeweihui.modules.common.dao.MultimediaDao;
import com.yeweihui.modules.common.entity.MultimediaEntity;
import com.yeweihui.modules.common.service.MultimediaService;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;


@Service("fileService")
public class MultimediaServiceImpl extends ServiceImpl<MultimediaDao, MultimediaEntity> implements MultimediaService {

    @Resource
    SysOssService sysOssService;
    @Resource
    MultimediaDao multimediaDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<MultimediaEntity> page = this.selectPage(
                new Query<MultimediaEntity>(params).getPage(),
                new EntityWrapper<MultimediaEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public HashMap<String, Object> insertBatchByPublicity(MultipartFile[] files) throws Exception {
        List<Long> ids = new ArrayList<>();
        List<String> urls = new ArrayList<>();
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("ids",ids);
        resultMap.put("urls",urls);
        for (MultipartFile file : files) {
            String name = file.getOriginalFilename();
            String url = sysOssService.upload(file);
            urls.add(url);
            MultimediaEntity multimediaEntity = new MultimediaEntity();
            multimediaEntity.setCreateTime(new Date(System.currentTimeMillis()));
            multimediaEntity.setUpdateTime(new Date(System.currentTimeMillis()));
            multimediaEntity.setDescription(null);
            multimediaEntity.setRelatedId(0L);
            multimediaEntity.setSize(0);
            multimediaEntity.setRelatedType(9);
            multimediaEntity.setFileType(FileTypeEnum.PUBLICITY.getName());
            multimediaEntity.setName(name);
            multimediaEntity.setUrl(url);
            multimediaDao.insertAndGetId(multimediaEntity);
            System.out.println("multimedia id --> " + multimediaEntity.getId());
            ids.add(multimediaEntity.getId());
        }
        return resultMap;
    }

}
