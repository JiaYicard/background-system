package com.zzs.controller;


import com.zzs.base.BaseController;
import com.zzs.service.UserService;
import com.zzs.util.CommonResult;
import com.zzs.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author mountain
 * @since 2021-05-15
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    /**
     * 注册
     *
     * @param userName 用户名
     * @param password 密码
     * @param email    邮箱
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/registeredAccount")
    public CommonResult registeredAccount(String userName, String password, String email) {
        String result = userService.registeredAccount(userName, password, email);
        if (result.equals(Constant.SUCCESS)) {
            return CommonResult.success("注册成功");
        }
        return CommonResult.error(result);
    }

    /**
     * 注销账号
     *
     * @param userId
     * @return
     */
    @PostMapping(value = "/logoutAccount")
    public CommonResult logoutAccount(Long userId) {
        String result = userService.logoutAccount(userId);
        if (result.equals(Constant.SUCCESS)) {
            return CommonResult.success("注销账号成功");
        }
        return CommonResult.error(result);
    }


}

