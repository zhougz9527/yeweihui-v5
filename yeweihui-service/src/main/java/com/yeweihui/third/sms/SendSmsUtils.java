package com.yeweihui.third.sms;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import org.junit.Test;
import org.springframework.stereotype.Component;
/*
pom.xml
<dependency>
  <groupId>com.aliyun</groupId>
  <artifactId>aliyun-java-sdk-core</artifactId>
  <version>4.0.3</version>
</dependency>
*/

/*
AccessKeyId	                               AccessKeySecret
LTAI4FrKZx5CEuMeSJjrZAPb	xJL1c6UXyihvhhNtivqDnoSu9oTm59
https://api.aliyun.com/new?spm=a2c4g.11186623.2.13.55a219d96Zeebg#/?product=Dysmsapi&api=SendSms&params={"RegionId":"cn-hangzhou","PhoneNumbers":"18626871943","SignName":"维心网络","TemplateCode":"SMS_175574332","TemplateParam":"{\"verifyCode\",%20\"111111\"}","SmsUpExtendCode":"1","OutId":"1"}&tab=DOC&lang=JAVA
*/
@Component
public class SendSmsUtils {

    @Test
    public void test() {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAI4FrKZx5CEuMeSJjrZAPb",
                "xJL1c6UXyihvhhNtivqDnoSu9oTm59");

        IAcsClient client = new DefaultAcsClient(profile);
    }

    /**
     * 根据模板id和模板参数给指定的手机号发送短信
     * 1、验证码模板 SMS_175574332     您的验证码为${verifyCode}，五分钟后失效，请妥善保管
     * 2、通知bd新用户申请小区 SMS_175539736  ${plat}收到新客户的开通申请，请尽快联系。客户姓名：${name}；电话：${phone}；小区名字：${zoneName}；小区地址：${address}。
     * @param phoneNumbers
     * @param templateCode
     * @param templateParam
     */

    public void sendSmsByTem(String phoneNumbers, String templateCode, String templateParam){
            DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAI4FrKZx5CEuMeSJjrZAPb",
                    "xJL1c6UXyihvhhNtivqDnoSu9oTm59");
            IAcsClient client = new DefaultAcsClient(profile);

            CommonRequest request = new CommonRequest();
            request.setMethod(MethodType.POST);
            request.setDomain("dysmsapi.aliyuncs.com");
            request.setVersion("2017-05-25");
            request.setAction("SendSms");
            request.putQueryParameter("RegionId", "cn-hangzhou");
            request.putQueryParameter("PhoneNumbers", phoneNumbers);
            request.putQueryParameter("SignName", "维心网络");
            request.putQueryParameter("TemplateCode", templateCode);
            request.putQueryParameter("TemplateParam", templateParam);
            //            request.putQueryParameter("SmsUpExtendCode", "1");
            //            request.putQueryParameter("OutId", "1");
            try {
                CommonResponse response = client.getCommonResponse(request);
                System.out.println(response.getData());
            } catch (ServerException e) {
                e.printStackTrace();
            } catch (ClientException e) {
                e.printStackTrace();
            }
    }

    /**
     * 根据模板id给指定的手机号发送短信
     * 通知管理员进行认证模版：SMS_191818114
     * 通知业主认证通过模版：SMS_191818116
     * 通知业主认证失败模版：SMS_191833074
     * 通知业主投票还有24小时结束模版：SMS_194051130
     * @param phoneNumbers
     * @param templateCode
     */

    public void sendSmsNoParam(String phoneNumbers, String templateCode){
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAI4FrKZx5CEuMeSJjrZAPb",
                "xJL1c6UXyihvhhNtivqDnoSu9oTm59");
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phoneNumbers);
        request.putQueryParameter("SignName", "蝴蝶投票");
        request.putQueryParameter("TemplateCode", templateCode);
        //            request.putQueryParameter("SmsUpExtendCode", "1");
        //            request.putQueryParameter("OutId", "1");
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args) {
//        SendSmsByTemBO sendSmsByTemBO = new SendSmsByTemBO();
//        sendSmsByTemBO.setAddress("地址");
//        sendSmsByTemBO.setName("客户姓名");
//        sendSmsByTemBO.setPhone("18758363327");
//        sendSmsByTemBO.setZoneName("小区名称");
//        sendSmsByTemBO.setPlat(PlatEnum.getName(1));
//        new SendSmsUtils().sendSmsByTem("17710505670", "SMS_175539736", StrUtils.toJson(sendSmsByTemBO));

//        new SendSmsUtils().sendSmsByTem("17710505670", "SMS_175574332", "{\"verifyCode\":\"123\"}");
        testNoParam();

    }

    private static void testNoParam() {
        /**
         * 根据模板id给指定的手机号发送短信
         * 通知管理员进行认证模版：SMS_191818114
         * 通知业主认证通过模版：SMS_191818116
         * 通知业主认证失败模版：SMS_191833074
         //     * @param phoneNumbers
         //     * @param templateCode
         */

        new SendSmsUtils().sendSmsNoParam("17710505670","SMS_191818114");

        }

}