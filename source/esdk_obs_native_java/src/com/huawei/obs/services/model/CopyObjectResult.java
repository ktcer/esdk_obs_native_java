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

/**描述复制对象的返回结果
 */
public class CopyObjectResult
{
    private String etag;
    
    private Date lastModified;
    
    /**获取对象的eTag
     * 
     * @return 返回对象的eTag
     */
    public String getEtag()
    {
        return etag;
    }
    
    /**设置对象的eTag
     * 
     * @param objEtag 对象的eTag
     */
    public void setEtag(String objEtag)
    {
        this.etag = objEtag;
    }
    
    /**获取对象的修改时间
     * 
     * @return 返回日期
     */
    public Date getLastModified()
    {
        return lastModified;
    }
    
    /**设置对象的修改时间
     * 
     * @param lastModified 对象的修改时间
     */
    public void setLastModified(Date lastModified)
    {
        this.lastModified = lastModified;
    }
    
}
