package com.zzs.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.zzs.base.BaseEntity;
import lombok.Data;

/**
 * 用户表
 *
 * @author mm013
 * @Date 2021/5/15 17:16
 */
@Data
@TableName(value = "sys_user")
public class User extends BaseEntity {
    /**
     * 用户名
     */
    @Column(name = "user_name", type = MySqlTypeConstant.VARCHAR)
    private String userName;

    /**
     * 密码
     */
    @Column(name = "password", type = MySqlTypeConstant.VARCHAR)
    private String password;

    /**
     * 邮箱
     */
    @Column(name = "email", type = MySqlTypeConstant.VARCHAR)
    private String email;

    /**
     * 用户身份
     */
    @Column(name = "role_name_enum", type = MySqlTypeConstant.INT, length = 1, defaultValue = "0")
    private String roleNameEnum;

    public enum roleNameEnum {
        /**
         * 普通用户
         */
        COMMON,
        /**
         * 管理员
         */
        ADMIN;
    }
}
