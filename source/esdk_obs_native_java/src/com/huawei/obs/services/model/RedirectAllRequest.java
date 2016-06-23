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
 * 所有请求重定向节点<br/>
 * 描述所有请求的重定向行为，如果这个节点出现，所有其他的兄弟节点都不能出现。
 */
public class RedirectAllRequest
{
    private String protocol;
    
    private String hostName;
    
    /**
     * 返回重定向使用的协议
     * @return 协议
     */
    public String getProtocol()
    {
        return protocol;
    }
    
    /**
     * 设置重定向使用的协议
     * @param protocol 协议
     */
    public void setProtocol(String protocol)
    {
        this.protocol = protocol;
    }
    
    /**
     * 返回主机地址
     * @return 重定向到的主机地址
     */
    public String getHostName()
    {
        return hostName;
    }
    
    /**
     * 设置主机地址
     * @param hostName 重定向到的主机地址
     */
    public void setHostName(String hostName)
    {
        this.hostName = hostName;
    }
    
}
