/*
 * Copyright Notice:
 *      Copyright  1998-2009, Huawei Technologies Co., Ltd.  ALL Rights Reserved.
 *
 *      Warning: This computer software sourcecode is protected by copyright law
 *      and international treaties. Unauthorized reproduction or distribution
 *      of this sourcecode, or any portion of it, may result in severe civil and
 *      criminal penalties, and will be prosecuted to the maximum extent
 *      possible under the law.
 */
package com.huawei.obs.services.model;

import java.util.Date;
/**
 * 描述对象以及版本信息
 */
public class VersionOrDeleteMarker
{
    private String key = null;
    private String versionId = null;
    private boolean isLatest = false;
    private Date lastModified = null;
    private Owner owner = null;
    
    /**
     * 构造方法
     * @param key 对象名称
     * @param versionId 版本
     * @param isLatest 是不是最新的版本
     * @param lastModified 最后修改日期
     * @param owner 所有者
     */
    public VersionOrDeleteMarker(String key, String versionId, boolean isLatest,
        Date lastModified, Owner owner)
    {
        this.key = key;
        this.versionId = versionId;
        this.isLatest = isLatest;
        this.lastModified = lastModified;
        this.owner = owner;
    }
    
    /**
     * 返回对象名称
     * @return 对象名称
     */
    public String getKey()
    {
        return key;
    }
    
    /**
     * 设置对象名
     * @param key 对象名称
     */
    public void setKey(String key)
    {
        this.key = key;
    }
    
    /**
     * 返回版本号
     * @return 版本号
     */
    public String getVersionId()
    {
        return versionId;
    }
    
    /**
     * 设置版本号
     * @param versionId 版本号
     */
    public void setVersionId(String versionId)
    {
        this.versionId = versionId;
    }
    
    /**
     * 判断对象版本是否是最新的
     * @return true是最新的版本，false不是最新的版本
     */
    public boolean isLatest()
    {
        return isLatest;
    }
    
    /**
     * 设置对象版本的是否是最新的
     * @param isLatest 最新版本标志，true表示是最新版本，false表示不是最新版本
     */
    public void setLatest(boolean isLatest)
    {
        this.isLatest = isLatest;
    }
    
    /**
     * 获得最后的修改日期
     * @return 最后修改日期
     */
    public Date getLastModified()
    {
        return lastModified;
    }
    
    /**
     * 设置最后的修改日期
     * @param lastModified 最后修改日期
     */
    public void setLastModified(Date lastModified)
    {
        this.lastModified = lastModified;
    }
    
    /**
     * 获得对象的所有者
     * @return 对象的所有者
     */
    public Owner getOwner()
    {
        return owner;
    }
    
    /**
     * 设置对象的所有者
     * @param owner 对象的所有者
     */
    public void setOwner(Owner owner)
    {
        this.owner = owner;
    }
    
    @Override
    public String toString()
    {
        return "BaseVersionOrDeleteMarker [key=" + key + ", versionId=" + versionId + ", isLatest=" + isLatest
            + ", lastModified=" + lastModified + ", owner=" + owner + "]";
    }
    
}
