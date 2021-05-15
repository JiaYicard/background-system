package com.zzs.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.zzs.base.BaseEntity;
import lombok.Data;

/**
 * 角色表
 *
 * @author mm013
 * @Date 2021/5/12 15:51
 */
@Data
@TableName(value = "sys_role")
public class Role extends BaseEntity {
    /**
     * 用户id
     */
    @Column(name = "user_id", type = MySqlTypeConstant.BIGINT)
    private Long userId;


    /**
     * 角色名
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
