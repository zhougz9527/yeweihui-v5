package com.yeweihui.service.user;

import com.yeweihui.modules.user.service.MpUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MpUserServiceTest {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    MpUserService mpUserService;

    @Test
    public void testPublic() {
        mpUserService.fetchPublicUserInfo();
    }

}
