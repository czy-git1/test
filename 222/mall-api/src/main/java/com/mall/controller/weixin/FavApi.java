package com.mall.controller.weixin;

import com.alibaba.fastjson.JSONObject;
import com.mall.author.AuthRequired;
import com.mall.controller.vo.JsonResult;
import com.mall.dao.mapper.*;
import com.mall.dao.model.Cart;
import com.mall.dao.model.Favorite;
import com.mall.dao.model.Product;
import com.mall.dao.model.User;
import com.mall.utils.page.PageView;
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

@RestController
@RequestMapping("api/fav")
public class FavApi extends ApiBaseController{

    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;
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
     * 查询收藏夹列表
     * @return
     */
    @RequestMapping("list")
    @AuthRequired
    public JsonResult getFavList(@RequestBody JSONObject req, HttpServletRequest request) {
        User user = (User) request.getAttribute("loginUser");
        PageView<Favorite> pageView = new PageView<>();
        pageView =  pageView.startPageWxmp(req);
        List<Favorite> list = favoriteMapper.selectMyFav(user.getUserId(), pageView.getFirstResult(), pageView.getMaxresult());
        pageView.setRecords(list);
        Integer total = favoriteMapper.selectMyFavCount(user.getUserId());
        pageView.setTotalrecord(total);
        JsonResult ret = new JsonResult();
        ret.setData(pageView);
        return ret;
    }

    /**
     * 移出收藏夹
     * @param fav
     * @return
     */
    @RequestMapping("remove/{fav}")
    public JsonResult removeFavorite(@PathVariable("fav") Integer fav) {
        favoriteMapper.deleteByPrimaryKey(fav);
        JsonResult ret = new JsonResult();
        ret.setSuccess(true);
        return ret;
    }

    @RequestMapping("remove2/{productId}")
    @AuthRequired
    public JsonResult removeFavoriteByProductId(@PathVariable("productId") Integer productId, HttpServletRequest request) {
        JsonResult ret = new JsonResult();
        if(request.getAttribute("loginUser")==null){
            ret.setData(0);
            return ret;
        }
        User user = (User) request.getAttribute("loginUser");
        Favorite fav = new Favorite();
        fav.setProductId(productId);
        fav.setUserId(user.getUserId());
        favoriteMapper.delete(fav);
        ret.setSuccess(true);
        return ret;
    }

    @RequestMapping("check/{productId}")
    @AuthRequired
    public JsonResult removeFavorite(@PathVariable("productId") Integer productId, HttpServletRequest request) {
        JsonResult ret = new JsonResult();
        if(request.getAttribute("loginUser")==null){
            ret.setData(0);
            return ret;
        }
        User user = (User) request.getAttribute("loginUser");
        Favorite fav = new Favorite();
        fav.setUserId(user.getUserId());
        fav.setProductId(productId);
        if(favoriteMapper.selectCount(fav)>0){
            ret.setData(1);
        }else{
            ret.setData(0);
        }
        ret.setSuccess(true);
        return ret;
    }
}
