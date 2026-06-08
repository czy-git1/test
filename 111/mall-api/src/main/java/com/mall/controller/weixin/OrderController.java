package com.mall.controller.weixin;

import com.alibaba.fastjson.JSONObject;
import com.mall.author.AuthRequired;
import com.mall.controller.vo.JsonResult;
import com.mall.dao.mapper.*;
import com.mall.dao.model.*;
import com.mall.exception.StorageException;
import com.mall.service.OrderService;
import com.mall.utils.page.PageView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import jakarta.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 微信小程序接口：预约订单相关
 */
@RestController
@RequestMapping("api/order")
public class OrderController extends ApiBaseController{

    private final Logger logger = LoggerFactory.getLogger(OrderController.class);
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private TOrderMapper tOrderMapper;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;

    @InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {
        //转换日期格式
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        //注册自定义的编辑器
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * 订单预览数据，进入订单确认页面
     * @return
     */
    @RequestMapping("preview")
    @AuthRequired
    public JsonResult preview(@RequestBody JSONObject req, HttpServletRequest request) {
        JsonResult ret = new JsonResult();
        User user = (User) request.getAttribute("loginUser");
        Integer orderType = req.getInteger("orderType");//1表示购物车结算，2表示商品详情页面点“立即购买”
        Integer productId = req.getInteger("productId");
        TOrder order = orderService.previewOrder(orderType, productId, user);//生成预览订单，未插入数据库
        ret.setSuccess(true);
        ret.setData(order);
        return ret;
    }

    /**
     * 提交并生成订单
     * @param req
     * @param request
     * @return
     */
    @RequestMapping("save")
    @AuthRequired
    public JsonResult submitOrder(@RequestBody JSONObject req, HttpServletRequest request) {
        JsonResult ret = new JsonResult();
        User user = (User) request.getAttribute("loginUser");
        try {
            TOrder order = orderService.generateOrder(user, req);//生成预览订单，未插入数据库
            ret.setSuccess(true);
            ret.setData(order);
        } catch (StorageException e) {
            logger.error("生成订单发生异常！", e);
            ret.setSuccess(false);
            ret.setMsg("库存不足！");
        }
        return ret;
    }

    /**
     * 支付订单
     * @param orderId
     * @param request
     * @return
     */
    @RequestMapping("pay")
    @AuthRequired
    public JsonResult payOrder(@RequestParam Integer orderId, HttpServletRequest request) {
        JsonResult ret = new JsonResult();
        try {
            orderService.payOrder(orderId);
            ret.setSuccess(true);
        } catch (Exception e) {
            ret.setSuccess(false);
            ret.setMsg(e.getMessage());
        }
        return ret;
    }

    /**
     * 我的订单列表
     * @param req
     * @param request
     * @return
     */
    @RequestMapping("myorders")
    @AuthRequired
    public JsonResult myOrders(@RequestBody JSONObject req, HttpServletRequest request) {
        JsonResult ret = new JsonResult();
        User user = (User) request.getAttribute("loginUser");
        PageView<TOrder> pageView = orderService.getOrderListByUserId(user.getUserId(), req);
        ret.setSuccess(true);
        ret.setData(pageView);
        return ret;
    }

    /**
     * 读取订单详情
     * @param oid
     * @return
     */
    @RequestMapping("detail/{oid}")
    public JsonResult getOrderDetail(@PathVariable("oid") Integer oid) {
        TOrder tOrder = orderService.getOrderById(oid);
        JsonResult ret = new JsonResult();
        ret.setData(tOrder);
        return ret;
    }

    /**
     * 完成订单
     * @param oid
     * @return
     */
    @RequestMapping("complete/{oid}")
    public JsonResult completeOrder(@PathVariable("oid") Integer oid) {
        TOrder tOrder = orderService.getOrderById(oid);
        tOrder.setStatus(OrderService.ORDER_STATUS_FINISH);
        tOrderMapper.updateByPrimaryKeySelective(tOrder);
        JsonResult ret = new JsonResult();
        ret.setData(tOrder);
        return ret;
    }

    /**
     * 取消订单
     * @param oid
     * @return
     */
    @RequestMapping("cancel/{oid}")
    public JsonResult cancelOrder(@PathVariable("oid") Integer oid) {
        TOrder tOrder = orderService.getOrderById(oid);
        tOrder.setStatus(OrderService.ORDER_STATUS_CANCEL);
        tOrderMapper.updateByPrimaryKeySelective(tOrder);
        JsonResult ret = new JsonResult();
        ret.setData(tOrder);
        return ret;
    }

    /**
     * 发布评论
     * @param req
     * @param request
     * @return
     */
    @RequestMapping("feedback/save")
    @AuthRequired
    public JsonResult saveFeedback(@RequestBody JSONObject req, HttpServletRequest request) {
        JsonResult ret = new JsonResult();
        User user = (User) request.getAttribute("loginUser");
        try {
            Comment comment = JSONObject.toJavaObject(req, Comment.class);
            comment.setUser(user.getUserId());
            comment.setPublishTime(new Date());
            commentMapper.insertSelective(comment);//插入数据库
            //将对应的order_item评论字段置为1
            OrderItem orderItem = orderItemMapper.selectByPrimaryKey(comment.getOrderItemId());
            orderItem.setIscomment(1);
            orderItemMapper.updateByPrimaryKeySelective(orderItem);//更新
        } catch (Exception e) {
            ret.setMsg(e.getMessage());
            ret.setSuccess(false);
            return ret;
        }
        ret.setSuccess(true);
        return ret;
    }
}
