package org.javatribe.lottery.entity;

import java.io.Serializable;

import lombok.Data;


/**
 * (Lottery)实体类
 *
 * @author jimzising
 * @since 2019-10-14 20:11:02
 */
    

@Data
public class Lottery implements Serializable {
    private static final long serialVersionUID = -87879914685818662L;
    /** 
     * 参与的抽奖
     */        
    private Integer prizeId;
    /** 
     * 用户id
     */        
    private Integer userId;
    /** 
     * 所中奖项
     */        
    private Integer itemId;
    /** 
     * 抽奖时间
     */        
    private Long createTime;

}