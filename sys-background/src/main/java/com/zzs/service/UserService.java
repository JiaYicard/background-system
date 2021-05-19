package com.zzs.service;

import com.zzs.base.BaseService;
import com.zzs.entity.User;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author mountain
 * @since 2021-05-17
 */
public interface UserService extends BaseService<User> {

    /**
     * 注册账号
     *
     * @param userName 用户名
     * @param password 密码
     * @param email    邮箱
     * @return
     */
    String registeredAccount(String userName, String password, String email);

    /**
     * 注销账号
     *
     * @param userId 用户id
     * @return
     */
    String logoutAccount(Long userId);
}
