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

/**列举多段上传任务的请求
 */
public class ListMultipartUploadsRequest
{
    private String bucketName;
    
    private String prefix;

    private String delimiter;
    
    private Integer maxUploads;

    private String keyMarker;
    
    private String uploadIdMarker;
    
    /**获取列举多段上传任务的匹配字符串，对象名符合前缀的多段上传任务将被列举
     * 
     * @return 匹配字符串
     */
    public String getPrefix()
    {
        return prefix;
    }
    
    /**设置列举多段上传任务的匹配前缀，对象名符合前缀的多段上传任务将被列举
     * 
     * @param prefix 匹配字符串
     */
    public void setPrefix(String prefix)
    {
        this.prefix = prefix;
    }
    
    /**获取分隔符，分隔符会与prefix共同作用，对返回的多段上传任务进行分组
     * 
     * @return 分隔符
     */
    public String getDelimiter()
    {
        return delimiter;
    }
    
    /**设置分隔符，分隔符会与prefix共同作用，对返回的多段上传任务进行分组
     * 
     * @param delimiter 分隔符
     */
    public void setDelimiter(String delimiter)
    {
        this.delimiter = delimiter;
    }
    
    /**获取查询的起始位置
     * 
     * @return 返回查询的起始位置标识
     */
    public String getKeyMarker()
    {
        return keyMarker;
    }

    /**设置列举时返回指定的key-marker之后的多段上传任务
     * 
     * @param keyMarker 查询的起始位置标识
     */
    public void setKeyMarker(String keyMarker)
    {
        this.keyMarker = keyMarker;
    }

    /**获取下一分页的起始位置
     * 
     * @return 返回下一分页的起始位置
     */
    public String getUploadIdMarker()
    {
        return uploadIdMarker;
    }

    /**设置下一分页的起始位置
     * 
     * @param uploadIdMarker 起始位置标识
     */
    public void setUploadIdMarker(String uploadIdMarker)
    {
        this.uploadIdMarker = uploadIdMarker;
    }


    /**获取多段上传任务所属的桶
     * 
     * @return 桶的名称
     */
    public String getBucketName()
    {
        return bucketName;
    }

    /**设置多段上传任务所属的桶
     * 
     * @param bucketName 桶的名称
     */
    public void setBucketName(String bucketName)
    {
        this.bucketName = bucketName;
    }

    /**获取列举的多段上传任务的最大条目
     * 
     * @return 返回列举的多段上传任务的最大条目
     */
    public Integer getMaxUploads()
    {
        return maxUploads;
    }

    /**设置列举的多段上传任务的最大条目
     * 
     * @param maxUploads 列举的多段上传任务的最大条目
     */
    public void setMaxUploads(Integer maxUploads)
    {
        this.maxUploads = maxUploads;
    }
    
}
