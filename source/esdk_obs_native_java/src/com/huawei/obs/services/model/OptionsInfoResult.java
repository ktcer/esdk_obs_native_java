/**
* Copyright 2015 Huawei Technologies Co., Ltd. All rights reserved.
* eSDK is licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.huawei.obs.services.model;

import java.util.List;

/**描述OPTION桶或对象的返回结果
 *
 */
public class OptionsInfoResult
{
    /**
     * 如果请求的Origin满足服务端的CORS配置，则在响应中包含这个Origin
     */
    private String allowOrigin;
    
    /**
     * 如果请求的headers满足服务端的CORS配置，则在响应中包含这个headers。
     */
    private List<String>allowHeaders;
    
    /**
     * 服务端CORS配置中的MaxAgeSeconds
     */
    private int maxAge;
    
    /**
     * 如果请求的Access-Control-Request-Method满足服务端的CORS配置，则在响应中包含这条rule中的Methods
     */
    private List<String>allowMethods;
    
    /**
     * 服务端CORS配置中的ExposeHeader。
     */
    private List<String>exposeHeaders;

    /**获取请求的Origin，（如果请求的Origin满足服务端的CORS配置）
     * 
     * @return 跨域请求Origin
     */
    public String getAllowOrigin()
    {
        return allowOrigin;
    }

    /**设置获取请求的Origin
     * 
     * @param allowOrigin 跨域请求Origin
     */
    public void setAllowOrigin(String allowOrigin)
    {
        this.allowOrigin = allowOrigin;
    }

    /** 获取CORS配置的headers （如果请求的headers满足服务端的CORS配置）
     * 
     * @return CORS配置的headers列表
     */
    public List<String> getAllowHeaders()
    {
        return allowHeaders;
    }

    /**设置CORS配置的headers
     * 
     * @param allowHeaders CORS配置的headers列表
     */
    public void setAllowHeaders(List<String> allowHeaders)
    {
        this.allowHeaders = allowHeaders;
    }

    /** 获取CORS配置中的MaxAgeSeconds
     * 
     * @return CORS配置中的MaxAgeSeconds
     */
    public int getMaxAge()
    {
        return maxAge;
    }

    /**设置CORS配置中的MaxAgeSeconds
     * 
     * @param maxAge CORS配置中的MaxAgeSeconds
     */
    public void setMaxAge(int maxAge)
    {
        this.maxAge = maxAge;
    }

    /**获取CORS配置的HTTP请求方法  （如果请求的Access-Control-Request-Method满足服务端的CORS配置）
     * 
     * @return HTTP请求方法
     */
    public List<String> getAllowMethods()
    {
        return allowMethods;
    }

    /**设置CORS配置的HTTP请求方法  （如果请求的Access-Control-Request-Method满足服务端的CORS配置）
     * 
     * @param allowMethods HTTP请求方法
     */
    public void setAllowMethods(List<String> allowMethods)
    {
        this.allowMethods = allowMethods;
    }

    /**获取服务端CORS配置中的ExposeHeader
     * 
     * @return  CORS配置中的ExposeHeader列表
     */
    public List<String> getExposeHeaders()
    {
        return exposeHeaders;
    }

    /**设置服务端CORS配置中的ExposeHeader
     * 
     * @param exposeHeaders CORS配置中的ExposeHeader列表
     */
    public void setExposeHeaders(List<String> exposeHeaders)
    {
        this.exposeHeaders = exposeHeaders;
    }
    
    
    
}
