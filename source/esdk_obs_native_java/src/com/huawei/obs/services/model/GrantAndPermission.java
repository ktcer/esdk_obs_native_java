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

/**ACL中的访问控制策略条目的容器，包含用户grantee和对应的权限permission
 */
public class GrantAndPermission
{
    private GranteeInterface grantee = null;
    
    private Permission permission = null;
    
    /**指定策略用户和策略的权限创建策略的构造函数
     * 
     * @param grantee 策略对应的用户
     * @param permission 策略的权限
     */
    public GrantAndPermission(GranteeInterface grantee, Permission permission)
    {
        this.grantee = grantee;
        this.permission = permission;
    }
    
    /**获取策略对应的用户
     * 
     * @return 策略所属的用户
     */
    public GranteeInterface getGrantee()
    {
        return grantee;
    }
    
    /**获取策略的权限
     * 
     * @return 策略的权限
     */
    public Permission getPermission()
    {
        return permission;
    }
    
    /**对象的比较方法
     * 
     * @param object 要比较的对象
     * @return true，对象相等，false，对象不等
     */
    public boolean equals(Object object)
    {
        return object instanceof GrantAndPermission
            && this.getGrantee().equals(((GrantAndPermission) object).getGrantee()) && this.getPermission().equals(
                ((GrantAndPermission) object).getPermission());
    }
    
    /**对象的HASH方法
     * 
     * @return 对象的HASH值
     */
    public int hashCode()
    {
        return (grantee + ":" + permission.toString()).hashCode();
    }
    
    /**返回策略对象描述信息
     * 
     * @return 返回策略对象描述字符串
     */
    public String toString()
    {
        return "GrantAndPermission [grantee=" + grantee + ", permission=" + permission + "]";
    }
    
}
