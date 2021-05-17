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

    @PostMapping(value = "/registeredAccount")
    private CommonResult registeredAccount(String userName, String password) throws Exception {
        String result = userService.registeredAccount(userName, password);
        if (result.equals(Constant.SUCCESS)) {
            CommonResult.success("注册成功");
        }
        return CommonResult.error(result);
    }
}
