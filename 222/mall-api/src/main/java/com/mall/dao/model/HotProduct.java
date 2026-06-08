package com.mall.dao.model;

import javax.persistence.*;
import java.util.List;

@Table(name = "hot_product")
public class HotProduct {
    /**
     * 主键
     */
    @Id
    @Column(name = "hot_id")
    private Integer hotId;

    /**
     * 商品ID
     */
    @Column(name = "product_id")
    private Integer productId;

    @Transient
    private Product product;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 类型：1推荐、2热销
     */
    @Column(name = "hot_type")
    private Integer hotType;

    /**
     * 标签
     */
    private String tags;

    @Transient
    private List<String> tagList;

    /**
     * 获取主键
     *
     * @return hot_id - 主键
     */
    public Integer getHotId() {
        return hotId;
    }

    /**
     * 设置主键
     *
     * @param hotId 主键
     */
    public void setHotId(Integer hotId) {
        this.hotId = hotId;
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
     * 获取排序
     *
     * @return sort - 排序
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * 设置排序
     *
     * @param sort 排序
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    /**
     * 获取类型：1推荐、2热销
     *
     * @return hot_type - 类型：1推荐、2热销
     */
    public Integer getHotType() {
        return hotType;
    }

    /**
     * 设置类型：1推荐、2热销
     *
     * @param hotType 类型：1推荐、2热销
     */
    public void setHotType(Integer hotType) {
        this.hotType = hotType;
    }

    /**
     * 获取标签
     *
     * @return tags - 标签
     */
    public String getTags() {
        return tags;
    }

    /**
     * 设置标签
     *
     * @param tags 标签
     */
    public void setTags(String tags) {
        this.tags = tags == null ? null : tags.trim();
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<String> getTagList() {
        return tagList;
    }

    public void setTagList(List<String> tagList) {
        this.tagList = tagList;
    }
}