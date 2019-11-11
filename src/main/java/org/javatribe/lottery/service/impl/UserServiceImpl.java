package org.javatribe.lottery.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.javatribe.lottery.entity.User;
import org.javatribe.lottery.mapper.UserMapper;
import org.javatribe.lottery.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * userService层接口实现
 *
 * @author JimZiSing
 */
@Service
@Slf4j
public class UserServiceImpl implements IUserService {

    @Autowired
    UserMapper userMapper;
//    @Autowired
//    RedisTemplate redisTemplate;

    @Override
    public User addUser(User user) {
        User resultUser = selectUserByOpenid(user.getOpenid());
        if (resultUser == null) {
            userMapper.insertUser(user);
            return user;
        }
        return resultUser;
    }

    @Override
    public User selectUserByOpenid(String openid) {
//        User user = (User) redisTemplate.opsForValue().get("USER:" + openid);
//        if (user!= null){
//            return user;
//        }
        User user = userMapper.queryUserByOpenid(openid);
//        if (user != null){
//            redisTemplate.opsForValue().set("user:"+openid,user);
//        }
        return user;
    }

    @Override
    public List<User> selectUsers() {
        return userMapper.queryAllUser();
    }

    @Override
    public String bindUserWithIp(String realIp) {
        User user = userMapper.queryUserByIp(realIp);
        if (user != null){
            return user.getOpenid();
        }else {
            user = new User();
        }
        user.setOpenid(UUID.randomUUID().toString().replaceAll("-",""));
        user.setIp(realIp);
        int i = userMapper.insertUser(user);
        return user.getOpenid();
    }
}
