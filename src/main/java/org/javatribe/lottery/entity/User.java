package org.javatribe.lottery.entity;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;


/**
 * (User)实体类
 *
 * @author jimzising
 * @since 2019-09-11 19:48:01
 */
    

@Data
@ToString
public class User implements Serializable {
    private static final long serialVersionUID = 715138750693442842L;
            
    private Integer id;
            
    private String openid;
            
    private String nickname;
            
    private String headImgUrl;

}