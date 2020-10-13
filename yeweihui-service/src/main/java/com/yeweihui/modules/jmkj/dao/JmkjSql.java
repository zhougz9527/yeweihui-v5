package com.yeweihui.modules.jmkj.dao;

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
            "where `vote`.end_time<NOW() AND `vote`.zone_id=#{zoneId} AND (`vote`.create_time>=#{timeStart} AND `vote`.create_time<=#{timeEnd}) " +

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
     * 履职率
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
            "if(`vote`.end_time<NOW(),1,0) as complete," +
            "if(`vote`.end_time>=NOW(),1,0) as fail " +
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

}
