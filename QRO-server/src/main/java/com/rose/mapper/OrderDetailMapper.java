package com.rose.mapper;

import com.rose.dto.GoodsSalesDTO;
import com.rose.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderDetailMapper {
    void insertBatch(ArrayList<OrderDetail> orderDetails);


    List<GoodsSalesDTO> getSaLesTop10(Map<Object, Object> map);

    /**
     * 根据订单id查询订单明细
     * @param orderId
     * @return
     */
    @Select("select * from order_detail where order_id = #{orderId}")
    List<OrderDetail> getByOrderId(Long orderId);
}
