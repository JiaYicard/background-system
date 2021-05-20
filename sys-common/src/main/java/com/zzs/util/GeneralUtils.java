package com.zzs.util;

import java.util.Random;

/**
 * 生成工具
 *
 * @author mm013
 * @Date 2021/5/20 15:16
 */
public class GeneralUtils {

    /**
     * 生成六位数验证码
     *
     * @return
     */
    public static Integer generalVerificationCode() {
        return new Random().nextInt(5);
    }

}
