package org.javatribe.lottery.entity;

import java.io.Serializable;

import lombok.Data;


/**
 * (User)实体类
 *
 * @author jimzising
 * @since 2019-11-11 20:32:53
 */
    

@Data
public class User implements Serializable {
    private static final long serialVersionUID = 418605312082384656L;
            
    private Integer id;
    /** 
     * 用户唯一标识
     */        
    private String openid;
            
    private String ip;

}