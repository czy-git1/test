package com.mall.dao.model;

import java.util.Date;
import javax.persistence.*;

public class Storage {
    /**
     * 主键
     */
    @Id
    @Column(name = "storage_id")
    private Integer storageId;

    /**
     * 操作时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 商品ID
     */
    @Column(name = "product_id")
    private Integer productId;

    @Transient
    private Product product;

    /**
     * 操作类型：1入库，2出库，3盘点
     */
    @Column(name = "in_out")
    private Integer inOut;

    /**
     * 发生数量
     */
    private Integer quantity;

    /**
     * 备注
     */
    private String remark;

    /**
     * 操作后的商品数量
     */
    @Column(name = "new_quantity")
    private Integer newQuantity;

    /**
     * 获取主键
     *
     * @return storage_id - 主键
     */
    public Integer getStorageId() {
        return storageId;
    }

    /**
     * 设置主键
     *
     * @param storageId 主键
     */
    public void setStorageId(Integer storageId) {
        this.storageId = storageId;
    }

    /**
     * 获取操作时间
     *
     * @return create_time - 操作时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置操作时间
     *
     * @param createTime 操作时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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
     * 获取操作类型：1入库，2出库，3盘点
     *
     * @return in_out - 操作类型：1入库，2出库，3盘点
     */
    public Integer getInOut() {
        return inOut;
    }

    /**
     * 设置操作类型：1入库，2出库，3盘点
     *
     * @param inOut 操作类型：1入库，2出库，3盘点
     */
    public void setInOut(Integer inOut) {
        this.inOut = inOut;
    }

    /**
     * 获取发生数量
     *
     * @return quantity - 发生数量
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * 设置发生数量
     *
     * @param quantity 发生数量
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * 获取备注
     *
     * @return remark - 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * 获取操作后的商品数量
     *
     * @return new_quantity - 操作后的商品数量
     */
    public Integer getNewQuantity() {
        return newQuantity;
    }

    /**
     * 设置操作后的商品数量
     *
     * @param newQuantity 操作后的商品数量
     */
    public void setNewQuantity(Integer newQuantity) {
        this.newQuantity = newQuantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}