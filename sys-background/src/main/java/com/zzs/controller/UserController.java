package com.zzs.controller;


import com.zzs.base.BaseController;
import com.zzs.service.UserService;
import com.zzs.util.CommonResult;
import com.zzs.util.Constant;
import com.zzs.vo.EmailCodeVo;
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
     * @param userName   用户名
     * @param password   密码
     * @param email      邮箱
     * @param emailCode  邮箱验证码
     * @param emailToken 邮箱token
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/registeredAccount")
    public CommonResult registeredAccount(String userName, String password, String email, String emailCode, String emailToken) {
        String result = userService.registeredAccount(userName, password, email, emailCode, emailToken);
        if (result.equals(Constant.SUCCESS)) {
            return CommonResult.success("注册成功");
        }
        return CommonResult.error(result);
    }

    /**
     * 发送邮箱验证码
     *
     * @param email 邮箱
     * @return
     */
    @PostMapping(value = "/sendEmailCode")
    public CommonResult sendEmailCode(String email) {
        EmailCodeVo emailCodeVo = userService.sendEmailCode(email);
        if (emailCodeVo.getResult().equals("发送成功，请注意查收")) {
            return CommonResult.success(emailCodeVo);
        }
        return CommonResult.error(emailCodeVo.getResult());
    }

    /**
     * 修改密码
     *
     * @param userId      用户id
     * @param password    旧密码
     * @param newPassword 新密码
     * @return
     */
    @PostMapping(value = "/updatePassword")
    public CommonResult updatePassword(Long userId, String password, String newPassword) {
        String result = userService.updatePassword(userId, password, newPassword);
        if (result.equals(Constant.SUCCESS)) {
            return CommonResult.success("修改成功");
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

