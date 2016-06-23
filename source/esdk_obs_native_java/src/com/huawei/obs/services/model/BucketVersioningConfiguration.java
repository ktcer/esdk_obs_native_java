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
 * 版本状态
 */
public class BucketVersioningConfiguration
{
    /**
     * 暂停多版本状态
     */
    public static final String SUSPENDED = "Suspended";
    
    /**
     * 启用多版本状态
     */
    public static final String ENABLED = "Enabled";

    private String status;
    
    /**
     * 创建一个版本状态
     * <p>
     * 提示：如果一个桶的多版本状态一旦被启用，则版本状态将无法关闭，或更改为{@link #SUSPENDED suspended}，
     * @param status
     * @see #ENABLED
     * @see #SUSPENDED
     */
    public BucketVersioningConfiguration(String status)
    {
        setStatus(status);
    }

    /**
     * 获取多版本状态
     * @return status 多版本状态
     */
    public String getStatus()
    {
        return status;
    }
    
    /**
     * 设置多版本状态
     * @param status
     */
    public void setStatus(String status)
    {
        this.status = status;
    }

}
