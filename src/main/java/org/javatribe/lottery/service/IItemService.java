package org.javatribe.lottery.service;

import org.javatribe.lottery.entity.Item;
import org.javatribe.lottery.entity.Prize;
import org.javatribe.lottery.entity.PrizeItem;

/**
 * 奖项
 * @author JimZiSing
 */
public interface IItemService {
    /**
     * 查询所属抽奖和对应的奖项信息
     * @param itemId
     * @return
     */
    PrizeItem selectItem(Integer itemId);

    /**
     * 添加抽奖
     * @param item
     * @return
     */
    void addItem(Item item);

    /**
     * 更新奖项信息
     * @param item
     */
    void updateItem(Item item);

    /**
     * 减少剩余量减一
     * @param id
     */
    void updateItemDecrById(Integer id);
}
