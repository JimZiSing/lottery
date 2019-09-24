package org.javatribe.lottery.service.impl;

import org.javatribe.lottery.entity.Prize;
import org.javatribe.lottery.entity.PrizeItem;
import org.javatribe.lottery.mapper.PrizeMapper;
import org.javatribe.lottery.service.IPrizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
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
    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public PrizeItem selectPrizeAndItem(Integer prizeId) {
        PrizeItem prizeItem = (PrizeItem) redisTemplate.opsForValue().get("prize:" + prizeId);
        //二重加锁机制，防止多次查询数据库。
        if (null == prizeItem) {
                prizeItem = (PrizeItem) redisTemplate.opsForValue().get("prize:" + prizeId);
                synchronized (LOCK) {
                    if (null == prizeItem) {
                        prizeItem = prizeMapper.queryPrizeAndItem(prizeId);
                        redisTemplate.opsForValue().set("prize:" + prizeId, prizeItem, prizeItem.getEndTime() - System.currentTimeMillis());
                    }
                }
        }
        return prizeItem;
    }

    @Override
    public void addPrize(Prize prize) {
        prizeMapper.insertPrize(prize);
    }

    @Override
    public List<Prize> selectPrizes() {
        return prizeMapper.queryPrizes();
    }
}
