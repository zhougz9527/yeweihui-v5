package com.yeweihui.service;


import com.yeweihui.ServiceApplication;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.BeanUtil;
import com.yeweihui.common.utils.StrUtils;
import com.yeweihui.common.vo.BasePageQueryParam;
import com.yeweihui.modules.sys.service.DuqinUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class DuqinUserServiceTest {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DuqinUserService duqinUserService;

    @Test
    public void test(){
        BasePageQueryParam basePageQueryParam = new BasePageQueryParam();
        basePageQueryParam.setPage("1");
        basePageQueryParam.setLimit("10");
        PageUtils pageUtils = duqinUserService.queryPage(BeanUtil.bean2map(basePageQueryParam));
        logger.info("pageUtils:{}", StrUtils.toJson(pageUtils));
    }

}
