package com.yeweihui.utils;

import com.alibaba.fastjson.JSONObject;
import com.github.promeg.pinyinhelper.Pinyin;
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

@RunWith(SpringRunner.class)
@SpringBootTest
public class PinyinUtilsTest {

    @Test
    public void toPinyin1Test() {
        /**
         * 如果c为汉字，则返回大写拼音；如果c不是汉字，则返回String.valueOf(c)
         */
        String result1 = Pinyin.toPinyin('周');
        String result2 = Pinyin.toPinyin('z').toUpperCase();
        System.out.println(result1 + "," + result2);
    }

    @Test
    public void isChineseTest() {
        /**
         * c为汉字，则返回true，否则返回false
         */
        boolean result1 = Pinyin.isChinese('周');
        boolean result2 = Pinyin.isChinese('z');
        System.out.println(result1 + "," + result2);
    }


    @Test
    public void toPinyin2Test() {
        /**
         * 将输入字符串转为拼音，转换过程中会使用之前设置的用户词典，以字符为单位插入分隔符
         */
        String result = Pinyin.toPinyin("周杰伦", ",");
        System.out.println(result);
        System.out.println(result.substring(0, 1));

    }

    @Test
    public void testPost() {
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
