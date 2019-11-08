package org.javatribe.lottery.utils;

import org.javatribe.lottery.entity.Item;
import org.javatribe.lottery.entity.Lottery;
import org.javatribe.lottery.entity.PrizeItem;
import org.javatribe.lottery.mapper.ItemMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author Jimzising
 * @Date 2019/10/17
 * @Desc
 */
public class LotteryUtils {

    @Autowired
    ItemMapper itemMapper;

    /**
     * 保存用户抽奖信息
     */
    public static Map<String,Integer> lotteryMap = new ConcurrentHashMap<>(1024);
     /**
     * 保存中奖信息
     */
    public static Map<String, Lottery> drawMap = new ConcurrentHashMap<>(32);

    /**
     * 奖项信息
     */
    public static List<Item> itemList;

    /**
     * 剩余数量
     */
    public static AtomicInteger surplus = new AtomicInteger(0);
    /**
     * 总奖项数
     */
    public static int totalAmount;
    public static PrizeItem prizeItem;

}
