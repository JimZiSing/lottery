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


    /**
     * 初始化方法
     */
    @PostConstruct
    public void init() {
        //获取本类的bean
        lotteryService = applicationContext.getBean(ILotteryService.class);
        //初始化锁
        readWriteLock = new ReentrantReadWriteLock();
        readLock = readWriteLock.readLock();
        writeLock = readWriteLock.writeLock();

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String luckDraw(String openid, Integer userId, PrizeItem prizeItem) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer draw(String openid, Integer prizeId) {

        //判断抽奖是否结束
        if (LotteryUtils.surplus.get() <= 0) {
            LotteryUtils.lotteryMap.put(openid, -1);
            return -1;
        }
        //查询用户信息，获取用户id
        User user = userService.selectUserByOpenid(openid);
        Integer userId = user.getId();

        //主要抽奖逻辑
        List<Item> itemList = LotteryUtils.itemList;
        Random random = new Random();
        //用openid取哈希值加上当前时间戳
        long hash = openid.hashCode() + System.currentTimeMillis();
        //取一个随机值
        int ran = random.nextInt(LotteryUtils.totalAmount) + 1;
        //用哈希值对随机值取余，如果小于随机值取余奖项总数，初步判定为中将
        if (Math.abs(hash) % ran < ran % LotteryUtils.totalAmount) {
//        if (Math.abs(l) % ran <= LotteryUtils.surplus.get() * 2) {
            //取随机值来判断中奖等级
            int level = random.nextInt(itemList.size());
            Item item = itemList.get(level);
            try {
                //加上写锁
                writeLock.lock();
                // 判断所中的奖项是否还有，还有即中奖，否则不中奖
                if (LotteryUtils.surplus.get() > 0 && item.getSurplus1().get() > 0) {
                    //设置中奖信息
                    Lottery lottery = new Lottery();
                    lottery.setPrizeId(prizeId);
                    lottery.setUserId(userId);
                    lottery.setCreateTime(System.currentTimeMillis());
                    lottery.setItemId(item.getId());
                    //剩余总奖项数减一
                    int surplus = LotteryUtils.surplus.decrementAndGet();

                    //当前奖项剩余减一
                    int i = item.getSurplus1().decrementAndGet();
                    //在map中保存中奖信息
                    LotteryUtils.drawMap.put(openid, lottery);
                    LotteryUtils.lotteryMap.put(openid, item.getId());
                    log.info("用户:" + openid + " 获得：" + item.getItemName());
                    //当剩余奖项为0，一次修改数据库
                    if (i == 0){
                        itemService.drawItemDone(item.getId());
                    }
                    //当所有奖项抽完，统一写入数据库
                    if (surplus == 0) {
                        lotteryService.addLotteryList();
                    }
                    //返回中奖id
                    return item.getId();
                } else {
                    //不中奖，记录不中奖
                    LotteryUtils.lotteryMap.put(openid, -1);
                    return -1;
                }
            } finally {
                //释放锁
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
        //判断是否已经抽奖，已经抽奖就直接返回结果
        if (null != prize) {
            return true;
        }
        return false;
    }

    @Override
    @Async("taskExecutor")
    public void addLotteryList() {
        //异步写入结果
        Map map = LotteryUtils.drawMap;
        lotteryMapper.insertLotteryList(map);
    }
}
