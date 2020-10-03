package com.yeweihui.common.config;

import com.baomidou.mybatisplus.mapper.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

/**
 * Created by cutie on 2018/10/27.
 */
public class MybatisPlusObjectHandler extends MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        //新增时填充的字段
        setFieldValByName("createTime", new Date(), metaObject);
        setFieldValByName("modifyTime", new Date(), metaObject);
        setFieldValByName("updateTime", new Date(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        //更新时 需要填充字段
        setFieldValByName("modifyTime", new Date(), metaObject);
        setFieldValByName("updateTime", new Date(), metaObject);
    }
}
