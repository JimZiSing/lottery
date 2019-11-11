package org.javatribe.lottery.service.impl;

import org.javatribe.lottery.entity.Item;
import org.javatribe.lottery.entity.PrizeItem;
import org.javatribe.lottery.mapper.ItemMapper;
import org.javatribe.lottery.mapper.UserMapper;
import org.javatribe.lottery.service.IItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * itemService接口实现类
 * @author JimZiSing
 */
@Service
public class ItemServiceImpl implements IItemService {
    @Autowired
    ItemMapper itemMapper;
    @Override
    public PrizeItem selectItem(Integer itemId) {
        return null;
    }

    @Override
    public void addItem(Item item) {
        itemMapper.insertItem(item);
    }

    @Override
    @Async
    public void updateItem(Item item) {
        itemMapper.updateItem(item);
    }

    @Override
    @Async
    public void updateItemDecrById(Integer id) {
        itemMapper.updateItemDecrById(id);

    }

    @Override
    public void drawItemDone(Integer id) {
        itemMapper.updateItemAmountTo0(id);
    }
}
