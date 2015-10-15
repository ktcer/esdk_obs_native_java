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

import java.util.List;

/**查询多段上传任务的返回结果
 */
public class MultipartUploadListing
{
    private String bucketName;
    
    private String keyMarker;
    
    private String uploadIdMarker;

    private String nextKeyMarker;
    
    private String nextUploadIdMarker;
    
    private int maxUploads;
    
    private boolean truncated;
    
    private List<MultipartUpload> multipartTaskList;
    
    private String delimiter;
    
    private String[] commonPrefixes;

    /**是否未返回所有的查询结果
     * 
     * @return 返回true或者false
     */
    public boolean isTruncated()
    {
        return truncated;
    }
    
    /**设置结果是否分段
     * 
     * @param isTruncated 显示的结果是否分段
     */
    public void setTruncated(boolean isTruncated)
    {
        this.truncated = isTruncated;
    }
    
    /**获取列举对象的匹配字符串，对象名匹配则列举出来
     * 
     * @return 匹配字符串
     */
    public String[] getCommonPrefixes()
    {
        return commonPrefixes;
    }
    
    /**设置匹配字符串
     * 
     * @param commonPrefixes 匹配字符串
     */
    public void setCommonPrefixes(String[] commonPrefixes)
    {
        this.commonPrefixes = commonPrefixes;
    }
    
    /**获取查询的起始位置标识
     * 
     * @return 返回查询的起始位置标识
     */
    public String getUploadIdMarker()
    {
        return uploadIdMarker;
    }
    
    /**设置下一分页的起始位置标识
     * 
     * @param uploadIdMarker 起始位置标识
     */
    public void setUploadIdMarker(String uploadIdMarker)
    {
        this.uploadIdMarker = uploadIdMarker;
    }
    
    
    /**获取查询的起始位置
     * 
     * @return 返回查询的起始位置
     */
    public String getNextKeyMarker()
    {
        return nextKeyMarker;
    }
    
    /**设置列举时返回指定的key-marker之后的多段上传任务
     * 
     * @param nextKeyMarker 查询的起始位置标识
     */
    public void setNextKeyMarker(String nextKeyMarker)
    {
        this.nextKeyMarker = nextKeyMarker;
    }
    
    /**获取下一分页的起始段ID
     * 
     * @return 返回下一分页的起始段ID
     */
    public String getNextUploadIdMarker()
    {
        return nextUploadIdMarker;
    }
    
    /**设置下一分页的起始段ID
     * 
     * @param nextUploadIdMarker 下一分页的起始段ID
     */
    public void setNextUploadIdMarker(String nextUploadIdMarker)
    {
        this.nextUploadIdMarker = nextUploadIdMarker;
    }
    
    /**获取桶中还未完成的多段上传任务
     * 
     * @return 返回桶中还未完成的多段上传任务
     */
    public List<MultipartUpload> getMultipartTaskList()
    {
        return multipartTaskList;
    }
    
    /**设置桶中还未完成的多段上传任务
     * 
     * @param multipartTaskList 多段上传任务列表
     */
    public void setMultipartTaskList(List<MultipartUpload> multipartTaskList)
    {
        this.multipartTaskList = multipartTaskList;
    }
    
    /**获取多段上传任务所属的桶
     * 
     * @return 返回桶的名称
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
     * @return 返回查询的起始位置
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
    
    /**获取列举的多段上传任务的最大条目
     * 
     * @return 返回列举的多段上传任务的最大条目
     */
    public int getMaxUploads()
    {
        return maxUploads;
    }
    
    /**设置列举的多段上传任务的最大条目
     * 
     * @param maxUploads 列举的多段上传任务的最大条目
     */
    public void setMaxUploads(int maxUploads)
    {
        this.maxUploads = maxUploads;
    }
}
