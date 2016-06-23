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

/**ACL中策略容器的接口
 */
public interface GranteeInterface
{
    /**设置策略中用户的ID的接口
     * 
     * @param id 策略中用户的ID
     */
    public void setIdentifier(String id);
    
    /**获取策略中用户的ID的接口
     * 
     * @return 返回用户的ID
     */
    public String getIdentifier();
}
