package com.yeweihui.third;


import com.yeweihui.third.getui.GetuiUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;


@RunWith(SpringRunner.class)
@SpringBootTest
public class GetuiUtilsTest {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    GetuiUtils getuiUtils;

    /**
     * 别名绑定
     */
    @Test
    public void bindAliasTest(){
        //getuiUtils.bindAlias("aliastest1", "e605a0db5ce3cca9b76b012978064940");
    }

    /**
     * 别名推送
     */
    @Test
    public void pushToSingleTest(){
        //getuiUtils.pushToSingle("aliastest1", "测试", "测试");
    }

}
