package com.flighty.projectDirectory.user.service;

import java.util.Map;

/**
 * 描述：用户登录注册Service
 *
 * @author zhoushan
 * @create 2019/04/29 11:18
 */
public interface UserService {
    /**
     * 查询用户是否存在
     * @param paramt
     * @return
     */
    public int selectWx(Map<String,Object> paramt);

    /**
     * 新用户注册
     * @param params
     * @return
     */
    public int wxRegister(Map params);

    /**
     * 查询用户信息
     * @param paramt
     * @return
     */
    public Map selectUser(Map<String,Object> paramt);
}
