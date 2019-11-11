package org.javatribe.lottery.service;


import org.javatribe.lottery.entity.PrizeItem;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * lotteryService 层接口 处理抽奖逻辑
 * @author JimZiSing
 */
public interface ILotteryService {
    /**
     * 抽奖方法,没在本次抽奖中使用
     * @param openid
     * @param prizeId
     * @param prizeItem
     * @return
     */
    String luckDraw(String openid, Integer prizeId, PrizeItem prizeItem);

    /**
     * 抽奖业务逻辑
     * @param openid
     * @param prizeId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    Integer draw(String openid, Integer prizeId);

    /**
     * 判断用户是否抽过奖
     * @param openid
     * @return
     */
    boolean isDraw(String openid);

    /**
     * 向数据库写入抽奖结果
     */
    @Async("taskExecutor")
    void addLotteryList();
}
