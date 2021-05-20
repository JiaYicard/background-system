package com.zzs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzs.dao.UserDao;
import com.zzs.entity.User;
import com.zzs.service.UserService;
import com.zzs.util.Constant;
import com.zzs.util.SendEmailUtils;
import com.zzs.vo.EmailCodeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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
    private RedisTemplate redisTemplate;

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
    public String registeredAccount(String userName, String password, String email, Integer emailCode, String emailToken) {
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
        if (!email.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")) {
            return "邮箱格式有误";
        }
        ;
        if (emailCode == null || !emailToken.equals(redisTemplate.opsForValue().get(emailCode))) {
            return "验证码有误";
        }

        User user = new User();
        user.setUserName(userName);
        user.setPassword(password);
        user.setEmail(email);
        userDao.insert(user);

        redisTemplate.opsForHash().delete(emailCode);
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
            redisTemplate.opsForValue().set(emailCode, token, 5, TimeUnit.MINUTES);
            emailCodeVo.setResult(Constant.SUCCESS);
            emailCodeVo.setEmailCode(emailCode);
            return emailCodeVo;
        } catch (Exception e) {
            e.printStackTrace();
            emailCodeVo.setResult("邮件发送失败，请检查邮箱后重试");
            return emailCodeVo;
        }
    }


}
