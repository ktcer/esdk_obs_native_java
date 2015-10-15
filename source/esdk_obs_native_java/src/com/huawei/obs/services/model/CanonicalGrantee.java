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

/**
 * ACL策略中保存用户的容器，包含grantId和displayName
 */
public class CanonicalGrantee implements GranteeInterface
{
    private String grantId = null;

    private String displayName = null;

    /**
     * 默认构造函数
     */
    public CanonicalGrantee()
    {
    }

    /**
     * 指定策略中用户ID属性的构造函数
     * 
     * @param identifier 策略中用户的ID
     */
    public CanonicalGrantee(String identifier)
    {
        this.grantId = identifier;
    }

    /**
     * 设置策略中用户ID
     * 
     * @param canonicalGrantId 策略中用户的ID
     */
    public void setIdentifier(String canonicalGrantId)
    {
        this.grantId = canonicalGrantId;
    }

    /**
     * 获取策略中用户ID
     * 
     * @return 策略中用户ID
     */
    public String getIdentifier()
    {
        return grantId;
    }

    /**
     * 设置策略中用户名称
     * 
     * @param displayName 策略中用户的名称
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }

    /**
     * 获取策略中用户名称
     * 
     * @return 策略中用户名称
     */
    public String getDisplayName()
    {
        return this.displayName;
    }

    /**
     * 对象比较方法
     * 
     * @param obj 要比较的对象
     * @return true 比较的两个对象相同， false 比较的两个对象不同
     */
    public boolean equals(Object obj)
    {
        if (obj instanceof CanonicalGrantee)
        {
            CanonicalGrantee canonicalGrantee = (CanonicalGrantee) obj;
            return grantId.equals(canonicalGrantee.grantId);
        }
        return false;
    }

    /**
     * 对象的HASH方法
     * 
     * @return 返回对象的HASH值
     */
    public int hashCode()
    {
        // for Coverity checking, 复用父类方法
        return super.hashCode();
    }

    /**
     * 对象转换成字符串，供调试使用
     * 
     * @return 返回对象描述字符串
     */
    public String toString()
    {
        return "CanonicalGrantee [id=" + grantId
                + (displayName != null ? ", displayName=" + displayName : "")
                + "]";
    }
}
