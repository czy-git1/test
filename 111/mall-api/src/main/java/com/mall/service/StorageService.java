package com.mall.service;

import com.mall.dao.model.Product;
import com.mall.dao.model.Storage;
import com.mall.exception.StorageException;

public interface StorageService {

    /**
     * 新商品上架初始化库存
     * @param product
     */
    void initStorage(Product product);

    /**
     * 增加库存
     * @param productId
     * @param quantity
     */
    void addStorage(Integer productId, int quantity, String remark);

    /**
     * 减少库存
     * @param productId
     * @param quantity
     */
    void minusStorage(Integer productId, int quantity, String remark) throws StorageException;

    /**
     * 仓库库存盘点
     * @param productId
     * @param quantity
     */
    void stocktaking(Integer productId, int quantity, String remark);

    /**
     * 查询最新库存
     * @param productId
     * @return
     */
    Storage findLastStorage(Integer productId);
}
