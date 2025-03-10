package com.rose.mapper;

import com.github.pagehelper.Page;
import com.rose.dto.OrdersPageQueryDTO;
import com.rose.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {


    /**
     * 插入订单数据
     *
     * @param orders
     */
    void insert(Orders orders);

    /**
     * 根据订单号查询订单
     *
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     *
     * @param orders
     */
    void update(Orders orders);


    /**
     * 根据订单状态和下单时间查询订单
     * @param pendingPayment
     * @param time
     * @return
     */
    @Select("select * from sky_take_out.orders where status=#{pendingPayment} and order_time<#{time}")
    List<Orders> selectTimeOut(Integer pendingPayment, LocalDateTime time);

    /**
     * 根据id查询订单
     * @param id
     * @return
     */
    @Select("select * from sky_take_out.orders where id=#{id}")
    Orders getById(Long id);

    /**
     * 根据日期统计营业额
     * @param map
     * @return
     */
    Double sumTurnoverByMap(Map<Object, Object> map);

    /**
     *
     * @param map
     * @return
     */
    Integer getOrderAmountByMap(Map<Object, Object> map);
    /**
     * 分页条件查询并按下单时间排序
     * @param ordersPageQueryDTO
     */
    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 根据状态统计订单数量
     * @param status
     */
    @Select("select count(id) from orders where status = #{status}")
    Integer countStatus(Integer status);
}
