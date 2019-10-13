package org.javatribe.lottery.controller;

import lombok.extern.slf4j.Slf4j;
import org.javatribe.lottery.annotation.NeedToken;
import org.javatribe.lottery.entity.Prize;
import org.javatribe.lottery.entity.PrizeItem;
import org.javatribe.lottery.entity.Result;
import org.javatribe.lottery.enums.ResultEnum;
import org.javatribe.lottery.service.ILotteryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.Callable;

/**
 * lotteryController 处理抽奖
 *
 * @author JimZiSing
 */
@RestController
@Slf4j
public class LotteryController {
    @Autowired
    ILotteryService lotteryService;
    @Autowired
    RedisTemplate redisTemplate;

    @PostMapping("/luck-draw")
    @NeedToken
    public Result luckDraw(String openid, Integer userId,/* @RequestParam(required = false, defaultValue = "0")*/
            Integer prizeId) throws Exception {
        log.info("用户："+userId+" 抽奖");
        //采用异步请求
//        Callable<Result> callable = new Callable<Result>() {
//            @Override
//            public Result call() throws Exception {
//                String result = lotteryService.luckDraw(openid, userId, prizeId);
//                return Result.success(result);
//            }
//        };
//        return callable.call();
        String result = lotteryService.luckDraw(openid, userId, prizeId);
        return Result.success(result);
    }

}