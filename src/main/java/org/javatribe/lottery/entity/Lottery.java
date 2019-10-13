package org.javatribe.lottery.entity;

import java.io.Serializable;

import lombok.Data;


/**
 * (Lottery)实体类
 *
 * @author jimzising
 * @since 2019-09-23 18:45:23
 */
    

@Data
public class Lottery implements Serializable {
    private static final long serialVersionUID = -29230470343036402L;
    /** 
     * 用户id
     */        
    private Integer userId;
    /** 
     * 抽奖时间
     */        
    private Long createTime;
    /** 
     * 参与抽奖
     */        
    private Integer prizeId;
    /** 
     * 所中奖项
     */        
    private Integer itemId;

}