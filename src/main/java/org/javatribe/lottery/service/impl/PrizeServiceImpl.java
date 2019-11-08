package org.javatribe.lottery.service.impl;

import org.javatribe.lottery.entity.Item;
import org.javatribe.lottery.entity.Page;
import org.javatribe.lottery.entity.Prize;
import org.javatribe.lottery.entity.PrizeItem;
import org.javatribe.lottery.mapper.PrizeMapper;
import org.javatribe.lottery.service.IPrizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
//import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * prizeService层接口实现类
 *
 * @author JimZiSing
 */
@Service
public class PrizeServiceImpl implements IPrizeService {

    private static final Object LOCK = new Object();

    @Autowired
    PrizeMapper prizeMapper;
//    @Autowired
//    RedisTemplate redisTemplate;

//    @Override
//    public PrizeItem selectPrizeAndItem(Integer prizeId) {
//        PrizeItem prizeItem = (PrizeItem) redisTemplate.opsForValue().get("PRIZE:" + prizeId);
//        //二重加锁机制，防止多次查询数据库。
//        if (null == prizeItem) {
//            prizeItem = (PrizeItem) redisTemplate.opsForValue().get("PRIZE:" + prizeId);
//            synchronized (LOCK) {
//                if (null == prizeItem) {
//                    prizeItem = prizeMapper.queryPrizeAndItem(prizeId);
//                    redisTemplate.opsForValue().set("PRIZE:" + prizeId, prizeItem/*,
//                            prizeItem.getEndTime() - System.currentTimeMillis()*/);
////                    int amount = 0;
////                    for (Item item : prizeItem.getItems()) {
////                        amount += item.getAmount();
////                    }
////                    redisTemplate.opsForValue().set("PRIZE_TOTAL_AMOUNT:" + prizeId, amount);
//                }
//            }
//        }
//        return prizeItem;
//    }

    @Override
    public PrizeItem selectPrizeAndItem(Integer prizeId) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addPrize(Prize prize) {
        prizeMapper.insertPrize(prize);
    }

    @Override
    public Page selectPrizePage(int pageNumber, int pageSize) {
        pageSize = pageSize <= 0 ? 8 : pageSize;
        pageNumber = pageNumber <= 0 ? 1 : pageNumber;
        int totalRecord = selectTotalRecord();
        int totalPage = totalRecord / pageSize + totalRecord % pageSize > 0 ? 1 : 0;
        List<Prize> prizes = prizeMapper.queryPrizes((pageNumber - 1) * pageSize, pageSize);
        return new Page(totalRecord, pageSize, totalPage, pageNumber, prizes);
    }

    @Override
    public int selectTotalRecord() {
        int totalRecord = prizeMapper.selectTotalRecord();
        return totalRecord;
    }

    @Override
    public int selectPrizeSurplus(Integer prizeId) {
        return 0;
    }

//    @Override
//    public int selectPrizeSurplus(Integer prizeId) {
//        Object surplus = redisTemplate.opsForValue().get("PRIZE_TOTAL_SURPLUS:" + prizeId);
//        //二重加锁机制，防止多次查询数据库。
//        if (null == surplus) {
//            surplus = redisTemplate.opsForValue().get("PRIZE_TOTAL_SURPLUS:" + prizeId);
//            synchronized (LOCK) {
//                if (null == surplus) {
//                    surplus = prizeMapper.selectPrizeSurplus(prizeId);
////                    redisTemplate.opsForValue().set("PRIZE_TOTAL_SURPLUS:" + prizeId, surplus);
//                }
//            }
//        }
//        return (int)surplus;
//    }
}
