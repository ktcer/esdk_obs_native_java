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

/**描述复制策略
 */
public class StoragePolicy
{
    private String storagePolicyName;

    /**获取复制策略的名称
     * 
     * @return 返回复制策略的名称
     */
    public String getStoragePolicyName()
    {
        return storagePolicyName;
    }

    /**设置复制策略的名称
     * 
     * @param storagePolicyName 复制策略的名称
     */
    public void setStoragePolicyName(String storagePolicyName)
    {
        this.storagePolicyName = storagePolicyName;
    }
    
}
