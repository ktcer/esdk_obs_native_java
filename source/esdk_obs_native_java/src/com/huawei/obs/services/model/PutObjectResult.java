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

/**描述put方式上传对象返回结果
 */
public class PutObjectResult
{
    private String etag;

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
     * @param objEtag 对象eTag
     */
    public void setEtag(String objEtag)
    {
        this.etag = objEtag;
    }

}
