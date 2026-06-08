package com.mall.dao.model;

import javax.persistence.*;

@Table(name = "slide_pic")
public class SlidePic {
    /**
     * 主键
     */
    @Id
    @Column(name = "slide_pic_id")
    private Integer slidePicId;

    /**
     * 图片url
     */
    @Column(name = "pic_url")
    private String picUrl;

    /**
     * 排序
     */
    private Integer seq;

    /**
     * 链接
     */
    private String link;

    /**
     * 备注
     */
    private String remark;

    /**
     * 获取主键
     *
     * @return slide_pic_id - 主键
     */
    public Integer getSlidePicId() {
        return slidePicId;
    }

    /**
     * 设置主键
     *
     * @param slidePicId 主键
     */
    public void setSlidePicId(Integer slidePicId) {
        this.slidePicId = slidePicId;
    }

    /**
     * 获取图片url
     *
     * @return pic_url - 图片url
     */
    public String getPicUrl() {
        return picUrl;
    }

    /**
     * 设置图片url
     *
     * @param picUrl 图片url
     */
    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl == null ? null : picUrl.trim();
    }

    /**
     * 获取排序
     *
     * @return seq - 排序
     */
    public Integer getSeq() {
        return seq;
    }

    /**
     * 设置排序
     *
     * @param seq 排序
     */
    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    /**
     * 获取链接
     *
     * @return link - 链接
     */
    public String getLink() {
        return link;
    }

    /**
     * 设置链接
     *
     * @param link 链接
     */
    public void setLink(String link) {
        this.link = link == null ? null : link.trim();
    }

    /**
     * 获取备注
     *
     * @return remark - 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}