package org.javatribe.lottery.controller;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.javatribe.lottery.entity.Result;
import org.javatribe.lottery.entity.User;
import org.javatribe.lottery.service.IWxService;
import org.javatribe.lottery.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


/**
 * 操作wx 接口
 *
 * @author JimZiSing
 */
@Slf4j
@Controller
public class WxController {

    @Value("${appId}")
    private String APPID;
    @Value("${redirect_url}")
    private String REDIRECT_URL;
    @Value("${scope}")
    private String SCOPE;


    @Autowired
    IWxService wxService;


    /**
     * 验证微信返来的token
     *
     * @param signature
     * @param timestamp
     * @param nonce
     * @param echostr
     * @return
     */
    @GetMapping("/check")
    @ResponseBody
    public String check(String signature, String timestamp, String nonce, String echostr) {
        boolean pass = wxService.checkToken(signature, timestamp, nonce);
        if (pass) {
            log.info("验证成功");
            return echostr;
        } else {
            log.info("验证失败");
            return "";
        }
    }

    @GetMapping("/api/login")
    public String toWxLogin() {
        log.info("进行微信授权");
        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?" +
                "appid=" + APPID +
                "&redirect_uri=" + REDIRECT_URL +
                "&response_type=code&" +
                "scope=" + SCOPE +
                "&state=STATE#wechat_redirect";
        return "redirect:" + url;
    }

    @GetMapping("/api/callback")
    @ResponseBody
    public Result callback(String code) {
        if (code == null) {
            log.info("未进行授权，请通过微信授权");
            return Result.error(400001, "未进行授权，请通过微信授权");
        }
        User user = null;
        try {
            user = wxService.getUserMessage(code);
        } catch (Exception e) {
            return Result.error(400001, e.getMessage());
        }
        Map map = new HashMap(2);
        map.put("id", user.getId());
        map.put("openid", user.getOpenid());
        String token = null;
        try {
            token = JwtUtils.createJWT(map, 60 * 60 * 1000 * 100);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(token);
        return Result.success(token);
    }
}
