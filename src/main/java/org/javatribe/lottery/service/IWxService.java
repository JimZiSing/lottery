package org.javatribe.lottery.service;


import org.javatribe.lottery.entity.User;
import org.javatribe.lottery.entity.WxMessage;

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
     * @throws Exception
     */
    User getUserMessage(String code) throws Exception;

    /**
     * 向微信用户推送抽奖结果
     * @param openid
     * @param itemName
     */
    void sendLotteryResult(String openid, String itemName);

    /**
     * 接收用户发来的信息
     * @param msg
     * @return
     */
    WxMessage receiveWxMessage(WxMessage msg);

    String getQRCodeTicket();
}
