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
 * 重定向信息
 */
public class Redirect
{
    private String protocol;

    private String hostName;

    private String replaceKeyPrefixWith;

    private String replaceKeyWith;

    private String httpRedirectCode;
    
    /**
     * 返回协议
     * @return 重定向请求时使用的协议
     */
    public String getProtocol()
    {
        return protocol;
    }

    /**
     * 设置协议
     * @param protocol 重定向请求时使用的协议
     */
    public void setProtocol(String protocol)
    {
        this.protocol = protocol;
    }

    /**
     * 返回主机名
     * @return 主机名称
     */
    public String getHostName()
    {
        return hostName;
    }

    /**
     * 设置主机名
     * @param hostName 主机名称
     */
    public void setHostName(String hostName)
    {
        this.hostName = hostName;
    }

    /**
     * 返回对象名前缀
     * @return 重定向请求时使用的对象名前缀。
     */
    public String getReplaceKeyPrefixWith()
    {
        return replaceKeyPrefixWith;
    }

    /**
     * 设置对象名前缀
     * @param replaceKeyPrefixWith 重定向请求时使用的对象名前缀。
     */
    public void setReplaceKeyPrefixWith(String replaceKeyPrefixWith)
    {
        this.replaceKeyPrefixWith = replaceKeyPrefixWith;
    }

    /**
     * 返回重定向请求时使用的对象名
     * @return 重定向请求时使用的对象名。
     */
    public String getReplaceKeyWith()
    {
        return replaceKeyWith;
    }

    /**
     * 设置重定向请求时使用的对象名
     * @param replaceKeyWith 重定向请求时使用的对象名。
     * <p>
     * 例如：重定向请求到error.html。不可与ReplaceKeyPrefixWith同时存在。
     */
    public void setReplaceKeyWith(String replaceKeyWith)
    {
        this.replaceKeyWith = replaceKeyWith;
    }

    /**
     * 获取HTTP状态码
     * @return HTTP状态码。
     */
    public String getHttpRedirectCode()
    {
        return httpRedirectCode;
    }

    /**
     * 设置HTTP状态码
     * @param httpRedirectCode HTTP状态码。
     */
    public void setHttpRedirectCode(String httpRedirectCode)
    {
        this.httpRedirectCode = httpRedirectCode;
    }
    
    @Override
    public String toString()
    {
        return "RedirectRule [protocol=" + protocol + ", hostName=" + hostName + ", replaceKeyPrefixWith="
            + replaceKeyPrefixWith + ", replaceKeyWith=" + replaceKeyWith + ", httpRedirectCode=" + httpRedirectCode
            + "]";
    }
}
