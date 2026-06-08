package com.mall.dao.mapper;

import com.mall.dao.model.Product;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ProductMapper extends Mapper<Product> {

    Product selectProductById(@Param("productId") Integer productId);

    List<Product> selectProductByCategory(@Param("category") Integer category, @Param("start") Integer start, @Param("end") Integer end);

    Integer selectProductCountByCategory(@Param("category") Integer category);
}