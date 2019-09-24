package org.javatribe.lottery.service.impl;

import org.javatribe.lottery.entity.Lottery;
import org.javatribe.lottery.entity.Prize;
import org.javatribe.lottery.entity.PrizeItem;
import org.javatribe.lottery.mapper.LotteryMapper;
import org.javatribe.lottery.service.ILotteryService;
import org.javatribe.lottery.service.IPrizeService;
import org.javatribe.lottery.utils.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * lotteryService 层接口实现
 * @author JimZiSing
 */
@Service
public class LotteryServiceImpl implements ILotteryService {
    @Autowired
    LotteryMapper lotteryMapper;
    @Autowired
    IPrizeService prizeService;

    @Override
    public void luckDraw(String openid, Integer prizeId) {

        PrizeItem prizeItem = prizeService.selectPrizeAndItem(prizeId);
        Lottery lottery = new Lottery();
        lottery.setPrizeId(prizeItem.getId());
        lottery.setCreateTime(System.currentTimeMillis());
        lotteryMapper.insertLottery(lottery);
    }
}
