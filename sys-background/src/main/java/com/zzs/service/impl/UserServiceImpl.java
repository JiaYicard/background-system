package com.zzs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
    //是否可以提交
    public static volatile boolean IS_OK = true;

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
    public String registeredAccount(String userName, String password) throws Exception {
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
        //TODO 多线程事务
        User user = new User();
        user.setUserName(userName);
        user.setPassword(password);
        userDao.insert(user);

        //子线程等待主线程通知
        CountDownLatch mainMonitor = new CountDownLatch(1);

        //根据任务数动态创建线程
        ExecutorService exectutor = Executors.newCachedThreadPool();
        exectutor.execute(() -> {
            Role role = new Role();
            role.setUserId(user.getId());
            roleDao.insert(role);
            Permission permission = new Permission();
            permission.setRoleId(role.getId());
            permissionDao.insert(permission);
            System.out.println(1 / 0);
        });


        Future<?> future = exectutor.submit(new Runnable() {
            @Transactional(rollbackFor = Exception.class)
            @Override
            public void run() {
//                //设置回滚点
//                Object savepoint = null;
//
//                try {
//                    savepoint = TransactionAspectSupport.currentTransactionStatus().createSavepoint();
//                    Role role = new Role();
//                    role.setUserId(user.getId());
//                    roleDao.insert(role);
//                    Permission permission = new Permission();
//                    permission.setRoleId(role.getId());
//                    permissionDao.insert(permission);
//                    System.out.println(1 / 0);
//                } catch (Exception e) {
//                    TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savepoint);
////                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//                }

            }
        });
        //获取子线程结果
        future.get();
        //关闭线程池
        exectutor.shutdown();

        return Constant.SUCCESS;
    }
}
