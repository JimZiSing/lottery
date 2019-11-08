package org.javatribe.lottery.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.javatribe.lottery.entity.*;
import org.javatribe.lottery.mapper.LotteryMapper;
import org.javatribe.lottery.service.*;
import org.javatribe.lottery.utils.LotteryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * lotteryService 层接口实现
 *
 * @author JimZiSing
 */
@Service
@Slf4j
public class LotteryServiceImpl implements ILotteryService {
    @Autowired
    private LotteryMapper lotteryMapper;
    @Autowired
    private IPrizeService prizeService;
    @Autowired
    private IItemService itemService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IWxService wxService;
    @Autowired
    private ApplicationContext applicationContext;
    private ILotteryService lotteryService;

    private ReentrantLock lock = new ReentrantLock();
    private ReentrantReadWriteLock readWriteLock;
    private ReentrantReadWriteLock.ReadLock readLock;
    private ReentrantReadWriteLock.WriteLock writeLock;


    @PostConstruct
    public void init() {
        lotteryService = applicationContext.getBean(ILotteryService.class);
        readWriteLock = new ReentrantReadWriteLock();
        readLock = readWriteLock.readLock();
        writeLock = readWriteLock.writeLock();

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String luckDraw(String openid, Integer userId, PrizeItem prizeItem) {
//        Integer prizeId = prizeItem.getId();
//
//        String prize = (String) redisTemplate.boundValueOps("DRAW:" + prizeId + ":" + userId).get();
//        if (null != prize || null != lotteryMapper.queryLotteryByUserIdAndPrizeId(userId, prizeId)) {
//            log.info("已参与抽奖！");
//            return "已参与抽奖！";
//        }
//        if (prizeService.selectPrizeSurplus(prizeId) <= 0) {
//            Lottery lottery = new Lottery();
//            lottery.setPrizeId(prizeId);
//            lottery.setItemId(0);
//            lottery.setUserId(userId);
//            lottery.setCreateTime(System.currentTimeMillis());
//            try {
//                System.out.println(lottery);
//                lotteryMapper.insertLottery(lottery);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            redisTemplate.boundValueOps("DRAW:" + prizeId + ":" + userId).set("很遗憾，本次未中奖"/*,
//                    prizeItem.getEndTime() - System.currentTimeMillis()*/);
//            log.info("用户:" + userId + " 本次未中奖");
//            return "很遗憾，本次未中奖";
//        }
//
//        List<Item> items = prizeItem.getItems();
//        int size = items.size();
//        int total = 0;
//        for (int i = 0; i < size; i++) {
//            Item item = items.get(i);
//            total += item.getAmount();
//        }
//        Random random = new Random();
//        int i = random.nextInt(100);
//        long l = openid.hashCode() + System.currentTimeMillis();
//        Lottery lottery = new Lottery();
//        lottery.setPrizeId(prizeId);
//        lottery.setUserId(userId);
//        lottery.setCreateTime(System.currentTimeMillis());
//        if (Math.abs(l) % total == i % total) {
//            int level = random.nextInt(size);
//            Item item = items.get(level);
//            synchronized (LOCK) {
//                if (prizeService.selectPrizeSurplus(prizeId) > 0) {
//                    redisTemplate.opsForValue().decrement("PRIZE_TOTAL_SURPLUS:" + prizeId);
//                    redisTemplate.boundValueOps("DRAW:" + prizeId + ":" + userId).set(item.getItemName()/*,
//                            prizeItem.getEndTime() - System.currentTimeMillis()*/);
//                    lottery.setItemId(item.getId());
//                    log.info("用户:" + userId + " 获得：" + item.getItemName());
//                } else {
//                    redisTemplate.boundValueOps("DRAW:" + prizeId + ":" + userId).set("很遗憾，本次未中奖");
//                    lottery.setItemId(0);
//                    log.info("用户:" + userId + " 本次未中奖");
////                    return "很遗憾，本次未中奖";
//                }
//            }
//            try {
//                lotteryMapper.insertLottery(lottery);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return lottery.getItemId() == 0 ? "很遗憾，本次未中奖" : "恭喜你获得：" + item.getItemName();
////            return "恭喜你获得：" + item.getItemName();
//        } else {
//            lottery.setItemId(0);
//            lotteryMapper.insertLottery(lottery);
//            redisTemplate.boundValueOps("DRAW:" + prizeId + ":" + userId).set("很遗憾，本次未中奖");
//            log.info("用户:" + userId + " 本次未中奖");
//            return "很遗憾，本次未中奖";
//        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer draw(String openid, Integer prizeId) {
        User user = userService.selectUserByOpenid(openid);
        Integer userId = user.getId();
        if (LotteryUtils.surplus.get() <= 0) {

            LotteryUtils.lotteryMap.put(openid, -1);
            return -1;
        }

        List<Item> itemList = LotteryUtils.itemList;
        Random random = new Random();
        int ran = random.nextInt(LotteryUtils.totalAmount) + 1;
        long l = openid.hashCode() + System.currentTimeMillis();
        if (Math.abs(l) % ran < ran % LotteryUtils.totalAmount) {
//        if (Math.abs(l) % ran <= LotteryUtils.surplus.get() * 2) {
            int level = random.nextInt(itemList.size());
            Item item = itemList.get(level);

            try {
                writeLock.lock();
                if (LotteryUtils.surplus.get() > 0 && item.getSurplus1().get() > 0) {
                    Lottery lottery = new Lottery();
                    lottery.setPrizeId(prizeId);
                    lottery.setUserId(userId);
                    lottery.setCreateTime(System.currentTimeMillis());
                    lottery.setItemId(item.getId());
                    int surplus = LotteryUtils.surplus.decrementAndGet();
                    itemService.updateItemDecrById(item.getId());
                    int i = item.getSurplus1().decrementAndGet();
                    LotteryUtils.drawMap.put(openid, lottery);
                    LotteryUtils.lotteryMap.put(openid, item.getId());
                    log.info("用户:" + openid + " 获得：" + item.getItemName());
                    if (surplus == 0) {
                        lotteryService.addLotteryList();
                    }
                    return item.getId();
                } else {
                    LotteryUtils.lotteryMap.put(openid, -1);
                    return -1;
                }
            } finally {
                writeLock.unlock();
            }
        }
        return -1;
    }

    @Override
    public boolean isDraw(String openid) {
        Integer prize;
        try {
            readLock.lock();
            prize = LotteryUtils.lotteryMap.get(openid);
        } finally {
            readLock.unlock();
        }
        if (null != prize) {
            return true;
        }
        return false;
    }

    @Override
    @Async("taskExecutor")
    public void addLotteryList() {
        Map map = LotteryUtils.drawMap;
        lotteryMapper.insertLotteryList(map);
    }
}
