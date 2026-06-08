package com.mall.dao.model;

import javax.persistence.*;

@Table(name = "order_item")
public class OrderItem {
    /**
     * 主键
     */
    @Id
    @Column(name = "item_id")
    private Integer itemId;

    /**
     * 订单ID
     */
    @Column(name = "order_id")
    private Integer orderId;

    /**
     * 商品ID
     */
    @Column(name = "product_id")
    private Integer productId;

    @Transient
    private Product product;

    /**
     * 商品单价
     */
    private Double price;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 是否评论
     */
    private Integer iscomment;

    /**
     * 获取主键
     *
     * @return item_id - 主键
     */
    public Integer getItemId() {
        return itemId;
    }

    /**
     * 设置主键
     *
     * @param itemId 主键
     */
    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    /**
     * 获取订单ID
     *
     * @return order_id - 订单ID
     */
    public Integer getOrderId() {
        return orderId;
    }

    /**
     * 设置订单ID
     *
     * @param orderId 订单ID
     */
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
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

    /**
     * 获取商品单价
     *
     * @return price - 商品单价
     */
    public Double getPrice() {
        return price;
    }

    /**
     * 设置商品单价
     *
     * @param price 商品单价
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * 获取数量
     *
     * @return qutity - 数量
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * 设置数量
     *
     * @param quantity 数量
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * 获取是否评论
     *
     * @return iscomment - 是否评论
     */
    public Integer getIscomment() {
        return iscomment;
    }

    /**
     * 设置是否评论
     *
     * @param iscomment 是否评论
     */
    public void setIscomment(Integer iscomment) {
        this.iscomment = iscomment;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}