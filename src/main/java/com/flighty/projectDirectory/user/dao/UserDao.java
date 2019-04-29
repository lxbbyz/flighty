package com.flighty.projectDirectory.user.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * 描述：用户登录注册Dao
 *
 * @author zhoushan
 * @create 2019/04/29 11:18
 */
@Mapper
public interface UserDao {
    /**
     * 查询用户是否存在
     * @param paramt
     * @return
     */
    public int selectWx(Map<String,Object> paramt);
}