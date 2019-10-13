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
    StringRedisTemplate stringRedisTemplates;

    private static final Object LOCK = new Object();

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String luckDraw(String openid, Integer userId, Integer prizeId) {
        Lottery lottery = new Lottery();
        PrizeItem prizeItem = (PrizeItem) redisTemplate.boundValueOps("PRIZE:" + prizeId).get();
        if (prizeItem == null) {
            prizeItem = prizeService.selectPrizeAndItem(prizeId);
        }
//        else {
////            prizeItem = JSONObject.parseObject(o.toString(), PrizeItem.class);
//        }
        if (System.currentTimeMillis() - prizeItem.getStartTime() < 0
                || prizeItem.getEndTime() - System.currentTimeMillis() < 0) {
            log.info("不在抽奖时间");
            return "不在抽奖时间";
        }
        if ((int) redisTemplate.opsForValue().get("PRIZE_TOTAL_AMOUNT:" + prizeId) <= 0) {
            lottery.setUserId(userId);
            lottery.setPrizeId(prizeId);
            lottery.setItemId(0);
            lottery.setCreateTime(System.currentTimeMillis());
            lotteryMapper.insertLottery(lottery);
            stringRedisTemplates.boundValueOps("DRAW:" + prizeId + ":" + userId).set("很遗憾，本次未中奖"/*,
                    prizeItem.getEndTime() - System.currentTimeMillis()*/);
            log.info("用户:" + userId + " 本次未中奖");
            return "很遗憾，本次未中奖";
        }
        String prize = stringRedisTemplates.boundValueOps("DRAW:" + prizeId + ":" + userId).get();
        if (null != prize) {
            log.info("已参与抽奖！");
            return "已参与抽奖！";
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
        if (Math.abs(l) % total < i % total) {
            int level = random.nextInt(size);
            Item item = items.get(level);
            synchronized (LOCK) {
                if ((int) redisTemplate.opsForValue().get("PRIZE_TOTAL_AMOUNT:" + prizeId) > 0) {
                    redisTemplate.opsForValue().decrement("PRIZE_TOTAL_AMOUNT:" + prizeId);
                    stringRedisTemplates.boundValueOps("DRAW:" + prizeId + ":" + userId).set(item.getItemName(),
                            prizeItem.getEndTime() - System.currentTimeMillis());
                    lottery.setItemId(item.getId());
                    log.info("用户:" + userId + " 获得：" + item.getItemName());
                } else {
                    lottery.setItemId(0);
                    log.info("用户:" + userId + " 本次未中奖");
                }
            }
            lottery.setUserId(userId);
            lottery.setPrizeId(prizeId);
            lottery.setCreateTime(System.currentTimeMillis());
            lotteryMapper.insertLottery(lottery);
            stringRedisTemplates.boundValueOps("DRAW:" + prizeId + ":" + userId).set("很遗憾，本次未中奖"/*, prizeItem.getEndTime() - System.currentTimeMillis()*/);
//            log.info("用户:" + userId + " 本次未中奖");
            return "恭喜你获得：" + item.getItemName();
        }
        stringRedisTemplates.boundValueOps("DRAW:" + prizeId + ":" + userId).set("很遗憾，本次未中奖"/*, prizeItem.getEndTime() - System.currentTimeMillis()*/);
        log.info("用户:" + userId + " 本次未中奖");
        return "很遗憾，本次未中奖";

    }
}
