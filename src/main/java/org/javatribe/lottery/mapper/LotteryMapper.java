package org.javatribe.lottery.mapper;

import org.apache.ibatis.annotations.Param;
import org.javatribe.lottery.entity.Lottery;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * 处理 lottery 表mapper 接口
 *
 * @author JimZiSing
 */
@Repository
public interface LotteryMapper {
    /**
     * 插入一条数据
     *
     * @param lottery
     */
    void insertLottery(Lottery lottery);

    /**
     * 查询用户抽奖情况
     *
     * @param userId
     * @param prizeId
     * @return
     */
    Lottery queryLotteryByUserIdAndPrizeId(@Param("userId") Integer userId,@Param("prizeId") Integer prizeId);

    /**
     * 插入一条数据
     *
     * @param lottery
     */
    void insertLotteryList(@Param("map")Map map);
}
