package org.javatribe.lottery.entity;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;


/**
 * (Prize)实体类
 *
 * @author jimzising
 * @since 2019-09-23 18:45:25
 */
    

@Data
@ToString
public class Prize implements Serializable {
    private static final long serialVersionUID = -96011923852102609L;
            
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

}