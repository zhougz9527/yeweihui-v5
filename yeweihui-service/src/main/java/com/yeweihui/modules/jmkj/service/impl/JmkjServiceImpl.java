package com.yeweihui.modules.jmkj.service.impl;

import com.yeweihui.modules.jmkj.Entity.JmkjLoginStatusBean;
import com.yeweihui.modules.jmkj.Entity.PerformanceOfDutiesBean;
import com.yeweihui.modules.jmkj.Entity.PerformanceRateBean;
import com.yeweihui.modules.jmkj.dao.JmkjSql;
import com.yeweihui.modules.jmkj.service.JmkjService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class JmkjServiceImpl implements JmkjService{

    @Autowired
    JmkjSql jmkjSql;


    @Transactional
    @Override
    public boolean updateTime(Long uid,Long time) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");

        List<JmkjLoginStatusBean> JmkjLoginStatus= jmkjSql.selectJmkjLoginStatus(uid);

        JmkjLoginStatusBean mJmkjLoginStatusBean;

        if (JmkjLoginStatus.size()>0 && simpleDateFormat.format(new Date().getTime()).equals(simpleDateFormat.format(JmkjLoginStatus.get(0).getMonthTime()))){

            mJmkjLoginStatusBean = JmkjLoginStatus.get(0);

            jmkjSql.updateJmkjLoginStatus(mJmkjLoginStatusBean.getOnLineTime()+time,uid,mJmkjLoginStatusBean.getMonthTime());

        }else {

            mJmkjLoginStatusBean = new JmkjLoginStatusBean();

            mJmkjLoginStatusBean.setUid(uid);
            mJmkjLoginStatusBean.setOnLineTime(time);
            mJmkjLoginStatusBean.setLoginTimes(0l);
            mJmkjLoginStatusBean.setCreateTime(new Date().getTime());
            mJmkjLoginStatusBean.setUpdateTime(new Date().getTime());
            mJmkjLoginStatusBean.setMonthTime(new Date().getTime());
            jmkjSql.insertJmkjLoginStatus(mJmkjLoginStatusBean);

        }

        return true;
    }

    @Transactional
    @Override
    public boolean updateNum(Long uid) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");

        List<JmkjLoginStatusBean> JmkjLoginStatus= jmkjSql.selectJmkjLoginStatus(uid);

        JmkjLoginStatusBean mJmkjLoginStatusBean;

        if (JmkjLoginStatus.size()>0 && simpleDateFormat.format(new Date().getTime()).equals(simpleDateFormat.format(JmkjLoginStatus.get(0).getMonthTime()))){

            mJmkjLoginStatusBean = JmkjLoginStatus.get(0);

            jmkjSql.updateJmkjLoginStatusNum(mJmkjLoginStatusBean.getLoginTimes()+1,uid,mJmkjLoginStatusBean.getMonthTime());

        }else {

            mJmkjLoginStatusBean = new JmkjLoginStatusBean();

            mJmkjLoginStatusBean.setUid(uid);
            mJmkjLoginStatusBean.setOnLineTime(0l);
            mJmkjLoginStatusBean.setLoginTimes(1l);
            mJmkjLoginStatusBean.setCreateTime(new Date().getTime());
            mJmkjLoginStatusBean.setUpdateTime(new Date().getTime());
            mJmkjLoginStatusBean.setMonthTime(new Date().getTime());
            jmkjSql.insertJmkjLoginStatus(mJmkjLoginStatusBean);

        }

        return true;

    }


    @Override
    public Object getMyData(Long uid) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date); // 设置为当前时间
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1); // 设置为上一个月
        date = calendar.getTime();
        String accDate = simpleDateFormat.format(date);

        List<JmkjLoginStatusBean> JmkjLoginStatus= jmkjSql.selectJmkjLoginStatus(uid);

        for (JmkjLoginStatusBean mJmkjLoginStatusBean:JmkjLoginStatus){

            if (accDate.equals(simpleDateFormat.format(new Date(mJmkjLoginStatusBean.getMonthTime())))){

                Map<String,Object> data = new HashMap<>();

                data.put("onLineTime",mJmkjLoginStatusBean.getOnLineTime());
                data.put("loginTimes",mJmkjLoginStatusBean.getLoginTimes());

                return data;
            }
        }

        return null;
    }


    @Override
    public Object operationTask(int type,long uid,Long zoneId,Long timeStart,Long timeEnd) {


        if(type==1){

            return getPerformanceOfDutiesList(uid,zoneId,timeStart,timeEnd);

        }else if (type==2){

            return getPerformanceRateBeans(uid,zoneId,timeStart,timeEnd);

        }else {

            return null;
        }


    }

    /**
     * 操作性任务-履职量
     * */
    public Object getPerformanceOfDutiesList(long uid,Long zoneId,Long timeStart,Long timeEnd){

        if (timeStart==null)timeStart=0l;
        if (timeEnd==null)timeEnd=new Date().getTime();

        List<PerformanceOfDutiesBean> PerformanceOfDutiesBeans = jmkjSql.getPerformanceOfDutiesList(zoneId,new Date(timeStart),new Date(timeEnd));

        Map<String,Object> map = new HashMap<>();


        for (int i=0;i<PerformanceOfDutiesBeans.size();i++){

            if (uid==PerformanceOfDutiesBeans.get(i).getUid().longValue()){

                map.put("realname",PerformanceOfDutiesBeans.get(i).getRealname());
                map.put("avatarUrl",PerformanceOfDutiesBeans.get(i).getAvatarUrl());
                map.put("num",PerformanceOfDutiesBeans.get(i).getNum());
                map.put("ranking",i+1);
                break;
            }
        }

        map.put("listData",PerformanceOfDutiesBeans);

        return map;
    }

    /**
     * 操作性任务-履职率
     * */
    public Object getPerformanceRateBeans(long uid,Long zoneId,Long timeStart,Long timeEnd){

        if (timeStart==null)timeStart=0l;
        if (timeEnd==null)timeEnd=new Date().getTime();

        List<PerformanceRateBean> PerformanceRateBeans = jmkjSql.getPerformanceRateBeans(zoneId,new Date(timeStart),new Date(timeEnd));

        Map<String,Object> map = new HashMap<>();


        for (int i=0;i<PerformanceRateBeans.size();i++){

            if (uid==PerformanceRateBeans.get(i).getUid().longValue()){

                map.put("realname",PerformanceRateBeans.get(i).getRealname());
                map.put("avatarUrl",PerformanceRateBeans.get(i).getAvatarUrl());
                map.put("proportion",PerformanceRateBeans.get(i).getProportion());
                map.put("ranking",i+1);
                break;
            }
        }

        map.put("listData",PerformanceRateBeans);

        return map;
    }
}
