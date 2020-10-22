package com.yeweihui.modules.jmkj.dao;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.yeweihui.modules.jmkj.Entity.*;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

public interface JmkjSql {

    /**
     * 查询用户登录状态信息列表
     * */
    @Select("select " +
            "id as id," +
            "uid as uid," +
            "on_line_time as onLineTime," +
            "login_times as loginTimes," +
            "create_time as createTime," +
            "update_time as updateTime," +
            "month_time as monthTime " +
            "from " +
            "jmkj_login_status " +
            "where uid=#{uid} " +
            "order by month_time desc")
    List<JmkjLoginStatusBean> selectJmkjLoginStatus(@Param("uid")Long uid);

    /**
     * 添加用户登录状态
     * */
    @Insert("INSERT INTO " +
            "jmkj_login_status(uid,on_line_time,login_times,create_time,update_time,month_time) " +
            "VALUES(#{uid},#{onLineTime},#{loginTimes},#{createTime},#{updateTime},#{monthTime})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    int insertJmkjLoginStatus(JmkjLoginStatusBean mJmkjLoginStatusBean);

    /**
     * 修改用户登录时间
     * */
    @Update("UPDATE jmkj_login_status SET on_line_time=#{time} WHERE uid=#{uid} AND month_time=#{month}")
    int updateJmkjLoginStatus(@Param("time")Long time,@Param("uid")Long uid,@Param("month")Long month);

    /**
     * 修改用户登录次数
     * */
    @Update("UPDATE jmkj_login_status SET login_times=#{num} WHERE uid=#{uid} AND month_time=#{month}")
    int updateJmkjLoginStatusNum(@Param("num")Long num,@Param("uid")Long uid,@Param("month")Long month);

    /**
     * 履职量查询
     * */
    @Select("select * from " +
            "(" +
            "select " +
            "uid," +
            "realname," +
            "avatarUrl," +
            "sum(if(create_time>=#{timeStart} AND create_time<=#{timeEnd} and md=1,1,0)) as num " +
            "from " +
            "(" +

            "select " +
            "`vote`.create_time as create_time,"+
            "if(`vote`.end_time>=`vote_member`.vote_time AND `vote_member`.`status`!=4,1,0) as md," +
            "`user`.id as uid," +
            "`user`.realname as realname," +
            "`user`.avatar_url as avatarUrl " +
            "from `user` LEFT JOIN `vote_member` ON `vote_member`.uid=`user`.id " +
            "LEFT JOIN `vote` ON `vote_member`.vid=`vote`.id " +
            "LEFT JOIN `zones` ON vote.zone_id = zones.id " +
            "where true ${ew.sqlSegment} " +
            "GROUP BY `user`.id " +

            "UNION ALL " +

            "select " +
            "`request`.create_time as create_time," +
            "if(`request`.use_date>=`request_member`.verify_time,1,0) as md," +
            "`user`.id as uid," +
            "`user`.realname as realname," +
            "`user`.avatar_url as avatarUrl " +
            "FROM `user` LEFT JOIN `request_member` ON `request_member`.uid=`user`.id " +
            "LEFT JOIN `request` ON `request_member`.rid=`request`.id " +
            "LEFT JOIN `zones` ON request.zone_id = zones.id " +
            "where true ${ew.sqlSegment} " +
            "GROUP BY `user`.id " +

            "UNION ALL " +

            "select " +
            "`meeting`.create_time as create_time," +
            "if(`meeting_member`.sign_name_time IS NOT NULL,1,0) as md," +
            "`user`.id as uid," +
            "`user`.realname as realname," +
            "`user`.avatar_url as avatarUrl " +
            "FROM `user` LEFT JOIN `meeting_member` ON `meeting_member`.uid=`user`.id " +
            "LEFT JOIN `meeting` ON `meeting_member`.mid=`meeting`.id " +
            "LEFT JOIN `zones` ON meeting.zone_id = zones.id " +
            "where true ${ew.sqlSegment} " +
            "GROUP BY `user`.id " +

            "UNION ALL " +

            "select " +
            "`bill`.create_time as create_time," +
            "if(`bill_member`.verify_time IS NOT NULL,1,0) as md," +
            "`user`.id as uid," +
            "`user`.realname as realname," +
            "`user`.avatar_url as avatarUrl " +
            "FROM `user` LEFT JOIN `bill_member` ON `bill_member`.uid=`user`.id " +
            "LEFT JOIN `bill` ON `bill_member`.bid=`bill`.id " +
            "LEFT JOIN `zones` ON bill.zone_id = zones.id " +
            "where true ${ew.sqlSegment} " +
            "GROUP BY `user`.id " +

            "UNION ALL " +

            "select " +
            "`paper`.create_time as create_time," +
            "if(`paper_member`.sign_time IS NOT NULL,1,0) as md,"+
            "`user`.id as uid," +
            "`user`.realname as realname," +
            "`user`.avatar_url as avatarUrl " +
            "FROM `user` LEFT JOIN `paper_member` ON `paper_member`.uid=`user`.id " +
            "LEFT JOIN `paper` ON `paper_member`.pid=`paper`.id " +
            "LEFT JOIN `zones` ON paper.zone_id = zones.id " +
            "where true ${ew.sqlSegment} " +
            "GROUP BY `user`.id " +

            ")tablea " +
            "WHERE tablea.realname IS NOT NULL " +
            "GROUP BY tablea.uid " +
            ")a " +
            "ORDER BY a.num DESC")
    List<PerformanceOfDutiesBean> getPerformanceOfDutiesList(@Param("timeStart")Date timeStart,@Param("timeEnd")Date timeEnd,@Param("ew") Wrapper<PerformanceOfDutiesBean> var2);

    /**
     * 履职率查询
     * */
    @Select("select " +
            "uid," +
            "realname," +
            "avatarUrl," +
            "complete/(complete+a.fail) as proportion " +
            "from " +
            "(" +
            "select " +
            "uid," +
            "realname," +
            "avatarUrl," +
            "sum(if(create_time>=#{timeStart} AND create_time<=#{timeEnd},complete,0)) as complete," +
            "sum(if(create_time>=#{timeStart} AND create_time<=#{timeEnd},fail,0)) as fail " +
            "from " +
            "(" +

            "select " +
            "`vote`.create_time as create_time," +
            "`user`.id as uid," +
            "`user`.realname as realname," +
            "`user`.avatar_url as avatarUrl," +
            "sum(if(`vote`.end_time>=`vote_member`.vote_time AND `vote_member`.`status`!=4,1,0)) as complete," +
            "sum(if(`vote`.end_time<`vote_member`.vote_time OR `vote_member`.`status`=4 OR `vote_member`.vote_time IS NULL,1,0)) as fail " +
            "from `user` LEFT JOIN `vote_member` ON `vote_member`.uid=`user`.id " +
            "LEFT JOIN `vote` ON `vote_member`.vid=`vote`.id " +
            "LEFT JOIN `zones` ON vote.zone_id = zones.id " +
            "where true ${ew.sqlSegment} " +
            "GROUP BY `user`.id " +

            "UNION ALL " +

            "select " +
            "`request`.create_time as create_time," +
            "`user`.id as uid," +
            "`user`.realname as realname," +
            "`user`.avatar_url as avatarUrl," +
            "sum(if(`request`.use_date>=`request_member`.verify_time,1,0)) as complete," +
            "sum(if(`request`.use_date<`request_member`.verify_time OR `request_member`.verify_time IS NULL,1,0)) as fail " +
            "FROM `user` LEFT JOIN `request_member` ON `request_member`.uid=`user`.id " +
            "LEFT JOIN `request` ON `request_member`.rid=`request`.id " +
            "LEFT JOIN `zones` ON request.zone_id = zones.id " +
            "where true ${ew.sqlSegment} " +
            "GROUP BY `user`.id " +

            "UNION ALL " +

            "select " +
            "`meeting`.create_time as create_time," +
            "`user`.id as uid," +
            "`user`.realname as realname," +
            "`user`.avatar_url as avatarUrl," +
            "sum(if(`meeting_member`.sign_name_time IS NOT NULL,1,0)) as complete," +
            "sum(if(`meeting_member`.sign_name_time IS NULL,1,0)) as fail " +
            "FROM `meeting_member` LEFT JOIN `user` ON `meeting_member`.uid=`user`.id " +
            "LEFT JOIN `meeting` ON `meeting_member`.mid=`meeting`.id " +
            "LEFT JOIN `zones` ON meeting.zone_id = zones.id " +
            "where true ${ew.sqlSegment} " +
            "GROUP BY `user`.id " +

            "UNION ALL " +

            "select " +
            "`bill`.create_time as create_time," +
            "`user`.id as uid," +
            "`user`.realname as realname," +
            "`user`.avatar_url as avatarUrl," +
            "sum(if(`bill_member`.verify_time IS NOT NULL,1,0)) as complete," +
            "sum(if(`bill_member`.verify_time IS NULL,1,0)) as fail " +
            "FROM `bill_member` LEFT JOIN `user` ON `bill_member`.uid=`user`.id " +
            "LEFT JOIN `bill` ON `bill_member`.bid=`bill`.id " +
            "LEFT JOIN `zones` ON bill.zone_id = zones.id " +
            "where true ${ew.sqlSegment} " +
            "GROUP BY `user`.id " +

            "UNION ALL " +

            "select " +
            "`paper`.create_time as create_time," +
            "`user`.id as uid," +
            "`user`.realname as realname," +
            "`user`.avatar_url as avatarUrl," +
            "sum(if(`paper_member`.sign_time IS NOT NULL,1,0)) as complete," +
            "sum(if(`paper_member`.sign_time IS NULL,1,0)) as fail " +
            "FROM `paper_member` LEFT JOIN `user` ON `paper_member`.uid=`user`.id " +
            "LEFT JOIN `paper` ON `paper_member`.pid=`paper`.id " +
            "LEFT JOIN `zones` ON paper.zone_id = zones.id " +
            "where `paper_member`.sign_time IS NOT NULL ${ew.sqlSegment} " +
            "GROUP BY `user`.id " +

            ")tablea " +
            "WHERE tablea.realname IS NOT NULL " +
            "GROUP BY tablea.uid " +
            ")a " +
            "ORDER BY a.complete/(a.complete+a.fail) desc")
    List<PerformanceRateBean> getPerformanceRateBeans(@Param("timeStart")Date timeStart,@Param("timeEnd")Date timeEnd,@Param("ew") Wrapper<PerformanceRateBean> var2);

    /**
     * 逾期量查询
     * */
    @Select("select * from " +
            "(" +
            "select " +
            "uid," +
            "realname," +
            "avatarUrl," +
            "sum(if(create_time>=#{timeStart} AND create_time<=#{timeEnd},1,0)) as num "+
            "from " +
            "(" +

            "select " +
            "`vote`.create_time as create_time," +
            "`user`.id as uid," +
            "`user`.realname as realname," +
            "`user`.avatar_url as avatarUrl " +
            "from `user` LEFT JOIN `vote_member` ON `vote_member`.uid=`user`.id " +
            "LEFT JOIN `vote` ON `vote_member`.vid=`vote`.id " +
            "LEFT JOIN `zones` ON vote.zone_id = zones.id " +
            "where (`vote`.end_time<`vote_member`.vote_time OR `vote_member`.`status`=4) ${ew.sqlSegment} " +
            "GROUP BY `user`.id " +

            "UNION ALL " +

            "select " +
            "`request`.create_time as create_time," +
            "`user`.id as uid," +
            "`user`.realname as realname," +
            "`user`.avatar_url as avatarUrl " +
            "FROM `user` LEFT JOIN `request_member` ON `request_member`.uid=`user`.id " +
            "LEFT JOIN `request` ON `request_member`.rid=`request`.id " +
            "LEFT JOIN `zones` ON request.zone_id = zones.id " +
            "where `request`.use_date<`request_member`.verify_time ${ew.sqlSegment} " +
            "GROUP BY `user`.id " +

            "UNION ALL " +

            "select " +
            "`meeting`.create_time as create_time," +
            "`user`.id as uid," +
            "`user`.realname as realname," +
            "`user`.avatar_url as avatarUrl " +
            "FROM `user` LEFT JOIN `meeting_member` ON `meeting_member`.uid=`user`.id " +
            "LEFT JOIN `meeting` ON `meeting_member`.mid=`meeting`.id " +
            "LEFT JOIN `zones` ON meeting.zone_id = zones.id " +
            "where `meeting_member`.sign_name_time IS NULL ${ew.sqlSegment} " +
            "GROUP BY `user`.id " +

            "UNION ALL " +

            "select " +
            "`bill`.create_time as create_time," +
            "`user`.id as uid," +
            "`user`.realname as realname," +
            "`user`.avatar_url as avatarUrl " +
            "FROM `user` LEFT JOIN `bill_member` ON `bill_member`.uid=`user`.id " +
            "LEFT JOIN `bill` ON `bill_member`.bid=`bill`.id " +
            "LEFT JOIN `zones` ON bill.zone_id = zones.id " +
            "where `bill_member`.verify_time IS NULL ${ew.sqlSegment} " +
            "GROUP BY `user`.id " +

            "UNION ALL " +

            "select " +
            "`paper`.create_time as create_time," +
            "`user`.id as uid," +
            "`user`.realname as realname," +
            "`user`.avatar_url as avatarUrl " +
            "FROM `user` LEFT JOIN `paper_member` ON `paper_member`.uid=`user`.id " +
            "LEFT JOIN `paper` ON `paper_member`.pid=`paper`.id " +
            "LEFT JOIN `zones` ON paper.zone_id = zones.id " +
            "where `paper_member`.sign_time IS NULL ${ew.sqlSegment} " +
            "GROUP BY `user`.id " +

            ")tablea " +
            "WHERE tablea.realname IS NOT NULL " +
            "GROUP BY tablea.uid " +
            ")a " +
            "ORDER BY a.num DESC")
    List<PerformanceOfDutiesBean> OverdueQuantity(@Param("timeStart")Date timeStart,@Param("timeEnd")Date timeEnd,@Param("ew") Wrapper<PerformanceOfDutiesBean> var2);

    /**
     * 逾期率
     * */
    @Select("select " +
            "uid," +
            "realname," +
            "avatarUrl," +
            "a.fail/(complete+a.fail) as proportion " +
            "from " +
            "(" +
            "select " +
            "uid," +
            "realname," +
            "avatarUrl," +
            "sum(if(md=1 and complete=1,1,0)) as complete," +
            "sum(if(md=1 and fail=1,1,0)) as fail " +
            "from " +
            "(" +

            "select " +
            "`user`.id as uid," +
            "`user`.realname as realname," +
            "`user`.avatar_url as avatarUrl," +
            "if((`vote`.create_time>=#{timeStart} AND `vote`.create_time<=#{timeEnd}),1,0) as md," +
            "if(`vote`.end_time>=`vote_member`.vote_time AND `vote_member`.`status`!=4,1,0) as complete," +
            "if(`vote`.end_time<`vote_member`.vote_time OR `vote_member`.`status`=4,1,0) as fail " +
            "from `user` LEFT JOIN `vote_member` ON `vote_member`.uid=`user`.id " +
            "LEFT JOIN `vote` ON `vote_member`.vid=`vote`.id " +
            "LEFT JOIN `zones` ON vote.zone_id = zones.id " +
            "where true ${ew.sqlSegment} " +
            "GROUP BY `user`.id " +

            "UNION ALL " +

            "select " +
            "`user`.id as uid," +
            "`user`.realname as realname," +
            "`user`.avatar_url as avatarUrl," +
            "if((`request`.create_time>=#{timeStart} AND `request`.create_time<=#{timeEnd}),1,0) as md," +
            "if(`request`.use_date>=`request_member`.verify_time,1,0) as complete," +
            "if(`request`.use_date<`request_member`.verify_time,1,0) as fail " +
            "FROM `user` LEFT JOIN `request_member` ON `request_member`.uid=`user`.id " +
            "LEFT JOIN `request` ON `request_member`.rid=`request`.id " +
            "LEFT JOIN `zones` ON request.zone_id = zones.id " +
            "where true ${ew.sqlSegment} " +
            "GROUP BY `user`.id " +

            "UNION ALL " +

            "select " +
            "`user`.id as uid," +
            "`user`.realname as realname," +
            "`user`.avatar_url as avatarUrl," +
            "if((`meeting`.create_time>=#{timeStart} AND `meeting`.create_time<=#{timeEnd}),1,0) as md," +
            "if(`meeting_member`.sign_name_time IS NOT NULL,1,0) as complete," +
            "if(`meeting_member`.sign_name_time IS NULL,1,0) as fail " +
            "FROM `user` LEFT JOIN `meeting_member` ON `meeting_member`.uid=`user`.id " +
            "LEFT JOIN `meeting` ON `meeting_member`.mid=`meeting`.id " +
            "LEFT JOIN `zones` ON meeting.zone_id = zones.id " +
            "where true ${ew.sqlSegment} " +
            "GROUP BY `user`.id " +

            "UNION ALL " +

            "select " +
            "`user`.id as uid," +
            "`user`.realname as realname," +
            "`user`.avatar_url as avatarUrl," +
            "if((`bill`.create_time>=#{timeStart} AND `bill`.create_time<=#{timeEnd}),1,0) as md," +
            "if(`bill_member`.verify_time IS NOT NULL,1,0) as complete," +
            "if(`bill_member`.verify_time IS NULL,1,0) as fail " +
            "FROM `user` LEFT JOIN `bill_member` ON `bill_member`.uid=`user`.id " +
            "LEFT JOIN `bill` ON `bill_member`.bid=`bill`.id " +
            "LEFT JOIN `zones` ON bill.zone_id = zones.id " +
            "where true ${ew.sqlSegment} " +
            "GROUP BY `user`.id " +

            "UNION ALL " +

            "select " +
            "`user`.id as uid," +
            "`user`.realname as realname," +
            "`user`.avatar_url as avatarUrl," +
            "if(`paper_member`.sign_time IS NOT NULL AND (`paper`.create_time>=#{timeStart} AND `paper`.create_time<=#{timeEnd}),1,0) as md," +
            "if(`paper_member`.sign_time IS NOT NULL,1,0) as complete," +
            "if(`paper_member`.sign_time IS NULL,1,0) as fail " +
            "FROM `user` LEFT JOIN `paper_member` ON `paper_member`.uid=`user`.id " +
            "LEFT JOIN `paper` ON `paper_member`.pid=`paper`.id " +
            "LEFT JOIN `zones` ON paper.zone_id = zones.id " +
            "where true ${ew.sqlSegment} " +
            "GROUP BY `user`.id " +

            ")tablea " +
            "WHERE tablea.realname IS NOT NULL " +
            "GROUP BY tablea.uid " +
            ")a " +
            "ORDER BY a.fail/(a.complete+a.fail) desc")
    List<PerformanceRateBean> OverdueRate(@Param("timeStart")Date timeStart,@Param("timeEnd")Date timeEnd,@Param("ew") Wrapper<PerformanceRateBean> var2);

    /**
     * 操作性任务新建总量
     * */
    @Select("select * from " +
            "(" +
            "select " +
            "uid," +
            "realname," +
            "avatarUrl," +
            "sum(if(md=1,1,0))as num " +
            "from " +
            "(" +

            "select " +
            "`user`.id as uid," +
            "`user`.realname as realname," +
            "if((`vote`.create_time>=#{timeStart} AND `vote`.create_time<=#{timeEnd}),1,0) as md," +
            "`user`.avatar_url as avatarUrl " +
            "from `user` LEFT JOIN `vote` ON `vote`.uid=`user`.id " +
            "LEFT JOIN `zones` ON vote.zone_id = zones.id " +
            "where true ${ew.sqlSegment} " +
            "GROUP BY `user`.id " +

            "UNION ALL " +

            "select " +
            "`user`.id as uid," +
            "`user`.realname as realname," +
            "if((`request`.create_time>=#{timeStart} AND `request`.create_time<=#{timeEnd}),1,0) as md," +
            "`user`.avatar_url as avatarUrl " +
            "FROM `user` LEFT JOIN `request` ON `request`.uid=`user`.id " +
            "LEFT JOIN `zones` ON request.zone_id = zones.id " +
            "where true ${ew.sqlSegment} " +
            "GROUP BY `user`.id " +

            "UNION ALL " +

            "select " +
            "`user`.id as uid," +
            "`user`.realname as realname," +
            "if((`meeting`.create_time>=#{timeStart} AND `meeting`.create_time<=#{timeEnd}),1,0) as md," +
            "`user`.avatar_url as avatarUrl " +
            "FROM `user` LEFT JOIN `meeting` ON `meeting`.uid=`user`.id " +
            "LEFT JOIN `zones` ON meeting.zone_id = zones.id " +
            "where true ${ew.sqlSegment} " +
            "GROUP BY `user`.id " +

            "UNION ALL " +

            "select " +
            "`user`.id as uid," +
            "`user`.realname as realname," +
            "if((`bill`.create_time>=#{timeStart} AND `bill`.create_time<=#{timeEnd}),1,0) as md," +
            "`user`.avatar_url as avatarUrl " +
            "FROM `user` LEFT JOIN `bill` ON `bill`.uid=`user`.id " +
            "LEFT JOIN `zones` ON bill.zone_id = zones.id " +
            "where true ${ew.sqlSegment} " +
            "GROUP BY `user`.id " +

            "UNION ALL " +

            "select " +
            "`user`.id as uid," +
            "`user`.realname as realname," +
            "if((`paper`.create_time>=#{timeStart} AND `paper`.create_time<=#{timeEnd}),1,0) as md," +
            "`user`.avatar_url as avatarUrl " +
            "FROM `user` LEFT JOIN `paper` ON `paper`.uid=`user`.id " +
            "LEFT JOIN `zones` ON paper.zone_id = zones.id " +
            "where true ${ew.sqlSegment} " +
            "GROUP BY `user`.id " +

            ")tablea " +
            "WHERE tablea.realname IS NOT NULL " +
            "GROUP BY tablea.uid " +
            ")a " +
            "ORDER BY a.num DESC")
    List<PerformanceOfDutiesBean> operationNum(@Param("timeStart")Date timeStart,@Param("timeEnd")Date timeEnd,@Param("ew") Wrapper<PerformanceOfDutiesBean> var2);

    /**
     * 浏览任务 完成总量
     * */
    @Select("select * from " +
            "(" +
            "select " +
            "uid," +
            "realname," +
            "avatarUrl," +
            "sum(if(md=1,1,0))as num " +
            "from " +
            "(" +

            "select " +
            "`user`.id as uid," +
            "`user`.realname as realname," +
            "if((`announce`.create_time>=#{timeStart} AND `announce`.create_time<=#{timeEnd}),1,0) as md," +
            "`user`.avatar_url as avatarUrl " +
            "from `user` LEFT JOIN `announce_member` ON `announce_member`.uid=`user`.id " +
            "LEFT JOIN `announce` ON `announce`.id=`announce_member`.aid " +
            "LEFT JOIN `zones` ON announce.zone_id = zones.id " +
            "where true ${ew.sqlSegment} " +
            "GROUP BY `user`.id " +

            "UNION ALL " +

            "select " +
            "`user`.id as uid," +
            "`user`.realname as realname," +
            "if((`notice`.create_time>=#{timeStart} AND `notice`.create_time<=#{timeEnd}),1,0) as md," +
            "`user`.avatar_url as avatarUrl " +
            "from `user` LEFT JOIN `notice_member` ON `notice_member`.uid=`user`.id " +
            "LEFT JOIN `notice` ON `notice`.id=`notice_member`.nid " +
            "LEFT JOIN `zones` ON notice.zone_id = zones.id " +
            "where true ${ew.sqlSegment} " +
            "GROUP BY `user`.id " +

            ")tablea " +
            "WHERE tablea.realname IS NOT NULL " +
            "GROUP BY tablea.uid " +
            ")a " +
            "ORDER BY a.num DESC")
    List<PerformanceOfDutiesBean> BrowseComplete(@Param("timeStart")Date timeStart,@Param("timeEnd")Date timeEnd,@Param("ew") Wrapper<PerformanceOfDutiesBean> var2);

    /**
     * 浏览任务 新建总量
     * */
    @Select("select * from " +
            "(" +
            "select " +
            "uid," +
            "realname," +
            "avatarUrl," +
            "sum(md=1,1,0)as num " +
            "from " +
            "(" +

            "select " +
            "`user`.id as uid," +
            "`user`.realname as realname," +
            "if((`announce`.create_time>=#{timeStart} AND `announce`.create_time<=#{timeEnd}),1,0) as md," +
            "`user`.avatar_url as avatarUrl " +
            "from `user` LEFT JOIN `announce` ON `announce`.uid=`user`.id " +
            "LEFT JOIN `zones` ON `announce`.zone_id = zones.id " +
            "where true ${ew.sqlSegment} " +
            "GROUP BY `user`.id " +

            "UNION ALL " +

            "select " +
            "`user`.id as uid," +
            "`user`.realname as realname," +
            "if((`notice`.create_time>=#{timeStart} AND `notice`.create_time<=#{timeEnd}),1,0) as md," +
            "`user`.avatar_url as avatarUrl " +
            "from `user` LEFT JOIN `notice` ON `notice`.uid=`user`.id " +
            "LEFT JOIN `zones` ON `notice`.zone_id = zones.id " +
            "where true ${ew.sqlSegment} " +
            "GROUP BY `user`.id " +

            ")tablea " +
            "WHERE tablea.realname IS NOT NULL " +
            "GROUP BY tablea.uid " +
            ")a " +
            "ORDER BY a.num DESC")
    List<PerformanceOfDutiesBean> NewBrowse(@Param("timeStart")Date timeStart,@Param("timeEnd")Date timeEnd,@Param("ew") Wrapper<PerformanceOfDutiesBean> var2);

    /**
     * 获取在线时长
     * */
    @Select("select * from " +
            "(" +
            "select " +
            "uid," +
            "realname," +
            "avatarUrl," +
            "sum(if(md=1,on_line_time,0))as num " +
            "from " +
            "(" +

            "select " +
            "`user`.id as uid," +
            "`user`.realname as realname," +
            "if((`jmkj_login_status`.create_time>=#{timeStart} AND `jmkj_login_status`.create_time<=#{timeEnd}),1,0) as md," +
            "`jmkj_login_status`.on_line_time as on_line_time," +
            "`user`.avatar_url as avatarUrl " +
            "from `user` LEFT JOIN `jmkj_login_status` ON `jmkj_login_status`.uid=`user`.id " +
            "LEFT JOIN `zones` ON `user`.zone_id = zones.id " +
            "where true ${ew.sqlSegment} " +
            "GROUP BY `user`.id " +

            ")tablea " +
            "WHERE tablea.realname IS NOT NULL " +
            "GROUP BY tablea.uid " +
            ")a " +
            "ORDER BY a.num DESC")
    List<PerformanceOfDutiesBean> OnlineDuration(@Param("timeStart")long timeStart,@Param("timeEnd")long timeEnd,@Param("ew") Wrapper<PerformanceOfDutiesBean> var2);

    /**
     * 获取登录次数
     * */
    @Select("select * from " +
            "(" +
            "select " +
            "uid," +
            "realname," +
            "avatarUrl," +
            "sum(login_times)as num " +
            "from " +
            "(" +

            "select " +
            "`user`.id as uid," +
            "`user`.realname as realname," +
            "if((`jmkj_login_status`.create_time>=#{timeStart} AND `jmkj_login_status`.create_time<=#{timeEnd}),1,0) as md," +
            "`jmkj_login_status`.login_times as login_times," +
            "`user`.avatar_url as avatarUrl " +
            "from `user` LEFT JOIN `jmkj_login_status` ON `jmkj_login_status`.uid=`user`.id " +
            "LEFT JOIN `zones` ON `user`.zone_id = zones.id " +
            "where true ${ew.sqlSegment} " +
            "GROUP BY `user`.id " +

            ")tablea " +
            "WHERE tablea.realname IS NOT NULL " +
            "GROUP BY tablea.uid " +
            ")a " +
            "ORDER BY a.num DESC")
    List<PerformanceOfDutiesBean> OnlineNum(@Param("timeStart")long timeStart,@Param("timeEnd")long timeEnd,@Param("ew") Wrapper<PerformanceOfDutiesBean> var2);

    /**
     * 行业主管列表
     * */
    @Select("select " +
            "division_manager.id as id," +
            "`user`.realname as realname," +
            "division_manager.`level` as `level`," +
            "division_manager.user_id as userId," +
            "division_manager.division_id as divisionId " +
            "FROM division_manager LEFT JOIN `user` ON division_manager.user_id=`user`.id " +
            "LEFT JOIN sys_role ON `user`.role_id=`sys_role`.role_id where true ${ew.sqlSegment}")
    List<IndustryDirectorBean> IndustryDirector(Page<IndustryDirectorBean> StudentPage, @Param("ew") Wrapper<IndustryDirectorBean> var2);

    /** 事务表决正常弃权人数 */
    @Select("select count(*) " +
            "FROM vote_member LEFT JOIN vote ON vote_member.vid = vote.id " +
            "WHERE vote_member.vid=#{vid} AND vote_member.`status`=3 AND vote_member.vote_time<=vote.end_time")
    int TimeVote(@Param("vid")Long vid);

    /** 事务表决超时弃权人数 */
    @Select("select count(*) " +
            "FROM vote_member LEFT JOIN vote ON vote_member.vid = vote.id " +
            "WHERE vote_member.vid=#{vid} AND vote_member.`status`=3 AND vote_member.vote_time>vote.end_time")
    int NoTimeVote(@Param("vid")Long vid);

    /**
     * 获得用户所在小区的 省 市 区 社区 街道
     * */
    @Select("SELECT " +
            "zones.id as zonesId," +
            "zones.province_id as provinceId," +
            "zones.city_id as cityId," +
            "zones.district_id as districtId," +
            "zones.subdistrict_id as subdistrictId," +
            "zones.community_id as communityId " +
            "FROM `user` LEFT JOIN zones ON `user`.zone_id=zones.id " +
            "WHERE `user`.id=#{uId}")
    AdministrationFrom getAdministrationFrom(@Param("uId") Long uId);


    /**
     * 获取用户的行业主管信息
     * */
    @Select("select * from division_manager where user_id=#{uId}")
    DivisionManagerBean getDivisionManagerBean(@Param("uId")Long uId);

    /**
     * 获取行业主管信息列表
     * */
    @Select("select " +
            "division_manager.id as id," +
            "division_manager.`user_id` as userId," +
            "division_manager.`level` as level," +
            "division_manager.`division_id` as divisionId," +
            "`user`.realname as realname," +
            "`user`.avatar_url as avatarUrl " +
            "from division_manager " +
            "LEFT JOIN `user` ON division_manager.user_id=`user`.id " +
            "where `level`=#{level} AND division_id=#{divisionId}")
    List<DivisionManagerBean> getDivisionManagerBeanList(@Param("level")String level,@Param("divisionId")Long divisionId);

    /**
     * 获取行政列表
     * */
    @Select("SELECT * FROM view_region WHERE type=#{typd} AND typeId=#{typeId}")
    List<ViewRegionBean> getViewRegionBeanList(@Param("typd")int typd,@Param("typeId")Long typeId);

    /**
     * 插叙某表（五张行政级表）的名字（地名）
     * */
    @Select("select `name` FROM ${tablename} where id=#{id}")
    String getNmsl(@Param("tablename")String tablename,@Param("id")Long id);


    /**
     * 查询邀请码所属小区的小区管理员电话
     * */
    @Select("SELECT " +
            "`user`.mobile " +
            "FROM " +
            "`user` LEFT JOIN `zones` ON `user`.zone_id=zones.id " +
            "LEFT JOIN sys_user_role ON `user`.id=sys_user_role.user_id " +
            "LEFT JOIN sys_role ON sys_user_role.role_id=sys_role.role_id " +
            "WHERE zones.invite_code=#{code} AND sys_role.role_name='小区管理员'")
    List<String> getCodeTel(String code);

}
