package org.javatribe.lottery.service;


/**
 * lotteryService 层接口 处理抽奖逻辑
 * @author JimZiSing
 */
public interface ILotteryService {
    /**
     * 抽奖方法
     * @param openid
     * @param prizeId
     * @param id
     * @return
     */
    String luckDraw(String openid, Integer prizeId, Integer id);
}
