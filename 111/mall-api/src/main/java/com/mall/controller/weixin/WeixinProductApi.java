package com.mall.controller.weixin;

import com.alibaba.fastjson.JSONObject;
import com.mall.author.AuthRequired;
import com.mall.controller.vo.JsonResult;
import com.mall.dao.mapper.*;
import com.mall.dao.model.Category;
import com.mall.dao.model.HotProduct;
import com.mall.dao.model.Product;
import com.mall.dao.model.User;
import com.mall.utils.page.PageView;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import tk.mybatis.mapper.entity.Example;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api/product")
public class WeixinProductApi extends ApiBaseController{

    @Autowired
    private SlidePicMapper slidePicMapper;
    @Autowired
    private HotProductMapper hotProductMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private UserMapper userMapper;

    @InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {
        //转换日期格式
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        //注册自定义的编辑器
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * 按分类获取产品列表
     * @return
     */
    @RequestMapping("list")
    public JsonResult getProductByCategory(@RequestBody JSONObject req) {
        PageView<Product> pageView = new PageView<>();
        pageView =  pageView.startPageWxmp(req);
        Integer category = req.getInteger("category");
        List<Product> list = productMapper.selectProductByCategory(category, pageView.getFirstResult(), pageView.getMaxresult());
        pageView.setRecords(list);
        Integer total = productMapper.selectProductCountByCategory(category);
        pageView.setTotalrecord(total);
        JsonResult ret = new JsonResult();
        ret.setData(pageView);
        return ret;
    }

    /**
     * 搜索商品
     * @param req
     * @return
     */
    @RequestMapping("search")
    public JsonResult searchProduct(@RequestBody JSONObject req) {
        Example example = new Example(Product.class);
        Example.Criteria criteria = example.createCriteria();
        String kw = req.getString("kw");
        if(StringUtils.isNotBlank(kw)){
            criteria.andLike("name", "%"+kw+"%");
        }
        List<Product> list = productMapper.selectByExample(example);
        JsonResult ret = new JsonResult();
        ret.setData(list);
        return ret;
    }

    /**
     * 商品详情
     * @param pid
     * @return
     */
    @RequestMapping("detail/{pid}")
    public JsonResult getProductDetail(@PathVariable("pid") Integer pid) {
        Product product = productMapper.selectByPrimaryKey(pid);
        String[] tags = product.getTags().split(";");
        product.setTagList(Arrays.asList(tags));
        String[] pics = product.getPics().split(",");
        product.setPicList(Arrays.asList(pics));
        JsonResult ret = new JsonResult();
        ret.setData(product);
        return ret;
    }

    /**
     * 查询所有分类
     * @return
     */
    @RequestMapping("category/list")
    public JsonResult allCategory() {
        Example example = new Example(Category.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDelete", 1);
        example.setOrderByClause("sort");
        List<Category> list = categoryMapper.selectByExample(example);
        JsonResult ret = new JsonResult();
        ret.setData(list);
        return ret;
    }

    @RequestMapping("category/{cid}")
    public JsonResult categoryDetail(@PathVariable("cid") Integer cid) {
        Category category = categoryMapper.selectByPrimaryKey(cid);
        JsonResult ret = new JsonResult();
        ret.setData(category);
        return ret;
    }

    /**
     * 热销产品
     * @param reqJson
     * @param request
     * @return
     */
    @RequestMapping("hot/products")
    public JsonResult getHotProducts(@RequestBody JSONObject reqJson, HttpServletRequest request) {
        List<HotProduct> list = hotProductMapper.selectHotProducts();
        JsonResult ret = new JsonResult();
        ret.setData(list);
        return ret;
    }

    /**
     * 推荐产品
     * @param reqJson
     * @param request
     * @return
     */
    @RequestMapping("rec/products")
    public JsonResult getRecProducts(@RequestBody JSONObject reqJson, HttpServletRequest request) {
        List<HotProduct> list = hotProductMapper.selectRecProducts();
        JsonResult ret = new JsonResult();
        ret.setData(list);
        return ret;
    }

    /**
     * 查询个人信息，用于实时更新小程序的缓存
     * @param request
     * @return
     */
    @PostMapping("load/user")
    @AuthRequired
    public JsonResult loadUserInfo(HttpServletRequest request) {
        JsonResult jr = new JsonResult();
        User user = (User) request.getAttribute("loginUser");
        User user2 = userMapper.selectByPrimaryKey(user.getUserId());
        jr.setData(user2);
        return jr;
    }
}
