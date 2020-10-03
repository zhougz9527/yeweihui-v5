package com.yeweihui;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeweihui.modules.operation.entity.NoticeEntity;
import org.junit.Test;

import java.io.IOException;

public class SimpleTest {




    @Test
    public void jsonParse() throws IOException {
        String s = "{\"title\":\"啊\",\"content\":\"<p><img src='https://ywh-hdj.oss-cn-hangzhou.aliyuncs.com/file/20191222/4c4eb1fef7df4a028ecfb174f2f0b687.jpg' width='100%'></p><p><br></p>\",\"noticeMemberEntityList\":[{\"uid\":294}],\"uid\":295,\"zoneId\":1}";

        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, false) ;

        NoticeEntity noticeEntity = mapper.readValue(s, NoticeEntity.class);

        System.out.println(noticeEntity);
    }
}
