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

import java.util.ArrayList;
import java.util.List;

/**描述查询到的桶内的对象列表
 */
public class ObjectListing
{
    private List<S3Object> objectSummaries = new ArrayList<S3Object>();
    
    private List<String> commonPrefixes = new ArrayList<String>();
    
    private String bucketName;
    
    private boolean truncated;
    
    private String prefix;
    
    private String marker;
    
    private int maxKeys;
    
    private String delimiter;
    
    /**
     * 返回对象列表
     * @return 对象列表
     */
    public List<S3Object> getObjectSummaries()
    {
        return objectSummaries;
    }
    
    /**把查询到的对象插入对象列表
     * 
     * @param objectSummaries 对象列表
     */
    public void setObjectSummaries(List<S3Object> objectSummaries)
    {
        this.objectSummaries = objectSummaries;
    }
    
    /**获取对象名称前缀列表
     * 
     * @return 对象前缀列表
     */
    public List<String> getCommonPrefixes()
    {
        return commonPrefixes;
    }
    
    /**设置对象名称前缀列表
     * 
     * @param commonPrefixes 对象前缀列表
     */
    public void setCommonPrefixes(List<String> commonPrefixes)
    {
        this.commonPrefixes = commonPrefixes;
    }
    
    /**获取桶的名称
     * 
     * @return 桶的名称
     */
    public String getBucketName()
    {
        return bucketName;
    }
    
    /**设置桶的名称
     * 
     * @param bucketName 桶的名称
     */
    public void setBucketName(String bucketName)
    {
        this.bucketName = bucketName;
    }

    /**是否未返回所有的查询结果
     * 
     * @return true 返回的是部分信息， false 返回的是全部信息
     */
    public boolean isTruncated()
    {
        return truncated;
    }
    
    /**设置是否未返回所有的查询结果标志
     * 
     * @param isTruncate 是否未返回所有的查询结果的布尔值
     */
    public void setTruncated(boolean isTruncate)
    {
        this.truncated = isTruncate;
    }
    
    /**获取查询的匹配前缀，对象名符合前缀的对象将被列举
     * 
     * @return 匹配前缀字符串
     */
    public String getPrefix()
    {
        return prefix;
    }
    
    /**设置查询的匹配前缀，对象名符合前缀的对象将被列举
     * 
     * @param  prefix 匹配前缀字符串
     */
    public void setPrefix(String prefix)
    {
        this.prefix = prefix;
    }
    
    /**获取列举对象的标志，查询对象时从此标志开始列举
     * 
     * @return 标志字符串
     */
    public String getMarker()
    {
        return marker;
    }
    
    /**设置列举对象的标志，查询对象时从此标志开始列举
     * 
     * @param marker 标志字符串
     */
    public void setMarker(String marker)
    {
        this.marker = marker;
    }
    
    /**获取一次列举对象的最大个数
     * 
     * @return 最大能够列举对象的数量
     */
    public int getMaxKeys()
    {
        return maxKeys;
    }
    
    /**设置一次列举对象的最大个数
     * 
     * @param maxKeys 最大能够列举对象的数量
     */
    public void setMaxKeys(int maxKeys)
    {
        this.maxKeys = maxKeys;
    }
    
    /**获取分隔符，分隔符会与prefix共同作用，对返回的对象进行分组
     * 
     * @return 分隔符
     */
    public String getDelimiter()
    {
        return delimiter;
    }
    
    /**设置分隔符，分隔符会与prefix共同作用，对返回的对象进行分组
     * 
     * @param delimiter 分隔符
     */
    public void setDelimiter(String delimiter)
    {
        this.delimiter = delimiter;
    }
}
