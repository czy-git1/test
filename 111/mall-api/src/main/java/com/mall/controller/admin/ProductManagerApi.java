package com.mall.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.mall.controller.vo.JsonResult;
import com.mall.dao.mapper.CategoryMapper;
import com.mall.dao.mapper.HotProductMapper;
import com.mall.dao.mapper.ProductMapper;
import com.mall.dao.mapper.StorageMapper;
import com.mall.dao.model.Category;
import com.mall.dao.model.HotProduct;
import com.mall.dao.model.Product;
import com.mall.dao.model.Storage;
import com.mall.service.StorageService;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 商品管理接口
 */
@RestController
@RequestMapping("/admin/product")
public class ProductManagerApi {

    private final Logger logger = LoggerFactory.getLogger(ProductManagerApi.class);

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private HotProductMapper hotProductMapper;
    @Autowired
    private StorageService storageService;

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
        PageView<Product> page = new PageView<>();
        page = page.startPage(request);
        String kw = request.getString("kw");
        Example example = new Example(Product.class);
        Example.Criteria criteria = example.createCriteria();
        if(StringUtils.isNotBlank(kw)){
            criteria.andLike("name", kw);
        }
        example.orderBy("publishTime").desc();
        List<Product> list = productMapper.selectByExampleAndRowBounds(example, new RowBounds(page.getFirstResult(), page.getMaxresult()));
        for(Product product : list){
            if(StringUtils.isNotBlank(product.getPics())){
                String[] tags = product.getTags().split(";");
                List<String> hTags = new ArrayList<>(Arrays.asList(tags));
                product.setTagList(hTags);
            }
            product.setCategoryBean(categoryMapper.selectByPrimaryKey(product.getCategory()));
            //判断是否设置为热销或推荐商品
            Example hotExample = new Example(HotProduct.class);
            Example.Criteria hotCriteria = hotExample.createCriteria();
            hotCriteria.andEqualTo("productId", product.getProductId());
            List<HotProduct> hList = hotProductMapper.selectByExample(hotExample);
            for(HotProduct hotProduct : hList) {
                if (hotProduct.getHotType() == 1) {
                    product.setRec(1);//已设置为推荐商品
                }else if(hotProduct.getHotType() == 2){
                    product.setHot(1);//已设置为热销商品
                }
            }
        }
        page.setRecords(list);
        page.setTotalrecord(productMapper.selectCountByExample(example));
        result.setData(page);
        return result;
    }

    /**
     * 所有商品
     * @return
     */
    @RequestMapping("all")
    public JsonResult allProductList() {
        JsonResult result = new JsonResult(true, 200, "查询成功");
        List<Product> allList = productMapper.selectAll();
        result.setData(allList);
        return result;
    }

    /**
     * 商品上架与下架
     * @param id
     * @return
     */
    @RequestMapping("/delete/{id}/{optType}")
    public JsonResult delete(@PathVariable("id") Integer id, @PathVariable("optType") Integer optType) {
        JsonResult result = new JsonResult(true, 200, "商品已下架");
        Product product = productMapper.selectByPrimaryKey(id);
        if(optType == 1){
            product.setStatus(1);
            result.setMsg("商品已重新上架");
        }else{
            product.setStatus(-1);
        }
        productMapper.updateByPrimaryKeySelective(product);
        return result;
    }

    @RequestMapping("info/{id}")
    public JsonResult info(@PathVariable("id") Integer id) {
        JsonResult result = new JsonResult(true, 200, "查询成功");
        Product product = productMapper.selectByPrimaryKey(id);
        if(StringUtils.isNotBlank(product.getPics())){
            String[] tags = product.getTags().split(";");
            List<String> hTags = new ArrayList<>(Arrays.asList(tags));
            product.setTagList(hTags);

            String[] pics = product.getPics().split(",");
            List<String> picList = new ArrayList<>(Arrays.asList(pics));
            product.setPicList(picList);
        }
        product.setCategoryBean(categoryMapper.selectByPrimaryKey(product.getCategory()));
        result.setData(product);
        return result;
    }

    /**
     * 添加或修改商品
     * @param product
     * @return
     */
    @RequestMapping("save")
    public JsonResult saveOrUpdateProduct(@RequestBody Product product) {
        JsonResult result = new JsonResult(true, 200, "操作成功");
        try {
            if(product.getProductId()==null){
                product.setPublishTime(new Date());
                productMapper.insertSelective(product);
                //写入初始库存记录
                storageService.initStorage(product);
            }else{
                productMapper.updateByPrimaryKeySelective(product);
            }
        } catch (Exception e) {
            logger.error("保存商品异常", e);
            result.setMsg("更新失败");
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 设置商品为推荐
     * @param req
     * @return
     */
    @RequestMapping("/set/rec")
    public JsonResult setRecommend(@RequestBody JSONObject req) {
        JsonResult result = new JsonResult(true, 200, "设置成功");
        Integer id = req.getInteger("id");
        Integer type = req.getInteger("type");
        if(type == -1){
            //取消推荐，删除记录
            HotProduct hp = new HotProduct();
            hp.setProductId(id);
            hotProductMapper.delete(hp);
            return result;
        }
        HotProduct record = new HotProduct();
        record.setProductId(id);
        record.setHotType(type);
        int count = hotProductMapper.selectCount(record);
        if(count==0){
            record.setSort(5);//默认为5
            hotProductMapper.insertSelective(record);
        }
        return result;
    }

    /**
     * 查询类型列表
     * @param page
     * @param kw
     * @return
     */
    @RequestMapping("category/list")
    public JsonResult categoryDataList(PageView<Category> page, String kw) {
        JsonResult result = new JsonResult(true, 200, "查询成功");
        Example example = new Example(Category.class);
        Example.Criteria criteria = example.createCriteria();
        if(StringUtils.isNotBlank(kw)){
            criteria.andLike("name", kw);
        }
//        example.orderBy("lastUpdate").desc();
        List<Category> categoryList = categoryMapper.selectByExampleAndRowBounds(example, new RowBounds(page.getFirstResult(), page.getMaxresult()));
        page.setRecords(categoryList);
        page.setTotalrecord(categoryMapper.selectCountByExample(example));
        result.setData(page);
        return result;
    }

    @RequestMapping("category/list/av")
    public JsonResult categoryAvDataList() {
        JsonResult result = new JsonResult(true, 200, "查询成功");
        Example example = new Example(Category.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDelete", 1);//只查询有效状态的分类
        List<Category> categoryList = categoryMapper.selectByExample(example);
        result.setData(categoryList);
        return result;
    }

    /**
     * 删除分类
     * @param id
     * @return
     */
    @RequestMapping("/category/delete/{id}")
    public JsonResult deleteCategory(@PathVariable("id") Integer id) {
        JsonResult result = new JsonResult(true, 200, "删除成功");
        Category category = categoryMapper.selectByPrimaryKey(id);
        category.setIsDelete(-1);
        categoryMapper.updateByPrimaryKeySelective(category);
        return result;
    }

    @RequestMapping("/category/rec/{id}")
    public JsonResult recoveryCategory(@PathVariable("id") Integer id) {
        JsonResult result = new JsonResult(true, 200, "删除成功");
        Category category = categoryMapper.selectByPrimaryKey(id);
        category.setIsDelete(1);
        categoryMapper.updateByPrimaryKeySelective(category);
        return result;
    }

    @RequestMapping("category/info/{id}")
    public JsonResult categoryInfo(@PathVariable("id") Integer id) {
        JsonResult result = new JsonResult(true, 200, "查询成功");
        Category category = categoryMapper.selectByPrimaryKey(id);
        result.setData(category);
        return result;
    }

    @RequestMapping("category/save")
    public JsonResult saveOrUpdateCategory(@RequestBody Category category) {
        JsonResult result = new JsonResult(true, 200, "操作成功");
        try {
            if(category.getCategoryId()==null){
                categoryMapper.insertSelective(category);
            }else{
                categoryMapper.updateByPrimaryKeySelective(category);
            }
        } catch (Exception e) {
            logger.error("更新类型异常", e);
            result.setMsg("更新失败");
            result.setSuccess(false);
        }
        return result;
    }
}
