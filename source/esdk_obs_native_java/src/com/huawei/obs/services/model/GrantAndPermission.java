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
