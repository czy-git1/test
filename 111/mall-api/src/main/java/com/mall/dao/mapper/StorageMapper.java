package com.mall.dao.mapper;

import com.mall.dao.model.Storage;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface StorageMapper extends Mapper<Storage> {

    /**
     * 查找产品的最新库存记录，只返回一个元素
     * @param productId
     * @return
     */
    List<Storage> selectLastStorageByProductId(@Param("productId") Integer productId);

    List<Storage> selectStorageList(@Param("kw") String kw, @Param("start") Integer start, @Param("end") Integer end);

    Integer selectStorageCount(@Param("kw") String kw);
}