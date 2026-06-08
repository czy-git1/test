package com.mall.dao.model;

import javax.persistence.*;

public class Cart {
    @Id
    @Column(name = "cart_id")
    private Integer cartId;

    /**
     * 商品的ID
     */
    @Column(name = "product_id")
    private Integer productId;

    @Transient
    private Product product;

    /**
     * 商品数量
     */
    private Integer quantity;

    /**
     * 购买者的ID
     */
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "is_select")
    private Integer isSelect;

    /**
     * @return cart_id
     */
    public Integer getCartId() {
        return cartId;
    }

    /**
     * @param cartId
     */
    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    /**
     * 获取商品的ID
     *
     * @return product_id - 商品的ID
     */
    public Integer getProductId() {
        return productId;
    }

    /**
     * 设置商品的ID
     *
     * @param productId 商品的ID
     */
    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    /**
     * 获取商品数量
     *
     * @return quantity - 商品数量
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * 设置商品数量
     *
     * @param quantity 商品数量
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * 获取购买者的ID
     *
     * @return user_id - 购买者的ID
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置购买者的ID
     *
     * @param userId 购买者的ID
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Product getProduct() {
        return product;
    }

    public Integer getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(Integer isSelect) {
        this.isSelect = isSelect;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}