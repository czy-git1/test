package com.mall.dao.mapper;

import com.mall.dao.model.HotProduct;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface HotProductMapper extends Mapper<HotProduct> {

    List<HotProduct> selectRecProducts();

    List<HotProduct> selectHotProducts();

    List<HotProduct> selectProductsList(@Param("start") Integer start, @Param("end") Integer end, @Param("type") Integer type);

    Integer selectProductsCount(@Param("type") Integer type);
}