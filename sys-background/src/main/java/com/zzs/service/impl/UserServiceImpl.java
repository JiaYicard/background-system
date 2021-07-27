package com.zzs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzs.dao.UserDao;
import com.zzs.entity.User;
import com.zzs.service.UserService;
import com.zzs.util.Constant;
import com.zzs.util.SendEmailUtils;
import com.zzs.vo.EmailCodeVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


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
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private UserDao userDao;

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
    @Override
    public String registeredAccount(String userName, String password, String email, String emailCode, String emailToken) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", userName);
        User selectOne = userDao.selectOne(queryWrapper);
        if (selectOne != null) {
            return "用户名已被占用";
        }
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("email", email);
        List<User> users = userDao.selectByMap(columnMap);
        if (!CollectionUtils.isEmpty(users)) {
            return "该邮箱已被注册，请更换一个";
        }
        if (StringUtils.isBlank(userName)) {
            return "用户名不能为空";
        }
        if (StringUtils.isBlank(password) || password.length() <= 6) {
            return "密码长度不能小于6位";
        }
        if (!email.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")) {
            return "邮箱格式有误";
        }
        if (StringUtils.isBlank(emailCode) || !emailToken.equals(stringRedisTemplate.opsForValue().get(emailCode))) {
            return "请输入正确的验证码";
        }

        User user = new User();
        user.setUserName(userName);
        user.setPassword(password);
        user.setEmail(email);
        userDao.insert(user);
        stringRedisTemplate.delete(emailCode);
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
        User user = userDao.selectById(userId);
        if (user == null) {
            return "此用户不存在";
        }
        userDao.deleteById(userId);
        return Constant.SUCCESS;
    }

    /**
     * 发送邮箱验证码
     *
     * @param email 邮箱
     * @return
     */
    @Override
    public EmailCodeVo sendEmailCode(String email) {
        EmailCodeVo emailCodeVo = new EmailCodeVo();
        if (!email.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")) {
            emailCodeVo.setResult("邮箱格式有误");
            return emailCodeVo;
        }
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("email", email);
        List<User> users = userDao.selectByMap(columnMap);
        if (!CollectionUtils.isEmpty(users)) {
            emailCodeVo.setResult("该邮箱已被注册，请更换一个");
            return emailCodeVo;
        }
        String emailCode;
        String token;
        try {
            emailCode = SendEmailUtils.sendQQEmail(email);
            token = DigestUtils.md5DigestAsHex(String.valueOf(UUID.randomUUID()).getBytes());
            stringRedisTemplate.opsForValue().set(emailCode, token, 5, TimeUnit.MINUTES);
            emailCodeVo.setResult("发送成功，请注意查收");
            emailCodeVo.setToken(token);
            return emailCodeVo;
        } catch (Exception e) {
            e.printStackTrace();
            emailCodeVo.setResult("邮件发送失败，请检查邮箱后重试");
            return emailCodeVo;
        }
    }

    /**
     * 修改密码
     *
     * @param userId      用户id
     * @param password    旧密码
     * @param newPassword 新密码
     * @return
     */
    @Override
    public String updatePassword(Long userId, String password, String newPassword) {
        User user = userDao.selectById(userId);
        if (user == null) {
            return "此用户不存在";
        }
        if (!user.getPassword().equals(password)) {
            return "旧密码有误";
        }
        if (user.getPassword().equals(newPassword)) {
            return "与旧密码重复";
        }
        if (StringUtils.isBlank(newPassword) || newPassword.length() <= 6) {
            return "密码长度不能小于6位";
        }
        user.setPassword(newPassword);
        userDao.updateById(user);
        return Constant.SUCCESS;
    }


}
