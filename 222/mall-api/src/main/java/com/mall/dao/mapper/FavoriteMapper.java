package com.mall.dao.mapper;

import com.mall.dao.model.Favorite;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface FavoriteMapper extends Mapper<Favorite> {

    List<Favorite> selectMyFav(@Param("userId") Integer userId, @Param("start") Integer start, @Param("end") Integer end);

    Integer selectMyFavCount(@Param("userId") Integer userId);
}