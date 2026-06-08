package com.mall.service;

import com.alibaba.fastjson.JSONObject;
import com.mall.dao.model.TOrder;
import com.mall.dao.model.User;
import com.mall.exception.StorageException;
import com.mall.utils.page.PageView;

import java.util.List;

public interface OrderService {

    //1-待支付；2-已支付待发货；3-已发货；4-已完成；-1：已取消
    static final Integer ORDER_STATUS_WAIT_PAY = 1;
    static final Integer ORDER_STATUS_PAID = 2;
    static final Integer ORDER_STATUS_SENT = 3;
    static final Integer ORDER_STATUS_FINISH = 4;
    static final Integer ORDER_STATUS_CANCEL = -1;

    /**
     * 生成订单预览
     * @param orderType 下单方式：1-购物车结算；2-商品详情页面立即购买
     * @param productId 商品ID
     * @param user 登录用户
     * @return
     */
    TOrder previewOrder(Integer orderType, Integer productId, User user);

    /**
     * 生成订单
     * @param user
     * @param req
     * @return
     */
    TOrder generateOrder(User user, JSONObject req) throws StorageException;

    /**
     * 订单支付
     * @param orderId
     */
    void payOrder(Integer orderId);

    /**
     * 分页查找我的订单
     * @param userId
     * @param req
     * @return
     */
    PageView<TOrder> getOrderListByUserId(Integer userId, JSONObject req);

    /**
     * 根据ID查找订单，关联查询订单Item与产品信息
     * @param orderId
     * @return
     */
    TOrder getOrderById(Integer orderId);
}
