package com.mall.dao.mapper;

import com.mall.dao.model.TOrder;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TOrderMapper extends Mapper<TOrder> {

    List<TOrder> selectOrderByUserId(@Param("userId") Integer userId, @Param("start") Integer start, @Param("end") Integer end);

    Integer selectOrderCountByUserId(@Param("userId") Integer userId);

    TOrder selectOrderById(@Param("orderId") Integer orderId);


    List<TOrder> selectOrderList(@Param("kw") String kw, @Param("start") Integer start, @Param("end") Integer end);

    Integer selectOrderCount(@Param("kw") String kw);

    List<Map<String, Object>> selectOrderDateCount(@Param("startDate") Date startDate, @Param("endDate")Date endDate);

    List<Map<String, Object>> selectOrderAmountCount(@Param("startDate") Date startDate, @Param("endDate")Date endDate);
}