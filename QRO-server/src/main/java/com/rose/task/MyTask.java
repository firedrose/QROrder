package com.rose.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class MyTask {
//    @Scheduled(cron = "0/5 * * * * ?")
    public void task(){
        log.info("开启定时任务调度{}",new Date());
    }
}
