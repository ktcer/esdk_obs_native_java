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

/**描述ACL的权限
 */
public final class Permission
{
    /**完全访问权限
     */
    public static final Permission PERMISSION_FULL_CONTROL = new Permission("FULL_CONTROL");
    
    /**读权限
     */
    public static final Permission PERMISSION_READ = new Permission("READ");
    
    /**写权限
     */
    public static final Permission PERMISSION_WRITE = new Permission("WRITE");
    
    /**读ACL权限
     */
    public static final Permission PERMISSION_READ_ACP = new Permission("READ_ACP");
    
    /**写ACL权限
     */
    public static final Permission PERMISSION_WRITE_ACP = new Permission("WRITE_ACP");
    
    private String permissionString = "";
    
    /**对permissionString进行赋值
     * 
     * @param permissionString 权限名称
     */
    private Permission(String permissionString)
    {
        this.permissionString = permissionString;
    }
    
    /**返回权限名称
     * 
     * @return 返回权限名称
     */
    public String getPermissionString()
    {
        return permissionString;
    }
    
    /**根据名称获取一个对应的权限对象
     * 
     * @param str 权限名称
     * @return 权限名称对应的权限对象
     */
    public static Permission parsePermission(String str)
    {
        Permission permission = null;
        
        if (str.equals(PERMISSION_FULL_CONTROL.toString()))
        {
            permission = PERMISSION_FULL_CONTROL;
        }
        else if (str.equals(PERMISSION_READ.toString()))
        {
            permission = PERMISSION_READ;
        }
        else if (str.equals(PERMISSION_WRITE.toString()))
        {
            permission = PERMISSION_WRITE;
        }
        else if (str.equals(PERMISSION_READ_ACP.toString()))
        {
            permission = PERMISSION_READ_ACP;
        }
        else if (str.equals(PERMISSION_WRITE_ACP.toString()))
        {
            permission = PERMISSION_WRITE_ACP;
        }
        else
        {
            permission = null;
        }
        return permission;
    }
    
    /**获取权限的名称
     * 
     * @return 返回权限的名称
     */
    public String toString()
    {
        return permissionString;
    }
    
    /**对象比较方法
     * 
     * @param obj 比较的对象
     * @return true 比较的两个对象相同， false 比较的两个对象不同 
     */
    public boolean equals(Object obj)
    {
        return (obj instanceof Permission) && toString().equals(obj.toString());
    }
    
    /**对象的HASH方法
     * 
     * @return 返回对象的HASH值
     */
    public int hashCode()
    {
        return permissionString.hashCode();
    }
}
