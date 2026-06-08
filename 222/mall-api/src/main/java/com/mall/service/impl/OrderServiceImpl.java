package com.mall.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mall.dao.mapper.*;
import com.mall.dao.model.*;
import com.mall.exception.StorageException;
import com.mall.service.OrderService;
import com.mall.service.StorageService;
import com.mall.utils.NumberUtils;
import com.mall.utils.page.PageView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private AddressMapper addressMapper;
    @Autowired
    private TOrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private StorageService storageService;
    //预览订单
    private final Map<String, TOrder> previewOrders = new HashMap<String, TOrder>();

    @Override
    public TOrder previewOrder(Integer orderType, Integer productId, User user) {
        TOrder order = new TOrder();
        order.setUserId(user.getUserId());
        order.setStatus(OrderService.ORDER_STATUS_WAIT_PAY);
        order.setOrderCode(NumberUtils.genBillNo());//生成订单号
        if(orderType == 1){
            //购物车结算，从数据库中获取该用户已勾选的商品列表
            List<Cart> cartList = cartMapper.selectCartByUserSelected(user.getUserId());
            List<OrderItem> orderItemList = new ArrayList<>();
            double totalAmount = 0;
            int totalNum = 0;
            for(Cart cart : cartList){
                Product product = productMapper.selectByPrimaryKey(cart.getProductId());
                OrderItem orderItem = new OrderItem();
                orderItem.setProductId(cart.getProductId());
                orderItem.setQuantity(cart.getQuantity());
                orderItem.setPrice(product.getPrice());
                orderItem.setProduct(product);
                orderItemList.add(orderItem);
                totalAmount += product.getPrice()*cart.getQuantity();
                totalNum += cart.getQuantity();
            }
            order.setOrderItems(orderItemList);
            order.setTotalNum(totalNum);
            order.setTotalPrice(totalAmount);
        }else{
            //商品详情页面点击“立即购买”，生成只有一个商品的订单
            List<OrderItem> orderItemList = new ArrayList<>();
            Product product = productMapper.selectByPrimaryKey(productId);
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(productId);
            orderItem.setProduct(product);
            orderItem.setQuantity(1);
            orderItem.setPrice(product.getPrice());
            orderItemList.add(orderItem);
            order.setOrderItems(orderItemList);
            order.setTotalPrice(product.getPrice()*orderItem.getQuantity());
            order.setTotalNum(1);
        }
        previewOrders.put(order.getOrderCode(), order);//临时保存预览订单
        return order;
    }

    @Override
    public TOrder generateOrder(User user, JSONObject req) throws StorageException {
        Integer addressId = req.getInteger("addressId");
        String orderCode = req.getString("orderCode");
        String remark = req.getString("remark");
        TOrder order = previewOrders.get(orderCode);
        List<OrderItem> orderItemList = order.getOrderItems();
        //先判断库存是否充足
        for(OrderItem orderItem : orderItemList){
            Storage storage = storageService.findLastStorage(orderItem.getProductId());
            if(storage == null){
                throw new StorageException("库存不足");
            }else{
                if(storage.getNewQuantity() < orderItem.getQuantity()){
                    throw new StorageException("库存不足");
                }
            }
        }

        Address address = addressMapper.selectByPrimaryKey(addressId);
        order.setOrderTime(new Date());
        order.setRemark(remark);
        order.setFullAddress(address.getArea()+address.getDetail());
        order.setReceiveName(address.getContact());
        order.setMobile(address.getMobile());
        orderMapper.insertSelective(order);
        for(OrderItem orderItem : orderItemList){
            orderItem.setOrderId(order.getOrderId());
            orderItem.setIscomment(0);//设置初始值为未评论
            orderItemMapper.insertSelective(orderItem);
            //从购物车移除
            Cart cart = new Cart();
            cart.setProductId(orderItem.getProductId());
            cart.setUserId(user.getUserId());
            cartMapper.delete(cart);
            //减少库存
            storageService.minusStorage(orderItem.getProductId(), orderItem.getQuantity(), "商品销售出库");
        }
        return order;
    }

    @Override
    public void payOrder(Integer orderId) {
        TOrder order = orderMapper.selectByPrimaryKey(orderId);
        order.setStatus(OrderService.ORDER_STATUS_PAID);
        orderMapper.updateByPrimaryKeySelective(order);
    }

    @Override
    public PageView<TOrder> getOrderListByUserId(Integer userId, JSONObject req) {
        PageView<TOrder> pageView = new PageView<>();
        pageView =  pageView.startPageWxmp(req);
        List<TOrder> list = orderMapper.selectOrderByUserId(userId, pageView.getFirstResult(), pageView.getMaxresult());
        //生成订单概况
        for(TOrder order : list){
            StringBuilder orderInfo = new StringBuilder();
            List<OrderItem> items = order.getOrderItems();
            for(OrderItem item : items){
                orderInfo.append(item.getProduct().getName()).append("; ");
                if(orderInfo.length()>30){
                    break;
                }
            }
            if(orderInfo.length()>30){
                String productDesc = orderInfo.substring(0, 30);
                productDesc = productDesc + "...等共"+items.size()+"件商品";
                order.setOrderInfo(productDesc);
            }else{
                order.setOrderInfo(orderInfo.toString());
            }
        }
        pageView.setRecords(list);
        pageView.setTotalrecord(orderMapper.selectOrderCountByUserId(userId));
        return pageView;
    }

    @Override
    public TOrder getOrderById(Integer orderId) {
        TOrder order = orderMapper.selectOrderById(orderId);
        StringBuilder orderInfo = new StringBuilder();
        List<OrderItem> items = order.getOrderItems();
        for(OrderItem item : items){
            orderInfo.append(item.getProduct().getName()).append("; ");
            if(orderInfo.length()>30){
                break;
            }
        }
        if(orderInfo.length()>30){
            String productDesc = orderInfo.substring(0, 30);
            productDesc = productDesc + "...等共"+items.size()+"件商品";
            order.setOrderInfo(productDesc);
        }else{
            order.setOrderInfo(orderInfo.toString());
        }
        return order;
    }
}
