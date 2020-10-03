package com.yeweihui.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;

/**
 * Created by admin on 2015/5/15.
 */
public class StrUtils {

    private static final Gson gson = new Gson();
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * 字符串转换unicode
     */
    public static String string2Unicode(String string) {
        StringBuffer unicode = new StringBuffer();
        for (int i = 0; i < string.length(); i++) {
            // 取出每一个字符
            char c = string.charAt(i);
            // 转换为unicode
            unicode.append("\\u" + Integer.toHexString(c));
        }
        return unicode.toString();
    }

    /**
     * unicode 转字符串
     */
    public static String unicode2String(String unicode) {
        StringBuffer string = new StringBuffer();
        String[] hex = unicode.split("\\\\u");
        for (int i = 1; i < hex.length; i++) {
            // 转换出每一个代码点
            int data = Integer.parseInt(hex[i], 16);
            // 追加成string
            string.append((char) data);
        }
        return string.toString();
    }

    public static String encode(String origin) {
        try {
            return URLEncoder.encode(origin, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String join(Collection collection, String split) {
        if (collection == null) {
            return null;
        }
        String result = "";
        for (Object obj : collection) {
            if (obj != null) {
                result += obj;
            }
        }
        return result;
    }

    public static String toJson(Object bean) {
        return gson.toJson(bean);
    }

    public static <T>T fromJson(String json, Class<T> clazz){
        return gson.fromJson(json,clazz);
    }

    public static String json(Object bean) {
        try {
            return mapper.writeValueAsString(bean);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 替换指定位置字符串
     * replace(3,7,18758363327, ****)
     * 18758363327 - 187****3327
     * @param start
     * @param end
     * @param oldStr
     * @param newStr
     * @return
     */
    public static String replace(int start, int end, String oldStr, String newStr){
        return new StringBuilder(oldStr).replace(start, end, newStr).toString();
    }

}
