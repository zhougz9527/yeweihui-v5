package com.yeweihui.common.log;

import com.yeweihui.common.utils.IPUtils;
import com.yeweihui.common.utils.StrUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Configuration
public class ParamLogHandler {
    private static final Logger logger = LoggerFactory.getLogger(ParamLogHandler.class);

    //一个是admin，一个是api
    @Pointcut("execution(* com.yeweihui.modules.*.controller..*.*(..)) || execution(* com.yeweihui.controller..*.*(..))")
    //todo: post表单参数有问题 找时间换一个
    public void excudeService() {

    }

    @Around("excudeService()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();

        String contentType =  request.getContentType();
            String ipAddr = IPUtils.getIpAddr(request);
        String url = request.getRequestURL().toString();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        Object[] args = pjp.getArgs();
        String params = "";
        //获取请求参数集合并进行遍历拼接
        /*if(args.length>0){
            //todo: post表单参数有问题 数组获取
            if("POST".equals(method) && !contentType.equals("application/x-www-form-urlencoded; charset=UTF-8")){
                Object object = args[0];
                Map map = getKeyAndValue(object);
                params = StrUtils.toJson(map);
            }else if("GET".equals(method)){
                params = queryString;
            }
        }*/

        //可以添加ip，
        logger.info("ipAddr:[{}] request : uri:[{}], method:[{}], params:[{}]", ipAddr, uri, method, params);

        Object result = pjp.proceed();
//        logger.info("response : [{}]", StrUtils.toJson(result));
        return result;
    }

    public static Map<String, Object> getKeyAndValue(Object obj) {
        Map<String, Object> map = new HashMap<>();
        // 得到类对象
        Class userCla = (Class) obj.getClass();
        /* 得到类中的所有属性集合 */
        Field[] fs = userCla.getDeclaredFields();
        for (int i = 0; i < fs.length; i++) {
            Field f = fs[i];
            f.setAccessible(true);
            Object val = new Object();
            try {
                val = f.get(obj);
                map.put(f.getName(), val);
            } catch (IllegalArgumentException e) {
                logger.error(e.getMessage(), e);
            } catch (IllegalAccessException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return map;
    }
}
