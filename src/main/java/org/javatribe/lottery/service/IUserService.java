package org.javatribe.lottery.service;

import org.javatribe.lottery.entity.User;

/**
 * userservice层接口
 * @author JimZiSing
 */
public interface IUserService {

    User addUser(User user);

    User selectUserByOpenid(String openid);
}
