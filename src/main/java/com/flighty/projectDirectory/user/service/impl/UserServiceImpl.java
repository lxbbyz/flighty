package com.flighty.projectDirectory.user.service.impl;

import com.flighty.projectDirectory.user.dao.UserDao;
import com.flighty.projectDirectory.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 描述：用户登录注册ServiceImpl
 *
 * @author zhoushan
 * @create 2019/04/29 11:18
 */
@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public int selectWx(Map<String, Object> paramt) {
        return userDao.selectWx(paramt);
    }

    @Override
    public int wxRegister(Map params) {
        return userDao.wxRegister(params);
    }

    @Override
    public Map selectUser(Map<String, Object> paramt) {
        return userDao.selectUser(paramt);
    }
}
