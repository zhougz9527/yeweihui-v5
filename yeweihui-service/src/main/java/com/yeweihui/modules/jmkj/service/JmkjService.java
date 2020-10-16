package com.yeweihui.modules.jmkj.service;

import com.yeweihui.modules.jmkj.Entity.IndustryDirectorParameterBean;

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

    Object IndustryDirector(IndustryDirectorParameterBean IndustryDirectorParameterBean);

    int TimeVote(long vid);

    int NoTimeVote(long vid);

    Object administratorList(Long uId);
}
