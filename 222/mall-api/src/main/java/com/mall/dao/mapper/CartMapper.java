package com.mall.dao.mapper;

import com.mall.dao.model.Cart;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface CartMapper extends Mapper<Cart> {

    void selectAllItem(@Param("userId") Integer userId);

    void disSelectAll(@Param("userId") Integer userId);

    void selectCart(@Param("cartId") Integer cartId, @Param("isSelect") Integer isSelect);

    List<Cart> selectCartByUserSelected(@Param("userId") Integer userId);
}