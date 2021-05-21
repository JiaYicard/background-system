package com.zzs.service;

import com.zzs.base.BaseService;
import com.zzs.entity.User;
import com.zzs.vo.EmailCodeVo;

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
     * @param userName   用户名
     * @param password   密码
     * @param email      邮箱
     * @param emailCode  邮箱验证码
     * @param emailToken 邮箱token
     * @return
     */
    String registeredAccount(String userName, String password, String email, String emailCode, String emailToken);

    /**
     * 注销账号
     *
     * @param userId 用户id
     * @return
     */
    String logoutAccount(Long userId);

    /**
     * 发送邮箱验证码
     *
     * @param email 邮箱
     * @return
     */
    EmailCodeVo sendEmailCode(String email);

    /**
     * 修改密码
     *
     * @param userId      用户id
     * @param password    旧密码
     * @param newPassword 新密码
     * @return
     */
    String updatePassword(Long userId, String password, String newPassword);
}
