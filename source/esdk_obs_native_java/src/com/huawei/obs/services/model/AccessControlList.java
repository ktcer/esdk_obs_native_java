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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
/**描述访问控制策略ACL
 */
public class AccessControlList
{
    private final Set<GrantAndPermission> grants = new HashSet<GrantAndPermission>();
    
    private Owner owner = null;
    
    /**获取ACL的属主
     * 
     * @return ACL的属主
     */
    public Owner getOwner()
    {
        return owner;
    }
    
    /**设置ACL的属主
     * 
     * @param owner ACL的属主
     */
    public void setOwner(Owner owner)
    {
        this.owner = owner;
    }
    
    /**
     * 获取ACL中指定策略的权限
     * @param grantee 属主
     * @return 列举ACL符合指定策略的权限
     */
    public List<Permission> getPermissionsForGrantee(GranteeInterface grantee)
    {
        List<Permission> permissions = new ArrayList<Permission>();
        for (GrantAndPermission gap : grants)
        {
            if (gap.getGrantee().equals(grantee))
            {
                permissions.add(gap.getPermission());
            }
        }
        return permissions;
    }
    
    /**添加一个一个指定权限的策略
     * 
     * @param grantee 策略的属主
     * @param permission 策略
     */
    public void grantPermission(GranteeInterface grantee, Permission permission)
    {
        grants.add(new GrantAndPermission(grantee, permission));
    }
    
    /**添加一组指定权限的策略
     * 
     * @param grantAndPermissions 策略组
     */
    public void grantAllPermissions(GrantAndPermission[] grantAndPermissions)
    {
        for (int i = 0; i < grantAndPermissions.length; i++)
        {
            GrantAndPermission gap = grantAndPermissions[i];
            grantPermission(gap.getGrantee(), gap.getPermission());
        }
    }
    
    /**获取ACL中的策略组并以数组方式返回
     * 
     * @return 返回ACL中的策略组
     */
    public GrantAndPermission[] getGrantAndPermissions()
    {
        return grants.toArray(new GrantAndPermission[grants.size()]);
    }
    
}
