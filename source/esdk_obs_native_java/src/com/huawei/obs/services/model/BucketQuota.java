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

/**描述桶配额
 */
public class BucketQuota
{
    private long bucketQuota;

    /**获取配额信息,单位为字节
     * 
     * @return 返回配额信息
     */
    public long getBucketQuota()
    {
        return bucketQuota;
    }

    /**设置配额
     * 
     * @param quota 配额信息,单位为字节
     */
    public void setBucketQuota(long quota)
    {
        this.bucketQuota = quota;
    }

}
