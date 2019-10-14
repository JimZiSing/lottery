package org.javatribe.lottery.service.impl;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.javatribe.lottery.entity.*;
import org.javatribe.lottery.mapper.LotteryMapper;
import org.javatribe.lottery.service.ILotteryService;
import org.javatribe.lottery.service.IPrizeService;
import org.javatribe.lottery.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

/**
 * lotteryService 层接口实现
 *
 * @author JimZiSing
 */
@Service
@Slf4j
public class LotteryServiceImpl implements ILotteryService {
    @Autowired
    LotteryMapper lotteryMapper;
    @Autowired
    IPrizeService prizeService;
    @Autowired
    IUserService userService;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    ILotteryService lotteryService;

    private static final Object LOCK = new Object();

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String luckDraw(String openid, Integer userId, Integer prizeId) {
//        PrizeItem prizeItem = (PrizeItem) redisTemplate.boundValueOps("PRIZE:" + prizeId).get();
//        if (prizeItem == null) {
//            prizeItem = prizeService.selectPrizeAndItem(prizeId);
//        }
//        else {
////            prizeItem = JSONObject.parseObject(o.toString(), PrizeItem.class);
//        }
        PrizeItem prizeItem = prizeService.selectPrizeAndItem(prizeId);
        if (System.currentTimeMillis() - prizeItem.getStartTime() < 0
                || prizeItem.getEndTime() - System.currentTimeMillis() < 0) {
            log.info("不在抽奖时间");
            return "不在抽奖时间";
        }
        String prize = (String) redisTemplate.boundValueOps("DRAW:" + prizeId + ":" + userId).get();
        if (null != prize ) {
            log.info("已参与抽奖！");
            return "已参与抽奖！";
        }
        if ((int) redisTemplate.opsForValue().get("PRIZE_TOTAL_AMOUNT:" + prizeId) <= 0) {
            Lottery lottery = new Lottery();
            lottery.setItemId(0);
            lottery.setUserId(userId);
            lottery.setCreateTime(System.currentTimeMillis());
            try {
                lotteryMapper.insertLottery(lottery);
            } catch (Exception e) {
                e.printStackTrace();
            }
            redisTemplate.boundValueOps("DRAW:" + prizeId + ":" + userId).set("很遗憾，本次未中奖"/*,
                    prizeItem.getEndTime() - System.currentTimeMillis()*/);
            log.info("用户:" + userId + " 本次未中奖");
            return "很遗憾，本次未中奖";
        }

        List<Item> items = prizeItem.getItems();
        int size = items.size();
        int total = 0;
        for (int i = 0; i < size; i++) {
            Item item = items.get(i);
            total += item.getAmount();
        }
        Random random = new Random();
        int i = random.nextInt(100);
        long l = openid.hashCode() + System.currentTimeMillis();
        if (Math.abs(l) % total == i % total) {
            Lottery lottery = new Lottery();
            int level = random.nextInt(size);
            Item item = items.get(level);
            synchronized (LOCK) {
                if ((int) redisTemplate.opsForValue().get("PRIZE_TOTAL_AMOUNT:" + prizeId) > 0) {
                    redisTemplate.opsForValue().decrement("PRIZE_TOTAL_AMOUNT:" + prizeId);
                    redisTemplate.boundValueOps("DRAW:" + prizeId + ":" + userId).set(item.getItemName()/*,
                            prizeItem.getEndTime() - System.currentTimeMillis()*/);
                    lottery.setItemId(item.getId());
                    log.info("用户:" + userId + " 获得：" + item.getItemName());
                } else {
                    redisTemplate.boundValueOps("DRAW:" + prizeId + ":" + userId).set("很遗憾，本次未中奖");
                    lottery.setItemId(0);
                    log.info("用户:" + userId + " 本次未中奖");
//                    return "很遗憾，本次未中奖";
                }
            }
            lottery.setUserId(userId);
            lottery.setCreateTime(System.currentTimeMillis());
            try {
                lotteryMapper.insertLottery(lottery);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return lottery.getItemId() == 0 ? "很遗憾，本次未中奖" : "恭喜你获得：" + item.getItemName();
//            return "恭喜你获得：" + item.getItemName();
        }else {
            redisTemplate.boundValueOps("DRAW:" + prizeId + ":" + userId).set("很遗憾，本次未中奖");
            log.info("用户:" + userId + " 本次未中奖");
            return "很遗憾，本次未中奖";
        }
    }
}
