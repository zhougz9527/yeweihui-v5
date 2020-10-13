package com.yeweihui.modules.jmkj.service;

public interface JmkjService {

    boolean updateTime(Long uid,Long time);

    boolean updateNum(Long uid);

    Object getMyData(Long uid);

    Object operationTask(int type,long uid,Long zoneId,Long timeStart,Long timeEnd);

}
