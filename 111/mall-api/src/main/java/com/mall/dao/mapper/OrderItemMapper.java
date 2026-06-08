package com.mall.dao.mapper;

import com.mall.dao.model.OrderItem;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OrderItemMapper extends Mapper<OrderItem> {

    List<OrderItem> selectOrderItemByOrderId(@Param("orderId") Integer orderId);
}