package com.zzs.base;

import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * @author mm013
 * @Date 2021/5/17 10:50
 */
@Transactional(rollbackFor = Exception.class)
public interface BaseService<T extends Serializable> {

}
