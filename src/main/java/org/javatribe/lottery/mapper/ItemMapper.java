package org.javatribe.lottery.mapper;

import org.javatribe.lottery.entity.Item;
import org.springframework.stereotype.Repository;

/**
 * t_item表
 * @author JimZiSing
 */
@Repository
public interface ItemMapper {

    /**
     * 插入一行数据
     * @param item
     * @return
     */
    int insertItem(Item item);

    /**
     * 根据prizeId查询
     * @param prizeId
     * @return
     */
    Item queryItemByPrizeId(Integer prizeId);
}
