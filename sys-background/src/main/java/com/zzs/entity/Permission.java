package com.zzs.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.zzs.base.BaseEntity;
import lombok.Data;

/**
 * @author mm013
 * @Date 2021/5/15 17:33
 */
@Data
@TableName(value = "sys_permission")
public class Permission extends BaseEntity {

    /**
     * 角色id
     */
    @Column(name = "role_id", type = MySqlTypeConstant.BIGINT)
    private Long roleId;

    /**
     * 权限
     */
    @Column(name = "permission_enum", type = MySqlTypeConstant.INT, length = 1, defaultValue = "0")
    private String permissionEnum;

    public enum permissionEnum {
        /**
         * 默认权限
         */
        DEFAULT,
        /**
         * 所有权限
         */
        ALL
    }
}
