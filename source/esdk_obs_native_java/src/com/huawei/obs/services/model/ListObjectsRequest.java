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

/**描述查询桶对象的请求
 */
public class ListObjectsRequest
{
    private String bucketName;
    
    private String prefix;
    
    private String marker;
    
    private int maxKeys;
    
    private String delimiter;
    
    /**创建查询对象的请求的默认构造函数
     */
    public ListObjectsRequest()
    {
        /* empty */
    }
    
    /**指定桶的名称的构造函数
     * 
     *  @param bucketName 桶的名称
     */
    public ListObjectsRequest(String bucketName)
    {
        this(bucketName, null, null, null, 0);
    }
    
    /**指定详细参数的构造函数
     * 
     *  @param bucketName 桶的名称
     *  @param prefix 列举以指定的字符串prefix开头的对象
     *  @param marker 列举对象列表的位置标识
     *  @param delimiter 分隔符
     *  @param maxKeys 返回的对象最多个数
     */
    public ListObjectsRequest(String bucketName, String prefix, String marker, String delimiter, int maxKeys)
    {
        this.bucketName = bucketName;
        this.prefix = prefix;
        this.marker = marker;
        this.delimiter = delimiter;
        this.maxKeys = maxKeys;
    }
    
    /**获取桶的名称
     * 
     * @return 返回桶的名称
     */
    public String getBucketName()
    {
        return bucketName;
    }
    
    /**设置要遍历桶的名称
     * 
     * @param bucketName 桶的名称
     */
    public void setBucketName(String bucketName)
    {
        this.bucketName = bucketName;
    }
    
    /**获取列举对象的匹配前缀，对象名符合前缀的对象将被列举
     * 
     * @return 匹配字符串
     */
    public String getPrefix()
    {
        return prefix;
    }
    
    /**设置列举对象的匹配前缀，对象名符合前缀的对象将被列举
     * 
     * @param prefix 匹配字符串
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
