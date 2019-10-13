package org.javatribe.lottery.config;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.net.UnknownHostException;

/**
 * 自定义redis配置
 *
 * @author JimZiSing
 */
@Configuration
public class RedisConfig {

    /**
     * 使用fastjson序列化
     */
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
        RedisTemplate<Object, Object> template = new RedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        //redis开启事务
        template.setEnableTransactionSupport(true);
        template.setDefaultSerializer(new GenericFastJsonRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }

}