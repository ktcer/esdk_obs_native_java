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

/**描述桶或对象的属主
 */
public class Owner
{
    private String displayName;
    
    private String id;
    
    /**获取属主名称
     * 
     * @return 属主名称
     */
    public String getDisplayName()
    {
        return displayName;
    }
    
    /**设置属主的名称
     * 
     * @param displayName 属主的名称
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }
    
    /**获取属主标识
     * 
     * @return 属主标识
     */
    public String getId()
    {
        return id;
    }
    
    /**设置属主的标识
     * 
     * @param id 属主标识
     */
    public void setId(String id)
    {
        this.id = id;
    }
    
}
