package org.javatribe.lottery.mapper;

import org.apache.ibatis.annotations.Param;
import org.javatribe.lottery.entity.Prize;
import org.javatribe.lottery.entity.PrizeItem;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * prize表 ，mybtis mapper 接口
 * @author JimZiSing
 */
@Repository
public interface PrizeMapper {
    PrizeItem queryPrizeAndItem(Integer prizeId);

    /**
     * 插入一行数据
     * @param prize
     * @return
     */
    int insertPrize(Prize prize);

    /**
     * 查询抽奖列表
     * @return
     * @param begin
     * @param pageSize
     */
    List<Prize> queryPrizes(int begin, int pageSize);

    /**
     * 查询总记录数
     * @return
     */
    int selectTotalRecord();

    int selectPrizeSurplus(@Param("prizeId") Integer prizeId);
}
