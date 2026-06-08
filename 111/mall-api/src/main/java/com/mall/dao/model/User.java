package com.mall.dao.model;

import javax.persistence.*;

public class User {
    /**
     * 物理ID
     */
    @Id
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 微信的openid
     */
    @Column(name = "open_id")
    private String openId;

    /**
     * 状态：1-正常；2-已实名；-1已注销
     */
    private Integer status;

    /**
     * 所在城市
     */
    private String location;

    /**
     * 头像图片
     */
    private String pic;

    /**
     * 昵称
     */
    @Column(name = "nick_name")
    private String nickName;

    /**
     * 性别
     */
    private String gender;

    /**
     * 获取物理ID
     *
     * @return user_id - 物理ID
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置物理ID
     *
     * @param userId 物理ID
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取手机号
     *
     * @return mobile - 手机号
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * 设置手机号
     *
     * @param mobile 手机号
     */
    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    /**
     * 获取微信的openid
     *
     * @return open_id - 微信的openid
     */
    public String getOpenId() {
        return openId;
    }

    /**
     * 设置微信的openid
     *
     * @param openId 微信的openid
     */
    public void setOpenId(String openId) {
        this.openId = openId == null ? null : openId.trim();
    }

    /**
     * 获取状态：1-正常；2-已实名；-1已注销
     *
     * @return status - 状态：1-正常；2-已实名；-1已注销
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态：1-正常；2-已实名；-1已注销
     *
     * @param status 状态：1-正常；2-已实名；-1已注销
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取所在城市
     *
     * @return location - 所在城市
     */
    public String getLocation() {
        return location;
    }

    /**
     * 设置所在城市
     *
     * @param location 所在城市
     */
    public void setLocation(String location) {
        this.location = location == null ? null : location.trim();
    }

    /**
     * 获取头像图片
     *
     * @return pic - 头像图片
     */
    public String getPic() {
        return pic;
    }

    /**
     * 设置头像图片
     *
     * @param pic 头像图片
     */
    public void setPic(String pic) {
        this.pic = pic == null ? null : pic.trim();
    }

    /**
     * 获取昵称
     *
     * @return nick_name - 昵称
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * 设置昵称
     *
     * @param nickName 昵称
     */
    public void setNickName(String nickName) {
        this.nickName = nickName == null ? null : nickName.trim();
    }

    /**
     * 获取性别
     *
     * @return gender - 性别
     */
    public String getGender() {
        return gender;
    }

    /**
     * 设置性别
     *
     * @param gender 性别
     */
    public void setGender(String gender) {
        this.gender = gender == null ? null : gender.trim();
    }

}