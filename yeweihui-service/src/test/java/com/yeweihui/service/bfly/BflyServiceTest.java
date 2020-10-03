package com.yeweihui.service.bfly;

import com.yeweihui.modules.bfly.service.BflyVoteService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BflyServiceTest {
    @Autowired
    BflyVoteService bflyVoteService;

    @Test
    public void crontabUpdateVoteTest() {
        bflyVoteService.crontabUpdateVote();
    }
}
