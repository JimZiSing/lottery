package org.javatribe.lottery.service;


import org.javatribe.lottery.entity.User;

/**
 * 处理微信接口service
 * @author JimZiSing
 */
public interface IWxService {
    /**
     * 响应微信验证token签名
     * @param signature
     * @param timestamp
     * @param nonce
     * @return
     */
    boolean checkToken(String signature, String timestamp, String nonce);

    /**
     * 获取用户的微信信息
     * @param code
     * @return
     */
    User getUserMessage(String code) throws Exception;
}
