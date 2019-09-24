package org.javatribe.lottery.controller;

import org.javatribe.lottery.entity.Result;
import org.javatribe.lottery.service.ILotteryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**lotteryController 处理抽奖
 * @author JimZiSing
 */
@RestController
public class LotteryController {
    @Autowired
    ILotteryService lotteryService;

    @PostMapping("/luck-draw")
    public Result luckDraw(String openid,@RequestParam(required = false,defaultValue = "0")Integer prizeId){
        lotteryService.luckDraw(openid,prizeId);
        return Result.success("参与抽奖成功，请耐心等待开奖");
    }

}