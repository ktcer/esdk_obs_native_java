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

/**
 * HTTP请求方式
 */
public enum HttpMethodEnum
{
    /**
     * 请求服务器返回指定资源，如获取桶列表、下载对象等。
     */
    GET("Get"),

    /**
     * 请求服务器存储指定资源，如创建桶、上传对象等。
     */
    PUT("Put"),

    /**
     * 请求服务器存储特殊的资源或执行特殊操作，如初始化上传段任务、合并段等。
     */
    POST("Post"),

    /**
     * 请求服务器删除指定资源，如删除对象等。
     */
    DELETE("Delete"),

    /**
     * 请求服务器返回指定资源的概要，如获取对象元数据等。
     */
    HEAD("Head"),

    /**
     * 预请求，是客户端发送给服务端的一种请求，通常被用于检测客户端是否具有对服务端进行操作的权限。
     */
    OPTIONS("Options");
    
    /**
     * stringCode对应数据库中和外部的Code
     */
    private String stringCode;
    
    private HttpMethodEnum(String stringCode)
    {
        if (stringCode == null)
        {
            throw new IllegalArgumentException("stringCode is null");
        }
        this.stringCode = stringCode;
    }
    
    public String getStringCode()
    {
        return this.stringCode.toUpperCase();
    }
    
    public static HttpMethodEnum getValueFromStringCode(String stringCode)
    {
        if (stringCode == null)
        {
            throw new IllegalArgumentException("string code is null");
        }
        
        for (HttpMethodEnum installMode : HttpMethodEnum.values())
        {
            if (installMode.getStringCode().equals(stringCode.toUpperCase()))
            {
                return installMode;
            }
        }
        
        throw new IllegalArgumentException("string code is illegal");
    }
}
