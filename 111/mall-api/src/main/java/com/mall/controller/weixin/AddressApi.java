package com.mall.controller.weixin;

import com.alibaba.fastjson.JSONObject;
import com.mall.author.AuthRequired;
import com.mall.controller.vo.JsonResult;
import com.mall.dao.mapper.AddressMapper;
import com.mall.dao.mapper.CartMapper;
import com.mall.dao.mapper.ProductMapper;
import com.mall.dao.model.*;
import com.mall.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import tk.mybatis.mapper.entity.Example;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 小程序地址管理接口
 */
@RestController
@RequestMapping("api/address")
public class AddressApi extends ApiBaseController{

    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private OrderService orderService;
    @Autowired
    private AddressMapper addressMapper;

    @InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {
        //转换日期格式
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        //注册自定义的编辑器
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * 查询我的地址列表
     * @return
     */
    @RequestMapping("list")
    @AuthRequired
    public JsonResult getAddressList(@RequestBody JSONObject req, HttpServletRequest request) {
        User user = (User) request.getAttribute("loginUser");
        Example example = new Example(Address.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", user.getUserId());
        example.orderBy("isDefault").desc();
        List<Address> list = addressMapper.selectByExample(example);
        JsonResult ret = new JsonResult();
        ret.setData(list);
        return ret;
    }

    /**
     * 订单预览数据，进入订单确认页面
     * @return
     */
    @RequestMapping("save")
    @AuthRequired
    public JsonResult save(@RequestBody JSONObject req, HttpServletRequest request) {
        JsonResult ret = new JsonResult();
        User user = (User) request.getAttribute("loginUser");
        Address address = JSONObject.toJavaObject(req, Address.class);
        //如果设置了默认，要把当前的默认去掉
        if(address.getIsDefault()==1){
            Address record = new Address();
            record.setUserId(user.getUserId());
            record.setIsDefault(1);
            if(addressMapper.selectCount(record) > 0){
                List<Address> defaultAddress = addressMapper.select(record);
                for (Address add:defaultAddress){
                    add.setIsDefault(0);
                    addressMapper.updateByPrimaryKeySelective(add);
                }
            }
        }
        if(address.getAddressId()!=null){
            addressMapper.updateByPrimaryKeySelective(address);
        }else {
            address.setUserId(user.getUserId());
            addressMapper.insertSelective(address);
        }
        ret.setSuccess(true);
        ret.setData(address);
        return ret;
    }

    /**
     * 更新地址
     * @param req
     * @param request
     * @return
     */
    @RequestMapping("update")
    @AuthRequired
    public JsonResult update(@RequestBody JSONObject req, HttpServletRequest request) {
        JsonResult ret = new JsonResult();
        Address address = JSONObject.toJavaObject(req, Address.class);
        addressMapper.updateByPrimaryKeySelective(address);
        ret.setSuccess(true);
        ret.setData(address);
        return ret;
    }

    /**
     * 删除地址
     * @param req
     * @param request
     * @return
     */
    @RequestMapping("delete")
    @AuthRequired
    public JsonResult delete(@RequestBody JSONObject req, HttpServletRequest request) {
        Integer id = req.getInteger("id");
        addressMapper.deleteByPrimaryKey(id);
        JsonResult ret = new JsonResult();
        ret.setSuccess(true);
        return ret;
    }

    /**
     * 读取详情
     * @param id
     * @return
     */
    @RequestMapping("detail/{id}")
    public JsonResult addressDetail(@PathVariable("id") Integer id) {
        Address address = addressMapper.selectByPrimaryKey(id);
        JsonResult ret = new JsonResult();
        ret.setData(address);
        return ret;
    }
}
