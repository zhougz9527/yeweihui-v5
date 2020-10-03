package com.yeweihui.common.log;

import com.yeweihui.common.utils.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Created by cutie on 2018/9/17.
 */
@ControllerAdvice
public class RequestBodyLogHandler implements RequestBodyAdvice {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public Object handleEmptyBody(Object o, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        logger.info(StrUtils.toJson(o));
        return o;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) throws IOException {
//        logger.info(StrUtils.toJson(httpInputMessage));
        return httpInputMessage;
    }

    @Override
    public Object afterBodyRead(Object o, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        logger.info("token:{}, class:[{}], methodName:[{}], body:[{}]",
                httpInputMessage.getHeaders().get("token"), methodParameter.getContainingClass().getName(), methodParameter.getMethod().getName(), StrUtils.toJson(o));
        return o;
    }
}
