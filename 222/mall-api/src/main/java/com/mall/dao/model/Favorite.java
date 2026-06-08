package com.mall.dao.model;

import javax.persistence.*;

public class Favorite {
    /**
     * 主键
     */
    @Id
    @Column(name = "favorite_id")
    private Integer favoriteId;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 商品ID
     */
    @Column(name = "product_id")
    private Integer productId;

    @Transient
    private Product product;

    /**
     * 获取主键
     *
     * @return favorite_id - 主键
     */
    public Integer getFavoriteId() {
        return favoriteId;
    }

    /**
     * 设置主键
     *
     * @param favoriteId 主键
     */
    public void setFavoriteId(Integer favoriteId) {
        this.favoriteId = favoriteId;
    }

    /**
     * 获取用户ID
     *
     * @return user_id - 用户ID
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置用户ID
     *
     * @param userId 用户ID
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取商品ID
     *
     * @return product_id - 商品ID
     */
    public Integer getProductId() {
        return productId;
    }

    /**
     * 设置商品ID
     *
     * @param productId 商品ID
     */
    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}