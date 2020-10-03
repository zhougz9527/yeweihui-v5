package com.yeweihui.service.operation;


import com.yeweihui.modules.operation.service.VoteService;
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
public class VoteServiceTest {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private VoteService voteService;

    @Test
    @Rollback(false)
    public void expireVoteTest(){
        voteService.expireVote();
    }

}
