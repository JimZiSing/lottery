package org.javatribe.lottery.service.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.javatribe.lottery.entity.User;
import org.javatribe.lottery.entity.WxMessage;
import org.javatribe.lottery.entity.WxTemplate;
import org.javatribe.lottery.service.IUserService;
import org.javatribe.lottery.service.IWxService;
import org.javatribe.lottery.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 处理微信api逻辑 service层
 *
 * @author JimZiSing
 */
@Service
@Slf4j
public class WxServiceImpl implements IWxService {

    /**
     * 微信校验token
     */
    @Value("${wxCheckToken}")
    private String checkToken;
    @Value("${appId}")
    private String APPID;
    @Value("${appSecret}")
    private String APPSECRET;
    @Value("${pushUrl}")
    private String pushUrl;
    @Value("${template_id}")
    private String templateId;
    @Value("${page_address}")
    private String pageAddress;

    private String access_token;

    @Autowired
    private IUserService userService;

    /**
     * 响应微信验证token签名
     *
     * @param signature
     * @param timestamp
     * @param nonce
     * @return
     */
    @Override
    public boolean checkToken(String signature, String timestamp, String nonce) {

        String[] strs = {checkToken, timestamp, nonce};
        Arrays.sort(strs);
        //对预设置的token拼接上timestamp和nonce进行sha1加密
        String sha1Hex = DigestUtils.sha1Hex(strs[0] + strs[1] + strs[2]);
        return signature.equals(sha1Hex);
    }

    @Override
    public User getUserMessage(String code) throws Exception {
        String getAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                "appid=" + APPID +
                "&secret=" + APPSECRET +
                "&code=" + code +
                "&grant_type=authorization_code";
        //进行get请求,获取用户的access_token和openid
        Map map = HttpClientUtil.doGet(getAccessTokenUrl);
        //获取用户的微信信息
        String getUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo?" +
                "access_token=" + map.get("access_token") +
                "&openid=" + map.get("openid") +
                "&lang=zh_CN";
        Map userInfo = HttpClientUtil.doGet(getUserInfoUrl);
        log.info("获取用户消息：" + userInfo);
        User user = new User();
        user.setOpenid((String) userInfo.get("openid"));
        user.setNickname((String) userInfo.get("nickname"));
        user.setHeadImgUrl((String) userInfo.get("headimgurl"));
        return userService.addUser(user);
    }

    @Override
    @Async
    public void sendLotteryResult(String openid, String item) {
        String accessToken = getAccessToken();
        WxTemplate wxTemplate = new WxTemplate();
        wxTemplate.setTouser(openid);
        wxTemplate.setTemplate_id(templateId);
        wxTemplate.setUrl("http://localtest.free.idcfengye.com/draw-result/" + openid);
        Map<String, Object> data = new HashMap<>(4);
        data.put("value", item);
        data.put("color", "#173177");
        wxTemplate.getData().put("item", data);
        String s = JSON.toJSON(wxTemplate).toString();
        System.out.println(s);
        Map map = HttpClientUtil.doPost(pushUrl + accessToken, s);
        System.out.println(map);
    }

    @Override
    public WxMessage receiveWxMessage(WxMessage msg) {
        if ("抽奖".equals(msg.getContent()) || "抽奖".equals(msg.getEventKey())) {
            User user = userService.selectUserByOpenid(msg.getFromUserName());
            if (user == null) {
                user = new User();
                user.setOpenid(msg.getFromUserName());
                userService.addUser(user);
            }
            WxMessage returnMsg = new WxMessage();
            returnMsg.setToUserName(msg.getFromUserName());
            returnMsg.setFromUserName(msg.getToUserName());
            returnMsg.setMsgType("text");
            returnMsg.setContent(pageAddress + msg.getFromUserName());
            returnMsg.setCreateTime(System.currentTimeMillis());
            return returnMsg;
        }
        log.info("用户：" + msg.getFromUserName() + " 发来 " + msg.getContent());
        WxMessage returnMsg = new WxMessage();
        returnMsg.setToUserName(msg.getFromUserName());
        returnMsg.setFromUserName(msg.getToUserName());
        returnMsg.setMsgType("text");
        returnMsg.setContent("谢谢你的消息，我们看到后会给你答复的哦");
        returnMsg.setCreateTime(System.currentTimeMillis());
        return returnMsg;
    }

    private String getAccessToken() {
        String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?" +
                "grant_type=client_credential" +
                "&appid=" + APPID +
                "&secret=" + APPSECRET;
        Map map = HttpClientUtil.doGet(access_token_url);
        System.out.println(map);
        access_token = map.get("access_token").toString();
        return map.get("access_token").toString();
    }

    @Override
    public String getQRCodeTicket() {
        String accessToken = getAccessToken();
        System.out.println(accessToken);
        String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + accessToken;
        String param = "{\"expire_seconds\": 604800, \"action_name\": \"QR_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \"抽奖\"}}}";
        Map map = HttpClientUtil.doPost(url, param);
        System.out.println(map);
        /**
         * {"expire_seconds": 604800, "action_name": "QR_STR_SCENE", "action_info": {"scene": {"scene_str": "test"}}}
         */

        return map.get("ticket").toString();
    }
}
