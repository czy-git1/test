package com.mall.dao.model;

import java.util.Date;
import javax.persistence.*;

public class Config {
    @Id
    @Column(name = "config_id")
    private Integer configId;

    @Column(name = "conf_key")
    private String confKey;

    private String remark;

    @Column(name = "last_update")
    private Date lastUpdate;

    private String value;

    /**
     * @return config_id
     */
    public Integer getConfigId() {
        return configId;
    }

    /**
     * @param configId
     */
    public void setConfigId(Integer configId) {
        this.configId = configId;
    }

    /**
     * @return conf_key
     */
    public String getConfKey() {
        return confKey;
    }

    /**
     * @param confKey
     */
    public void setConfKey(String confKey) {
        this.confKey = confKey == null ? null : confKey.trim();
    }

    /**
     * @return remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * @return last_update
     */
    public Date getLastUpdate() {
        return lastUpdate;
    }

    /**
     * @param lastUpdate
     */
    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /**
     * @return value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value
     */
    public void setValue(String value) {
        this.value = value == null ? null : value.trim();
    }
}