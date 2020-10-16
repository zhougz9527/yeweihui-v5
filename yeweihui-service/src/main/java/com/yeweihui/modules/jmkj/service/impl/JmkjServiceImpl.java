package com.yeweihui.modules.jmkj.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.yeweihui.modules.jmkj.Entity.*;
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
    public Object getPerformanceOfDutiesList(long uid,Long zoneId,Long timeStart,Long timeEnd) {


        if (timeStart==null)timeStart=0l;
        if (timeEnd==null)timeEnd=new Date().getTime();

        AdministrationFrom mAdministrationFrom = jmkjSql.getAdministrationFrom(uid);

        EntityWrapper<PerformanceOfDutiesBean> IndustryDirectorWrapper = new EntityWrapper<PerformanceOfDutiesBean>();

        if (mAdministrationFrom!=null)
        if (zoneId==1){

            IndustryDirectorWrapper.eq("zones.id",mAdministrationFrom.getZonesId());

        }else if (zoneId==2){

            IndustryDirectorWrapper.eq("zones.community_id",mAdministrationFrom.getCommunityId());

        }else if (zoneId==3){

            IndustryDirectorWrapper.eq("zones.subdistrict_id",mAdministrationFrom.getSubdistrictId());

        }else if (zoneId==4){

            IndustryDirectorWrapper.eq("zones.district_id",mAdministrationFrom.getDistrictId());

        }else if (zoneId==5){

            IndustryDirectorWrapper.eq("zones.city_id",mAdministrationFrom.getCityId());

        }else if (zoneId==6){

            IndustryDirectorWrapper.eq("zones.province_id",mAdministrationFrom.getProvinceId());
        }

        List<PerformanceOfDutiesBean> PerformanceOfDutiesBeans = jmkjSql.getPerformanceOfDutiesList(new Date(timeStart),new Date(timeEnd),IndustryDirectorWrapper);

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

    @Override
    public Object getPerformanceRateBeans(long uid,Long zoneId,Long timeStart,Long timeEnd){

        if (timeStart==null)timeStart=0l;
        if (timeEnd==null)timeEnd=new Date().getTime();

        AdministrationFrom mAdministrationFrom = jmkjSql.getAdministrationFrom(uid);

        EntityWrapper<PerformanceRateBean> IndustryDirectorWrapper = new EntityWrapper<PerformanceRateBean>();

        if (mAdministrationFrom!=null)
        if (zoneId==1){

            IndustryDirectorWrapper.eq("zones.id",mAdministrationFrom.getZonesId());

        }else if (zoneId==2){

            IndustryDirectorWrapper.eq("zones.community_id",mAdministrationFrom.getCommunityId());

        }else if (zoneId==3){

            IndustryDirectorWrapper.eq("zones.subdistrict_id",mAdministrationFrom.getSubdistrictId());

        }else if (zoneId==4){

            IndustryDirectorWrapper.eq("zones.district_id",mAdministrationFrom.getDistrictId());

        }else if (zoneId==5){

            IndustryDirectorWrapper.eq("zones.city_id",mAdministrationFrom.getCityId());

        }else if (zoneId==6){

            IndustryDirectorWrapper.eq("zones.province_id",mAdministrationFrom.getProvinceId());
        }

        List<PerformanceRateBean> PerformanceRateBeans = jmkjSql.getPerformanceRateBeans(new Date(timeStart),new Date(timeEnd),IndustryDirectorWrapper);

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

    @Override
    public Object OverdueQuantity(long uid, Long zoneId, Long timeStart, Long timeEnd) {

        if (timeStart==null)timeStart=0l;
        if (timeEnd==null)timeEnd=new Date().getTime();

        AdministrationFrom mAdministrationFrom = jmkjSql.getAdministrationFrom(uid);

        EntityWrapper<PerformanceOfDutiesBean> IndustryDirectorWrapper = new EntityWrapper<PerformanceOfDutiesBean>();

        if (mAdministrationFrom!=null)
        if (zoneId==1){

            IndustryDirectorWrapper.eq("zones.id",mAdministrationFrom.getZonesId());

        }else if (zoneId==2){

            IndustryDirectorWrapper.eq("zones.community_id",mAdministrationFrom.getCommunityId());

        }else if (zoneId==3){

            IndustryDirectorWrapper.eq("zones.subdistrict_id",mAdministrationFrom.getSubdistrictId());

        }else if (zoneId==4){

            IndustryDirectorWrapper.eq("zones.district_id",mAdministrationFrom.getDistrictId());

        }else if (zoneId==5){

            IndustryDirectorWrapper.eq("zones.city_id",mAdministrationFrom.getCityId());

        }else if (zoneId==6){

            IndustryDirectorWrapper.eq("zones.province_id",mAdministrationFrom.getProvinceId());
        }

        List<PerformanceOfDutiesBean> PerformanceOfDutiesBeans = jmkjSql.OverdueQuantity(new Date(timeStart),new Date(timeEnd),IndustryDirectorWrapper);

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

    @Override
    public Object OverdueRate(long uid, Long zoneId, Long timeStart, Long timeEnd) {

        if (timeStart==null)timeStart=0l;
        if (timeEnd==null)timeEnd=new Date().getTime();

        AdministrationFrom mAdministrationFrom = jmkjSql.getAdministrationFrom(uid);

        EntityWrapper<PerformanceRateBean> IndustryDirectorWrapper = new EntityWrapper<PerformanceRateBean>();

        if (mAdministrationFrom!=null)
        if (zoneId==1){

            IndustryDirectorWrapper.eq("zones.id",mAdministrationFrom.getZonesId());

        }else if (zoneId==2){

            IndustryDirectorWrapper.eq("zones.community_id",mAdministrationFrom.getCommunityId());

        }else if (zoneId==3){

            IndustryDirectorWrapper.eq("zones.subdistrict_id",mAdministrationFrom.getSubdistrictId());

        }else if (zoneId==4){

            IndustryDirectorWrapper.eq("zones.district_id",mAdministrationFrom.getDistrictId());

        }else if (zoneId==5){

            IndustryDirectorWrapper.eq("zones.city_id",mAdministrationFrom.getCityId());

        }else if (zoneId==6){

            IndustryDirectorWrapper.eq("zones.province_id",mAdministrationFrom.getProvinceId());
        }

        List<PerformanceRateBean> PerformanceRateBeans = jmkjSql.OverdueRate(new Date(timeStart),new Date(timeEnd),IndustryDirectorWrapper);

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

    @Override
    public Object operationNum(long uid, Long zoneId, Long timeStart, Long timeEnd) {

        if (timeStart==null)timeStart=0l;
        if (timeEnd==null)timeEnd=new Date().getTime();

        AdministrationFrom mAdministrationFrom = jmkjSql.getAdministrationFrom(uid);

        EntityWrapper<PerformanceOfDutiesBean> IndustryDirectorWrapper = new EntityWrapper<PerformanceOfDutiesBean>();

        if (mAdministrationFrom!=null)
        if (zoneId==1){

            IndustryDirectorWrapper.eq("zones.id",mAdministrationFrom.getZonesId());

        }else if (zoneId==2){

            IndustryDirectorWrapper.eq("zones.community_id",mAdministrationFrom.getCommunityId());

        }else if (zoneId==3){

            IndustryDirectorWrapper.eq("zones.subdistrict_id",mAdministrationFrom.getSubdistrictId());

        }else if (zoneId==4){

            IndustryDirectorWrapper.eq("zones.district_id",mAdministrationFrom.getDistrictId());

        }else if (zoneId==5){

            IndustryDirectorWrapper.eq("zones.city_id",mAdministrationFrom.getCityId());

        }else if (zoneId==6){

            IndustryDirectorWrapper.eq("zones.province_id",mAdministrationFrom.getProvinceId());
        }

        List<PerformanceOfDutiesBean> PerformanceOfDutiesBeans = jmkjSql.operationNum(new Date(timeStart),new Date(timeEnd),IndustryDirectorWrapper);

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

    @Override
    public Object BrowseComplete(long uid, Long zoneId, Long timeStart, Long timeEnd) {

        if (timeStart==null)timeStart=0l;
        if (timeEnd==null)timeEnd=new Date().getTime();

        AdministrationFrom mAdministrationFrom = jmkjSql.getAdministrationFrom(uid);
        EntityWrapper<PerformanceOfDutiesBean> IndustryDirectorWrapper = new EntityWrapper<PerformanceOfDutiesBean>();

        if (mAdministrationFrom!=null)
        if (zoneId==1){

            IndustryDirectorWrapper.eq("zones.id",mAdministrationFrom.getZonesId());

        }else if (zoneId==2){

            IndustryDirectorWrapper.eq("zones.community_id",mAdministrationFrom.getCommunityId());

        }else if (zoneId==3){

            IndustryDirectorWrapper.eq("zones.subdistrict_id",mAdministrationFrom.getSubdistrictId());

        }else if (zoneId==4){

            IndustryDirectorWrapper.eq("zones.district_id",mAdministrationFrom.getDistrictId());

        }else if (zoneId==5){

            IndustryDirectorWrapper.eq("zones.city_id",mAdministrationFrom.getCityId());

        }else if (zoneId==6){

            IndustryDirectorWrapper.eq("zones.province_id",mAdministrationFrom.getProvinceId());
        }

        List<PerformanceOfDutiesBean> PerformanceOfDutiesBeans = jmkjSql.BrowseComplete(new Date(timeStart),new Date(timeEnd),IndustryDirectorWrapper);

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

    @Override
    public Object NewBrowse(long uid, Long zoneId, Long timeStart, Long timeEnd) {

        if (timeStart==null)timeStart=0l;
        if (timeEnd==null)timeEnd=new Date().getTime();

        AdministrationFrom mAdministrationFrom = jmkjSql.getAdministrationFrom(uid);
        EntityWrapper<PerformanceOfDutiesBean> IndustryDirectorWrapper = new EntityWrapper<PerformanceOfDutiesBean>();

        if (mAdministrationFrom!=null)
        if (zoneId==1){

            IndustryDirectorWrapper.eq("zones.id",mAdministrationFrom.getZonesId());

        }else if (zoneId==2){

            IndustryDirectorWrapper.eq("zones.community_id",mAdministrationFrom.getCommunityId());

        }else if (zoneId==3){

            IndustryDirectorWrapper.eq("zones.subdistrict_id",mAdministrationFrom.getSubdistrictId());

        }else if (zoneId==4){

            IndustryDirectorWrapper.eq("zones.district_id",mAdministrationFrom.getDistrictId());

        }else if (zoneId==5){

            IndustryDirectorWrapper.eq("zones.city_id",mAdministrationFrom.getCityId());

        }else if (zoneId==6){

            IndustryDirectorWrapper.eq("zones.province_id",mAdministrationFrom.getProvinceId());
        }

        List<PerformanceOfDutiesBean> PerformanceOfDutiesBeans = jmkjSql.NewBrowse(new Date(timeStart),new Date(timeEnd),IndustryDirectorWrapper);

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

    @Override
    public Object OnlineDuration(long uid, Long zoneId, Long timeStart, Long timeEnd) {

        if (timeStart==null)timeStart=0l;
        if (timeEnd==null)timeEnd=new Date().getTime();

        AdministrationFrom mAdministrationFrom = jmkjSql.getAdministrationFrom(uid);
        EntityWrapper<PerformanceOfDutiesBean> IndustryDirectorWrapper = new EntityWrapper<PerformanceOfDutiesBean>();

        if (mAdministrationFrom!=null)
        if (zoneId==1){

            IndustryDirectorWrapper.eq("zones.id",mAdministrationFrom.getZonesId());

        }else if (zoneId==2){

            IndustryDirectorWrapper.eq("zones.community_id",mAdministrationFrom.getCommunityId());

        }else if (zoneId==3){

            IndustryDirectorWrapper.eq("zones.subdistrict_id",mAdministrationFrom.getSubdistrictId());

        }else if (zoneId==4){

            IndustryDirectorWrapper.eq("zones.district_id",mAdministrationFrom.getDistrictId());

        }else if (zoneId==5){

            IndustryDirectorWrapper.eq("zones.city_id",mAdministrationFrom.getCityId());

        }else if (zoneId==6){

            IndustryDirectorWrapper.eq("zones.province_id",mAdministrationFrom.getProvinceId());
        }

        List<PerformanceOfDutiesBean> PerformanceOfDutiesBeans = jmkjSql.OnlineDuration(timeStart.longValue(),timeEnd.longValue(),IndustryDirectorWrapper);

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

    @Override
    public Object OnlineNum(long uid, Long zoneId, Long timeStart, Long timeEnd) {

        if (timeStart==null)timeStart=0l;
        if (timeEnd==null)timeEnd=new Date().getTime();

        AdministrationFrom mAdministrationFrom = jmkjSql.getAdministrationFrom(uid);
        EntityWrapper<PerformanceOfDutiesBean> IndustryDirectorWrapper = new EntityWrapper<PerformanceOfDutiesBean>();

        if (mAdministrationFrom!=null)
        if (zoneId==1){

            IndustryDirectorWrapper.eq("zones.id",mAdministrationFrom.getZonesId());

        }else if (zoneId==2){

            IndustryDirectorWrapper.eq("zones.community_id",mAdministrationFrom.getCommunityId());

        }else if (zoneId==3){

            IndustryDirectorWrapper.eq("zones.subdistrict_id",mAdministrationFrom.getSubdistrictId());

        }else if (zoneId==4){

            IndustryDirectorWrapper.eq("zones.district_id",mAdministrationFrom.getDistrictId());

        }else if (zoneId==5){

            IndustryDirectorWrapper.eq("zones.city_id",mAdministrationFrom.getCityId());

        }else if (zoneId==6){

            IndustryDirectorWrapper.eq("zones.province_id",mAdministrationFrom.getProvinceId());
        }

        List<PerformanceOfDutiesBean> PerformanceOfDutiesBeans = jmkjSql.OnlineNum(timeStart.longValue(),timeEnd.longValue(),IndustryDirectorWrapper);

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

    @Override
    public Object IndustryDirector(IndustryDirectorParameterBean IndustryDirectorParameterBean) {

        EntityWrapper<IndustryDirectorBean> IndustryDirectorWrapper = new EntityWrapper<IndustryDirectorBean>();
        IndustryDirectorWrapper.eq("sys_role.group","行业主管");

        if (IndustryDirectorParameterBean.getTelephone()!=null)IndustryDirectorWrapper.eq("`user`.mobile",IndustryDirectorParameterBean.getTelephone());
        if (IndustryDirectorParameterBean.getName()!=null)IndustryDirectorWrapper.eq("`user`.realname",IndustryDirectorParameterBean.getTelephone());
        if (IndustryDirectorParameterBean.getLevel()==null)IndustryDirectorWrapper.eq("`division_manager`.level",IndustryDirectorParameterBean.getLevel());

        Page<IndustryDirectorBean> mPage = new Page<IndustryDirectorBean>(IndustryDirectorParameterBean.getPages(), IndustryDirectorParameterBean.getSize());

        return mPage.setRecords(jmkjSql.IndustryDirector(mPage,IndustryDirectorWrapper));
    }

    @Override
    public int TimeVote(long vid) {
        return jmkjSql.TimeVote(vid);
    }

    @Override
    public int NoTimeVote(long vid) {
        return jmkjSql.NoTimeVote(vid);
    }

    @Override
    public Object administratorList(Long uId) {

        AdministrationFrom mAdministrationFrom = jmkjSql.getAdministrationFrom(uId);

        DivisionManagerBean mDivisionManagerBean = jmkjSql.getDivisionManagerBean(uId);

        List<DivisionManagerBean> mDivisionManagerBeanList = new ArrayList<>();

        if (mDivisionManagerBean==null){

            mDivisionManagerBeanList.addAll(jmkjSql.getDivisionManagerBeanList("province",mAdministrationFrom.getProvinceId())); //省
            mDivisionManagerBeanList.addAll(jmkjSql.getDivisionManagerBeanList("city",mAdministrationFrom.getCityId())); //市
            mDivisionManagerBeanList.addAll(jmkjSql.getDivisionManagerBeanList("district",mAdministrationFrom.getDistrictId())); //区
            mDivisionManagerBeanList.addAll(jmkjSql.getDivisionManagerBeanList("subdistrict",mAdministrationFrom.getSubdistrictId())); //街道
            mDivisionManagerBeanList.addAll(jmkjSql.getDivisionManagerBeanList("community",mAdministrationFrom.getCommunityId())); //社区

        }else{

            switch (mDivisionManagerBean.getLevel()){

                case "province":

                    getDivisionManagerBeanList(mDivisionManagerBeanList,2,mDivisionManagerBean.getDivisionId());
                    break;

                case "city":

                    mDivisionManagerBeanList.addAll(jmkjSql.getDivisionManagerBeanList("province",mAdministrationFrom.getProvinceId())); //省
                    getDivisionManagerBeanList(mDivisionManagerBeanList,3,mDivisionManagerBean.getDivisionId());
                    break;

                case "district":

                    mDivisionManagerBeanList.addAll(jmkjSql.getDivisionManagerBeanList("province",mAdministrationFrom.getProvinceId())); //省
                    mDivisionManagerBeanList.addAll(jmkjSql.getDivisionManagerBeanList("city",mAdministrationFrom.getCityId())); //市
                    getDivisionManagerBeanList(mDivisionManagerBeanList,4,mDivisionManagerBean.getDivisionId());
                    break;

                case "subdistrict":

                    mDivisionManagerBeanList.addAll(jmkjSql.getDivisionManagerBeanList("province",mAdministrationFrom.getProvinceId())); //省
                    mDivisionManagerBeanList.addAll(jmkjSql.getDivisionManagerBeanList("city",mAdministrationFrom.getCityId())); //市
                    mDivisionManagerBeanList.addAll(jmkjSql.getDivisionManagerBeanList("district",mAdministrationFrom.getDistrictId())); //区
                    getDivisionManagerBeanList(mDivisionManagerBeanList,5,mDivisionManagerBean.getDivisionId());
                    break;

                case "community":

                    mDivisionManagerBeanList.addAll(jmkjSql.getDivisionManagerBeanList("province",mAdministrationFrom.getProvinceId())); //省
                    mDivisionManagerBeanList.addAll(jmkjSql.getDivisionManagerBeanList("city",mAdministrationFrom.getCityId())); //市
                    mDivisionManagerBeanList.addAll(jmkjSql.getDivisionManagerBeanList("district",mAdministrationFrom.getDistrictId())); //区
                    mDivisionManagerBeanList.addAll(jmkjSql.getDivisionManagerBeanList("subdistrict",mAdministrationFrom.getSubdistrictId())); //街道
                    getDivisionManagerBeanList(mDivisionManagerBeanList,6,mDivisionManagerBean.getDivisionId());
                    break;
            }

        }

        return mDivisionManagerBeanList;
    }

    /**
     * 递归查询行政级别下的所有子级
     * */
    public void getDivisionManagerBeanList(List<DivisionManagerBean> DivisionManagerBeanList,int type,Long nmzlId){

        if (type==6)return;

        String level = null;
        switch (type){
            
            case 1:
                level="province";
                break;
            case 2:
                level="city";
                break;
            case 3:
                level="district";
                break;
            case 4:
                level="subdistrict";
                break;
            case 5:
                level="community";
                break;
        }
        
        
        List<ViewRegionBean> mViewRegionBeanList = jmkjSql.getViewRegionBeanList(type,nmzlId);

        for (ViewRegionBean mViewRegionBean:mViewRegionBeanList){

            DivisionManagerBeanList.addAll(jmkjSql.getDivisionManagerBeanList(level,mViewRegionBean.getId()));
            getDivisionManagerBeanList(DivisionManagerBeanList,type+1,mViewRegionBean.getId());
        }
    }

}
