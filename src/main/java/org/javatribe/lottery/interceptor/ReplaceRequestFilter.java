package org.javatribe.lottery.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.javatribe.lottery.config.ParameterServletRequestWrapper;
import org.javatribe.lottery.utils.HttpUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * 在过滤器里替换掉request为自定义的ParameterServletRequestWrapper，使request可以修改参数
 *
 * @author JimZiSing
 */
//@Configuration
//@WebFilter(filterName = "replaceRequest", urlPatterns = "/**")
@Slf4j
public class ReplaceRequestFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("在过滤器里替换掉request为自定义的request");
        Map<String, String[]> parameterMap = new HashMap<>(request.getParameterMap());
        // 创建包装类
        ParameterServletRequestWrapper req = new ParameterServletRequestWrapper(request, parameterMap);
        // 替换request为自己的包装类
        super.doFilter(req, response, filterChain);
    }

}