package com.zzs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzs.dao.PermissionDao;
import com.zzs.dao.RoleDao;
import com.zzs.dao.UserDao;
import com.zzs.entity.Permission;
import com.zzs.entity.Role;
import com.zzs.entity.User;
import com.zzs.service.UserService;
import com.zzs.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author mountain
 * @since 2021-05-15
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PermissionDao permissionDao;

    /**
     * 注册账号
     *
     * @param userName 用户名
     * @param password 密码
     * @return
     */
    @Override
    public String registeredAccount(String userName, String password) {
        User user = new User();
        user.setUserName(userName);
        user.setPassword(password);
        userDao.insert(user);

        //TODO 事务回滚未解决
        Role role = new Role();
        role.setUserId(user.getId());
        roleDao.insert(role);

        Permission permission = new Permission();
        permission.setRoleId(role.getId());
        permissionDao.insert(permission);
        return Constant.SUCCESS;
    }
}
