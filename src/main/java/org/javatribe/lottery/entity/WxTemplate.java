package org.javatribe.lottery.entity;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Jimzising
 * @Date 2019/10/20
 * @Desc
 */
@Data
public class  WxTemplate {

    private String touser;

    private String template_id;

    private String url;

    private Map<String, Object> data = new HashMap<>();

}
