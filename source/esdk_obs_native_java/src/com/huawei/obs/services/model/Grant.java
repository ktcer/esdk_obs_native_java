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
