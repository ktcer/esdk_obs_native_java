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
