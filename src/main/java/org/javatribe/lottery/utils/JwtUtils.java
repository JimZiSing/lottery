package org.javatribe.lottery.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * jwt工具类，用于生成和解析token
 * @author JimZiSing
 */
public class JwtUtils {

    private static final String JWT_SECRET = "gjal*jkjljo_dsgf_pbwei_65fa9sdf_jcewd112gdsfd161bfof_1564d16";

    /**
     * 生成token
     * @param map    集合参数
     * @param ttlMillis    超时时间
     * @return
     * @throws Exception
     */
    public static String createJWT(Map map, long ttlMillis) throws Exception {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        //签发jwt的时间
        long nowMillis=System.currentTimeMillis();
        SecretKey key = generalKey();
        JwtBuilder builder = Jwts.builder()
                .addClaims(map)
                //存入用户信息
                .setIssuedAt(new Date())
                .signWith(signatureAlgorithm, key)
                .setExpiration(new Date(nowMillis + ttlMillis));
        return builder.compact();
    }

    /**
     * 生成key
     * @return
     */
    private static SecretKey generalKey() {
        //秘钥
        String stringKey =JwtUtils.JWT_SECRET;
        byte[] encodedKey = Base64.decodeBase64(stringKey);
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }

    /**
     *
     * 解析token
     * @param jwt
     * @return
     * @throws Exception
     */
    public static Claims parseJWT(String jwt) throws Exception{
        SecretKey key = generalKey();
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(jwt).getBody();
        return claims;
    }
}
