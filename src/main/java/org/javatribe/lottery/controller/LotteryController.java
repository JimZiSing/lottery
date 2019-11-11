package org.javatribe.lottery.controller;

import lombok.extern.slf4j.Slf4j;
import org.javatribe.lottery.annotation.NeedToken;
import org.javatribe.lottery.entity.PrizeItem;
import org.javatribe.lottery.entity.Result;
import org.javatribe.lottery.enums.ResultEnum;
import org.javatribe.lottery.service.ILotteryService;
import org.javatribe.lottery.service.IPrizeService;
import org.javatribe.lottery.utils.LotteryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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
    IPrizeService prizeService;

    @PostMapping("/luck-draw/{userId}")
    public Result luckDraw(@PathVariable String userId/*,@PathVariable Integer prizeId, Integer userId*/) throws Exception {
        //采用异步请求
        Callable<Result> callable = () -> {
            if (lotteryService.isDraw(userId)) {
                return Result.success(200, LotteryUtils.lotteryMap.get(userId), "已参与抽奖");
            }
            Integer result = lotteryService.draw(userId, 1);
            return Result.success(result);
        };
        return callable.call();
    }


    /**
     * 判断用户是否已经抽奖
     *
     * @return
     */
    @PostMapping("/is-draw")
    public Result isDraw(String userId) throws Exception {
        Callable<Result> callable = () -> {
            if (lotteryService.isDraw(userId)) {
                return Result.success(200, LotteryUtils.lotteryMap.get(userId), "已参与抽奖");
            }
            return Result.success("还没参与抽奖");
        };
        return callable.call();
    }

    @GetMapping("/draw-result/{openid}")
    public Result getDrawResult(@PathVariable String openid) {
        return Result.success();
    }

    /**
     * 获取所有的中奖结果
     * @return
     */
    @PostMapping("/allDraw")
    public Result getAllDraw() {
        return Result.success(LotteryUtils.drawMap);
    }
}