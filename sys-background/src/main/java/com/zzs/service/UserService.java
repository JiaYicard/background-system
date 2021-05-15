package com.zzs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzs.entity.User;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author mountain
 * @since 2021-05-15
 */
public interface UserService extends IService<User> {

    /**
     * 注册账号
     *
     * @param userName 用户名
     * @param password 密码
     * @return
     */
    String registeredAccount(String userName, String password);
}
