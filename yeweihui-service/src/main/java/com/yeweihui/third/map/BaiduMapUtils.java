package com.yeweihui.third.map;


import com.alibaba.fastjson.JSONObject;
import com.yeweihui.common.exception.RRException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by cutie on 2018/7/8.
 */
@Component
public class BaiduMapUtils {
    private final Logger logger= LoggerFactory.getLogger(this.getClass());

    /*我的百度地图sk*/
//    private static final String ak = "oGLftzxzu43lNIr1Y7nbWpVW4KxOLI0G";
    private static final String ak = "8ClNzqfDwL5FpCx7Nx51rlGN6cu1mqtd";


    /**
     * 根据地址获取经纬度
     * @param addr
     * @return
     * @throws IOException
     */
    public String[] getCoordinate(String addr){
        String lng = null;//经度
        String lat = null;//纬度
        String address = null;
        try {
            address = java.net.URLEncoder.encode(addr, "UTF-8");
        }catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
            throw new RRException(e.getMessage());
        }
        //System.out.println(address);
        String url = "http://api.map.baidu.com/geocoder/v2/?output=json&ak="+ ak +"&address="+address;
        URL myURL = null;

        URLConnection httpsConn = null;
        try {
            myURL = new URL(url);
        } catch (MalformedURLException e) {
           logger.error(e.getMessage(), e);
           throw new RRException(e.getMessage());
        }
        InputStreamReader insr = null;
        BufferedReader br = null;
        try {
            httpsConn = (URLConnection) myURL.openConnection();
            if (httpsConn != null) {
                insr = new InputStreamReader( httpsConn.getInputStream(), "UTF-8");
                br = new BufferedReader(insr);
                String data = null;
                while((data= br.readLine())!=null){
                    JSONObject json = JSONObject.parseObject(data);
                    if(null != json.getJSONArray("results") && json.getJSONArray("results").size() == 0){
                        throw new RRException("找不到填写的地址信息");
                    }
                    lng = json.getJSONObject("result").getJSONObject("location").getString("lng");
                    lat = json.getJSONObject("result").getJSONObject("location").getString("lat");
                }
            }
        } catch (IOException e) {
           logger.error(e.getMessage(), e);
           throw new RRException(e.getMessage());
        } finally {
            if(insr!=null){
                try {
                    insr.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                    throw new RRException(e.getMessage());
                }
            }
            if(br!=null){
                try {
                    br.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                    throw new RRException(e.getMessage());
                }
            }
        }
        return new String[]{lng,lat};
    }

    /**
     * 根据经纬度获取地址
     * @param lng
     * @param lat
     * @return
     * @throws IOException
     */
    public String[] getAddr(String lng,String lat) {
        String url = "http://api.map.baidu.com/geocoder/v2/?output=json&ak="+ ak +"&location="+lat+","+lng;
        URL myURL = null;
        String province = "";
        String city = "";
        String district = "";
        URLConnection httpsConn = null;
        try {
            myURL = new URL(url);
        } catch (MalformedURLException e) {
           logger.error(e.getMessage(), e);
           throw new RRException(e.getMessage());
        }
        InputStreamReader insr = null;
        BufferedReader br = null;
        try {
            httpsConn = (URLConnection) myURL.openConnection();
            if (httpsConn != null) {
                insr = new InputStreamReader( httpsConn.getInputStream(), "UTF-8");
                br = new BufferedReader(insr);
                String data = null;
                while((data= br.readLine())!=null){
                    JSONObject json = JSONObject.parseObject(data);
                    province = json.getJSONObject("result").getJSONObject("addressComponent").getString("province");
                    city = json.getJSONObject("result").getJSONObject("addressComponent").getString("city");
                    district= json.getJSONObject("result").getJSONObject("addressComponent").getString("district");
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new RRException(e.getMessage());
        } finally {
            if(insr!=null){
                try {
                    insr.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                    throw new RRException(e.getMessage());
                }
            }
            if(br!=null){
                try {
                    br.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                    throw new RRException(e.getMessage());
                }
            }
        }
        return new String[]{province,city,district};
    }

}
