package com.yeweihui.modules.jmkj.service;

public interface JmkjService {

    boolean updateTime(Long uid,Long time);

    boolean updateNum(Long uid);

    Object getMyData(Long uid);

    Object getPerformanceOfDutiesList(long uid,Long zoneId,Long timeStart,Long timeEnd);

    Object getPerformanceRateBeans(long uid,Long zoneId,Long timeStart,Long timeEnd);

    Object OverdueQuantity(long uid,Long zoneId,Long timeStart,Long timeEnd);

    Object OverdueRate(long uid,Long zoneId,Long timeStart,Long timeEnd);

    Object operationNum(long uid,Long zoneId,Long timeStart,Long timeEnd);

    Object BrowseComplete(long uid,Long zoneId,Long timeStart,Long timeEnd);

    Object NewBrowse(long uid,Long zoneId,Long timeStart,Long timeEnd);

    Object OnlineDuration(long uid,Long zoneId,Long timeStart,Long timeEnd);

    Object OnlineNum(long uid,Long zoneId,Long timeStart,Long timeEnd);
}
