package com.yeweihui.third.pay;

import com.egzosn.pay.common.api.PayService;
import com.egzosn.pay.common.bean.MethodType;
import com.egzosn.pay.common.bean.PayOrder;
import com.egzosn.pay.wx.api.WxPayConfigStorage;
import com.egzosn.pay.wx.api.WxPayService;
import com.egzosn.pay.wx.bean.WxTransactionType;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

/**
 *
 *
 * 微信
 * @author egan
 * @email egzosn@gmail.com
 * @date 2017/8/18
 */
public class WxPayTest {

    public static void main(String[] args) {
        WxPayConfigStorage wxPayConfigStorage = new WxPayConfigStorage();
        /*wxPayConfigStorage.setMchId("合作者id（商户号）");
        wxPayConfigStorage.setAppid("应用id");
        wxPayConfigStorage.setKeyPublic("密钥");
        wxPayConfigStorage.setKeyPrivate("密钥");
        wxPayConfigStorage.setNotifyUrl("异步回调地址");
        wxPayConfigStorage.setReturnUrl("同步回调地址");
        wxPayConfigStorage.setSignType("签名方式");
        wxPayConfigStorage.setInputCharset("utf-8");*/

        wxPayConfigStorage.setMchId("1508574411");
        wxPayConfigStorage.setAppid("wxf07b72358766d329");
        wxPayConfigStorage.setKeyPublic("密钥");
        wxPayConfigStorage.setKeyPrivate("ganimal13123901502ganimal1881688");
        wxPayConfigStorage.setNotifyUrl("gpetadmin.gchongpet.com/pay/unifiedOrder/callback");
//        wxPayConfigStorage.setReturnUrl("同步回调地址");
        wxPayConfigStorage.setSignType("HMAC-SHA256");
        wxPayConfigStorage.setInputCharset("utf-8");

        //是否为测试账号，沙箱环境 此处暂未实现
        wxPayConfigStorage.setTest(false);
        //支付服务
        PayService service =  new WxPayService(wxPayConfigStorage);
        //支付订单基础信息
        PayOrder payOrder = new PayOrder("订单title", "摘要",  new BigDecimal(0.01) , UUID.randomUUID().toString().replace("-", ""));
        /*-----------扫码付-------------------*/
//        payOrder.setTransactionType(WxTransactionType.NATIVE);
        //获取扫码付的二维码
//        BufferedImage image = service.genQrPay(payOrder);
        /*-----------/扫码付-------------------*/

        /*-----------APP-------------------*/
        payOrder.setTransactionType(WxTransactionType.APP);
        //获取APP支付所需的信息组，直接给app端就可使用
        Map appOrderInfo = service.orderInfo(payOrder);
        /*-----------/APP-------------------*/

        /*----------- WAP 网页支付-------------------*/

        payOrder.setTransactionType(WxTransactionType.MWEB); //  网页支付
        //获取支付所需的信息
        Map directOrderInfo = service.orderInfo(payOrder);
        //获取表单提交对应的字符串，将其序列化到页面即可,
        String directHtml = service.buildRequest(directOrderInfo, MethodType.POST);
        /*-----------/ WAP 网页支付-------------------*/



        /*-----------条码付 刷卡付-------------------*/
        payOrder.setTransactionType(WxTransactionType.MICROPAY);//条码付
        payOrder.setAuthCode("条码信息");
        // 支付结果
        Map params = service.microPay(payOrder);

        /*-----------/条码付 刷卡付-------------------*/

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
}
