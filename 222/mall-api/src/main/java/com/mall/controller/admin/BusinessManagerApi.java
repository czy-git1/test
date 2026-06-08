package com.mall.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.mall.controller.vo.JsonResult;
import com.mall.dao.mapper.CategoryMapper;
import com.mall.dao.mapper.HotProductMapper;
import com.mall.dao.mapper.ProductMapper;
import com.mall.dao.model.Category;
import com.mall.dao.model.HotProduct;
import com.mall.dao.model.Product;
import com.mall.utils.page.PageView;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
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
 * 运营管理接口
 */
@RestController
@RequestMapping("/admin/biz")
public class BusinessManagerApi {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private HotProductMapper hotProductMapper;

    @InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {
        //转换日期格式
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //注册自定义的编辑器
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * 查询热销/推荐列表
     * @param request
     * @return
     */
    @RequestMapping("rec/list")
    public JsonResult dataList(@RequestBody JSONObject request) {
        PageView<HotProduct> page = new PageView<>();
        page.startPage(request);
        Integer type = request.getInteger("type");
        JsonResult result = new JsonResult(true, 200, "查询成功");
        List<HotProduct> list = hotProductMapper.selectProductsList(page.getFirstResult(), page.getMaxresult(), type);
        for (HotProduct hotProduct : list) {
            if(StringUtils.isNotBlank(hotProduct.getTags())){
                String[] tagArrays = hotProduct.getTags().split(";");
                hotProduct.setTagList(Arrays.asList(tagArrays));
            }
        }
        page.setRecords(list);
        page.setTotalrecord(hotProductMapper.selectProductsCount(type));
        result.setData(page);
        return result;
    }

    /**
     * 删除热销商品
     * @param id
     * @return
     */
    @RequestMapping("/delete/{id}")
    public JsonResult delete(@PathVariable("id") Integer id) {
        JsonResult result = new JsonResult(true, 200, "删除成功");
        hotProductMapper.deleteByPrimaryKey(id);
        return result;
    }

    /**
     * 添加或修改热销推荐信息
     * @param hotProduct
     * @return
     */
    @RequestMapping("save")
    public JsonResult saveOrUpdateProduct(@RequestBody HotProduct hotProduct) {
        JsonResult result = new JsonResult(true, 200, "操作成功");
        try {
            if(hotProduct.getHotId()==null){
                hotProductMapper.insertSelective(hotProduct);
            }else{
                hotProductMapper.updateByPrimaryKeySelective(hotProduct);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("更新失败");
            result.setSuccess(false);
        }
        return result;
    }

}
