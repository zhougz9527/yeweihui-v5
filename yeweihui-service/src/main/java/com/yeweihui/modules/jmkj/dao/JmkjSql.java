package com.yeweihui.modules.jmkj.dao;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.yeweihui.modules.jmkj.Entity.IndustryDirectorBean;
import com.yeweihui.modules.jmkj.Entity.JmkjLoginStatusBean;
import com.yeweihui.modules.jmkj.Entity.PerformanceOfDutiesBean;
import com.yeweihui.modules.jmkj.Entity.PerformanceRateBean;
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
            "count(*)as num " +
            "from " +
            "(" +

            "select " +
            "`vote_member`.uid as uid," +
            "`user`.realname as realname," +
            "`user`.avatar_url as avatarUrl " +
            "from `vote_member` LEFT JOIN `user` ON `vote_member`.uid=`user`.id " +
            "LEFT JOIN `vote` ON `vote_member`.vid=`vote`.id " +
            "where `vote`.end_time>=`vote_member`.vote_time AND `vote_member`.`status`!=4 AND `vote`.zone_id=#{zoneId} AND (`vote`.create_time>=#{timeStart} AND `vote`.create_time<=#{timeEnd}) " +

            "UNION ALL " +

            "select " +
            "`request_member`.uid as uid," +
            "`user`.realname as realname," +
            "`user`.avatar_url as avatarUrl " +
            "FROM `request_member` LEFT JOIN `user` ON `request_member`.uid=`user`.id " +
            "LEFT JOIN `request` ON `request_member`.rid=`request`.id " +
            "where `request`.use_date>=`request_member`.verify_time AND `request`.zone_id=#{zoneId} AND (`request`.create_time>=#{timeStart} AND `request`.create_time<=#{timeEnd}) " +

            "UNION ALL " +

            "select " +
            "`meeting_member`.uid as uid," +
            "`user`.realname as realname," +
            "`user`.avatar_url as avatarUrl " +
            "FROM `meeting_member` LEFT JOIN `user` ON `meeting_member`.uid=`user`.id " +
            "LEFT JOIN `meeting` ON `meeting_member`.mid=`meeting`.id " +
            "where `meeting_member`.sign_name_time IS NOT NULL AND `meeting`.zone_id=#{zoneId} AND (`meeting`.create_time>=#{timeStart} AND `meeting`.create_time<=#{timeEnd}) " +

            "UNION ALL " +

            "select " +
            "`bill_member`.uid as uid," +
            "`user`.realname as realname," +
            "`user`.avatar_url as avatarUrl " +
            "FROM `bill_member` LEFT JOIN `user` ON `bill_member`.uid=`user`.id " +
            "LEFT JOIN `bill` ON `bill_member`.bid=`bill`.id " +
            "where `bill_member`.verify_time IS NOT NULL AND `bill`.zone_id=#{zoneId} AND (`bill`.create_time>=#{timeStart} AND `bill`.create_time<=#{timeEnd}) " +

            "UNION ALL " +

            "select " +
            "`paper_member`.uid as uid," +
            "`user`.realname as realname," +
            "`user`.avatar_url as avatarUrl " +
            "FROM `paper_member` LEFT JOIN `user` ON `paper_member`.uid=`user`.id " +
            "LEFT JOIN `paper` ON `paper_member`.pid=`paper`.id " +
            "where `paper_member`.sign_time IS NOT NULL AND `paper`.zone_id=#{zoneId} AND (`paper`.create_time>=#{timeStart} AND `paper`.create_time<=#{timeEnd}) " +

            ")tablea " +
            "WHERE tablea.realname IS NOT NULL " +
            "GROUP BY tablea.uid " +
            ")a " +
            "ORDER BY a.num DESC")
    List<PerformanceOfDutiesBean> getPerformanceOfDutiesList(@Param("zoneId")Long zoneId, @Param("timeStart")Date timeStart,@Param("timeEnd")Date timeEnd);

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
            "sum(if(complete=1,1,0)) as complete," +
            "sum(if(fail=1,1,0)) as fail " +
            "from " +
            "(" +

            "select " +
            "`vote_member`.uid as uid," +
            "`user`.realname as realname," +
            "`user`.avatar_url as avatarUrl," +
            "if(`vote`.end_time>=`vote_member`.vote_time AND `vote_member`.`status`!=4,1,0) as complete," +
            "if(`vote`.end_time<`vote_member`.vote_time OR `vote_member`.`status`=4,1,0) as fail " +
            "from `vote_member` LEFT JOIN `user` ON `vote_member`.uid=`user`.id " +
            "LEFT JOIN `vote` ON `vote_member`.vid=`vote`.id " +
            "where `vote`.zone_id=#{zoneId} AND (`vote`.create_time>=#{timeStart} AND `vote`.create_time<=#{timeEnd}) " +

            "UNION ALL " +

            "select " +
            "`request_member`.uid as uid," +
            "`user`.realname as realname," +
            "`user`.avatar_url as avatarUrl," +
            "if(`request`.use_date>=`request_member`.verify_time,1,0) as complete," +
            "if(`request`.use_date<`request_member`.verify_time,1,0) as fail " +
            "FROM `request_member` LEFT JOIN `user` ON `request_member`.uid=`user`.id " +
            "LEFT JOIN `request` ON `request_member`.rid=`request`.id " +
            "where `request`.zone_id=#{zoneId} AND (`request`.create_time>=#{timeStart} AND `request`.create_time<=#{timeEnd}) " +

            "UNION ALL " +

            "select " +
            "`meeting_member`.uid as uid," +
            "`user`.realname as realname," +
            "`user`.avatar_url as avatarUrl," +
            "if(`meeting_member`.sign_name_time IS NOT NULL,1,0) as complete," +
            "if(`meeting_member`.sign_name_time IS NULL,1,0) as fail " +
            "FROM `meeting_member` LEFT JOIN `user` ON `meeting_member`.uid=`user`.id " +
            "LEFT JOIN `meeting` ON `meeting_member`.mid=`meeting`.id " +
            "where `meeting`.zone_id=#{zoneId} AND (`meeting`.create_time>=#{timeStart} AND `meeting`.create_time<=#{timeEnd}) " +

            "UNION ALL " +

            "select " +
            "`bill_member`.uid as uid," +
            "`user`.realname as realname," +
            "`user`.avatar_url as avatarUrl," +
            "if(`bill_member`.verify_time IS NOT NULL,1,0) as complete," +
            "if(`bill_member`.verify_time IS NULL,1,0) as fail " +
            "FROM `bill_member` LEFT JOIN `user` ON `bill_member`.uid=`user`.id " +
            "LEFT JOIN `bill` ON `bill_member`.bid=`bill`.id " +
            "where `bill`.zone_id=#{zoneId} AND (`bill`.create_time>=#{timeStart} AND `bill`.create_time<=#{timeEnd}) " +

            "UNION ALL " +

            "select " +
            "`paper_member`.uid as uid," +
            "`user`.realname as realname," +
            "`user`.avatar_url as avatarUrl," +
            "if(`paper_member`.sign_time IS NOT NULL,1,0) as complete," +
            "if(`paper_member`.sign_time IS NULL,1,0) as fail " +
            "FROM `paper_member` LEFT JOIN `user` ON `paper_member`.uid=`user`.id " +
            "LEFT JOIN `paper` ON `paper_member`.pid=`paper`.id " +
            "where `paper_member`.sign_time IS NOT NULL AND `paper`.zone_id=#{zoneId} AND (`paper`.create_time>=#{timeStart} AND `paper`.create_time<=#{timeEnd}) " +

            ")tablea " +
            "WHERE tablea.realname IS NOT NULL " +
            "GROUP BY tablea.uid " +
            ")a " +
            "ORDER BY a.complete/(a.complete+a.fail) desc")
    List<PerformanceRateBean> getPerformanceRateBeans(@Param("zoneId")Long zoneId, @Param("timeStart")Date timeStart,@Param("timeEnd")Date timeEnd);

    /**
     * 逾期量查询
     * */
    @Select("select * from " +
            "(" +
            "select " +
            "uid," +
            "realname," +
            "avatarUrl," +
            "count(*)as num " +
            "from " +
            "(" +

            "select " +
            "`vote_member`.uid as uid," +
            "`user`.realname as realname," +
            "`user`.avatar_url as avatarUrl " +
            "from `vote_member` LEFT JOIN `user` ON `vote_member`.uid=`user`.id " +
            "LEFT JOIN `vote` ON `vote_member`.vid=`vote`.id " +
            "where (`vote`.end_time<`vote_member`.vote_time OR `vote_member`.`status`=4) AND `vote`.zone_id=#{zoneId} AND (`vote`.create_time>=#{timeStart} AND `vote`.create_time<=#{timeEnd}) " +

            "UNION ALL " +

            "select " +
            "`request_member`.uid as uid," +
            "`user`.realname as realname," +
            "`user`.avatar_url as avatarUrl " +
            "FROM `request_member` LEFT JOIN `user` ON `request_member`.uid=`user`.id " +
            "LEFT JOIN `request` ON `request_member`.rid=`request`.id " +
            "where `request`.use_date<`request_member`.verify_time AND `request`.zone_id=#{zoneId} AND (`request`.create_time>=#{timeStart} AND `request`.create_time<=#{timeEnd}) " +

            "UNION ALL " +

            "select " +
            "`meeting_member`.uid as uid," +
            "`user`.realname as realname," +
            "`user`.avatar_url as avatarUrl " +
            "FROM `meeting_member` LEFT JOIN `user` ON `meeting_member`.uid=`user`.id " +
            "LEFT JOIN `meeting` ON `meeting_member`.mid=`meeting`.id " +
            "where `meeting_member`.sign_name_time IS NULL AND `meeting`.zone_id=#{zoneId} AND (`meeting`.create_time>=#{timeStart} AND `meeting`.create_time<=#{timeEnd}) " +

            "UNION ALL " +

            "select " +
            "`bill_member`.uid as uid," +
            "`user`.realname as realname," +
            "`user`.avatar_url as avatarUrl " +
            "FROM `bill_member` LEFT JOIN `user` ON `bill_member`.uid=`user`.id " +
            "LEFT JOIN `bill` ON `bill_member`.bid=`bill`.id " +
            "where `bill_member`.verify_time IS NULL AND `bill`.zone_id=#{zoneId} AND (`bill`.create_time>=#{timeStart} AND `bill`.create_time<=#{timeEnd}) " +

            "UNION ALL " +

            "select " +
            "`paper_member`.uid as uid," +
            "`user`.realname as realname," +
            "`user`.avatar_url as avatarUrl " +
            "FROM `paper_member` LEFT JOIN `user` ON `paper_member`.uid=`user`.id " +
            "LEFT JOIN `paper` ON `paper_member`.pid=`paper`.id " +
            "where `paper_member`.sign_time IS NULL AND `paper`.zone_id=#{zoneId} AND (`paper`.create_time>=#{timeStart} AND `paper`.create_time<=#{timeEnd}) " +

            ")tablea " +
            "WHERE tablea.realname IS NOT NULL " +
            "GROUP BY tablea.uid " +
            ")a " +
            "ORDER BY a.num DESC")
    List<PerformanceOfDutiesBean> OverdueQuantity(@Param("zoneId")Long zoneId, @Param("timeStart")Date timeStart,@Param("timeEnd")Date timeEnd);

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
            "sum(if(complete=1,1,0)) as complete," +
            "sum(if(fail=1,1,0)) as fail " +
            "from " +
            "(" +

            "select " +
            "`vote_member`.uid as uid," +
            "`user`.realname as realname," +
            "`user`.avatar_url as avatarUrl," +
            "if(`vote`.end_time>=`vote_member`.vote_time AND `vote_member`.`status`!=4,1,0) as complete," +
            "if(`vote`.end_time<`vote_member`.vote_time OR `vote_member`.`status`=4,1,0) as fail " +
            "from `vote_member` LEFT JOIN `user` ON `vote_member`.uid=`user`.id " +
            "LEFT JOIN `vote` ON `vote_member`.vid=`vote`.id " +
            "where `vote`.zone_id=#{zoneId} AND (`vote`.create_time>=#{timeStart} AND `vote`.create_time<=#{timeEnd}) " +

            "UNION ALL " +

            "select " +
            "`request_member`.uid as uid," +
            "`user`.realname as realname," +
            "`user`.avatar_url as avatarUrl," +
            "if(`request`.use_date>=`request_member`.verify_time,1,0) as complete," +
            "if(`request`.use_date<`request_member`.verify_time,1,0) as fail " +
            "FROM `request_member` LEFT JOIN `user` ON `request_member`.uid=`user`.id " +
            "LEFT JOIN `request` ON `request_member`.rid=`request`.id " +
            "where `request`.zone_id=#{zoneId} AND (`request`.create_time>=#{timeStart} AND `request`.create_time<=#{timeEnd}) " +

            "UNION ALL " +

            "select " +
            "`meeting_member`.uid as uid," +
            "`user`.realname as realname," +
            "`user`.avatar_url as avatarUrl," +
            "if(`meeting_member`.sign_name_time IS NOT NULL,1,0) as complete," +
            "if(`meeting_member`.sign_name_time IS NULL,1,0) as fail " +
            "FROM `meeting_member` LEFT JOIN `user` ON `meeting_member`.uid=`user`.id " +
            "LEFT JOIN `meeting` ON `meeting_member`.mid=`meeting`.id " +
            "where `meeting`.zone_id=#{zoneId} AND (`meeting`.create_time>=#{timeStart} AND `meeting`.create_time<=#{timeEnd}) " +

            "UNION ALL " +

            "select " +
            "`bill_member`.uid as uid," +
            "`user`.realname as realname," +
            "`user`.avatar_url as avatarUrl," +
            "if(`bill_member`.verify_time IS NOT NULL,1,0) as complete," +
            "if(`bill_member`.verify_time IS NULL,1,0) as fail " +
            "FROM `bill_member` LEFT JOIN `user` ON `bill_member`.uid=`user`.id " +
            "LEFT JOIN `bill` ON `bill_member`.bid=`bill`.id " +
            "where `bill`.zone_id=#{zoneId} AND (`bill`.create_time>=#{timeStart} AND `bill`.create_time<=#{timeEnd}) " +

            "UNION ALL " +

            "select " +
            "`paper_member`.uid as uid," +
            "`user`.realname as realname," +
            "`user`.avatar_url as avatarUrl," +
            "if(`paper_member`.sign_time IS NOT NULL,1,0) as complete," +
            "if(`paper_member`.sign_time IS NULL,1,0) as fail " +
            "FROM `paper_member` LEFT JOIN `user` ON `paper_member`.uid=`user`.id " +
            "LEFT JOIN `paper` ON `paper_member`.pid=`paper`.id " +
            "where `paper_member`.sign_time IS NOT NULL AND `paper`.zone_id=#{zoneId} AND (`paper`.create_time>=#{timeStart} AND `paper`.create_time<=#{timeEnd}) " +

            ")tablea " +
            "WHERE tablea.realname IS NOT NULL " +
            "GROUP BY tablea.uid " +
            ")a " +
            "ORDER BY a.fail/(a.complete+a.fail) desc")
    List<PerformanceRateBean> OverdueRate(@Param("zoneId")Long zoneId, @Param("timeStart")Date timeStart,@Param("timeEnd")Date timeEnd);

    /**
     * 操作性任务新建总量
     * */
    @Select("select * from " +
            "(" +
            "select " +
            "uid," +
            "realname," +
            "avatarUrl," +
            "count(*)as num " +
            "from " +
            "(" +

            "select " +
            "`vote`.uid as uid," +
            "`user`.realname as realname," +
            "`user`.avatar_url as avatarUrl " +
            "from `vote` LEFT JOIN `user` ON `vote`.uid=`user`.id " +
            "where `vote`.zone_id=#{zoneId} AND (`vote`.create_time>=#{timeStart} AND `vote`.create_time<=#{timeEnd}) " +

            "UNION ALL " +

            "select " +
            "`request`.uid as uid," +
            "`user`.realname as realname," +
            "`user`.avatar_url as avatarUrl " +
            "FROM `request` LEFT JOIN `user` ON `request`.uid=`user`.id " +
            "where `request`.zone_id=#{zoneId} AND (`request`.create_time>=#{timeStart} AND `request`.create_time<=#{timeEnd}) " +

            "UNION ALL " +

            "select " +
            "`meeting`.uid as uid," +
            "`user`.realname as realname," +
            "`user`.avatar_url as avatarUrl " +
            "FROM `meeting` LEFT JOIN `user` ON `meeting`.uid=`user`.id " +
            "where `meeting`.zone_id=#{zoneId} AND (`meeting`.create_time>=#{timeStart} AND `meeting`.create_time<=#{timeEnd}) " +

            "UNION ALL " +

            "select " +
            "`bill`.uid as uid," +
            "`user`.realname as realname," +
            "`user`.avatar_url as avatarUrl " +
            "FROM `bill` LEFT JOIN `user` ON `bill`.uid=`user`.id " +
            "where `bill`.zone_id=#{zoneId} AND (`bill`.create_time>=#{timeStart} AND `bill`.create_time<=#{timeEnd}) " +

            "UNION ALL " +

            "select " +
            "`paper`.uid as uid," +
            "`user`.realname as realname," +
            "`user`.avatar_url as avatarUrl " +
            "FROM `paper` LEFT JOIN `user` ON `paper`.uid=`user`.id " +
            "where `paper`.zone_id=#{zoneId} AND (`paper`.create_time>=#{timeStart} AND `paper`.create_time<=#{timeEnd}) " +

            ")tablea " +
            "WHERE tablea.realname IS NOT NULL " +
            "GROUP BY tablea.uid " +
            ")a " +
            "ORDER BY a.num DESC")
    List<PerformanceOfDutiesBean> operationNum(@Param("zoneId")Long zoneId, @Param("timeStart")Date timeStart,@Param("timeEnd")Date timeEnd);

    /**
     * 浏览任务 完成总量
     * */
    @Select("select * from " +
            "(" +
            "select " +
            "uid," +
            "realname," +
            "avatarUrl," +
            "count(*)as num " +
            "from " +
            "(" +

            "select " +
            "`announce_member`.uid as uid," +
            "`user`.realname as realname," +
            "`user`.avatar_url as avatarUrl " +
            "from `announce_member` LEFT JOIN `user` ON `announce_member`.uid=`user`.id " +
            "LEFT JOIN `announce` ON `announce`.id=`announce_member`.aid " +
            "where `announce`.zone_id=#{zoneId} AND (`announce`.create_time>=#{timeStart} AND `announce`.create_time<=#{timeEnd}) " +

            "UNION ALL " +

            "select " +
            "`notice_member`.uid as uid," +
            "`user`.realname as realname," +
            "`user`.avatar_url as avatarUrl " +
            "from `notice_member` LEFT JOIN `user` ON `notice_member`.uid=`user`.id " +
            "LEFT JOIN `notice` ON `notice`.id=`notice_member`.nid " +
            "where `notice`.zone_id=#{zoneId} AND (`notice`.create_time>=#{timeStart} AND `notice`.create_time<=#{timeEnd}) " +

            ")tablea " +
            "WHERE tablea.realname IS NOT NULL " +
            "GROUP BY tablea.uid " +
            ")a " +
            "ORDER BY a.num DESC")
    List<PerformanceOfDutiesBean> BrowseComplete(@Param("zoneId")Long zoneId, @Param("timeStart")Date timeStart,@Param("timeEnd")Date timeEnd);

    /**
     * 浏览任务 新建总量
     * */
    @Select("select * from " +
            "(" +
            "select " +
            "uid," +
            "realname," +
            "avatarUrl," +
            "count(*)as num " +
            "from " +
            "(" +

            "select " +
            "`announce`.uid as uid," +
            "`user`.realname as realname," +
            "`user`.avatar_url as avatarUrl " +
            "from `announce` LEFT JOIN `user` ON `announce`.uid=`user`.id " +
            "where `announce`.zone_id=#{zoneId} AND (`announce`.create_time>=#{timeStart} AND `announce`.create_time<=#{timeEnd}) " +

            "UNION ALL " +

            "select " +
            "`notice`.uid as uid," +
            "`user`.realname as realname," +
            "`user`.avatar_url as avatarUrl " +
            "from `notice` LEFT JOIN `user` ON `notice`.uid=`user`.id " +
            "where `notice`.zone_id=#{zoneId} AND (`notice`.create_time>=#{timeStart} AND `notice`.create_time<=#{timeEnd}) " +

            ")tablea " +
            "WHERE tablea.realname IS NOT NULL " +
            "GROUP BY tablea.uid " +
            ")a " +
            "ORDER BY a.num DESC")
    List<PerformanceOfDutiesBean> NewBrowse(@Param("zoneId")Long zoneId, @Param("timeStart")Date timeStart,@Param("timeEnd")Date timeEnd);

    /**
     * 获取在线时长
     * */
    @Select("select * from " +
            "(" +
            "select " +
            "uid," +
            "realname," +
            "avatarUrl," +
            "sum(on_line_time)as num " +
            "from " +
            "(" +

            "select " +
            "`jmkj_login_status`.uid as uid," +
            "`user`.realname as realname," +
            "`jmkj_login_status`.on_line_time as on_line_time," +
            "`user`.avatar_url as avatarUrl " +
            "from `jmkj_login_status` LEFT JOIN `user` ON `jmkj_login_status`.uid=`user`.id " +
            "where `user`.zone_id=#{zoneId} AND (`jmkj_login_status`.create_time>=#{timeStart} AND `jmkj_login_status`.create_time<=#{timeEnd}) " +

            ")tablea " +
            "WHERE tablea.realname IS NOT NULL " +
            "GROUP BY tablea.uid " +
            ")a " +
            "ORDER BY a.num DESC")
    List<PerformanceOfDutiesBean> OnlineDuration(@Param("zoneId")Long zoneId, @Param("timeStart")Date timeStart,@Param("timeEnd")Date timeEnd);

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
            "`jmkj_login_status`.uid as uid," +
            "`user`.realname as realname," +
            "`jmkj_login_status`.login_times as login_times," +
            "`user`.avatar_url as avatarUrl " +
            "from `jmkj_login_status` LEFT JOIN `user` ON `jmkj_login_status`.uid=`user`.id " +
            "where `user`.zone_id=#{zoneId} AND (`jmkj_login_status`.create_time>=#{timeStart} AND `jmkj_login_status`.create_time<=#{timeEnd}) " +

            ")tablea " +
            "WHERE tablea.realname IS NOT NULL " +
            "GROUP BY tablea.uid " +
            ")a " +
            "ORDER BY a.num DESC")
    List<PerformanceOfDutiesBean> OnlineNum(@Param("zoneId")Long zoneId, @Param("timeStart")Date timeStart,@Param("timeEnd")Date timeEnd);

    /**
     * 行业主管列表
     * */
    @Select("select " +
            "division_manager.id as id," +
            "`user`.realname as realname," +
            "division_manager.`level` as `level`," +
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
}
