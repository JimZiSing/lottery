package org.javatribe.lottery.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import lombok.Data;
import lombok.ToString;


/**
 * (PrizeItem)实体类
 *
 * @author jimzising
 * @since 2019-09-20 15:12:48
 */
    

@Data
@ToString
public class PrizeItem implements Serializable {
    private static final long serialVersionUID = -35499733904590469L;

    private Integer id;
    /**
     * 抽奖名称
     */
    private String prizeName;
    /**
     * 开始时间
     */
    private Long startTime;
    /**
     * 结束时间
     */
    private Long endTime;
    /**
     * 开设奖项
    */
    List<Item> items;

}