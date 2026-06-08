package com.mall.controller.admin;

import com.mall.controller.vo.JsonResult;
import com.mall.dao.mapper.SlidePicMapper;
import com.mall.dao.model.SlidePic;
import com.mall.utils.page.PageView;
import org.apache.ibatis.session.RowBounds;
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
 * 轮播图的api
 */
@RestController
@RequestMapping("/admin/slide")
public class SlidePictureManagerApi {

    @Autowired
    private SlidePicMapper slidePicMapper;

    @InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {
        //转换日期格式
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //注册自定义的编辑器
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * 查询数据列表
     * @param page
     * @param kw
     * @return
     */
    @RequestMapping("list")
    public JsonResult userList(PageView<SlidePic> page, String kw) {
        JsonResult result = new JsonResult(true, 200, "查询成功");
        Example example = new Example(SlidePic.class);
        Example.Criteria criteria = example.createCriteria();
//        if(StringUtils.isNotBlank(kw)){
//            criteria.andLike("remark", kw);
//        }
        example.orderBy("seq").asc();
        List<SlidePic> list = slidePicMapper.selectByExampleAndRowBounds(example, new RowBounds(page.getFirstResult(), page.getMaxresult()));
        page.setRecords(list);
        page.setTotalrecord(slidePicMapper.selectCountByExample(example));
        result.setData(page);
        return result;
    }

    /**
     * 删除参数
     * @param slidePicId
     * @return
     */
    @RequestMapping("delete/{id}")
    public JsonResult delete(@PathVariable("id") Integer slidePicId) {
        JsonResult result = new JsonResult(true, 200, "删除成功");
        slidePicMapper.deleteByPrimaryKey(slidePicId);
        return result;
    }

    @RequestMapping("info/{id}")
    public JsonResult info(@PathVariable("id") Integer slidePicId) {
        JsonResult result = new JsonResult(true, 200, "查询成功");
        SlidePic config = slidePicMapper.selectByPrimaryKey(slidePicId);
        result.setData(config);
        return result;
    }

    @RequestMapping("save")
    public JsonResult saveOrUpdateApartment(@RequestBody SlidePic slidePic) {
        JsonResult result = new JsonResult(true, 200, "查询成功");
        try {
            if(slidePic.getSlidePicId()==null){
                slidePicMapper.insertSelective(slidePic);
            }else{
                slidePicMapper.updateByPrimaryKeySelective(slidePic);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("更新失败");
            result.setSuccess(false);
        }
        return result;
    }

}
