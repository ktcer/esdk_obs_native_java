/*
 * Copyright Notice:
 *      Copyright  1998-2015, Huawei Technologies Co., Ltd.  ALL Rights Reserved.
 *
 *      Warning: This computer software sourcecode is protected by copyright law
 *      and international treaties. Unauthorized reproduction or distribution
 *      of this sourcecode, or any portion of it, may result in severe civil and
 *      criminal penalties, and will be prosecuted to the maximum extent
 *      possible under the law.
 */
package com.huawei.obs.services;

/**OBS配置，记录系统的全局信息
 */
public class ObsConfiguration
{
    /**
     * 记录配置信息
     */
    private int connectionTimeout;
    
    private int maxConnections;
    
    private int maxErrorRetry; /*业务请求失败时，最大重试的次数*/
    
    private int socketTimeout;
    
    private String endPoint; /*OBS 请求发送的目的地（服务器的域名）*/
    
    private int endpointHttpPort;/*HTTP 请求端口*/
    
    private int endpointHttpsPort;/*HTTPS 请求端口*/
    
    private boolean httpsOnly;/*是否使用HTTPS发送请求*/

    private boolean disableDnsBucket;
    
    private String defaultBucketLocation; /*桶的区域 */
    
    private String signatString;/*鉴权方式*/

    /**默认构造函数
     */
    public ObsConfiguration()
    {
        this.connectionTimeout = ObsConstraint.HTTP_CONNECT_TIMEOUT_VALUE;
        this.maxConnections = ObsConstraint.HTTP_MAX_CONNECT_VALUE;
        this.maxErrorRetry = ObsConstraint.HTTP_RETRY_MAX_VALUE;
        this.socketTimeout = ObsConstraint.HTTP_SOCKET_TIMEOUT_VALUE;
        this.endpointHttpPort = ObsConstraint.HTTP_PORT_VALUE;
        this.endpointHttpsPort = ObsConstraint.HTTPS_PORT_VALUE;
        this.httpsOnly = true;
        this.disableDnsBucket = true;
        this.defaultBucketLocation = ObsConstraint.DEFAULT_BUCKET_LOCATION_VALUE;
        this.signatString = ObsConstraint.DEFAULT_BUCKET_LOCATION;
    }
    
    /**
     * 获取登陆鉴权的方式
     * @return 鉴权的方式
     */
    public String getSignatString()
    {
        return signatString;
    }

    /**
     * 获取登陆鉴权的方式
     * disableDns 鉴权方式: s3或者v4
     * @return 
     */
    public void setSignatString(String signatString)
    {
        this.signatString = signatString;
    }

    /**获取是否把桶名加入DNS域名标志
     * 
     * @return 返回是否把桶名加入DNS域名标志
     */
    public boolean isDisableDnsBucket()
    {
        return disableDnsBucket;
    }

    /**设置是否把桶名加入DNS域名标志（默认： true）
     * 
     * @param disableDns 是否不把桶名加入DNS域名,e: bucket.huawei.com.cn
     */
    public void setDisableDnsBucket(boolean disableDns)
    {
        this.disableDnsBucket = disableDns;
    }
    
    /**获取socket连接超时时间
     * 
     * @return 返回socket连接超时时间
     */
    public int getConnectionTimeout()
    {
        return connectionTimeout;
    }

    /**设置socket的超时时间（默认：6000ms）
     * 
     * @param connectionTimeout socket连接超时时间
     */
    public void setConnectionTimeout(int connectionTimeout)
    {
        this.connectionTimeout = connectionTimeout;
    }

    /**获取业务请求的最大连接数
     * 
     * @return 返回业务请求的最大连接数
     */
    public int getMaxConnections()
    {
        return maxConnections;
    }

    /**设置业务请求的最大连接数（默认：20）
     * 
     * @param maxConnections 最大连接数
     */
    public void setMaxConnections(int maxConnections)
    {
        this.maxConnections = maxConnections;
    }

    /**获取业务请求失败的最大重试次数
     * 
     * @return 返回业务请求失败的最大重试次数
     */
    public int getMaxErrorRetry()
    {
        return maxErrorRetry;
    }

    /**设置业务请求失败的最大重试次数（默认：5）
     * 
     * @param maxErrorRetry 业务请求失败的最大重试次数
     */
    public void setMaxErrorRetry(int maxErrorRetry)
    {
        this.maxErrorRetry = maxErrorRetry;
    }

    /**获取socket超时时间
     * 
     * @return 返回socket超时时间
     */
    public int getSocketTimeout()
    {
        return socketTimeout;
    }

    /**设置socket超时时间（默认：6000ms）
     * 
     * @param socketTimeout socket超时时间
     */
    public void setSocketTimeout(int socketTimeout)
    {
        this.socketTimeout = socketTimeout;
    }

    /**获取海量存储服务器域名
     * 
     * @return 返回海量存储服务器域名
     */
    public String getEndPoint()
    {
        return endPoint;
    }

    /**设置海量存储服务器域名
     * 
     * @param endPoint 海量存储服务器域名
     */
    public void setEndPoint(String endPoint)
    {
        this.endPoint = endPoint;
    }

    /**获取当前设置的HTTP请求对应的端口
     * 
     * @return 返回当前设置的HTTP请求对应的端口
     */
    public int getEndpointHttpPort()
    {
        return endpointHttpPort;
    }

    /**设置当前设置的HTTP请求对应的端口（默认：5080）
     * 
     * @param endpointHttpPort HTTP请求对应的端口号
     */
    public void setEndpointHttpPort(int endpointHttpPort)
    {
        this.endpointHttpPort = endpointHttpPort;
    }

    /**获取当前设置的HTTPS请求端口
     * 
     * @return 返回当前设置的HTTPS请求端口
     */
    public int getEndpointHttpsPort()
    {
        return endpointHttpsPort;
    }

    /**设置当前设置的HTTPS请求端口（默认：5443）
     * 
     * @param endpointHttpsPort HTTPS请求对应的端口
     */
    public void setEndpointHttpsPort(int endpointHttpsPort)
    {
        this.endpointHttpsPort = endpointHttpsPort;
    }

    /**设置是否使用HTTPS的开关（默认：true）
     * 
     * @param httpsOnly 是否使用HTTPS
     */
    public void setHttpsOnly(boolean httpsOnly)
    {
        this.httpsOnly = httpsOnly;
    }

    /**获取是否使用HTTPS的开关
     * 
     * @return 返回是否使用HTTPS的开关
     */
    public boolean isHttpsOnly()
    {
        return httpsOnly;
    }

    /**获取桶的默认区域
     * 
     * @return 返回桶的默认区域
     */
    public String getDefaultBucketLocation()
    {
        return defaultBucketLocation;
    }

    /**设置桶的区域
     * 
     * @param defaultBucketLocation 桶的默认区域（默认：CHINA）
     */
    public void setDefaultBucketLocation(String defaultBucketLocation)
    {
        this.defaultBucketLocation = defaultBucketLocation;
    }
     
}
