package com.yeweihui.modules.operation.service.impl;

import com.yeweihui.modules.common.entity.MultimediaEntity;
import com.yeweihui.modules.common.service.MultimediaService;
import com.yeweihui.modules.enums.BizTypeEnum;
import com.yeweihui.modules.enums.FileTypeEnum;
import com.yeweihui.modules.enums.request.RequestFileTypeEnum;
import com.yeweihui.modules.enums.request.RequestOpeTypeEnum;
import com.yeweihui.modules.enums.request.RequestSealEnum;
import com.yeweihui.modules.enums.request.RequestStatusEnum;
import com.yeweihui.modules.operation.entity.RequestEntity;
import com.yeweihui.modules.operation.entity.RequestMemberEntity;
import com.yeweihui.modules.user.entity.UserEntity;
import com.yeweihui.modules.vo.admin.file.FileEntity;
import com.yeweihui.modules.vo.query.MeetingLogQueryParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.Query;

import com.yeweihui.modules.operation.dao.MeetingLogDao;
import com.yeweihui.modules.operation.entity.MeetingLogEntity;
import com.yeweihui.modules.operation.service.MeetingLogService;


@Service("meetingLogService")
public class MeetingLogServiceImpl extends ServiceImpl<MeetingLogDao, MeetingLogEntity> implements MeetingLogService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    MultimediaService multimediaService;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<MeetingLogEntity> page = this.selectPage(
                new Query<MeetingLogEntity>(params).getPage(),
                new EntityWrapper<MeetingLogEntity>()
        );

        List<MeetingLogEntity> meetingLogEntityList = page.getRecords();
        for (MeetingLogEntity meetingLogEntity : meetingLogEntityList) {
            this.packageExtra(meetingLogEntity);
        }
        page.setRecords(meetingLogEntityList);

        return new PageUtils(page);
    }

    @Override
    public List<MeetingLogEntity> simpleList(MeetingLogQueryParam meetingLogQueryParam) {
        List<MeetingLogEntity> meetingLogEntityList = this.baseMapper.simpleList(meetingLogQueryParam);
        for (MeetingLogEntity meetingLogEntity : meetingLogEntityList) {
            this.packageExtra(meetingLogEntity);
        }
        return meetingLogEntityList;
    }

    @Override
    public void save(MeetingLogEntity meetingLog) {
        this.insert(meetingLog);
        this.saveOrUpdateExtra(meetingLog);
    }

    @Override
    public MeetingLogEntity info(Long id) {
        MeetingLogEntity meetingLog = this.selectById(id);
        this.packageExtra(meetingLog);
        return meetingLog;
    }


    private void saveOrUpdateExtra(MeetingLogEntity meetingLog){
        //删除关联的文件
        multimediaService.delete(new EntityWrapper<MultimediaEntity>()
                .eq("related_id", meetingLog.getId())
                .eq("related_type", BizTypeEnum.MEETING_LOG.getCode())
                .eq("file_type", FileTypeEnum.PICTURE.getCode())
        );
        //并插入关联的文件
        List<FileEntity> fileEntityList =  meetingLog.getFileList();
        if (null != fileEntityList && fileEntityList.size()>0){
            for (FileEntity fileEntity : fileEntityList) {
//                urlList.add(fileEntity.getResponse().getUrl());
                //保存图片
                MultimediaEntity multimediaEntity = new MultimediaEntity();
                multimediaEntity.setName(fileEntity.getName());
                multimediaEntity.setRelatedType(BizTypeEnum.MEETING_LOG.getCode());
                multimediaEntity.setRelatedId(meetingLog.getId());
                multimediaEntity.setFileType(FileTypeEnum.PICTURE.getCode());
                if(null != fileEntity.getResponse()){
                    multimediaEntity.setUrl(fileEntity.getResponse().getUrl());
                }else{
                    multimediaEntity.setUrl(fileEntity.getUrl());
                }
                multimediaEntity.setName(multimediaEntity.getName());
                multimediaService.insert(multimediaEntity);
            }
        }
    }

    /**
     * 封装
     * @param meetingLog
     */
    private void packageExtra(MeetingLogEntity meetingLog){
        List<FileEntity> fileEntityList = new ArrayList<>();
        List<MultimediaEntity> multimediaEntityList = multimediaService.selectList(
                new EntityWrapper<MultimediaEntity>()
                        .eq("related_id", meetingLog.getId())
                        .eq("related_type", BizTypeEnum.MEETING_LOG.getCode())
                        .eq("file_type", FileTypeEnum.PICTURE.getCode())
        );
        for (MultimediaEntity multimediaEntity : multimediaEntityList) {
            FileEntity fileEntity = new FileEntity();
            fileEntity.setName(multimediaEntity.getName());
            fileEntity.setUrl(multimediaEntity.getUrl());
            fileEntityList.add(fileEntity);
        }
        meetingLog.setFileList(fileEntityList);
    }

}
