package com.mall.dao.model;

import java.util.Date;
import java.util.List;
import javax.persistence.*;

public class Product {
    /**
     * 物理ID
     */
    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productId;

    /**
     * 分类 ID
     */
    private Integer category;

    @Transient
    private Category categoryBean;

    /**
     * 是否推荐
     */
    @Transient
    private int rec = 0;

    /**
     * 是否热销
     */
    @Transient
    private int hot = 0;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 单价
     */
    private Double price;

    /**
     * 上架时间
     */
    @Column(name = "publish_time")
    private Date publishTime;

    @Column(name = "main_pic")
    private String mainPic;

    /**
     * 图片
     */
    private String pics;

    @Transient
    private List<String> picList;

    private String tags;

    @Transient
    private List<String> tagList;

    /**
     * 折扣
     */
    private Double discount;

    /**
     * 库存数量
     */
    private Integer quantity;

    /**
     * 商品状态
     */
    private Integer status;

    /**
     * 浏览量
     */
    @Column(name = "view_count")
    private Integer viewCount;

    /**
     * 销量
     */
    @Column(name = "sale_count")
    private Integer saleCount;

    /**
     * 商品简述
     */
    private String describ;

    /**
     * 副标题
     */
    @Column(name = "sub_title")
    private String subTitle;

    /**
     * 获取物理ID
     *
     * @return product_id - 物理ID
     */
    public Integer getProductId() {
        return productId;
    }

    /**
     * 设置物理ID
     *
     * @param productId 物理ID
     */
    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    /**
     * 获取分类 ID
     *
     * @return category - 分类 ID
     */
    public Integer getCategory() {
        return category;
    }

    /**
     * 设置分类 ID
     *
     * @param category 分类 ID
     */
    public void setCategory(Integer category) {
        this.category = category;
    }

    /**
     * 获取商品名称
     *
     * @return name - 商品名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置商品名称
     *
     * @param name 商品名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取单价
     *
     * @return price - 单价
     */
    public Double getPrice() {
        return price;
    }

    /**
     * 设置单价
     *
     * @param price 单价
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * 获取上架时间
     *
     * @return publish_time - 上架时间
     */
    public Date getPublishTime() {
        return publishTime;
    }

    /**
     * 设置上架时间
     *
     * @param publishTime 上架时间
     */
    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    /**
     * 获取图片
     *
     * @return pic - 图片
     */
    public String getPics() {
        return pics;
    }

    /**
     * 设置图片
     *
     * @param pics 图片
     */
    public void setPics(String pics) {
        this.pics = pics == null ? null : pics.trim();
    }

    /**
     * 获取折扣
     *
     * @return discount - 折扣
     */
    public Double getDiscount() {
        return discount;
    }

    /**
     * 设置折扣
     *
     * @param discount 折扣
     */
    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    /**
     * 获取库存数量
     *
     * @return quantity - 库存数量
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * 设置库存数量
     *
     * @param quantity 库存数量
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * 获取商品状态
     *
     * @return status - 商品状态
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置商品状态
     *
     * @param status 商品状态
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取浏览量
     *
     * @return view_count - 浏览量
     */
    public Integer getViewCount() {
        return viewCount;
    }

    /**
     * 设置浏览量
     *
     * @param viewCount 浏览量
     */
    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    /**
     * 获取销量
     *
     * @return sale_count - 销量
     */
    public Integer getSaleCount() {
        return saleCount;
    }

    /**
     * 设置销量
     *
     * @param saleCount 销量
     */
    public void setSaleCount(Integer saleCount) {
        this.saleCount = saleCount;
    }

    /**
     * 获取商品简述
     *
     * @return describ - 商品简述
     */
    public String getDescrib() {
        return describ;
    }

    /**
     * 设置商品简述
     *
     * @param describ 商品简述
     */
    public void setDescrib(String describ) {
        this.describ = describ == null ? null : describ.trim();
    }

    /**
     * 获取副标题
     *
     * @return sub_title - 副标题
     */
    public String getSubTitle() {
        return subTitle;
    }

    /**
     * 设置副标题
     *
     * @param subTitle 副标题
     */
    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle == null ? null : subTitle.trim();
    }

    public List<String> getPicList() {
        return picList;
    }

    public void setPicList(List<String> picList) {
        this.picList = picList;
    }

    public String getMainPic() {
        return mainPic;
    }

    public void setMainPic(String mainPic) {
        this.mainPic = mainPic;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public List<String> getTagList() {
        return tagList;
    }

    public void setTagList(List<String> tagList) {
        this.tagList = tagList;
    }

    public Category getCategoryBean() {
        return categoryBean;
    }

    public void setCategoryBean(Category categoryBean) {
        this.categoryBean = categoryBean;
    }

    public int getRec() {
        return rec;
    }

    public void setRec(int rec) {
        this.rec = rec;
    }

    public int getHot() {
        return hot;
    }

    public void setHot(int hot) {
        this.hot = hot;
    }
}