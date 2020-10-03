package com.yeweihui.third.pay;

import com.egzosn.pay.ali.api.AliPayConfigStorage;
import com.egzosn.pay.ali.api.AliPayService;
import com.egzosn.pay.ali.bean.AliTransactionType;
import com.egzosn.pay.common.api.PayService;
import com.egzosn.pay.common.bean.MethodType;
import com.egzosn.pay.common.bean.PayOrder;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *
 *
 * 支付宝测试
 * @author egan
 * @email egzosn@gmail.com
 * @date 2017/8/18
 */
public class AliPayTest {

    public static void main(String[] args) {

        AliPayConfigStorage aliPayConfigStorage = new AliPayConfigStorage();
        /*aliPayConfigStorage.setPid("合作者id");
        aliPayConfigStorage.setAppId("应用id");
        aliPayConfigStorage.setKeyPublic("支付宝公钥");
        aliPayConfigStorage.setKeyPrivate("应用私钥");
        aliPayConfigStorage.setNotifyUrl("异步回调地址");
        aliPayConfigStorage.setReturnUrl("同步回调地址");
        aliPayConfigStorage.setSignType("签名方式");
        aliPayConfigStorage.setSeller("收款账号");
        aliPayConfigStorage.setInputCharset("utf-8");*/
//        aliPayConfigStorage.setPid("2088102169229565");
        aliPayConfigStorage.setAppId("2016073100129646");
        aliPayConfigStorage.setKeyPublic("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzyp2nH1G56p1VqkYqxiWV0wVTBh7HC3tQz2bGEx/+LbfGviWEJVKPj90rFd8rXuIXV6G81DaHwUDoDgDTrues7mHvZYh0+rQ3OGZ/HwqPjDvjJTKB7YZGdrS7pxKkLxSr5bwTNwoJPG0pet5kV3cCTIWda0bYaTK/jpQpAIzW503h5c1gIUFjyezNNnrX0ChFwcnch7DTMsnvKoF0ALtwVL6pegCDx0NjH4VGCSeMddlirlIKPAGWDLjqpxqQtpjEViKd2pbbMsBYkZhLnBeDifzqvX3WCz0a3+vAZ0e3ufudMl/YiYiPt1rFXSOsOh8l2Ny5U2OPsI2CFgP1qsEKwIDAQAB");
        aliPayConfigStorage.setKeyPrivate("MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCeiN4FfEJ5WMZn6P1wvX1yARoUGnTGH7mOO6o5d8VESCMPYBdtmLoW7mFpGZZaxRy/iuNlzhtkoqssIlWMPwusEZDeEDOWOy6jNG8+P0Wl+jQ+6MriTeZxuYCF0IOiANtNM9u76zcMyH/UQyFBrjhkPaXNgn3bxkn9kTUus0jF/TsNrR1XMoXbcaW1UVpG9Tjc/0J6XCxoLf1a9YwSEAJQXkKVg2TisfOkiDQFkHUPLpRognm9NdYuxqIKfrte+QILu7yBOvqFQza5zH2Jovo33a4GQ53gEmzha7byOHfoIngLzGTkbW9OcS17N+3/Kgb4hSc8kuN8NULFF9UBLXuHAgMBAAECggEAc7wveYmxba0liw5Il1l+jw/G3g5CWbEnYXKlXrbVSHfxQHRCJjxs/fihHH0VisaDqkHWqKfCz8EBfYLDOVN9Tey0+aiSQIaMEZFh9Ot0CSuToKmKE5QTO8rUzNvgfRBrCP1m2NVqpuZoi6lGShDKEkP/Gvy8rQkhUwAC90jjQ8Xm6u6Cwije1WHXRfWgU5PlhKNIaBraOH1hkjSf9hr04J0qMB5BjHEIaD9gtMGN+02xpKwcqzuMgsK2N6SaGhu1fjaVfAJco5Uz53g7coAXw51gqlPdPYKzv1+dZRiZyL7zibfZk7QUrTgCNiF6q2xQM636kMdYiRjNvGVukly1AQKBgQDfoXHz3dVAc0U9D+zCfz3NkHfC2O9i1QU2DMjasPXneGSq43W3eCl4tnR393cxOeC3HSyc9x+rLfK48c7urtDOIWU2bxEapUSGlq52I9lcmT9GhDCF4HgMoFV7IEzj1TRHNwzja9cTgIdWVufQY5vDsjTDvMN33Iq5S7YEzWH14QKBgQC1e0//14SEyk1YJT+/DEpHqQc/MJJLUpw8chOuxmLhUKIwhtavOJChUea9mGnahsHc8G2MWF/LmSKKHoO+OgZYdhO6Fmb0dEOmoXU1a60CYtAfKvu+2Rl7JYX5XDcNDqrVlkFA/Ix367lYMJmQ6+3UJqBGDjcA3RtQqhl6HvlOZwKBgQC7RdXJpQGKgX2ADy/0CN5WoR7JmyWNHFrK0pX5Hawv9RBIU0cnJ4eDQTfzpta38IIQlmvf3ss0bLwQyN7D3r2ZgTnjBcvLBo+GLfVXldnWw7bLdLO1yS5/VF2BSKFbQhYPdThj0tE/0aiEUy65wlCU501shU0juRqq5Y4+I0hfwQKBgFlmMXZ6mFFBxvARCWH3j+/VGDjMmIR6YXR1TbTfDX6o1fzJr6AJYVXWnq/vENYRZBaOaailMCt/eEBJ3+QpeU7Ce91Dg9wQGfNPvglezY9LVfLEndbZ42+CgIjiIExSWV118xcsUZYRjPAgoMV/M1062eEox3epc/c8k+hQsccpAoGBAIs50AyCPesdldCP/JqafLsVn0Elajiyp3OdC2Yvud7aEMUYWNtJS9ceYPBSeNKMgROxdyqCBk50Sn3dro9kP8fSpkDGe+jCobxsDf254f07+ZAof/u3MGk/EGXRoR5gjlO3ceUmpKskZ+qk99tHBXun2X+mIy399kFtcPk5H66c");
        aliPayConfigStorage.setNotifyUrl("http://gpetadmin.gchongpet.com/pay/alipayResult");
//        aliPayConfigStorage.setReturnUrl("同步回调地址");
        aliPayConfigStorage.setSignType("RSA2");
//        aliPayConfigStorage.setSeller("ieokqi2935@sandbox.com");
        aliPayConfigStorage.setInputCharset("utf-8");
        //是否为测试账号，沙箱环境
        aliPayConfigStorage.setTest(true);
        //支付服务
        PayService service = new AliPayService(aliPayConfigStorage);
        //支付订单基础信息
        PayOrder payOrder = new PayOrder("订单title", "摘要",  new BigDecimal(0.01) , UUID.randomUUID().toString().replace("-", ""));
        /*-----------扫码付-------------------*/
        payOrder.setTransactionType(AliTransactionType.SWEEPPAY);
        //获取扫码付的二维码
//        BufferedImage image = service.genQrPay(payOrder);
        /*-----------/扫码付-------------------*/

        /*-----------APP-------------------*/
        payOrder.setTransactionType(AliTransactionType.APP);
        //获取APP支付所需的信息组，直接给app端就可使用
        Map appOrderInfo = service.orderInfo(payOrder);
//        String sign = (String) appOrderInfo.get("sign");
//        appOrderInfo.remove("sign");
        String appOrderInfoStr = buildOrderParam(appOrderInfo);
//        String orderInfo = appOrderInfoStr + "&" + sign;
        System.out.println(appOrderInfoStr);
        /*-----------/APP-------------------*/

        /*-----------即时到帐 WAP 网页支付-------------------*/
//        payOrder.setTransactionType(AliTransactionType.WAP); //WAP支付

        payOrder.setTransactionType(AliTransactionType.DIRECT); // 即时到帐 PC网页支付
        //获取支付所需的信息
        Map directOrderInfo = service.orderInfo(payOrder);
        //获取表单提交对应的字符串，将其序列化到页面即可,
        String directHtml = service.buildRequest(directOrderInfo, MethodType.POST);
        /*-----------/即时到帐 WAP 网页支付-------------------*/



        /*-----------条码付 声波付-------------------*/

//        payOrder.setTransactionType(AliTransactionType.WAVE_CODE); //声波付
        payOrder.setTransactionType(AliTransactionType.BAR_CODE);//条码付

        payOrder.setAuthCode("条码信息或者声波信息");
        // 支付结果
        Map params = service.microPay(payOrder);

        /*-----------/条码付 声波付-------------------*/

        /*-----------回调处理-------------------*/
//        HttpServletRequest request
//        params = service.getParameter2Map(request.getParameterMap(), request.getInputStream());
        if (service.verify(params)){
            System.out.println("支付成功");
            return;
        }
        System.out.println("支付失败");


        /*-----------回调处理-------------------*/
    }

    /**
     * 构造支付订单参数信息
     *
     * @param map
     * 支付订单参数
     * @return
     */
    public static String buildOrderParam(Map<String, String> map) {
        List<String> keys = new ArrayList<String>(map.keySet());

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keys.size() - 1; i++) {
            String key = keys.get(i);
            String value = map.get(key);
            sb.append(buildKeyValue(key, value, true));
            sb.append("&");
        }

        String tailKey = keys.get(keys.size() - 1);
        String tailValue = map.get(tailKey);
        sb.append(buildKeyValue(tailKey, tailValue, true));

        return sb.toString();
    }

    /**
     * 拼接键值对
     *
     * @param key
     * @param value
     * @param isEncode
     * @return
     */
    private static String buildKeyValue(String key, String value, boolean isEncode) {
        StringBuilder sb = new StringBuilder();
        sb.append(key);
        sb.append("=");
        if (isEncode) {
            try {
                sb.append(URLEncoder.encode(value, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                sb.append(value);
            }
        } else {
            sb.append(value);
        }
        return sb.toString();
    }
}
