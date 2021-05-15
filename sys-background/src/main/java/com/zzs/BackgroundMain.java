package com.zzs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author mm013
 * @Date 2021/5/15 17:14
 */
@MapperScan({"com.gitee.sunchenbin.mybatis.actable.dao.*", "com.zzs.dao"})
@ComponentScan({"com.gitee.sunchenbin.mybatis.actable.manager.*", "com.zzs.*"})
@SpringBootApplication
public class BackgroundMain {
    public static void main(String[] args) {
        SpringApplication.run(BackgroundMain.class, args);
    }
}
