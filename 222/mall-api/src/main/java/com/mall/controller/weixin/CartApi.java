package com.mall.controller.weixin;

import com.alibaba.fastjson.JSONObject;
import com.mall.author.AuthRequired;
import com.mall.controller.vo.JsonResult;
import com.mall.dao.mapper.*;
import com.mall.dao.model.*;
import com.mall.utils.page.PageView;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import tk.mybatis.mapper.entity.Example;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api/cart")
public class CartApi extends ApiBaseController{

    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private FavoriteMapper favoriteMapper;

    @InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {
        //转换日期格式
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        //注册自定义的编辑器
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * 查询购物车商品
     * @return
     */
    @RequestMapping("list")
    @AuthRequired
    public JsonResult getCartList(@RequestBody JSONObject req, HttpServletRequest request) {
        User user = (User) request.getAttribute("loginUser");
        Example example = new Example(Cart.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", user.getUserId());
        example.orderBy("cartId").desc();
        List<Cart> list = cartMapper.selectByExample(example);
        for (Cart cart : list) {
            cart.setProduct(productMapper.selectByPrimaryKey(cart.getProductId()));
        }
        JsonResult ret = new JsonResult();
        ret.setData(list);
        return ret;
    }

    @RequestMapping("count")
    @AuthRequired
    public JsonResult getCartCount(@RequestBody JSONObject req, HttpServletRequest request) {
        User user = (User) request.getAttribute("loginUser");
        JsonResult ret = new JsonResult();
        if(user == null){
            ret.setData(0);
            return ret;
        }
        Example example = new Example(Cart.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", user.getUserId());
        int cartCount = cartMapper.selectCountByExample(example);
        ret.setData(cartCount);
        return ret;
    }

    /**
     * 加入购物车
     * @return
     */
    @RequestMapping("add")
    @AuthRequired
    public JsonResult addToCart(@RequestBody JSONObject req, HttpServletRequest request) {
        User user = (User) request.getAttribute("loginUser");
        Integer productId = req.getInteger("productId");
        Cart record = new Cart();
        record.setUserId(user.getUserId());
        record.setProductId(productId);
        if(cartMapper.selectCount(record)>0){
            //防止重复加入购物车，只增加数据
            record = cartMapper.selectOne(record);
            record.setQuantity(record.getQuantity()+1);
            cartMapper.updateByPrimaryKeySelective(record);
            return JsonResult.success();
        }
        Cart cart = new Cart();
        cart.setProductId(productId);
        cart.setUserId(user.getUserId());
        cart.setQuantity(1);
        cart.setIsSelect(1);
        cartMapper.insert(cart);
        JsonResult ret = new JsonResult();
        ret.setSuccess(true);
        ret.setData(cart);
        return ret;
    }

    /**
     * 全选
     * @param req
     * @param request
     * @return
     */
    @RequestMapping("select/all")
    public JsonResult selectAll(@RequestBody JSONObject req, HttpServletRequest request) {
        User user = (User) request.getAttribute("loginUser");
        cartMapper.selectAllItem(user.getUserId());
        JsonResult ret = new JsonResult();
        ret.setSuccess(true);
        return ret;
    }

    /**
     * 全部取消
     * @param req
     * @param request
     * @return
     */
    @RequestMapping("select/dis/all")
    public JsonResult disSelectAll(@RequestBody JSONObject req, HttpServletRequest request) {
        User user = (User) request.getAttribute("loginUser");
        cartMapper.disSelectAll(user.getUserId());
        JsonResult ret = new JsonResult();
        ret.setSuccess(true);
        return ret;
    }

    /**
     * 勾选
     * @param req
     * @return
     */
    @RequestMapping("select")
    public JsonResult selectCart(@RequestBody JSONObject req, HttpServletRequest request) {
        Integer cartId = req.getInteger("cartId");
        Integer isSelect = req.getInteger("isSelect");
        cartMapper.selectCart(cartId, isSelect);
        JsonResult ret = new JsonResult();
        ret.setSuccess(true);
        return ret;
    }

    /**
     * 取消选择
     * @param req
     * @return
     */
    @RequestMapping("select/dis")
    public JsonResult disSelectCart(@RequestBody JSONObject req, HttpServletRequest request) {
        Integer cartId = req.getInteger("cartId");
        Integer isSelect = req.getInteger("isSelect");
        cartMapper.selectCart(cartId, isSelect);
        JsonResult ret = new JsonResult();
        ret.setSuccess(true);
        return ret;
    }

    /**
     * 移出购物车
     * @param req
     * @return
     */
    @RequestMapping("remove")
    public JsonResult removeCart(@RequestBody JSONObject req) {
        Integer cartId = req.getInteger("cartId");
        cartMapper.deleteByPrimaryKey(cartId);
        JsonResult ret = new JsonResult();
        ret.setSuccess(true);
        return ret;
    }

    /**
     * 增加数量
     * @param req
     * @return
     */
    @RequestMapping("add/quantity")
    public JsonResult addQuantity(@RequestBody JSONObject req) {
        Integer cartId = req.getInteger("cartId");
        Cart cart = cartMapper.selectByPrimaryKey(cartId);
        cart.setQuantity(cart.getQuantity() + 1);
        cartMapper.updateByPrimaryKeySelective(cart);
        JsonResult ret = new JsonResult();
        ret.setSuccess(true);
        ret.setData(cart);
        return ret;
    }

    /**
     * 减少数据
     * @param req
     * @return
     */
    @RequestMapping("minus/quantity")
    public JsonResult minusQuantity(@RequestBody JSONObject req) {
        Integer cartId = req.getInteger("cartId");
        Cart cart = cartMapper.selectByPrimaryKey(cartId);
        cart.setQuantity(cart.getQuantity() - 1);
        cartMapper.updateByPrimaryKeySelective(cart);
        JsonResult ret = new JsonResult();
        ret.setSuccess(true);
        ret.setData(cart);
        return ret;
    }

    /**
     * 收藏商品
     * @param req
     * @param request
     * @return
     */
    @RequestMapping("fav")
    @AuthRequired
    public JsonResult favProduct(@RequestBody JSONObject req, HttpServletRequest request) {
        User user = (User) request.getAttribute("loginUser");
        Integer productId = req.getInteger("productId");
        Favorite fav = new Favorite();
        fav.setUserId(user.getUserId());
        fav.setProductId(productId);
        if(favoriteMapper.selectCount(fav)==0){
            favoriteMapper.insert(fav);
        }
        JsonResult ret = new JsonResult();
        ret.setSuccess(true);
        ret.setData(fav);
        return ret;
    }
}
