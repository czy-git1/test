package com.mall.controller.weixin;

import com.alibaba.fastjson.JSONObject;
import com.mall.controller.vo.JsonResult;
import com.mall.dao.mapper.HotProductMapper;
import com.mall.dao.mapper.SlidePicMapper;
import com.mall.dao.model.HotProduct;
import com.mall.dao.model.Product;
import com.mall.dao.model.SlidePic;
import com.mall.dao.model.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import tk.mybatis.mapper.entity.Example;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api/index")
public class IndexApi extends ApiBaseController{

    @Autowired
    private SlidePicMapper slidePicMapper;
    @Autowired
    private HotProductMapper hotProductMapper;

    @InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {
        //转换日期格式
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        //注册自定义的编辑器
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * 获取轮播图
     * @return
     */
    @RequestMapping("slide/pictures")
    public JsonResult getSlidePictures() {
        Example example = new Example(SlidePic.class);
        example.orderBy("seq").asc();
        List<SlidePic> list = slidePicMapper.selectByExample(example);
        JsonResult ret = new JsonResult();
        ret.setData(list);
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
}
