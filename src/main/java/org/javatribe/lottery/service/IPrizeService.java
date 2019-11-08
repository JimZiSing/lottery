package org.javatribe.lottery.service;

import org.javatribe.lottery.entity.Page;
import org.javatribe.lottery.entity.Prize;
import org.javatribe.lottery.entity.PrizeItem;

import java.util.List;

/**
 * 奖项
 * @author JimZiSing
 */
public interface IPrizeService {
    /**
     * 查询所属抽奖和对应的奖项信息
     * @param prizeId
     * @return
     */
    PrizeItem selectPrizeAndItem(Integer prizeId);

    /**
     * 添加抽奖
     * @param prize
     * @return
     */
    void addPrize(Prize prize);

    /**
     * 查询抽奖列表
     * @return
     * @param pageNumber
     * @param pageSize
     */
    Page selectPrizePage(int pageNumber, int pageSize);

    /**
     * 查询总记录数
     * @return
     */
    int selectTotalRecord();

    /**
     * 查询抽奖剩余的数量
     * @param prizeId
     * @return
     */
    int selectPrizeSurplus(Integer prizeId);
}
