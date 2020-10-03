package com.yeweihui.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TxImHttpUtilsTest {

    @Test
    public void testTxImAccountImport() {
        String url = "https://console.tim.qq.com/v4/im_open_login_svc/account_import?usersig=eJxlz11PgzAUgOF7fkXT2xltC4XgXR3uS0dsQAncNJV2rsEx7OocMf53I2ok8dw*78nJefcAADC-zc5lXe9fWydc32kILgFE8OwPu84oIZ3wrfqH*tQZq4XcOG0HxJRSgtC4MUq3zmzMTyHVzrQjPqhGDDe*9wOEcBz6UTBOzNOA6*tyuuTJKsXNfBs-tpTJqlRZf-U8ebCViaoqmCaTGYtw3vd57hq23LKUk2NmOYvpQmNEwnVSnFxZ8x7d3c-rGb95Sd9WiyJPL4rRSWd2*vehAOEgpGSkR20PZt8OAUGYYuKjr4Heh-cJiWtb0A__&apn=1&identifier=admin&sdkappid=1400196374&contenttype=json";
        JSONObject obj = JSONObject.parseObject("{\n" +
                "   \"Identifier\":\"test1\",\n" +
                "   \"Nick\":\"帅哥\",\n" +
                "   \"FaceUrl\":\"http://www.qq.com\"\n" +
                "}");
        String result = httpPost(url, obj);
        System.out.println(result);
    }

    /**
     * 以json的形式提交数据
     * @param url
     * @param obj
     * @return
     */
    public static String httpPost(String url, JSONObject obj) {
        String _encoding = "UTF-8";
        StringBuffer sb = new StringBuffer();
        BufferedReader in = null;

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        try {
            StringEntity entity = new StringEntity(obj.toString(),"utf-8");//解决中文乱码问题
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            post.setEntity(entity);
            System.out.println("getRequestLine:" + post.getRequestLine());

            // 执行请求
            HttpResponse response = client.execute(post);
            in = new BufferedReader(new InputStreamReader(response.getEntity()
                    .getContent(), _encoding));
            String line = "";
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line + NL);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e1) {
                }
            }
            // 关闭连接.
            try {
                client.getConnectionManager().shutdown();
            } catch (Exception e1) {
            }
        }
        String str = "";
        try {
            str = sb.toString();
        } catch (Exception e) {

        }
        return str;
    }

}
