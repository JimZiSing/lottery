package org.javatribe.lottery.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author Jimzising
 * @Date 2019/10/18
 * @Desc 处理跨域问题
 */
@Configuration
@Component
@WebFilter(filterName = "corsFilter", urlPatterns = "/*")
@Slf4j
public class CorsFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        log.info("允许跨域处理");
        String origin = request.getHeader("Origin");
        if(origin == null) {
            origin = request.getHeader("Referer");
        }
//        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Origin", origin);
        // 允许跨域的Http方法
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE");
        response.addHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type,"
                + " Accept,Access-Control-Request-Method,Access-Control-Request-Headers,token");
        // 设置允许cookie
        response.addHeader("Access-Control-Allow-Credentials", "true");
        // 本次预检请求的有效期为3000秒
        response.setHeader("Access-Control-Max-Age", "3600");

        filterChain.doFilter(request, response);
    }

}
