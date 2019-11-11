package org.javatribe.lottery.controller;

import lombok.extern.slf4j.Slf4j;
import org.javatribe.lottery.annotation.NeedToken;
import org.javatribe.lottery.entity.Result;
import org.javatribe.lottery.entity.User;
import org.javatribe.lottery.service.IUserService;
import org.javatribe.lottery.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.Callable;

/**
 * @author JimZiSing
 */
@RestController
@Slf4j
public class UserController {

    @Autowired
    IUserService userService;

    @GetMapping("api/user")
    @NeedToken
    public Result getUserInfo(String openid){
        log.info("用户登录，openid为："+ openid);
        User user = userService.selectUserByOpenid(openid);
        return Result.success(user);
    }

    /**
     * 根据ip绑定用户
     * @return
     */
    @GetMapping("/loading")
    @NeedToken
    public Result loading(HttpServletRequest request) throws Exception {
        Callable<Result> callable = () -> {
            String realIp = HttpUtils.getRealIp(request);
            String openid = userService.bindUserWithIp(realIp);
            return Result.success(openid);
        };
        return callable.call();
    }
}
