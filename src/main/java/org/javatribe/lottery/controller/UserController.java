package org.javatribe.lottery.controller;

import lombok.extern.slf4j.Slf4j;
import org.javatribe.lottery.annotation.NeedToken;
import org.javatribe.lottery.entity.Result;
import org.javatribe.lottery.entity.User;
import org.javatribe.lottery.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/lottery")
    @NeedToken
    public Result lotteryDraw(String openid){
        System.out.println(System.currentTimeMillis());
        return Result.success();
    }
}
