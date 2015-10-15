/*
 * Copyright Notice:
 *      Copyright  1998-2009, Huawei Technologies Co., Ltd.  ALL Rights Reserved.
 *
 *      Warning: This computer software sourcecode is protected by copyright law
 *      and international treaties. Unauthorized reproduction or distribution
 *      of this sourcecode, or any portion of it, may result in severe civil and
 *      criminal penalties, and will be prosecuted to the maximum extent
 *      possible under the law.
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
