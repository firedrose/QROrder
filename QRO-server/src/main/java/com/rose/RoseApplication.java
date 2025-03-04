package com.rose;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement //开启事务管理
@Slf4j//开启日志
@EnableCaching//开启缓存注解功能
@EnableScheduling//开启定时任务调度
public class RoseApplication {
    public static void main(String[] args) {
        SpringApplication.run(RoseApplication.class, args);
        log.info("server started");
    }
}
