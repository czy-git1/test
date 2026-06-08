package com.mall.dao.model;

import java.util.Date;
import javax.persistence.*;

public class Comment {
    @Id
    @Column(name = "comment_id")
    private Integer commentId;

    /**
     * 评论者的ID
     */
    private Integer user;

    /**
     * 商品ID
     */
    @Column(name = "product_id")
    private Integer productId;

    /**
     * 标题
     */
    private String title;

    /**
     * 发布时间
     */
    @Column(name = "publish_time")
    private Date publishTime;

    /**
     * 关联的订单ID
     */
    @Column(name = "order_id")
    private Integer orderId;

    @Column(name = "order_item_id")
    private Integer orderItemId;

    /**
     * 评论内容 
     */
    private String content;

    /**
     * @return comment_id
     */
    public Integer getCommentId() {
        return commentId;
    }

    /**
     * @param commentId
     */
    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    /**
     * 获取评论者的ID
     *
     * @return user - 评论者的ID
     */
    public Integer getUser() {
        return user;
    }

    /**
     * 设置评论者的ID
     *
     * @param user 评论者的ID
     */
    public void setUser(Integer user) {
        this.user = user;
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
     * 获取标题
     *
     * @return title - 标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置标题
     *
     * @param title 标题
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * 获取发布时间
     *
     * @return publish_time - 发布时间
     */
    public Date getPublishTime() {
        return publishTime;
    }

    /**
     * 设置发布时间
     *
     * @param publishTime 发布时间
     */
    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    /**
     * 获取关联的订单ID
     *
     * @return order_id - 关联的订单ID
     */
    public Integer getOrderId() {
        return orderId;
    }

    /**
     * 设置关联的订单ID
     *
     * @param orderId 关联的订单ID
     */
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    /**
     * 获取评论内容 
     *
     * @return content - 评论内容 
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置评论内容 
     *
     * @param content 评论内容 
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Integer getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Integer orderItemId) {
        this.orderItemId = orderItemId;
    }
}