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

/**描述桶或对象的属主
 */
public class Owner
{
    private String displayName;
    
    private String id;
    
    /**获取属主名称
     * 
     * @return 属主名称
     */
    public String getDisplayName()
    {
        return displayName;
    }
    
    /**设置属主的名称
     * 
     * @param displayName 属主的名称
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }
    
    /**获取属主标识
     * 
     * @return 属主标识
     */
    public String getId()
    {
        return id;
    }
    
    /**设置属主的标识
     * 
     * @param id 属主标识
     */
    public void setId(String id)
    {
        this.id = id;
    }
    
}
