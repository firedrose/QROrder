package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {
    @Autowired
    private OrderMapper orderMapper;

    /**
     * 处理超时订单
     */
    @Scheduled(cron = "0 0 * * * ?")//每小时触发一次
    public void processTimeoutOrder(){
        log.info("定时处理超时订单: {}", LocalDateTime.now());
        LocalDateTime time = LocalDateTime.now().plusMinutes(-15);
        List<Orders> list= orderMapper.selectTimeOut(Orders.PENDING_PAYMENT,time);

        if(list!=null&& !list.isEmpty()){
            for (Orders orders : list) {
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelReason("订单超时,自动取消");
                orders.setCancelTime(LocalDateTime.now());
                orderMapper.update(orders);
            }
        }
    }

    /**
     * 定时处理一直处于派送中的订单
     */
    @Scheduled(cron = "0 0 1 * * ? ")//每天凌晨一点触发
//    @Scheduled(cron = "0/20 * * * * ? ")//每天凌晨一点触发
    public void processDeliveryOrder(){
        log.info("处理派送中订单:{}", LocalDateTime.now());
        LocalDateTime yesterday = LocalDateTime.now().plusMinutes(-60);
        List<Orders> list= orderMapper.selectTimeOut(Orders.DELIVERY_IN_PROGRESS,yesterday);

        for (Orders orders : list) {
            orders.setStatus(Orders.COMPLETED);
            orderMapper.update(orders);
        }
    }

}
