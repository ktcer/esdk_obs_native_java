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

/**描述多段上传任务初始化请求
 */
public class InitiateMultipartUploadRequest
{
    private String bucketName;
    
    private String objectKey;
    
    private String webSiteRedirectLocation;
    
    /**获取要初始化的多段上传任务所属的桶
     * 
     * @return 返回桶的名称
     */
    public String getBucketName()
    {
        return bucketName;
    }
    
    /**设置要初始化的多段上传任务所属的桶
     * 
     * @param bucketName 桶的名称
     */
    public void setBucketName(String bucketName)
    {
        this.bucketName = bucketName;
    }
    
    /**获取多段上传任务要形成的对象名称
     * 
     * @return 返回多段上传任务要形成的对象名称
     */
    public String getObjectKey()
    {
        return objectKey;
    }
    
    /**设置多段上传任务要形成的对象名称
     * 
     * @param objectKey 多段上传任务要形成的对象名称
     */
    public void setObjectKey(String objectKey)
    {
        this.objectKey = objectKey;
    }
    
    /**
     * 设置Website重定向链接
     * 当桶设置了Website配置，可以将获取这个对象的请求重定向到桶内另一个对象或一个外部的URL。
     * @return  Website重定向链接
     */
    public String getWebSiteRedirectLocation()
    {
        return webSiteRedirectLocation;
    }

    /**
     * 当桶设置了Website重定向链接，可以将获取这个对象的请求重定向到桶内另一个对象或一个外部的URL。
     * @param webSiteRedirectLocation  Website重定向链接
     */
    public void setWebSiteRedirectLocation(String webSiteRedirectLocation)
    {
        this.webSiteRedirectLocation = webSiteRedirectLocation;
    }
    
}
