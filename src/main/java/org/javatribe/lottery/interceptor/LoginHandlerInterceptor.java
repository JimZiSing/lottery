package org.javatribe.lottery.interceptor;

import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.javatribe.lottery.annotation.NeedToken;
import org.javatribe.lottery.config.ParameterServletRequestWrapper;
import org.javatribe.lottery.entity.Result;
import org.javatribe.lottery.utils.HttpUtils;
import org.javatribe.lottery.utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @author JimZiSing
 */
@Component
@Slf4j
public class LoginHandlerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.info("interceptor");
        String uri = request.getRequestURI();
        log.info(uri);
        boolean isDruid = uri.startsWith("/druid");
        if (isDruid) {
            return true;
        }
        //如果不是映射到方法(controller的方法直接通过)
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        //检查是否有needToken注释，有需要认证
        if (method.isAnnotationPresent(NeedToken.class)) {
            NeedToken needToken = method.getAnnotation(NeedToken.class);
            if (needToken.required()) {
                log.info("接口有NeedToken注解， 需要验证token");
                // 从 http 请求头中取出 token
                String token = request.getHeader("token");
                try {
                    Claims claims = JwtUtils.parseJWT(token);
                    // 添加userId
                    ((ParameterServletRequestWrapper) request).addParameter("openid", claims.get("openid"));
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    log.info("未登录或登录过期");
                    try {
                        response.setCharacterEncoding("UTF-8");
                        response.setHeader("content-Type", "text/html;charset=utf-8");
                        response.getWriter().write(JSON.toJSONString(Result.error(40000, "请登录")));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
