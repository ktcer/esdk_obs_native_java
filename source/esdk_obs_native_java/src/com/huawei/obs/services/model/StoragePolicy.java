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

/**描述复制策略
 */
public class StoragePolicy
{
    private String storagePolicyName;

    /**获取复制策略的名称
     * 
     * @return 返回复制策略的名称
     */
    public String getStoragePolicyName()
    {
        return storagePolicyName;
    }

    /**设置复制策略的名称
     * 
     * @param storagePolicyName 复制策略的名称
     */
    public void setStoragePolicyName(String storagePolicyName)
    {
        this.storagePolicyName = storagePolicyName;
    }
    
}
