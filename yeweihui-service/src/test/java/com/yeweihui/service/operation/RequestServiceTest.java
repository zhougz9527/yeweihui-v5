package com.yeweihui.service.operation;


import com.yeweihui.common.utils.BeanUtil;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.StrUtils;
import com.yeweihui.common.vo.BasePageQueryParam;
import com.yeweihui.modules.operation.service.RequestService;
import com.yeweihui.modules.sys.service.DuqinUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class RequestServiceTest {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RequestService requestService;

    @Test
    @Rollback(false)
    public void expireRequestTest(){
        requestService.expireRequest();
    }

}
