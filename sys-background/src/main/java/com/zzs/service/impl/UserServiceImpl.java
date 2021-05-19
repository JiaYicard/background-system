package com.zzs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzs.dao.UserDao;
import com.zzs.entity.User;
import com.zzs.service.UserService;
import com.zzs.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author mountain
 * @since 2021-05-15
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {

    @Autowired
    private UserDao userDao;

    /**
     * 注册账号
     *
     * @param userName 用户名
     * @param password 密码
     * @param email    邮箱
     * @return
     */
    @Override
    public String registeredAccount(String userName, String password, String email) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", userName);
        User selectOne = userDao.selectOne(queryWrapper);
        if (selectOne != null) {
            return "用户名已被占用";
        }
        if (userName == null) {
            return "用户名不能为空";
        }
        if (password == null || password.length() <= 6) {
            return "密码长度不能小于6位";
        }
        if (!email.matches("^[a-z0-9A-Z]+[- | a-z0-9A-Z . _]+@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-z]{2,}$")) {
            return "邮箱格式有误";
        }

        User user = new User();
        user.setUserName(userName);
        user.setPassword(password);
        user.setEmail(email);
        userDao.insert(user);
        return Constant.SUCCESS;
    }

    /**
     * 注销账号
     *
     * @param userId 用户id
     * @return
     */
    @Override
    public String logoutAccount(Long userId) {
        if (userId == null) {
            return "请输入正确的id";
        }
        User user = userDao.selectById(userId);
        if (user == null) {
            return "此id用户不存在";
        }
        userDao.deleteById(userId);
        return Constant.SUCCESS;
    }


}
