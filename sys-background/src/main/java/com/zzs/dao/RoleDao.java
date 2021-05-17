package com.zzs.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzs.entity.Role;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author mountain
 * @since 2021-05-15
 */
@Transactional(rollbackFor = Exception.class)
public interface RoleDao extends BaseMapper<Role> {

}
