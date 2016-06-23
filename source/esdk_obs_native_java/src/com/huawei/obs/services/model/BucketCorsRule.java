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

import java.util.ArrayList;
import java.util.List;

/**
 * 桶的CORS规则
 */
public class BucketCorsRule
{
    /**
     * 一条Rule的标识，由不超过255个字符的字符串组成
     * 可选
     */
    private String id;
    
    /**
     * 【可选】客户端可以缓存的CORS响应时间，以秒为单位。
     */
    private int maxAgeSecond;
    
    /**
     * 【必选】CORS规则允许的Method,有效值：GET、PUT、HEAD、POST 、DELETE
     */
    private List<String>allowedMethod;
    
    /**
     * 【必选】CORS规则允许的Origin（表示域名的字符串），可以带一个匹配符”*”。每一个AllowedOrigin可以带最多一个“*”通配符
     */
    private List<String>allowedOrigin;
    
    /**
     * 【可选】配置CORS请求中允许携带的“Access-Control-Request-Headers”头域
     */
    private List<String>allowedHeader;
    
    /**
     * 【可选】CORS响应中带的附加头域，给客户端提供额外的信息，不可出现空格
     */
    private List<String>exposeHeader;

    /**获取标识ID
     * 
     * @return 标识ID
     */
    public String getId()
    {
        return id;
    }

    /**设置标识ID
     * 
     * @param id 标识ID
     */
    public void setId(String id)
    {
        this.id = id;
    }

    /**获取可以缓存的CORS响应时间
     * 
     * @return 可以缓存的CORS响应时间
     */
    public int getMaxAgeSecond()
    {
        return maxAgeSecond;
    }

    /**设置可以缓存的CORS响应时间
     * 
     * @param maxAgeSecond 可以缓存的CORS响应时间
     */
    public void setMaxAgeSecond(int maxAgeSecond)
    {
        this.maxAgeSecond = maxAgeSecond;
    }

    /**获取CORS规则允许的Method
     * 
     * @return Method列表
     */
    public List<String> getAllowedMethod()
    {
        if(null == allowedMethod)
        {
            return allowedMethod = new ArrayList<String>();
        }
        return allowedMethod;
    }

    /**设置CORS规则允许的Method
     * 
     * @param allowedMethod Method列表
     */
    public void setAllowedMethod(List<String> allowedMethod)
    {
        this.allowedMethod = allowedMethod;
    }

    /**获取CORS规则允许的Origin（表示域名的字符串）
     * 
     * @return Origin列表
     */
    public List<String> getAllowedOrigin()
    {
        if(null == allowedOrigin)
        {
            return allowedOrigin = new ArrayList<String>();
        }
        return allowedOrigin;
    }

    /**设置CORS规则允许的Origin（表示域名的字符串）
     * 
     * @param allowedOrigin Origin列表
     */
    public void setAllowedOrigin(List<String> allowedOrigin)
    {
        this.allowedOrigin = allowedOrigin;
    }

    /**获取配置CORS请求中允许携带的请求的头域
     * 
     * @return Header列表
     */
    public List<String> getAllowedHeader()
    {
        if(null == allowedHeader)
        {
            return allowedHeader = new ArrayList<String>();
        }
        return allowedHeader;
    }

    /**设置配置CORS请求中允许携带的请求的头域
     * 
     * @param allowedHeader Header列表
     */
    public void setAllowedHeader(List<String> allowedHeader)
    {
        this.allowedHeader = allowedHeader;
    }

    /**获取CORS响应中带的附加头域
     * 
     * @return 附加头域列表
     */
    public List<String> getExposeHeader()
    {
        if(null == exposeHeader)
        {
            return exposeHeader = new ArrayList<String>();
        }
        return exposeHeader;
    }

    /**设置CORS响应中带的附加头域
     * 
     * @param exposeHeader 附加头域列表
     */
    public void setExposeHeader(List<String> exposeHeader)
    {
        this.exposeHeader = exposeHeader;
    }

    @Override
    public String toString()
    {
        return "BucketCorsRule [id=" + id + ", maxAgeSecond=" + maxAgeSecond + ", allowedMethod=" + allowedMethod
            + ", allowedOrigin=" + allowedOrigin + ", allowedHeader=" + allowedHeader + ", exposeHeader="
            + exposeHeader + "]";
    }
    
}
