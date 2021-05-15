package com.zzs.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author mm013
 * @Date 2021/5/10 17:11
 */
@Data
public abstract class BaseEntity implements Serializable {

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Column(name = "id", type = MySqlTypeConstant.BIGINT, isKey = true, isAutoIncrement = true)
    private Long id;

    /**
     * 创建时间
     */
    @Column(name = "create_time", type = MySqlTypeConstant.DATETIME, isNull = false, defaultValue = "NOW()")
    private Date createTime;

    /**
     * 修改时间
     */
    @Column(name = "update_time", type = MySqlTypeConstant.DATETIME, isNull = false, defaultValue = "NOW() ON UPDATE NOW()")
    private Date updateTime;

    /**
     * 逻辑删除
     */
    @Column(name = "is_delete", type = MySqlTypeConstant.INT, length = 1, isNull = false, defaultValue = "0")
    private Boolean isDelete;

}
