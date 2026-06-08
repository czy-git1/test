package com.mall.controller.admin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mall.controller.vo.JsonResult;
import com.mall.dao.mapper.HotProductMapper;
import com.mall.dao.mapper.ProductMapper;
import com.mall.dao.mapper.TOrderMapper;
import com.mall.dao.model.*;
import com.mall.service.OrderService;
import com.mall.utils.page.PageView;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import tk.mybatis.mapper.entity.Example;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 订单管理接口
 */
@RestController
@RequestMapping("/admin/order")
public class AdminOrderManagerApi {

    private final Logger logger = LoggerFactory.getLogger(AdminOrderManagerApi.class);
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private OrderService orderService;
    @Autowired
    private TOrderMapper orderMapper;

    @InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {
        //转换日期格式
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //注册自定义的编辑器
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * 查询列表
     * @param request
     * @return
     */
    @RequestMapping("list")
    public JsonResult dataList(@RequestBody JSONObject request) {
        JsonResult result = new JsonResult(true, 200, "查询成功");
        PageView<TOrder> page = new PageView<>();
        page = page.startPage(request);
        String kw = request.getString("kw");
        List<TOrder> list = orderMapper.selectOrderList(kw, page.getFirstResult(), page.getMaxresult());
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
        page.setRecords(list);
        page.setTotalrecord(orderMapper.selectOrderCount(kw));
        result.setData(page);
        return result;
    }

    /**
     * 取消订单
     * @param id
     * @return
     */
    @RequestMapping("/cancel/{id}")
    public JsonResult cancelOrder(@PathVariable("id") Integer id) {
        JsonResult result = new JsonResult(true, 200, "订单已取消");
        TOrder order = orderMapper.selectByPrimaryKey(id);
        order.setStatus(OrderService.ORDER_STATUS_CANCEL);
        orderMapper.updateByPrimaryKeySelective(order);
        return result;
    }

    /**
     * 订单详情
     * @param id
     * @return
     */
    @RequestMapping("info/{id}")
    public JsonResult orderInfo(@PathVariable("id") Integer id) {
        TOrder tOrder = orderService.getOrderById(id);
        JsonResult ret = new JsonResult();
        ret.setData(tOrder);
        return ret;
    }

    /**
     * 订单发货
     * @param req
     * @return
     */
    @RequestMapping("send")
    public JsonResult saveOrUpdateProduct(@RequestBody JSONObject req) {
        JsonResult result = new JsonResult(true, 200, "操作成功");
        try {
            TOrder order = orderMapper.selectByPrimaryKey(req.getInteger("orderId"));
            order.setStatus(OrderService.ORDER_STATUS_SENT);//发货状态
            if(req.containsKey("expNo")){
                order.setExpNo(req.getString("expNo"));//快递单号
            }
            if(req.containsKey("remark")){
                order.setRemark(req.getString("remark"));//快递单号
            }
            orderMapper.updateByPrimaryKeySelective(order);
        } catch (Exception e) {
            logger.error("发货失败", e);
            result.setMsg("操作失败");
            result.setSuccess(false);
        }
        return result;
    }

    @RequestMapping("chart/data")
    public JsonResult getChartData(@RequestBody JSONObject req) {
        JsonResult result = new JsonResult(true, 200, "操作成功");
        Date startDate = req.getJSONArray("startDate").getDate(0);
        Date endDate = req.getJSONArray("startDate").getDate(1);
        List<Map<String, Object>> dayData = orderMapper.selectOrderDateCount(startDate, endDate);
        //从数据查询时是以日期进行分组，所以无法排序，需要重新按日期做一次排序
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");//日期格式
        dayData.sort(new Comparator<Map<String, Object>>() {
            //排序比较器
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                Date date1 = (Date) o1.get("odate");
                Date date2 = (Date) o2.get("odate");
                System.out.println(date1 + " " + date2);
                if (date1.before(date2)) {
                    return -1;
                } else if (date1.equals(date2)) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });
        //[{value:11,name:'A'}, {}]
        //排完序后的数据，分别放到X轴与Y轴的数组中
        JSONArray orderCountData = new JSONArray();
        for (int idx = 0; idx < dayData.size(); idx++) {
            Map<String, Object> item = dayData.get(idx);
            JSONObject orderInfo = new JSONObject();
            orderInfo.put("value", item.get("total"));
            orderInfo.put("name", item.get("odate").toString().substring(5));
            orderCountData.add(orderInfo);
        }



        List<Map<String, Object>> dayDataAmount = orderMapper.selectOrderAmountCount(startDate, endDate);
        //从数据查询时是以日期进行分组，所以无法排序，需要重新按日期做一次排序
        dayDataAmount.sort(new Comparator<Map<String, Object>>() {
            //排序比较器
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                Date date1 = (Date) o1.get("odate");
                Date date2 = (Date) o2.get("odate");
                System.out.println(date1 + " " + date2);
                if (date1.before(date2)) {
                    return -1;
                } else if (date1.equals(date2)) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });
        //[{value:11,name:'A'}, {}]
        //排完序后的数据，分别放到X轴与Y轴的数组中
        JSONArray AmountData = new JSONArray();
        for (int idx = 0; idx < dayDataAmount.size(); idx++) {
            Map<String, Object> item = dayDataAmount.get(idx);
            JSONObject orderInfo = new JSONObject();
            orderInfo.put("value", item.get("total"));
            orderInfo.put("name", item.get("odate").toString().substring(5));
            AmountData.add(orderInfo);
        }




        JSONObject data = new JSONObject();
        data.put("orderCountData", orderCountData);
        data.put("orderAmountData", AmountData);
        result.setData(data);
        return result;
    }
}
