package com.mall.dao.model;

import java.util.Date;
import java.util.List;
import javax.persistence.*;

@Table(name = "t_order")
public class TOrder {
    /**
     * 主键 
     */
    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;

    /**
     * 订单号
     */
    @Column(name = "order_code")
    private String orderCode;

    /**
     * 会员ID
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 预订时间
     */
    @Column(name = "order_time")
    private Date orderTime;

    /**
     * 订单总额
     */
    @Column(name = "total_price")
    private Double totalPrice;

    @Transient
    private Integer totalNum;

    /**
     * 收货人
     */
    @Column(name = "receive_name")
    private String receiveName;

    /**
     * 收货地址
     */
    @Column(name = "full_address")
    private String fullAddress;

    @Transient
    private List<OrderItem> orderItems;

    @Transient
    private String orderInfo;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 1-待支付；2-已支付待发货；3-已发货；4-已完成；-1：已取消
     */
    private Integer status;

    /**
     * 快递单号
     */
    @Column(name = "exp_no")
    private String expNo;

    /**
     * 备注
     */
    private String remark;

    /**
     * 获取主键 
     *
     * @return order_id - 主键 
     */
    public Integer getOrderId() {
        return orderId;
    }

    /**
     * 设置主键 
     *
     * @param orderId 主键 
     */
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    /**
     * 获取订单号
     *
     * @return order_code - 订单号
     */
    public String getOrderCode() {
        return orderCode;
    }

    /**
     * 设置订单号
     *
     * @param orderCode 订单号
     */
    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode == null ? null : orderCode.trim();
    }

    /**
     * 获取会员ID
     *
     * @return user_id - 会员ID
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置会员ID
     *
     * @param userId 会员ID
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取预订时间
     *
     * @return order_time - 预订时间
     */
    public Date getOrderTime() {
        return orderTime;
    }

    /**
     * 设置预订时间
     *
     * @param orderTime 预订时间
     */
    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    /**
     * 获取订单总额
     *
     * @return total_price - 订单总额
     */
    public Double getTotalPrice() {
        return totalPrice;
    }

    /**
     * 设置订单总额
     *
     * @param totalPrice 订单总额
     */
    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    /**
     * 获取收货人
     *
     * @return receive_name - 收货人
     */
    public String getReceiveName() {
        return receiveName;
    }

    /**
     * 设置收货人
     *
     * @param receiveName 收货人
     */
    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName == null ? null : receiveName.trim();
    }

    /**
     * 获取收货地址
     *
     * @return full_address - 收货地址
     */
    public String getFullAddress() {
        return fullAddress;
    }

    /**
     * 设置收货地址
     *
     * @param fullAddress 收货地址
     */
    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress == null ? null : fullAddress.trim();
    }

    /**
     * 获取手机号
     *
     * @return mobile - 手机号
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * 设置手机号
     *
     * @param mobile 手机号
     */
    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    /**
     * 获取1-待支付；2-已支付待发货；3-已发货；4-已完成；-1：已取消
     *
     * @return status - 1-待支付；2-已支付待发货；3-已发货；4-已完成；-1：已取消
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置1-待支付；2-已支付待发货；3-已发货；4-已完成；-1：已取消
     *
     * @param status 1-待支付；2-已支付待发货；3-已发货；4-已完成；-1：已取消
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取快递单号
     *
     * @return exp_no - 快递单号
     */
    public String getExpNo() {
        return expNo;
    }

    /**
     * 设置快递单号
     *
     * @param expNo 快递单号
     */
    public void setExpNo(String expNo) {
        this.expNo = expNo == null ? null : expNo.trim();
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

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public String getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(String orderInfo) {
        this.orderInfo = orderInfo;
    }
}