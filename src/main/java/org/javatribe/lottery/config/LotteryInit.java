package org.javatribe.lottery.config;

import lombok.extern.slf4j.Slf4j;
import org.javatribe.lottery.entity.Item;
import org.javatribe.lottery.mapper.ItemMapper;
import org.javatribe.lottery.utils.LotteryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author JimZiSing
 */
@Component
@Slf4j
public class LotteryInit implements ApplicationRunner {
    @Autowired
    private ItemMapper itemMapper;

    public void loadPrize() {
        synchronized (this){
            List list = itemMapper.queryItemByPrizeId(1);
            LotteryUtils.itemList = new CopyOnWriteArrayList<>(list);
            int size = LotteryUtils.itemList.size();
            for (int i = 0; i < size; i++) {
//                int surplus = LotteryUtils.surplus.get();
                int surplus = LotteryUtils.itemList.get(i).getSurplus();
                LotteryUtils.surplus.addAndGet(surplus);
                LotteryUtils.itemList.get(i).setSurplus1(new AtomicInteger(surplus));
                LotteryUtils.totalAmount += LotteryUtils.itemList.get(i).getAmount();
            }
            System.out.println(LotteryUtils.surplus.get());
            System.out.println(LotteryUtils.totalAmount);
        }
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        loadPrize();
    }
}
