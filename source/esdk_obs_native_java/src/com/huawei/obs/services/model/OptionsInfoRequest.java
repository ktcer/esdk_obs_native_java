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

/**描述OPTION桶或对象的请求
 * 
 */
public class OptionsInfoRequest
{
    /**
     * 【必选】预请求指定的跨域请求Origin（通常为域名）
     */
    private String origin;
    
    /**
     * 【必选】实际请求可以带的HTTP方法
     */
    private List<String>requestMethod;
    
    /**
     * 【可选】实际请求可以带的HTTP头域
     */
    private List<String>requestHeaders;

    
    /**获取预请求指定的跨域请求
     * 
     * @return 请求指定的跨域请求Origin
     */
    public String getOrigin()
    {
        return origin;
    }
    
    /**设置预请求指定的跨域请求
     * 
     * @param origin 请求指定的跨域请求Origin
     */
    public void setOrigin(String origin)
    {
        this.origin = origin;
    }

    /**获取请求可以带的HTTP方法
     * 
     * @return HTTP方法列表
     */
    public List<String> getRequestMethod()
    {
        return requestMethod;
    }

    /**设置请求可以带的HTTP方法
     * 
     * @param requestMethod HTTP方法列表
     */
    public void setRequestMethod(List<String> requestMethod)
    {
        this.requestMethod = requestMethod;
    }

    /**获取请求可以带的HTTP头域
     * 
     * @return HTTP头域列表
     */
    public List<String> getRequestHeaders()
    {
        return requestHeaders;
    }

    /**设置请求可以带的HTTP头域
     * 
     * @param requestHeaders HTTP头域列表
     */
    public void setRequestHeaders(List<String> requestHeaders)
    {
        this.requestHeaders = requestHeaders;
    }
    
    
    
}
