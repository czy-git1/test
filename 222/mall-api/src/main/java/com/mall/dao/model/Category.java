package com.mall.dao.model;

import javax.persistence.*;

public class Category {
    @Id
    @Column(name = "category_id")
    private Integer categoryId;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 简要描述
     */
    @Column(name = "short_desc")
    private String shortDesc;

    /**
     * 是否已删除
     */
    @Column(name = "is_delete")
    private Integer isDelete;

    @Column(name = "sort")
    private Integer sort;

    /**
     * 分类展示图片
     */
    private String pic;

    /**
     * @return category_id
     */
    public Integer getCategoryId() {
        return categoryId;
    }

    /**
     * @param categoryId
     */
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * 获取分类名称
     *
     * @return name - 分类名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置分类名称
     *
     * @param name 分类名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取简要描述
     *
     * @return short_desc - 简要描述
     */
    public String getShortDesc() {
        return shortDesc;
    }

    /**
     * 设置简要描述
     *
     * @param shortDesc 简要描述
     */
    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc == null ? null : shortDesc.trim();
    }

    /**
     * 获取是否已删除
     *
     * @return is_delete - 是否已删除
     */
    public Integer getIsDelete() {
        return isDelete;
    }

    /**
     * 设置是否已删除
     *
     * @param isDelete 是否已删除
     */
    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    /**
     * 获取分类展示图片
     *
     * @return pic - 分类展示图片
     */
    public String getPic() {
        return pic;
    }

    /**
     * 设置分类展示图片
     *
     * @param pic 分类展示图片
     */
    public void setPic(String pic) {
        this.pic = pic == null ? null : pic.trim();
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}