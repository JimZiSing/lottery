package org.javatribe.lottery.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.javatribe.lottery.entity.User;
import org.javatribe.lottery.service.IUserService;
import org.javatribe.lottery.service.IWxService;
import org.javatribe.lottery.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;

/**
 * 处理微信api逻辑 service层
 * @author JimZiSing
 */
@Service
@Slf4j
public class WxServiceImpl implements IWxService {

    /**
     * 微信校验token
     * */
    @Value("${wxCheckToken}")
    private String checkToken;
    @Value("${appId}")
    private String APPID;
    @Value("${appSecret}")
    private String APPSECRET;

    @Autowired
    private IUserService userService;

    /**
     * 响应微信验证token签名
     * @param signature
     * @param timestamp
     * @param nonce
     * @return
     */
    @Override
    public boolean checkToken(String signature, String timestamp, String nonce) {

        String[] strs = {checkToken,timestamp,nonce};
        Arrays.sort(strs);
        //对预设置的token拼接上timestamp和nonce进行sha1加密
        String sha1Hex = DigestUtils.sha1Hex(strs[0]+strs[1]+strs[2]);
        return signature.equals(sha1Hex);
    }

    @Override
    public User getUserMessage(String code) throws Exception {
        String getAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                "appid="+APPID +
                "&secret="+APPSECRET +
                "&code="+code +
                "&grant_type=authorization_code";
        //进行get请求,获取用户的access_token和openid
        Map map = HttpClientUtil.doGet(getAccessTokenUrl);
        //获取用户的微信信息
        String getUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo?" +
                "access_token="+map.get("access_token") +
                "&openid="+map.get("openid") +
                "&lang=zh_CN";
        Map userInfo = HttpClientUtil.doGet(getUserInfoUrl);
        log.info("获取用户消息："+userInfo);
        User user = new User();
        user.setOpenid((String) userInfo.get("openid"));
        user.setNickname((String) userInfo.get("nickname"));
        user.setHeadImgUrl((String) userInfo.get("headimgurl"));
        return userService.addUser(user);
    }
}
