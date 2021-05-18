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
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    public static volatile boolean IS_OK = true;

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private DataSourceTransactionManager dataSourceTransactionManager;

    @Autowired
    private TransactionDefinition transactionDefinition;

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
        List<Boolean> responseList = new ArrayList<>();
        CountDownLatch mainMonitor = new CountDownLatch(1);
        CountDownLatch childMonitor = new CountDownLatch(2);

        //根据任务数动态创建线程
        ExecutorService exectutor = Executors.newCachedThreadPool();
        exectutor.submit(() -> {
            TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
            try {
                User user = new User();
                responseList.add(Boolean.TRUE);
                user.setId(1L);
                user.setUserName(userName);
                user.setPassword(password);
                userDao.insert(user);
                childMonitor.countDown();
                mainMonitor.await();
                if (IS_OK) {
                    System.out.println("线程：" + Thread.currentThread().getName() + "任务运行正常，开始提交");
                    dataSourceTransactionManager.commit(transactionStatus);
                } else {
                    dataSourceTransactionManager.rollback(transactionStatus);
                }
            } catch (Exception e) {
                childMonitor.countDown();
                responseList.add(Boolean.FALSE);
                e.printStackTrace();
                System.out.println("线程：" + Thread.currentThread().getName() + "发生了异常，撤回提交，开始回滚" + "\n异常原因：" + e);
                dataSourceTransactionManager.rollback(transactionStatus);
            }
        });

        exectutor.submit(() -> {
            TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
            try {
                responseList.add(Boolean.TRUE);
                Role role = new Role();
                role.setId(1L);
                role.setUserId(1L);
                roleDao.insert(role);
                Permission permission = new Permission();
                permission.setId(1L);
                permission.setRoleId(role.getId());
                permissionDao.insert(permission);
                System.out.println(1 / 0);
                childMonitor.countDown();
                mainMonitor.await();
                if (IS_OK) {
                    System.out.println("线程：" + Thread.currentThread().getName() + "任务运行正常，开始提交");
                    dataSourceTransactionManager.commit(transactionStatus);
                } else {
                    dataSourceTransactionManager.rollback(transactionStatus);
                }
            } catch (Exception e) {
                childMonitor.countDown();
                responseList.add(Boolean.FALSE);
                e.printStackTrace();
                System.out.println("线程：" + Thread.currentThread().getName() + "发生了异常，撤回提交，开始回滚" + "\n异常原因：" + e);
                dataSourceTransactionManager.rollback(transactionStatus);
            }
        });

        try {
            childMonitor.await();
            //等待上面子线程的count倒计时结束运行
            for (Boolean resp : responseList) {
                if (!resp) {
                    IS_OK = false;
                    break;
                }
            }
            //控制if (IS_OK)段开始运行
            mainMonitor.countDown();
        } finally {
            exectutor.shutdown();
        }
        return Constant.SUCCESS;
    }
}
