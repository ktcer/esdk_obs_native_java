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
 * 用于标记用户及用户的权限。
 */
public class Grant
{
    private String grantee;
    
    private String permission;
    
    /**
     * 返回用户名
     * @return 用户名
     */
    public String getGrantee()
    {
        return grantee;
    }
    
    /**
     * 设置用户名
     * @param grantee 用户名
     */
    public void setGrantee(String grantee)
    {
        this.grantee = grantee;
    }
    
    /**
     * 返回用户权限
     * @return 用户权限
     */
    public String getPermission()
    {
        return permission;
    }
    
    /**
     * 设置用户权限
     * @param permission 用户权限
     */
    public void setPermission(String permission)
    {
        this.permission = permission;
    }
    
}
