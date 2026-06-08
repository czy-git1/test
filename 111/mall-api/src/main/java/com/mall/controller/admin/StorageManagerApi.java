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
import com.mall.exception.StorageException;
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
 * 商品库存管理接口
 */
@RestController
@RequestMapping("/admin/storage")
public class StorageManagerApi {

    private final Logger logger = LoggerFactory.getLogger(StorageManagerApi.class);

    @Autowired
    private StorageService storageService;
    @Autowired
    private StorageMapper storageMapper;

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
        PageView<Storage> page = new PageView<>();
        page = page.startPage(request);
        String kw = request.getString("kw");
        List<Storage> list = storageMapper.selectStorageList(kw, page.getFirstResult(), page.getMaxresult());
        page.setRecords(list);
        page.setTotalrecord(storageMapper.selectStorageCount(kw));
        result.setData(page);
        return result;
    }

    /**
     * 盘点库存
     * @param request
     * @return
     */
    @RequestMapping("stocktaking")
    public JsonResult stocktaking(@RequestBody JSONObject request) {
        JsonResult result = new JsonResult(true, 200, "查询成功");
        storageService.stocktaking(request.getInteger("productId"), request.getInteger("quantity"), request.getString("remark"));
        return result;
    }

    /**
     * 出入库管理
     * @param request
     * @return
     */
    @RequestMapping("operate")
    public JsonResult operate(@RequestBody JSONObject request) {
        JsonResult result = new JsonResult(true, 200, "操作成功");
        Integer operatorType = request.getInteger("operatorType");
        Integer productId = request.getInteger("productId");
        Integer quantity = request.getInteger("quantity");
        String remark = request.getString("remark");
        if(operatorType == 1){
            //入库
            storageService.addStorage(productId, quantity, remark);
        }else if(operatorType == -1){
            try {
                storageService.minusStorage(productId, quantity, remark);
            } catch (StorageException e) {
                logger.error("出库发生异常！", e);
                result.setSuccess(false);
                result.setCode(500);
                result.setMsg("库存不足！");
            }
        }else if(operatorType == 0){
            return this.stocktaking(request);
        }
        return result;
    }
}
